package StochasticWay.Z_other.QLearning;
import java.util.*;

public class QLearning {
    //Параметры среды
    static final int M = 3, N = 3; // Размер сетки
    static final double gamma = 0.9; // Коэффициент дисконтирования
    static final double alpha = 0.1; // Скорость обучения
    static final double epsilon = 0.1; // Вероятность исследования (epsilon-greedy)
    static final int numEpisodes = 1000; // Количество эпизодов

    // Целевые состояния и их награды
    static final Map<Pair, Double> goals = new HashMap<>();
    static {
        goals.put(new Pair(0, 2), 10.0);  // Цель с наградой +10
        goals.put(new Pair(2, 2), -10.0); // Цель с наградой -10
    }

    // Награды
    static double reward(Pair state) {
        return goals.getOrDefault(state, -1.0); // Штраф за каждый шаг
    }

    // Инициализация Q-функции
    static Map<Pair, Map<String, Double>> Q = new HashMap<>();
    static String[] actions = {"up", "down", "left", "right"};

    // Q-Learning
    public static void main(String[] args) {
        // Инициализация Q(s, a) нулями
        for (int x = 0; x < M; x++) {
            for (int y = 0; y < N; y++) {
                Pair state = new Pair(x, y);
                Q.put(state, new HashMap<>());
                for (String action : actions) {
                    Q.get(state).put(action, 0.0);
                }
            }
        }

        // Обучение
        for (int episode = 0; episode < numEpisodes; episode++) {
            Pair state = new Pair(0, 0); // Начальное состояние
            while (!goals.containsKey(state)) { // Пока не достигнуто целевое состояние
                // Выбор действия (epsilon-greedy)
                String action = chooseAction(state);

                // Выполнение действия и переход в новое состояние
                Pair nextState = transition(state, action);

                // Получение награды
                double reward = reward(nextState);

                // Обновление Q(s, a)
                double maxQNext = Collections.max(Q.get(nextState).values());
                double oldQ = Q.get(state).get(action);
                double newQ = oldQ + alpha * (reward + gamma * maxQNext - oldQ);
                Q.get(state).put(action, newQ);

                // Переход в следующее состояние
                state = nextState;
            }
        }

        // Вывод оптимальных Q-значений
        System.out.println("Оптимальные Q-значения:");
        for (int x = 0; x < M; x++) {
            for (int y = 0; y < N; y++) {
                Pair state = new Pair(x, y);
                System.out.printf("State (%d, %d):%n", x, y);
                for (String action : actions) {
                    System.out.printf("  %s: %.2f%n", action, Q.get(state).get(action));
                }
            }
        }
    }

    // Выбор действия (epsilon-greedy)
    static String chooseAction(Pair state) {
        if (Math.random() < epsilon) {
            // Случайное действие (исследование)
            return actions[(int) (Math.random() * actions.length)];
        } else {
            // Жадное действие (эксплуатация)
            return Collections.max(Q.get(state).entrySet(), Map.Entry.comparingByValue()).getKey();
        }
    }

    // Переход в новое состояние
    static Pair transition(Pair state, String action) {
        int x = state.x;
        int y = state.y;

        return switch (action) {
            case "up" -> new Pair(Math.max(x - 1, 0), y);
            case "down" -> new Pair(Math.min(x + 1, M - 1), y);
            case "left" -> new Pair(x, Math.max(y - 1, 0));
            case "right" -> new Pair(x, Math.min(y + 1, N - 1));
            default -> throw new IllegalArgumentException("Неизвестное действие: " + action);
        };
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
}