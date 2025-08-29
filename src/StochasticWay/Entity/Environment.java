package StochasticWay.Entity;

import StochasticWay.eNums.CellType;
import StochasticWay.eNums.Direction;

import java.util.*;
import java.util.stream.IntStream;

public class Environment {

    public int[][] blocks = {{2, 2}, {4, 2}, {1, 4}, {3, 6}, {5, 4}};
    public int[] win = {3, 5};
    public int[] loose = {3, 4};

    public int cols;
    public int rows;

    public Cell[][] world;
    public Agent agent;

    public Environment(int rows, int cols, Float p) {
        this.cols = cols + 2;
        this.rows = rows + 2;

        world = generate_world();
        agent = generate_agent();
        agent.set_p(p);

        mark_danger(loose);     // отмечаются опасные клетки

        List<int[]> danger = lead_way(Collections.singletonList(win)); // устанавливается направление движения к финишу, возвращая местоположение опасных клеток

        while (!danger.isEmpty()) {
            for (int[] pl : danger) {
                Cell[] dirs = direction_cells(pl[0], pl[1]);
                choose_direction(dirs, pl);    // движение в опасных клетках
            }
            danger = lead_way(danger);
        }
    }

    private Cell[][] generate_world() {
        world = new Cell[rows][cols];    // мир сделан больше(+2, +2), чтобы расставить cell-блок по границам
        for (int i = 0; i < world.length; i++) {      // заполнение мира
            for (int j = 0; j < world[i].length; j++) {
                if ((i == 0 | j == 0 | i == rows - 1 | j == cols - 1) | (check_block(i, j))) {
                    world[i][j] = new Cell(CellType.BLOCK);
                } else if (i == win[0] & j == win[1]) {
                    world[i][j] = new Cell(CellType.WIN);
                } else if (i == loose[0] & j == loose[1]) {
                    world[i][j] = new Cell(CellType.LOOSE);
                } else {
                    world[i][j] = new Cell(CellType.SPACE);
                }
            }
        }
        return world;
    }

    private Boolean check_block(int r, int c) {
        for (int[] block : blocks) {    // проверка, является ли ячейка block
            if (block[0] == r & block[1] == c) return true;
        }
        return false;
    }

    private Agent generate_agent() {
        int[] agentPlace = rand_free_place();
        return new Agent(agentPlace[0], agentPlace[1]);
    }

    private int[] rand_free_place() {
        Random rand = new Random();
        int[] arr = new int[2];
        do {    // по типу ячейки находится свободная
            arr[0] = rand.nextInt(1, rows) - 1;  // положение агента в среде без границ
            arr[1] = rand.nextInt(1, cols) - 1;
        }  while (world[arr[0] + 1][arr[1] + 1].get_type() != CellType.SPACE); // учет границ
        return arr;
    }

    private void mark_danger(int[] cell) {
        Cell[] nearCells = direction_cells(cell[0], cell[1]); // функция возвращает соседние ячейки, в которые можно пойти(cell != block
        for (Cell c : nearCells) {
            if (c.get_type() == CellType.SPACE) c.set_type(CellType.DANGER);
        }
    }

    private Cell[] direction_cells(int row, int col) {
        String[] directions = new String[]{"UP", "DOWN", "LEFT", "RIGHT"}; // порядок направлений
        Cell[] neighbours = new Cell[directions.length]; // лист соседей по направлению
        for (String dir : directions) {
            switch (dir) {
                case "UP": {
                    if (world[row-1][col].get_type() != CellType.BLOCK) {
                        neighbours[0] = world[row-1][col];
                    } else {
                        neighbours[0] = null;
                    }
                    break;
                }
                case "DOWN": {
                    if (world[row+1][col].get_type() != CellType.BLOCK) {
                        neighbours[1] = world[row+1][col];
                    } else {
                        neighbours[1] = null;
                    }
                    break;
                }
                case "LEFT": {
                    if (world[row][col-1].get_type() != CellType.BLOCK) {
                        neighbours[2] = world[row][col-1];
                    } else {
                        neighbours[2] = null;
                    }
                    break;
                }
                case "RIGHT": {
                    if (world[row][col+1].get_type() != CellType.BLOCK) {
                        neighbours[3] = world[row][col+1];
                    } else {
                        neighbours[3] = null;
                    }
                    break;
                }
            }
        }
        return neighbours;
    }

