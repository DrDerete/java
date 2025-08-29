package AntWay;

public class Town {

    private final int width;
    private final int height;
    private final int x;
    private final int y;

    public Town(int i, int mX, int mY, double angle, int wT, int hT) {
        x = (int) (Math.cos(i * angle) * mY/4 + (mX >> 1));
        y = (int) (Math.sin(i * angle) * mY/4 + (mY >> 1));
        width = wT;
        height = hT;
    }

    public int getLeftUpX() {
        return x - width /2;
    }

    public int getLeftUpY() {
        return y - height /2;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
