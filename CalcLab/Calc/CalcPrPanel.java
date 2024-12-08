package SomeLabs.CalcLab.Calc;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class CalcPrPanel extends JPanel {

    private final JTextField display;
    private long currentValue = 0;
    private String operator = "";
    private boolean startNewInput = true;
        
    public CalcPrPanel(JTextField display) {
        this.display = display;
        this.setLayout(new GridLayout(6, 4, 10, 10));
        this.setBackground(Color.red);

        String[] buttonLabels = new String[24];

        for (int i = 0; i < 10; i++) {
            buttonLabels[i]=""+i;
        }
        buttonLabels[10]="+";
        buttonLabels[11]="-";
        buttonLabels[12]="*";
        buttonLabels[13]="/";
        buttonLabels[14]="C";
        buttonLabels[15]="=";
        buttonLabels[16]="A";
        buttonLabels[17]="B";
        buttonLabels[18]="C";
        buttonLabels[19]="D";
        buttonLabels[20]="(-_-)";
        buttonLabels[21]="E";
        buttonLabels[22]="F";
        buttonLabels[23]="(+-+)";

        for (int i = 0; i < 24; i++) {
            JButton btn = new JButton(buttonLabels[i]);
            btn.addActionListener(new ButtonClickListener());
            this.add(btn);
            }
    }
    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.matches("[0-9A-F]")) {
                handleDigit(command);
            } else if (command.matches("[+\\-*/]")) {
                handleOperator(command);
            } else if (command.equals("=")) {
                handleEquals();
            } else if (command.equals("C")) {
                handleClear();
            } else {
                System.out.println("Button clicked: " + command);
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

        private void handleOperator(String op) {
            if (!operator.isEmpty()) {
                handleEquals();
            }
            currentValue = Long.parseLong(display.getText(), 16);
            operator = op;
            startNewInput = true;
        }

        private void handleEquals() {
            if (!operator.isEmpty()) {
                long newValue = Long.parseLong(display.getText(), 16);
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
                display.setText(Long.toHexString(currentValue).toUpperCase());
                operator = "";
                startNewInput = true;
            }
        }

        private void handleClear() {
            currentValue = 0;
            operator = "";
            display.setText("0");
            startNewInput = true;
        }
    }

}
