package Golf.Entity;

import Golf.eNums.GolfDirection;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GolfAgent {
    private final GolfMap map;
    private final double learningRate;
    private final double discountFactor;
    private final Random random = new Random();
    private double explorationRate;

    // Q-таблица: Map<Состояние, Map<Действие, Q-значение>>
    private final Map<String, Map<String, Double>> qTableDriver = new HashMap<>();
    private final Map<String, Map<String, Double>> qTablePutter = new HashMap<>();

    private final int DRIVER_POWER = 30; // Дальность дайвера (клетки)
    private final int PUTTER_POWER = 15; // Дальность паттера
    private boolean isTrainingPutterPhase = true;

    public GolfAgent(GolfMap map, double learningRate, double discountFactor, double explorationRate) {
        this.map = map;
        this.learningRate = learningRate;
        this.discountFactor = discountFactor;
        this.explorationRate = explorationRate;
    }

    // Переключение фазы обучения
    public void switchTrainingPhase(boolean isPutterPhase) {
        this.isTrainingPutterPhase = isPutterPhase;
    }

    // Кодирование состояния (x,y + тип поверхности)
    public String getStateKey(int h, int w) {
        return h + "," + w + "," + map.getCell(h, w);
    }

    // Один эпизод обучения
    public void trainEpisode() {
        int h = 22, w = 15;  // Стартовая позиция
        int maxSteps = 20;

        for (int step = 0; step < maxSteps; step++) {
            GolfDirection action = chooseAction(h, w);
            int[] newPos = move(h, w, action, isTrainingPutterPhase);
            double reward;

            if (newPos[0] == h && newPos[1] == w) {
                // удар за пределы поля
                reward = -30;
            } else {
                reward = calculateReward(h, w, newPos[0], newPos[1]);
            }
            // обновляем веса
            updateQValue(h, w, action, newPos[0], newPos[1], reward);
            // обновляем координаты
            h = newPos[0];
            w = newPos[1];
            // для попадания в лунку достаточно оказаться в области грина
            if (map.getCell(newPos[0], newPos[1]) == 2) break;
        }
    }

    // Выбор действия (ε-жадная стратегия) с учетом текущей фазы обучения
    public GolfDirection chooseAction(int h, int w) {
        String state = getStateKey(h, w);
        initStateIfNeeded(state);

        if (random.nextDouble() < explorationRate) {
            // Случайное исследование
            GolfDirection[] allDirections = GolfDirection.values();
            return allDirections[random.nextInt(allDirections.length)];
        } else {
            // Жадное использование Q-таблицы текущей фазы
            Map<String, Double> currentQTable = isTrainingPutterPhase ?
                    qTablePutter.get(state) : qTableDriver.get(state);

            return currentQTable.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(entry -> GolfDirection.valueOf(entry.getKey()))
                    .orElseGet(() ->
                            GolfDirection.values()[random.nextInt(GolfDirection.values().length)]
                    );
        }
    }

    // Инициализация Q-значений для состояния
    private void initStateIfNeeded(String state) {
        qTableDriver.putIfAbsent(state, new HashMap<>());
        qTablePutter.putIfAbsent(state, new HashMap<>());
        for (GolfDirection action : GolfDirection.values()) {
            qTableDriver.get(state).putIfAbsent(action.getName(), 0.0);
            qTablePutter.get(state).putIfAbsent(action.getName(), 0.0);
        }
    }

    // Применение действия к позиции
    public int[] move(int h, int w, GolfDirection action, boolean isPutter) {
        int power = isPutter ? PUTTER_POWER : DRIVER_POWER;
        // Добавляем случайность только для дайвера
        double angle;
        if (!isPutter) {
            // Случайное отклонение силы (±10%)
            power = (int) (power * (0.9 + 0.2 * random.nextDouble()));
            // Случайное отклонение угла (±10 градусов)
            double angleDeviation = Math.toRadians(random.nextDouble() * 20 - 10);
            angle = action.getAngleRadians() + angleDeviation;
        } else {
            angle = action.getAngleRadians();
        }
        double dh = power * Math.sin(angle);
        double dw = power * Math.cos(action.getAngleRadians());
        // Обновляем
        int newH = h - (int) Math.round(dh);
        int newW = w + (int) Math.round(dw);
        // Проверяем границы поля
        if (!isPositionValid(newH, newW)) {
            return new int[]{h, w};
        }
        return new int[]{newH, newW};
    }

    // Расчет награды
    private double calculateReward(int h, int w, int newH, int newW) {
//        if (map.getCell(newH, newW) == 3) return 100.0;  // Попадание в лунку
        if (map.getCell(newH, newW) == 2) return  50.0; // Попадание в грин
        if (map.getCell(newH, newW) == 1) return -10000.0;  // Песчаная ловушка

//    // Опционально: динамическая награда
//        if (useDistanceReward) {
//            double prevDist = distanceToGreen(h, w);
//            double newDist = distanceToGreen(newH, newW);
//            return (prevDist - newDist) * 2.0 - 1.0;
//        }

        return  - 1;  // штраф за ход
    }

    private Boolean isPositionValid(int h, int w) {
        return (0 <= w && w < map.getWidth()) && (0 <= h && h < map.getHeight());
    }

    public void updateQValue(int oldH, int oldW, GolfDirection action,
                             int newH, int newW, double reward) {
        String oldState = getStateKey(oldH, oldW);
        String newState = getStateKey(newH, newW);
        initStateIfNeeded(newState);

        // Выбираем соответствующую Q-таблицу
        Map<String, Map<String, Double>> currentQTable = isTrainingPutterPhase ? qTablePutter : qTableDriver;

        double maxQNew = currentQTable.get(newState).values().stream()
                .max(Double::compare).orElse(0.0);

        double currentQ = currentQTable.get(oldState).get(action.getName());
        double updatedQ = currentQ + learningRate * (reward + discountFactor * maxQNew - currentQ);

        currentQTable.get(oldState).put(action.getName(), updatedQ);
    }

    public Boolean choosingStick(String state) {
        double maxDriverQ = qTableDriver.getOrDefault(state, Collections.emptyMap())
                .values().stream()
                .max(Double::compare)
                .orElse(Double.NEGATIVE_INFINITY);

        double maxPutterQ = qTablePutter.getOrDefault(state, Collections.emptyMap())
                .values().stream()
                .max(Double::compare)
                .orElse(Double.NEGATIVE_INFINITY);

        return maxPutterQ > maxDriverQ;
    }

    public GolfDirection getBestAction(String state, boolean usePutter) {
        // Получаем соответствующую Q-таблицу
        Map<String, Double> qTable = usePutter ? qTablePutter.get(state) : qTableDriver.get(state);
        // Находим действие с максимальным Q-значением
        return qTable.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> GolfDirection.valueOf(entry.getKey()))
                .orElseGet(this::getRandomDirection);
    }

    private GolfDirection getRandomDirection() {
        GolfDirection[] directions = GolfDirection.values();
        return directions[random.nextInt(directions.length)];
    }

    public double getExplorationRate() { return explorationRate; }
    public void setExplorationRate(double rate) { this.explorationRate = rate; }

}
