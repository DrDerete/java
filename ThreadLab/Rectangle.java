import java.awt.*;

public class Rectangle extends Canvas implements Runnable {

    private  static final int
            SLEEP_TIME = 10,
            WIDTH = 30,
            HEIGHT = 30;

    private final Color color;
    private int x, y;
    private int dx, dy;

    public Rectangle(int velocity, Color color, int x, int y) {

        this.color = color;
        this.x = x;
        this.y = y;
        this.dx = velocity;
        this.dy = velocity;

    }

    private void move() {

        if (x > this.getWidth() - WIDTH || x < 0) {
            dx = -dx;
        }
        x += dx;

        if (y > this.getHeight() - HEIGHT || y < 0) {
            dy = -dy;
        }
        y += dy;

    }

    public void run() {

        while (true) {
            move();
            repaint();
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void paint(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, WIDTH, HEIGHT);
    }
}
