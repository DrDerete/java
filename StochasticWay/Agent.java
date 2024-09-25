package StochasticWay;

import java.util.List;
import java.util.Random;

public class Agent {

    private final int[] place;


    public Agent(int r, int c) {
        this.place = new int[]{r, c};
    }

    public void step(String command, List<String> ways) {  //шаг по команде в клетке

        if (!ways.isEmpty()) {
            command = randWay(command, ways);
        }

        switch (command) {
            case "left": {
                this.place[1] -= 1;
                break;
            }
            case "right": {
                this.place[1] += 1;
                break;
            }
            case "up": {
                this.place[0] -= 1;
                break;
            }
            case "down": {
                this.place[0] += 1;
            }
        }
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

    public int[] getCommand() {
        return place;
    }

}
