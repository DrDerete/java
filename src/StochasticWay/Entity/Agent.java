package StochasticWay.Entity;

import StochasticWay.eNums.CellType;
import StochasticWay.eNums.Direction;

import java.util.*;

public class Agent {

    private int row;
    private int col;

    private float p, oP;
    private Boolean active;

    private final Map<Integer, Map<Integer, Direction[]>> memory = new HashMap<>();  // структура вида {row: {col: [directions..]}}

    public Agent(int r, int c) {
        this.row = r;
        this.col = c;
        this.active = true;
    }

    public void memorized(Cell[][] world) {

        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world.length; j++) {
                if (world[i][j].get_direction() != null) {

                    Direction[] directions = new Direction[5];  // свободные ячейки направления
                    directions[4] = world[i][j].get_direction(); // направление к финишу

                    if (world[i + 1][j].get_type() != CellType.BLOCK) {  // добавление свободных направления
                        directions[0] = Direction.DOWN;
                    }
                    if (world[i - 1][j].get_type() != CellType.BLOCK) {
                        directions[1] = Direction.UP;
                    }
                    if (world[i][j + 1].get_type() != CellType.BLOCK) {
                        directions[2] = Direction.RIGHT;
                    }
                    if (world[i][j - 1].get_type() != CellType.BLOCK) {
                        directions[3] = Direction.LEFT;
                    }

                    if (memory.containsKey(i-1)) {    // сохранение
                        memory.get(i-1).put(j-1, directions);
                    } else {
                        Map<Integer, Direction[]> map = new HashMap<>();
                        map.put(j-1, directions);
                        memory.put(i-1, map);
                    }
                }
            }
        }

    }

    public Direction step() {
        // шаг по команде в клетке
        Direction[] directions = memory.get(row).get(col); // лист движений
        if (directions == null) {    // агент достиг win или loose
            active = false;
            return null;
        }

        Direction dir = directions[4]; // первый это движение в сторону финиша
        List<Direction> allWays = new ArrayList<>(); // это остальные свободные направления
        for (int i = 0; i < 4; i ++) {
            if (directions[i] != null) allWays.add(directions[i]);
        }

        if (!allWays.isEmpty()) {
            dir = rand_way(dir, allWays.toArray(new Direction[0])); // случайное движение
        }

        switch (dir) {
            case Direction.LEFT: {
                col -= 1;
                break;
            }
            case Direction.RIGHT: {
                col += 1;
                break;
            }
            case Direction.UP: {
                row -= 1;
                break;
            }
            case Direction.DOWN: {
                row += 1;
            }
        }

        return dir;
    }

    private Direction rand_way(Direction mainDir, Direction[] allDirs) {
        // направление, куда пошёл агент
        Random rand = new Random();
        float way = rand.nextFloat();
        //  rD это направления стохастических смещений
        List<Direction> rD = random_dirs(mainDir, allDirs);

        if (p < oP) {
            if (!rD.isEmpty()) {
                mainDir = rD.getFirst();
                rD = random_dirs(mainDir, allDirs);
            }
        }

        if (rD.size() == 1) {
            if (way > p + ((1 - p) / 4)) {  // если доступных направлений 2, работает этот вариант
                mainDir = rD.getFirst();
            }
        } else if (rD.size() == 2) {
            if (way > p + ((1 - p) / 2)) {  // если 3, то этот
                mainDir = rD.getLast();
            } else if (way > p) {
                mainDir = rD.getFirst();
            }
        }
        return mainDir;

    }

    private List<Direction> random_dirs(Direction direction, Direction[] otherDirs) {
        // направления, куда случайно может сместиться агент
        List<Direction> rDirs = new ArrayList<>();
        switch (direction) {
            case UP, DOWN -> {
                for (Direction d : otherDirs) {
                    if (d == Direction.LEFT || d == Direction.RIGHT) rDirs.add(d);
                }
            }
            default -> {
                for (Direction d : otherDirs) {
                    if (d == Direction.UP || d == Direction.DOWN) rDirs.add(d);
                }
            }
        }
        return rDirs;
    }

    public int[] get_env_position() {
        return new int[] {row+1, col+1};
    }

    public int[] get_sim_pos() {
        return new int[] {row, col};
    }

    public Boolean is_active() {
        return active;
    }

    public void set_p(float p) {
        this.p = p;
        this.oP = (1 - p) / 2;
    }

}
