package StochasticWay.Entity;

import StochasticWay.eNums.Direction;
import StochasticWay.panels.AgentPanel;

import java.util.*;

public class Agent {

    private int row;
    private int col;
    private final int size;

    private final Map<Integer, Map<Integer, List<String>>> memory = new HashMap<>();  // структура вида {row: {col: [directions..]}}


    public Agent(int r, int c) {
        this.row = r;
        this.col = c;
        this.size = 75;
    }

    public void memorized(Cell[][] world) {

        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world.length; j++) {
                if (world[i][j].getDirection() != null) {

                    List<String> directions = new LinkedList<>();  //свободные ячейки по направлянию дижения(надо для случаного шага)
                    directions.add(world[i][j].getDirection().name().toLowerCase(Locale.ROOT));

                    switch (world[i][j].getDirection()) {   // учет свободных ячеек, для последующего случаного движения
                        case Direction.LEFT, Direction.RIGHT: {
                            if (world[i + 1][j].getDirection() != null) {
                                directions.add("down");
                            }
                            if (world[i - 1][j].getDirection() != null) {
                                directions.add("up");
                            }
                            break;
                        }
                        case Direction.DOWN, Direction.UP: {
                            if (world[i][j + 1].getDirection() != null) {
                                directions.add("right");
                            }
                            if (world[i][j - 1].getDirection() != null) {
                                directions.add("left");
                            }
                        }
                    }

                    if (memory.containsKey(i)) {
                        memory.get(i).put(j, directions);
                    } else {
                        Map<Integer, List<String>> map = new HashMap<>();
                        map.put(j, directions);
                        memory.put(i, map);
                    }
                }
            }
        }

    }

    public int[] step(AgentPanel agPan) {  // шаг по команде в клетке

        List<String> directions = memory.get(row).get(col);
        String command = directions.getFirst();
        List<String> ways = directions.subList(1, directions.size());

        if (!ways.isEmpty()) {
            command = randWay(command, ways);
        }

        switch (command) {
            case "left": {
                col -= 1;
                agPan.moveAgent(col-1, true);
                break;
            }
            case "right": {
                col += 1;
                agPan.moveAgent(col-1, true);
                break;
            }
            case "up": {
                row -= 1;
                agPan.moveAgent(row-1, false);
                break;
            }
            case "down": {
                row += 1;
                agPan.moveAgent(row-1, false);
            }
        }

        return new int[] {row-1, col-1};
    }

    private String randWay(String com, List<String> ways) {

        Random rand = new Random();

        float way = rand.nextFloat();
        float chance = 0.8f;
        if (ways.size() == 1) {
                if (way > chance + 0.05f) {
                    com = ways.getFirst();
                }
        } else {
            if (way > chance + 0.1f) {
                com = ways.getLast();
            } else if (way > chance) {
                com = ways.getFirst();
            }
        }
        return com;
    }

    public int[] getPosition() {
        return new int[] {row-1, col-1};
    }

    public int getSize() {
        return size;
    }
}
