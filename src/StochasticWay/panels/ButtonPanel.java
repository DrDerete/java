package StochasticWay.panels;

import StochasticWay.Entity.Simulation;

import javax.swing.*;
import java.awt.*;

public class ButtonPanel extends JPanel {

    private final Dimension size;
    private final JTextField pField;
    private final JTextField leftField;
    private final JTextField rightField;

    public ButtonPanel(String buttonText, String actionCommand, Simulation listener) {

        size = Toolkit.getDefaultToolkit().getScreenSize();     // заполнение панели

        JPanel ag = new JPanel();
        ag.setBackground(Color.CYAN);
        ag.setBorder(BorderFactory.createLineBorder(Color.BLUE, 10));

        JButton btn = new JButton(buttonText);
        btn.setActionCommand(actionCommand);    // при нажатии listener различит эту кнопку по actionCommand
        btn.addActionListener(listener);        // установка listener к кнопке

        this.setLayout(new SpringLayout());
        this.add(btn);
        this.add(ag);

        centeringComponent(ag, 300, 300);
        centeringComponent(btn, 100, 30);

        pField = new JTextField(5);     // текст поля
        leftField = new JTextField(5);
        leftField.setEnabled(false);
        rightField = new JTextField(5);
        rightField.setEnabled(false);

        pField.addActionListener(_ -> updateP());   // listener на подсчет вероятности

        this.add(pField);
        this.add(leftField);
        this.add(rightField);

        centeringComponent(pField, false, -130);
        centeringComponent(rightField, true, 120);
        centeringComponent(leftField, true, -120);

    }

    private void centeringComponent(JComponent comp, int compWeight, int compHeight) {
        // центрирования JSwing компонента
        SpringLayout layout = (SpringLayout) getLayout();  // менеджер расположения

        SpringLayout.Constraints constraints = layout.getConstraints(comp); // установка размера
        constraints.setWidth(Spring.constant(compWeight));
        constraints.setHeight(Spring.constant(compHeight));

        int x = (size.width - compWeight) / 2;  // отступ для центрирования
        int y = (size.height - compHeight) / 2;

        layout.putConstraint(SpringLayout.WEST, comp, x, SpringLayout.WEST, this); // центрирование
        layout.putConstraint(SpringLayout.NORTH, comp, y, SpringLayout.NORTH, this);

    }

    private void centeringComponent(JComponent comp, boolean horizontal, int shift) {
        // центрирование со сдвигом для полей текста
        SpringLayout layout = (SpringLayout) getLayout();  // менеджер расположения

        SpringLayout.Constraints constraints = layout.getConstraints(comp); // установка размера
        constraints.setWidth(Spring.constant(35));
        constraints.setHeight(Spring.constant(15));

        int x = (horizontal)?((size.width - 35) / 2 + shift):((size.width - 35) / 2);  // отступ для центрирования
        int y = (horizontal)?((size.height - 15) / 2):((size.height - 15) / 2 + shift);

        layout.putConstraint(SpringLayout.WEST, comp, x, SpringLayout.WEST, this); // центрирование
        layout.putConstraint(SpringLayout.NORTH, comp, y, SpringLayout.NORTH, this);

    }

    private void updateP() {
        pField.setText(pField.getText().replace(",", "."));
        // число с текстового поля
        if (pField.getText().matches("-?\\d+(\\.\\d+)?")) {
            double p = Double.parseDouble(pField.getText());    // подсчет вероятности при вводе
            double oP = (1 - p) / 2;
            if (p >= 0 && p <= 1) {
                leftField.setText(String.format("%.2f", oP));
                rightField.setText(String.format("%.2f", oP));
            } else {
                JOptionPane.showMessageDialog(this, "Вероятность должна быть в отрезке между 0 и 1");
            }
        } else  {
            JOptionPane.showMessageDialog(this, "Cool story, вероятность должна быть числом");
        }
    }

    protected void paintComponent(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(10));
        g2d.setColor(Color.BLUE);

        int agentS = 300;  //  параметры для рисования стрелки
        int centerX = size.width / 2;
        int centerY = size.height / 2;
        int arrowL = 200;
        int arrowHead = 20;
        int shift = 5;

        int bArrowUp = centerY - (agentS / 2);
        int eArrowUp = bArrowUp - arrowL;

        g2d.drawLine(centerX, bArrowUp, centerX, eArrowUp); // стрелка вверх
        g2d.drawLine(centerX, eArrowUp - shift, centerX - arrowHead, eArrowUp - shift + arrowHead);
        g2d.drawLine(centerX, eArrowUp - shift, centerX + arrowHead, eArrowUp - shift + arrowHead);

        int bArrowLeft = centerX - (agentS / 2);
        int eArrowLeft = bArrowLeft - arrowL / 2;

        g2d.drawLine(bArrowLeft, centerY, eArrowLeft, centerY); // стрелка влево
        g2d.drawLine(eArrowLeft - shift, centerY, eArrowLeft - shift + arrowHead, centerY - arrowHead);
        g2d.drawLine(eArrowLeft - shift, centerY, eArrowLeft - shift + arrowHead, centerY + arrowHead);

        int bArrowRight = centerX + (agentS / 2);
        int eArrowRight = bArrowRight + arrowL / 2;

        g2d.drawLine(bArrowRight, centerY, eArrowRight, centerY); // стрелка вправо
        g2d.drawLine(eArrowRight + shift, centerY, eArrowRight + shift - arrowHead, centerY - arrowHead);
        g2d.drawLine(eArrowRight + shift, centerY, eArrowRight + shift - arrowHead, centerY + arrowHead);

    }

    public float getProbability() {
        return Float.parseFloat(pField.getText());
    }

    public boolean checkOtherProbability() {
        // проверка на заполнение(обработка нажатия кнопки без нажатия enter pField)
        String content = leftField.getText();
        if (content == null || content.trim().isEmpty()) {
            updateP();
        }
        content = leftField.getText();
        return content != null && !content.trim().isEmpty();
    }
}
