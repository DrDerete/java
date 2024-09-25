package StochasticWay;

import java.util.*;

public class Environment {

    public int[] win = {1, 4};
    public int[] loose = {2, 4};
    public int[] block = {2, 2};

    public int cols;
    public int rows;

    public Cell[][] world;

    public Environment(int rows, int cols) {

        this.cols = cols+2;
        this.rows = rows+2;

        // тут можно генерировать карту, т.е. точки win, loose, block (пока попробую фиксированный вариант)

        world = generatedWorld();

        run();
    }

    private Cell[][] generatedWorld() {

        world = new Cell[rows][cols];

        for (int i = 0; i < world.length; i++) {      // заполняем мир
            for (int j = 0; j < world[i].length; j++) {
                if ((i == 0 | j == 0 | i == rows-1 | j == cols-1) | (i == block[0] & j == block[1])) {
                    world[i][j] = new Cell("block");
                } else if (i == win[0] & j == win[1]) {
                    world[i][j] = new Cell("win");
                } else if (i == loose[0] & j == loose[1]) {
                    world[i][j] = new Cell("loose");
                } else {
                    world[i][j] = new Cell("space");
                }
            }
        }

        if (Objects.equals(world[loose[0] + 1][loose[1]].getName(), "space")) {     // отмечаем ячейки около loose
            if (Objects.equals(world[loose[0] + 2][loose[1]].getName(), "space")) {
                world[loose[0] + 1][loose[1]].setName("down");
            } else {
                if (loose[1] - win[1] < 0) {
                    world[loose[0] + 1][loose[1]].setName("right");
                } else {
                    world[loose[0] + 1][loose[1]].setName("left");
                }
            }
        }
        if (Objects.equals(world[loose[0] - 1][loose[1]].getName(), "space")) {
            if (Objects.equals(world[loose[0] - 2][loose[1]].getName(), "space")) {
                world[loose[0] - 1][loose[1]].setName("up");
            } else {
                if (loose[1] - win[1] < 0) {
                    world[loose[0] + 1][loose[1]].setName("right");
                } else {
                    world[loose[0] + 1][loose[1]].setName("left");
                }
            }
        }
        if (Objects.equals(world[loose[0]][loose[1] + 1].getName(), "space")) {
            if (Objects.equals(world[loose[0]][loose[1 + 2]].getName(), "space")) {
                world[loose[0]][loose[1] + 1].setName("right");
            } else {
                if (loose[0] - win[0] < 0) {
                    world[loose[0]][loose[1] + 1].setName("down");
                } else {
                    world[loose[0]][loose[1] + 1].setName("up");
                }
            }
        }
        if (Objects.equals(world[loose[0]][loose[1] - 1].getName(), "space")) {
            if (Objects.equals(world[loose[0]][loose[1] - 2].getName(), "space")) {
                world[loose[0] + 1][loose[1]].setName("left");
            } else {
                if (loose[0] - win[0] < 0) {
                    world[loose[0]][loose[1] - 1].setName("down");
                } else {
                    world[loose[0]][loose[1] - 1].setName("up");
                }
            }
        }

        Deque<int[]> neighbors = new ArrayDeque<>();

        neighbors.add(win);

        while (!neighbors.isEmpty()) { // отмечаем путь до финиша

            int[] current = neighbors.pollFirst();

            if (Objects.equals(world[current[0] + 1][current[1]].getName(), "space")) {
                neighbors.addLast(new int[]{current[0] + 1, current[1]});
                world[current[0] + 1][current[1]].setName("up");
            }
            if (Objects.equals(world[current[0] - 1][current[1]].getName(), "space")) {
                neighbors.addLast(new int[]{current[0] - 1, current[1]});
                world[current[0] - 1][current[1]].setName("down");
            }
            if (Objects.equals(world[current[0]][current[1] + 1].getName(), "space")) {
                neighbors.addLast(new int[]{current[0], current[1] + 1});
                world[current[0]][current[1] + 1].setName("left");
            }
            if (Objects.equals(world[current[0]][current[1] - 1].getName(), "space")) {
                neighbors.addLast(new int[]{current[0], current[1] - 1});
                world[current[0]][current[1] - 1].setName("right");
            }
        }

        return  world;
    }

    private void run() {    //запускаем симуляцию, машинка двигайся

        Agent ag = new Agent(3, 1);
        List<String> freeWay = new LinkedList<>();  //свободные ячейки по направлянию дижения(надо для случаного шага)

        while (true) {

            int[] com = ag.getCommand();

            switch (world[com[0]][com[1]].getName()) {   // учет свободных ячеек, для последующего случаного движения
                case "left", "right": {
                    if (!Objects.equals(world[com[0] + 1][com[1]].getName(), "block")) {
                        freeWay.add("down");
                    }
                    if (!Objects.equals(world[com[0] - 1][com[1]].getName(), "block")) {
                        freeWay.add("up");
                    }
                    break;
                }
                case "up", "down": {
                    if (!Objects.equals(world[com[0]][com[1] + 1].getName(), "block")) {
                        freeWay.add("right");
                    }
                    if (!Objects.equals(world[com[0]][com[1] - 1].getName(), "block")) {
                        freeWay.add("left");
                    }
                }
            }

            if (Arrays.toString(com).equals(Arrays.toString(win))) {
                System.out.println("Победа");
                break;
            } else if (Arrays.toString(com).equals(Arrays.toString(loose))) {
                System.out.println("Похороните меня иглами вниз.");
                break;
            } else {
                ag.step(world[com[0]][com[1]].getName(), freeWay);
                freeWay.clear();
            }
        }

//        int pix = 13;
//
//        JFrame frame = new JFrame();
//        frame.setSize(pix * pix *rows, pix * pix *cols);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        JPanel act = new JPanel();
//        act.setBackground(Color.blue);
//
//
//        frame.add(act);
//        frame.setVisible(true);
    }
}
