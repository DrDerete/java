import javax.swing.*;
import java.awt.*;

public class ConLab extends JFrame {

    public static final int
            WIDTH = 1024,
            HEIGHT = 720,
            cReact = 10,
            RECT_VELOCITY = 1;

    public ConLab() {

        super("Rectangles");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(cReact, 1));

        Rectangle[] rectangles = new Rectangle[cReact];
        for (int i = 0; i < cReact; i++) {
            rectangles[i] = new Rectangle(
                    RECT_VELOCITY,
                    new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255)),
                    1,
                    1
            );
            this.add(rectangles[i]);
        }

        Thread[] threads = new Thread[cReact];
        for (int i = 0; i < cReact; i++) {
            threads[i] = new Thread(rectangles[i]);
            threads[i].setPriority(Math.min(i+1, Thread.MAX_PRIORITY));
            threads[i].start();
        }

        setVisible(true);
    }

    public static void main(String[] args) {

        new ConLab();

    }
}