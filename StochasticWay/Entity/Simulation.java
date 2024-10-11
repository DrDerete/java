package StochasticWay.Entity;

import StochasticWay.panels.AgentPanel;
import StochasticWay.panels.ButtonPanel;
import StochasticWay.panels.GreedPanel;
import StochasticWay.eNums.CellType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Simulation extends JFrame implements ActionListener {

    private final JPanel cards;
    private final JLayeredPane dynamicP;

    private final int rows;
    private final int cols;

    private final Cell[][] world;
    private final Agent agent;

    public Simulation(Cell[][] world, Agent agent) {

        rows = world.length - 2;
        cols = world[0].length - 2;

        this.agent = agent;
        this.world = new Cell[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(world[i + 1], 1, this.world[i], 0, cols);
        }

        setTitle("Stochastic Way");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // окно на весь экран
        setUndecorated(true);   // убрать границы

        JPanel btnPanel = new ButtonPanel("Start", "Open", this);
        cards = new JPanel(new CardLayout());  // управление панелями
        dynamicP = new JLayeredPane();

        cards.add(btnPanel, "MAIN");
        cards.add(dynamicP, "DYNAMIC");

        setContentPane(cards);

        addKeyListener(new KeyAdapter() {   // кей листинер на закрытие
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose(); // Закрываем окно
                }
            }
        });

        setFocusable(true);
        requestFocusInWindow();

    }

    public void actionPerformed(ActionEvent e) { // обработка нажатия кнопки

        String cmd = e.getActionCommand();
        CardLayout cl = (CardLayout) (cards.getLayout());

        if (cmd.equals("Open")) {
            cl.show(cards, "DYNAMIC");  // смена окна

            JPanel gridPanel = new GreedPanel(world, getWidth(), getHeight());
            AgentPanel agentPanel = new AgentPanel(agent.getPosition(), agent.getSize(), sizeGreedCell());

            dynamicP.add(gridPanel, JLayeredPane.DEFAULT_LAYER);
            dynamicP.add(agentPanel, JLayeredPane.PALETTE_LAYER);

            while (true) {
                int[] pos = agent.step(agentPanel);

                if (world[pos[0]][pos[1]].getType() == CellType.WIN) {
                    System.out.println("Победа");
                    break;
                } else if (world[pos[0]][pos[1]].getType() == CellType.LOOSE) {
                    System.out.println("Только не это. DIED x_x ...");
                    break;
                }
            }
        }
    }

    private Dimension sizeGreedCell() {
        int h = getHeight()/rows;
        int w = getWidth()/cols;
        return new Dimension(w, h);
    }

}
