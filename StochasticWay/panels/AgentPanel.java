package StochasticWay.panels;

import javax.swing.*;
import java.awt.*;

public class AgentPanel extends JPanel {

    private int panX;  // координаты левого верхнего угла панели агента
    private int panY;

    private final int[] position;
    private final Dimension cellSize; // размер клетки среды
    private final int size = 75; // размер панели агента

    public AgentPanel(int[] beginPosition, Dimension cell) {

        position = beginPosition;
        cellSize = cell;

        panX = greed_pos_x(position[1]);
        panY = greed_pos_y(position[0]);

        setBounds(panX, panY, size, size);
        setBackground(Color.CYAN);
        setBorder(BorderFactory.createLineBorder(Color.BLUE, 10));

    }

    private int greed_pos_x(int col) { // для горизонтали
        return col * cellSize.width + (cellSize.width - size) / 2;
    }

    private int greed_pos_y(int row) { // для вертикали
        return row * cellSize.height + (cellSize.height - size) / 2;
    }

    public void move_agent(int trend, boolean horizontal) {  // движение
        // скорость движения панели
        int speed = 2;
        if (horizontal) {
            panX += speed * trend;
        } else {
            panY += speed * trend;
        }
        setLocation(panX, panY);
    }

    public int get_pix_pos(boolean horizontal) { // текущее положение агента по интересующей координате
        return (horizontal)?(this.panX):(this.panY);
    }

    public int get_target_pos(int targetCell, boolean horizontal) { // куда агент должен попасть
        if (horizontal) {
            return greed_pos_x(targetCell);
        } else {
            return greed_pos_y(targetCell);
        }
    }
    public int get_position(Boolean horizontal) {
        return (horizontal)?(position[1]):(position[0]);
    }

    public void set_position(Boolean horizontal, int cell) {
        if (horizontal) {
            position[1] = cell;
        } else {
            position[0] = cell;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

}