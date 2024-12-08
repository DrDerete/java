package SomeLabs.CalcLab.Calc;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class CalcPanel extends JPanel {
    
    private final JTextField display;
    private double currentValue = 0;
    private String operator = "";
    private boolean startNewInput = true;
        
    public CalcPanel(JTextField display) {
        this.display = display;
        this.setLayout(new GridLayout(4, 4, 10, 10));
        this.setBackground(Color.red);

        String[] buttonLabels = new String[16];

        for (int i = 0; i < 10; i++) {
            buttonLabels[i]=""+i;
        }
        buttonLabels[10]="+";
        buttonLabels[11]="-";
        buttonLabels[12]="*";
        buttonLabels[13]="/";
        buttonLabels[14]="C";
        buttonLabels[15]="=";

        for (int i = 0; i < 16; i++) {
            JButton btn = new JButton(buttonLabels[i]);
            btn.addActionListener(new ButtonClickListener());
            this.add(btn);
        }
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.matches("[0-9]")) {
                handleDigit(command);
            } else if (command.equals("C")) {
                clearDisplay();
            } else if (command.equals("=")) {
                calculateResult();
            } else {
                handleOperator(command);
            }
        }

        private void handleDigit(String digit) {
            if (startNewInput) {
                display.setText(digit);
                startNewInput = false;
            } else {
                display.setText(display.getText() + digit);
            }
        }

        private void clearDisplay() {
            display.setText("");
            currentValue = 0;
            operator = "";
            startNewInput = true;
        }

        private void calculateResult() {
            if (!operator.isEmpty()) {
                double newValue = Double.parseDouble(display.getText());
                switch (operator) {
                    case "+":
                        currentValue += newValue;
                        break;
                    case "-":
                        currentValue -= newValue;
                        break;
                    case "*":
                        currentValue *= newValue;
                        break;
                    case "/":
                        if (newValue != 0) {
                            currentValue /= newValue;
                        } else {
                            display.setText("Error");
                            return;
                        }
                        break;
                }
                display.setText(String.valueOf(currentValue));
                operator = "";
                startNewInput = true;
            }
        }

        private void handleOperator(String newOperator) {
            if (!operator.isEmpty()) {
                calculateResult();
            } else {
                currentValue = Double.parseDouble(display.getText());
            }
            operator = newOperator;
            startNewInput = true;
        }
    }
}
