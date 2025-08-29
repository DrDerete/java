package SomeLabs.CalcLab.Calc;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Calculator extends JFrame {
    
    public JTextField answer;
    public JPanel table;
    
    public Calculator() {
        
        super();
        setSize(300, 600);

        answer = new JTextField();
        add(answer, BorderLayout.NORTH);

        JPanel calcPanel = new CalcPanel(answer);
        JPanel calcPrPanel = new CalcPrPanel(answer);

        table = new JPanel(new CardLayout());
        table.add(calcPanel, "CalcPanel");
        table.add(calcPrPanel, "CalcPrPanel");
        this.add(table, BorderLayout.CENTER);

        JButton changePanel = new JButton("SWAP");
        this.add(changePanel, BorderLayout.SOUTH);

        changePanel.addActionListener(_ -> {
                CardLayout cl = (CardLayout) table.getLayout();
                cl.next(table);
        });

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
            
    }

    public static void main(String[] args) {
        Calculator calc = new Calculator();
        
    }
    
}
