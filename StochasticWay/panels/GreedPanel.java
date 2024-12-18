package StochasticWay.panels;

import StochasticWay.Entity.Cell;

import javax.swing.*;
import java.awt.*;

public class GreedPanel extends JPanel {

    public int cellW;
    public int cellH;

    public GreedPanel(Cell[][] world, int w, int h) {

        super();

        cellW = w / world[0].length;
        cellH = h / world.length;

        String winPath = "src\\StochasticWay\\source\\Win.jpg";
        String loosePath = "src\\StochasticWay\\source\\Loose.jpg";
        String arrowHLPath = "src\\StochasticWay\\source\\ArrowHL.jpg";
        String arrowHRPath = "src\\StochasticWay\\source\\ArrowHR.jpg";
        String arrowVUPath = "src\\StochasticWay\\source\\ArrowVU.jpg";
        String arrowVDPath = "src\\StochasticWay\\source\\ArrowVD.jpg";

        int rows = world.length;
        int cols = world[0].length;

        setLayout(new GridLayout(rows, cols));
        setBounds(0, 0, w, h);

        for (Cell[] cells : world) {
            for (Cell c : cells) {
                JPanel cell = new JPanel();
                switch (c.get_type()) {     //  WIN, LOOSE, BLOCK, MARKED. Если маркед, то смотрим Direction
                    case BLOCK -> cell.setBackground(Color.BLUE);
                    case WIN -> cell = new ImagePanel(winPath);
                    case LOOSE -> cell = new ImagePanel(loosePath);
                    case MARKED -> {
                        switch (c.get_direction()) {
                            case UP -> cell = new ImagePanel(arrowVUPath);
                            case DOWN -> cell = new ImagePanel(arrowVDPath);
                            case RIGHT -> cell = new ImagePanel(arrowHRPath);
                            case LEFT -> cell = new ImagePanel(arrowHLPath);
                        }
                    }
                }
                cell.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
                add(cell);
            }
        }
    }

    public Dimension get_cell_dim() {
        return new Dimension(cellW, cellH);
    }
}
