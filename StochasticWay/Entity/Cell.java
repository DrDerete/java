package StochasticWay.Entity;

import StochasticWay.eNums.CellType;
import StochasticWay.eNums.Direction;

public class Cell {
    private Direction direction = null;
    private CellType type;

    public Cell(CellType type) {
        this.type = type;
    }


    public CellType get_type() {
        return type;
    }

    public void set_type(CellType type) {
        this.type = type;
    }

    public Direction get_direction() {
        return direction;
    }

    public void set_direction(Direction direction) {
        this.direction = direction;
        type = CellType.MARKED;
    }
}

