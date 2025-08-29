package StochasticWay.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ResultPanel extends JPanel {
    private final Boolean result;
    private final ActionListener restartListener;
    private final ActionListener closeListener;

    public ResultPanel(Boolean result, ActionListener restartListener, ActionListener closeListener, Dimension cell) {
        this.result = result;
        this.restartListener = restartListener;
        this.closeListener = closeListener;
        // границы через клетку greedLayout
        setBounds(
                cell.width + cell.width / 2,
                cell.height + cell.height / 2,
                cell.width * 3,
                cell.height * 2
        );
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // бэкграунд
        Color back = result ? Color.GREEN : Color.RED;
        setBackground(back);

        // поле с результатом
        String congratulation = result ? "WIN": "LOOSE";
        JLabel resultLabel = new JLabel(congratulation, SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 112));
        resultLabel.setForeground(Color.BLACK);
        add(resultLabel, BorderLayout.CENTER);

        // создание панели с кнопками
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(0, 0, 0, 0)); //прозрачный бэк

        // кнопка для рестарта
        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(restartListener);
        buttonPanel.add(restartButton);

        // кнопка для закрытия
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(closeListener);
        buttonPanel.add(closeButton);

        // добавить кнопки
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
