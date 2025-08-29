package Golf.Entity;

import Golf.eNums.GolfDirection;
import org.bouncycastle.oer.Switch;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;;

public class GolfEnvironment {
    private final GolfMap map;
    private final GolfAgent agent;
    private int currentW;
    private int currentH;

    public GolfEnvironment(int h, int w, int count_sands) {
//      конструктор для генерации карт, дальше должен вызываться MapMaster и генерировать поле

        if (w <= 0 || h <= 0) {
            throw new IllegalArgumentException("Размеры поля должны быть положительными");
        }
        if (w <= h) {
            throw new IllegalArgumentException("Вытянутость поля должна быть больше его высоты");
        }

        if (h * w < 300) {
            throw new IllegalArgumentException("Поле меньше рекомендуемого размера (минимальная площадь 300 клеток)");
        }
//        заглушка на время, реализовать генерацию
        this.map = null;
        this.agent = null;
    }
    public GolfEnvironment(String mapName) {
//        Загрузка карты
        try {
            this.map = MapsMaster.loadMap(mapName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        создание агента
         this.currentW = 15;
         this.currentH = 22;
//        создание агента
         double learningRate = 0.1;
         double discountFactor = 0.9;
         double initialExplorationRate = 0.5;
         this.agent = new GolfAgent(map, learningRate, discountFactor, initialExplorationRate);

         trainAgent();

         List<String> adventure = makeMove();

         SwingUtilities.invokeLater(() -> {
            new GolfVisualizer(map, adventure);
         });
    }

    private void trainAgent() {
         int episodesPerPhase = 1000000; // Количество эпизодов для каждой фазы

         System.out.println("Тренируем путтер");
         // Фаза обучения паттера
         agent.switchTrainingPhase(true);
         for (int i = 0; i < episodesPerPhase; i++) {
             agent.trainEpisode();
             // Постепенно уменьшаем exploration rate
             if (i % 10000 == 0 && agent.getExplorationRate() > 0.01) {
                 agent.setExplorationRate(agent.getExplorationRate() * 0.9);
             }
         }

         System.out.println("Тренируем дайвер");
         // Фаза обучения дайвера
         agent.setExplorationRate(0.7);
         agent.switchTrainingPhase(false);
         for (int i = 0; i < episodesPerPhase; i++) {
             agent.trainEpisode();
             if (i % 10000 == 0 && agent.getExplorationRate() > 0.01) {
                 agent.setExplorationRate(agent.getExplorationRate() * 0.9);
             }
         }

         System.out.println("Агент готов");
         // Устанавливаем низкий exploration rate для эксплуатации
         agent.setExplorationRate(0.01);
    }

    // Метод для выполнения хода (используется после обучения)
    public List<String> makeMove() {
        List<String> agent_way = new ArrayList<>();
        // Начальная точка
        String currentState = agent.getStateKey(this.currentH, this.currentW);
        agent_way.add(currentState);
        while (true) {
            // Выбор клюшки
            Boolean putter = agent.choosingStick(currentState);
            // Выбор удара
            GolfDirection bestAction = agent.getBestAction(currentState, putter);
            // Обновление позиции
            int[] newPos = agent.move(this.currentH, this.currentW, bestAction, putter);

            this.currentH = newPos[0];
            this.currentW = newPos[1];

            currentState = agent.getStateKey(this.currentH, this.currentW);
            agent_way.add(currentState);

            if (map.getCell(currentH, currentW) == 2) {
                System.out.println("Победа! Мяч в грине!");
                agent_way.add(agent.getStateKey(map.getWinHeight(), map.getWinWidth()));
                break;
            }
        }

        return agent_way;
    }

    // Геттеры
    public GolfMap getMap() { return map; }
    public GolfAgent getAgent() { return agent; }
    public int getCurrentW() { return currentW; }
    public int getCurrentH() { return currentH; }
}
