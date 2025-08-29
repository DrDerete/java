package StochasticWay.Z_other.DynamicExample;

import java.util.*;

public class DynamicExample {

    // Параметры среды
    static final int M = 3, N = 3; // Размер сетки
    static final double gamma = 0.9; // Коэффициент дисконтирования
    static final double theta = 0.01; // Порог сходимости

    // Целевые состояния
    static final Map<Pair, Double> goals = new HashMap<>();
    static {
        goals.put(new Pair(0, 2), 1.0);
        goals.put(new Pair(2, 2), -1.0);
    }

    // Награды
    static double reward(Pair state) {
        if (goals.containsKey(state)) {
            return goals.get(state); // Награда за достижение цели
        }
        return -0.04; // Штраф за каждый шаг
    }

    // Вероятности переходов
    static Map<Pair, Double> transitionProb(Pair state, String action) {
        int x = state.x;
        int y = state.y;
        Map<Pair, Double> transitions = new HashMap<>();

        switch (action) {
            case "up":
                transitions.put(new Pair(x - 1, y), 0.8);
                transitions.put(new Pair(x, y - 1), 0.1);
                transitions.put(new Pair(x, y + 1), 0.1);
                break;
            case "down":
                transitions.put(new Pair(x + 1, y), 0.8);
                transitions.put(new Pair(x, y - 1), 0.1);
                transitions.put(new Pair(x, y + 1), 0.1);
                break;
            case "left":
                transitions.put(new Pair(x, y - 1), 0.8);
                transitions.put(new Pair(x - 1, y), 0.1);
                transitions.put(new Pair(x + 1, y), 0.1);
                break;
            case "right":
                transitions.put(new Pair(x, y + 1), 0.8);
                transitions.put(new Pair(x - 1, y), 0.1);
                transitions.put(new Pair(x + 1, y), 0.1);
                break;
        }

        // Убедимся, что все переходы ведут в допустимые состояния
        Map<Pair, Double> validTransitions = new HashMap<>();
        for (Map.Entry<Pair, Double> entry : transitions.entrySet()) {
            Pair sNew = entry.getKey();
            if (sNew.x >= 0 && sNew.x < M && sNew.y >= 0 && sNew.y < N) {
                validTransitions.put(sNew, entry.getValue());
            }
        }
        return validTransitions;
    }

    // Value Iteration
    static Map<Pair, Double> valueIteration(int M, int N, double gamma, double theta) {
        List<Pair> states = new ArrayList<>();
        for (int x = 0; x < M; x++) {
            for (int y = 0; y < N; y++) {
                states.add(new Pair(x, y));
            }
        }

        Map<Pair, Double> V = new HashMap<>();
        for (Pair s : states) {
            // Инициализация функции ценности
            V.put(s, goals.getOrDefault(s, 0.0)); // Фиксируем значения для целевых состояний
        }

        while (true) {
            double delta = 0;
            for (Pair s : states) {
                if (goals.containsKey(s)) {
                    continue; // Целевые состояния не обновляются
                }
                double v = V.get(s);
                double maxValue = Double.NEGATIVE_INFINITY;

                for (String a : new String[]{"up", "down", "left", "right"}) {
                    double sum = 0;
                    for (Map.Entry<Pair, Double> entry : transitionProb(s, a).entrySet()) {
                        Pair sNew = entry.getKey();
                        double p = entry.getValue();
                        sum += p * (reward(sNew) + gamma * V.get(sNew));
                    }
                    if (sum > maxValue) {
                        maxValue = sum;
                    }
                }

                V.put(s, maxValue);
                delta = Math.max(delta, Math.abs(v - V.get(s)));
            }

            if (delta < theta) {
                break;
            }
        }

        return V;
    }

    // Класс для представления состояния (координат)
    static class Pair {
        int x, y;

        Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return x == pair.x && y == pair.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    // Основной метод
    public static void main(String[] args) {
        Map<Pair, Double> optimalValues = valueIteration(M, N, gamma, theta);
        System.out.println("Оптимальные значения функции ценности:");
        for (int x = 0; x < M; x++) {
            for (int y = 0; y < N; y++) {
                Pair state = new Pair(x, y);
                System.out.printf("V(%d, %d) = %.2f | ", x, y, optimalValues.get(state));
            }
            System.out.println(); // значения есть, идем по ним
        }
    }
}

