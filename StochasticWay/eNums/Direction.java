package StochasticWay.eNums;

public enum Direction {
    UP, DOWN, LEFT, RIGHT;

    public static Direction from_index(int index) {
        if (index < 0 || index >= values().length) {
            throw new IllegalArgumentException("Invalid index: " + index);
        }
        return values()[index];
    }

    public static Boolean horizontal(Direction dir) {
        switch (dir) {
            case LEFT, RIGHT -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public static int direction(Direction dir) {
        switch (dir) {
            case RIGHT, DOWN -> {
                return 1;
            }
            default -> {
                return -1;
            }
        }
    }
}
