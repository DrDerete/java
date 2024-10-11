package StochasticWay.Entity;

import StochasticWay.eNums.CellType;
import StochasticWay.eNums.Direction;

import java.util.*;

public class Environment {

    public int[] win = {1, 4};
    public int[] loose = {2, 4};
    public int[] block = {2, 2};

    public int cols;
    public int rows;

    public Cell[][] world;
    public Agent agent;

    public Environment(int rows, int cols) {

        this.cols = cols + 2;
        this.rows = rows + 2;

//        placePoints();

        world = generatedWorld();
        agent = generatedAgent();

        new Simulation(world, agent).setVisible(true);

    }

    private void placePoints() {

        win = randArray();
        while (Arrays.equals(win, loose)) {
            loose = randArray();
        }
        while (Arrays.equals(win, block) | Arrays.equals(loose, block)) {
            block = randArray();
        }

    }

    private int[] randArray() {

        Random rand = new Random();

        int[] arr = new int[2];

        arr[0] = rand.nextInt(1, rows-1);
        arr[1] = rand.nextInt(1, cols-1);

        return arr;

    }

    private Cell[][] generatedWorld() {

        world = new Cell[rows][cols];

        for (int i = 0; i < world.length; i++) {      // заполняем мир
            for (int j = 0; j < world[i].length; j++) {
                if ((i == 0 | j == 0 | i == rows - 1 | j == cols - 1) | (i == block[0] & j == block[1])) {
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

        if (world[loose[0] + 1][loose[1]].getType() == CellType.SPACE) {             // отмечаем ячейки около loose
            if (world[loose[0] + 2][loose[1]].getType() == CellType.SPACE) {
                world[loose[0] + 1][loose[1]].setDirection(Direction.DOWN);
            } else {
                if (loose[1] - win[1] < 0) {
                    world[loose[0] + 1][loose[1]].setDirection(Direction.RIGHT);
                } else {
                    world[loose[0] + 1][loose[1]].setDirection(Direction.LEFT);
                }
            }
        }
        if (world[loose[0] - 1][loose[1]].getType() == CellType.SPACE) {
            if (world[loose[0] - 2][loose[1]].getType() == CellType.SPACE) {
                world[loose[0] - 1][loose[1]].setDirection(Direction.UP);
            } else {
                if (loose[1] - win[1] < 0) {
                    world[loose[0] + 1][loose[1]].setDirection(Direction.RIGHT);
                } else {
                    world[loose[0] + 1][loose[1]].setDirection(Direction.LEFT);
                }
            }
        }
        if (world[loose[0]][loose[1] + 1].getType() == CellType.SPACE) {
            if (world[loose[0]][loose[1] + 2].getType() == CellType.SPACE) {
                world[loose[0]][loose[1] + 1].setDirection(Direction.RIGHT);
            } else {
                if (loose[0] - win[0] < 0) {
                    world[loose[0]][loose[1] + 1].setDirection(Direction.DOWN);
                } else {
                    world[loose[0]][loose[1] + 1].setDirection(Direction.UP);
                }
            }
        }
        if (world[loose[0]][loose[1] - 1].getType() == CellType.SPACE) {
            if (world[loose[0]][loose[1] - 2].getType() == CellType.SPACE) {
                world[loose[0] + 1][loose[1]].setDirection(Direction.LEFT);
            } else {
                if (loose[0] - win[0] < 0) {
                    world[loose[0]][loose[1] - 1].setDirection(Direction.DOWN);
                } else {
                    world[loose[0]][loose[1] - 1].setDirection(Direction.UP);
                }
            }
        }

        Deque<int[]> neighbors = new ArrayDeque<>();

        neighbors.add(win);

        while (!neighbors.isEmpty()) {  // отмечаем путь до финиша

            int[] current = neighbors.pollFirst();

            if (world[current[0] + 1][current[1]].getType() == CellType.SPACE) {
                neighbors.addLast(new int[]{current[0] + 1, current[1]});
                world[current[0] + 1][current[1]].setDirection(Direction.UP);
            }
            if (world[current[0] - 1][current[1]].getType() == CellType.SPACE) {
                neighbors.addLast(new int[]{current[0] - 1, current[1]});
                world[current[0] - 1][current[1]].setDirection(Direction.DOWN);
            }
            if (world[current[0]][current[1] + 1].getType() == CellType.SPACE) {
                neighbors.addLast(new int[]{current[0], current[1] + 1});
                world[current[0]][current[1] + 1].setDirection(Direction.LEFT);
            }
            if (world[current[0]][current[1] - 1].getType() == CellType.SPACE) {
                neighbors.addLast(new int[]{current[0], current[1] - 1});
                world[current[0]][current[1] - 1].setDirection(Direction.RIGHT);
            }
        }

        return world;
    }

    private Agent generatedAgent() {

        Agent ag = new Agent(3, 1);

        ag.memorized(world);    // для движения агенту нужны инструкции

        return ag;

    }

}