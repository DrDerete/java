package StochasticWay.Entity;

import StochasticWay.eNums.Direction;
import StochasticWay.panels.AgentPanel;
import StochasticWay.panels.ButtonPanel;
import StochasticWay.panels.GreedPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class Simulation extends JFrame implements ActionListener {

    private final JPanel cards;
    private final JLayeredPane dynamicP;
    private final ButtonPanel btnPanel;
    private AgentPanel agentPanel;
    private GreedPanel gridPanel;

    private Cell[][] world;
    private int[] begin;
    private Direction[] way;

    private final Timer moveTimer;
    private int pathIndex = 0;

    public Simulation() {

        setTitle("Stochastic Way");     // настройки
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // окно на весь экран
        setUndecorated(true);   // убрать границы

        btnPanel = new ButtonPanel("Start", "Open", this);
        cards = new JPanel(new CardLayout());  // управление панелями
        dynamicP = new JLayeredPane();

        cards.add(btnPanel, "MAIN");
        cards.add(dynamicP, "DYNAMIC");

        setContentPane(cards);
        // Завершения программы на esc
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getRootPane().getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "closeWindow");
        actionMap.put("closeWindow", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        moveTimer = new Timer(10, _ -> animated());

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // обработка нажатия кнопки
        String cmd = e.getActionCommand();  // то, что услышал listener
        CardLayout cl = (CardLayout) cards.getLayout();   // менеджер для переключения
        // если нужная кнопка
        if (cmd.equals("Open")) {
            // смоделировать движение по карте
            create_entities();
            // смена окна
            cl.show(cards, "DYNAMIC");
            // создание панелей для нового окна
            gridPanel = new GreedPanel(world, getWidth(), getHeight());
            agentPanel = new AgentPanel(begin, gridPanel.get_cell_dim());
            // установка панелей в окно
            dynamicP.add(gridPanel, JLayeredPane.DEFAULT_LAYER);
            dynamicP.add(agentPanel, JLayeredPane.PALETTE_LAYER);
            // старт таймера
            moveTimer.start();
        }
    }

    private void create_entities() {
        Environment env = new Environment(5, 6, btnPanel.getProbability());
        world = env.get_simulation_world();
        begin = env.get_agent_begin_place();
        way = env.get_agent_directions();
        System.out.println(Arrays.toString(way));
    }

    private void animated() {
        // анимация движения AgentPanel по GreedPanel
        if (pathIndex < way.length) {
            // ось движения
            boolean horizontal = Direction.horizontal(way[pathIndex]);
            // позиция агента (row или col)
            int positionCell = agentPanel.get_position(horizontal);
            // на сколько изменится координата клетки
            int targetDir = Direction.direction(way[pathIndex]);
            // если текущее положение панели не равно целевому, то происходит движение
            int n1 = agentPanel.get_pix_pos(horizontal);
            int n2 = agentPanel.get_target_pos(positionCell + targetDir, horizontal);
            if (n1 != n2)  {
                agentPanel.move_agent(targetDir, horizontal);
            } else {
                // в противном случае, переход к следующему
                agentPanel.set_position(horizontal, positionCell + targetDir);
                pathIndex++;
            }
        } else {
            moveTimer.stop();
        }
    }

}
