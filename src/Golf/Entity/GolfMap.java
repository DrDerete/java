package Golf.Entity;
import java.util.Objects;

public class GolfMap {
    private final String name;
    private final int[][] field;
    private final int winHeight;
    private final int winWidth;


    public GolfMap(String name, int[][] field, int wh, int ww) {
        this.name = Objects.requireNonNull(name);
        this.field = field;
        this.winHeight = wh;
        this.winWidth = ww;
    }

    // Получение значения клетки
    public int getCell(int h, int w) {
        if ((h < 0 || h >= getHeight()) || (w < 0 || w >= getWidth())) {
            throw new IndexOutOfBoundsException("Invalid coordinates");
        }
        return field[h][w];
    }

    // Геттеры
    public int getWidth() { return field[0].length; }
    public int getHeight() { return field.length; }
    public String getName() { return name; }
    public int[][] getField() { return field; }
    public int getWinHeight() { return winHeight; }
    public int getWinWidth() { return winWidth; }

}
