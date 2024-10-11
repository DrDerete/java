package StochasticWay.panels;

import StochasticWay.Entity.Simulation;

import javax.swing.*;
import java.awt.*;

public class ButtonPanel extends JPanel {

    private final JButton btn;
    private final int BTN_W = 100;
    private final int BTN_H = 30;

    public ButtonPanel(String buttonText, String actionCommand, Simulation listener) {

        btn = new JButton(buttonText);
        btn.setActionCommand(actionCommand);
        btn.addActionListener(listener);
        btn.setPreferredSize(new Dimension(BTN_W, BTN_H));

        setLayout(new SpringLayout());
        add(btn);
        centerButton();

    }

    private void centerButton() {

        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();  // получение размера для установки кнопки

        SpringLayout layout = (SpringLayout) getLayout();  // кнопка в центре окна

        int x = (size.width-BTN_W) / 2;
        int y =  (size.height-BTN_H) / 2;

        layout.putConstraint(SpringLayout.WEST, btn, x, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, btn, y, SpringLayout.NORTH, this);

    }

}
