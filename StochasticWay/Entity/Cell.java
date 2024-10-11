package StochasticWay.Entity;

import StochasticWay.eNums.CellType;
import StochasticWay.eNums.Direction;

public class Cell {

    private CellType type;
    private Direction direction = null;

    public Cell(CellType type) {
        this.type = type;
    }

    public CellType getType() {
        return type;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        type = CellType.MARKED;
    }
}
