package StochasticWay.panels;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class AgentPanel extends JPanel {
    private int panX;
    private int panY;
    private final int size;
    private final Dimension cellSize;
    private boolean isMoving = false; // Флаг, указывающий, занят ли агент
    private final Queue<MoveTask> moveQueue = new LinkedList<>(); // Очередь задач перемещения

    public AgentPanel(int[] position, int size, Dimension cell) {
        cellSize = cell;
        this.size = size;
        panX = greedPosX(position[1]);
        panY = greedPosY(position[0]);
        setBounds(panX, panY, size, size);
        setBackground(Color.CYAN);
        setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
    }

    private int greedPosX(int col) {
        return col * cellSize.width + cellSize.width / 2 - size / 2;
    }

    private int greedPosY(int row) {
        return row * cellSize.height + cellSize.height / 2 - size / 2;
    }

    public synchronized void moveAgent(int pix, boolean horizontal) {
        MoveTask task = new MoveTask(pix, horizontal);
        moveQueue.add(task);
        processNextTask();
    }

    private synchronized void processNextTask() {
        if (!isMoving && !moveQueue.isEmpty()) {
            isMoving = true;
            MoveTask task = moveQueue.poll();
            int target = task.horizontal ? greedPosX(task.pix) : greedPosY(task.pix);
            int speed = 1;

            Timer timer = new Timer(1, e -> {
                int agPos = task.horizontal ? panX : panY;
                int dif = target - agPos > 0 ? 1 : -1;
                if (agPos != target) {
                    if (task.horizontal) {
                        panX += dif * speed;
                    } else {
                        panY += dif * speed;
                    }
                    setLocation(panX, panY);
                } else {
                    ((Timer) e.getSource()).stop();
                    isMoving = false;
                    processNextTask();
                }
            });
            timer.start();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    private record MoveTask(int pix, boolean horizontal) {
    }
}

