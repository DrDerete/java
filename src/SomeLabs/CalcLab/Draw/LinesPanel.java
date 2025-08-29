package SomeLabs.CalcLab.Draw;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LinesPanel extends JPanel {
    private int cX, cY;
    private int rad;

    public LinesPanel() {
        setPreferredSize(new Dimension(400, 400));
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                cX = e.getX();
                cY = e.getY();
                rad = Math.max(getWidth(), getHeight());
                repaint();
            }
        });
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (cX != 0 && cY != 0) {
            drawCircleWithRays(g);
        }
    }

    private void drawCircleWithRays(Graphics g) {
        g.setColor(Color.BLACK);
        for (int i = 0; i < 360; i++) {
            double angle = Math.toRadians(i);
            int endX = (int) (cX + rad * Math.cos(angle));
            int endY = (int) (cY + rad * Math.sin(angle));
            g.drawLine(cX, cY, endX, endY);
        }
        g.setColor(Color.BLUE);
        g.fillOval(cX - 50, cY - 50, 100, 100);
    }

    public static void main(String[] args) {
        JFrame fr = new JFrame("Mouse seeker");
        fr.setSize(1024, 720);
        fr.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        fr.add(new LinesPanel());
        fr.setVisible(true);
    }
}