    private List<int[]> lead_way(List<int[]> cells) {
        List<int[]> dangerCells = new LinkedList<>();
        Deque<int[]> neighbors = new ArrayDeque<>(cells);

        while (!neighbors.isEmpty()) {

            int[] current = neighbors.pollFirst();                     // текущая клетка
            Cell[] nears = direction_cells(current[0], current[1]);    // свободные соседние клетки
            Direction[] dirs = new Direction[]{Direction.DOWN, Direction.UP, Direction.RIGHT, Direction.LEFT};

            for (int i = 0; i < nears.length; i++) {
                if (nears[i] != null) {
                    if (nears[i].get_type() == CellType.SPACE) {
                        nears[i].set_direction(dirs[i]);             // направление в соседней клетке в сторону финиша
                        neighbors.add(get_cell_place(current, i));   // обновление очереди
                    } else if (nears[i].get_type() == CellType.DANGER) {
                        dangerCells.add(get_cell_place(current, i)); // запись опасной клетки
                    }
                }
            }
        }
        return dangerCells;
    }

    private int[] get_cell_place(int[] cell, int i) {
        int row, col;
        switch (i) {
            case 0: {       // case UP
                row = cell[0] - 1;
                col = cell[1];
                break;
            }
            case 1: {       // case DOWN
                row = cell[0] + 1;
                col = cell[1];
                break;
            }
            case 2: {       // case LEFT
                row = cell[0];
                col = cell[1] - 1;
                break;
            }
            default: {       // case RIGHT
                row = cell[0];
                col = cell[1] + 1;
                break;
            }
        }
        return new int[]{row, col};
    }

    private void choose_direction(Cell[] dirs, int[] cell) {

        Direction looseDir;     // движение к поражению, от него уходим
        switch (cell[0] - loose[0]) {
            case 1: {
                looseDir = Direction.UP;
                break;
            }
            case -1: {
                looseDir = Direction.DOWN;
                break;
            }
            default: {
                if (cell[1] - loose[1] == 1) {
                    looseDir = Direction.LEFT;
                } else {
                    looseDir = Direction.RIGHT;
                }
            }
        }

        OptionalInt indexOptional = IntStream.range(0, dirs.length)
                .filter(i -> dirs[i] != null && dirs[i].get_direction() != null)
                .findFirst();

        Direction winDir = Direction.from_index(
                (indexOptional.isPresent()) ? (indexOptional.getAsInt()) : (5)
        );  // направление к победе, к нему идем

        switch (looseDir) {
            case Direction.UP -> {
                if (dirs[1] != null) {
                    world[cell[0]][cell[1]].set_direction(Direction.DOWN);
                } else {
                    world[cell[0]][cell[1]].set_direction(winDir);
                }
            }
            case Direction.DOWN -> {
                if (dirs[0] != null) {
                    world[cell[0]][cell[1]].set_direction(Direction.UP);
                } else {
                    world[cell[0]][cell[1]].set_direction(winDir);
                }
            }
            case Direction.LEFT -> {
                if (dirs[3] != null) {
                    world[cell[0]][cell[1]].set_direction(Direction.RIGHT);
                } else {
                    world[cell[0]][cell[1]].set_direction(winDir);
                }
            }
            case Direction.RIGHT -> {
                if (dirs[2] != null) {
                    world[cell[0]][cell[1]].set_direction(Direction.LEFT);
                } else {
                    world[cell[0]][cell[1]].set_direction(winDir);
                }
            }
        }
    }

    public Cell[][] get_simulation_world() {
        int r = rows - 2;
        int c = cols - 2;
        Cell[][] simWorld = new Cell[r][c];
        for (int i = 0; i < r; i++) {
            System.arraycopy(world[i + 1], 1, simWorld[i], 0, c);
        }
        return simWorld;
    }

    public int[] get_agent_begin_place() { return agent.get_sim_pos(); }

    public Direction[] get_agent_directions() {
        List<Direction> way = new LinkedList<>();
        agent.memorized(world);
        while (agent.is_active()) {
            Direction d = agent.step();
            if (d != null) way.add(d);
        }
        return way.toArray(new Direction[0]);
    }

    public boolean get_result() {
        int[] result_pos = agent.get_env_position();
        return Arrays.equals(result_pos, win);
    }

}
