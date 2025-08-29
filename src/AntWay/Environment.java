package AntWay;

import javax.swing.*;
import java.awt.*;

public class Environment extends JFrame {

    private final int cTown;
    private final int maxX;
    private final int maxY;
    private final Town[] towns;

    public Environment(int countTown, int widthTown, int heightTown) {

        Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();  // узнаем размур экрана для paint
        maxX = sSize.width;
        maxY = sSize.height;

        cTown = countTown;
        towns = new Town[cTown];

        double angle = 2*Math.PI / cTown;  // угол для расположения по кругу

        for (int i = 0; i < cTown; i++) {   // заносим города
            towns[i] = new Town(i, maxX, maxY, angle, widthTown, heightTown);
        }

        this.setSize(maxX, maxY);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    public void paint(Graphics g) {

        for (int i = 0; i < cTown; i++) {   // отрисовка карты
            g.fillOval(towns[i].getLeftUpX(), towns[i].getLeftUpY(), 20, 20);
            g.drawLine(towns[i].getX(), towns[i].getY(), maxX/2, maxY/2);
            if (i + 1 == cTown) {
                g.drawLine(towns[i].getX(), towns[i].getY(), towns[0].getX(), towns[0].getY());
            } else {
                g.drawLine(towns[i].getX(), towns[i].getY(), towns[i+1].getX(), towns[i+1].getY());
            }
        }

    }

}
