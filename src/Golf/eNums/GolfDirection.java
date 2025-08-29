package Golf.eNums;

public enum GolfDirection  {
    E(0.0),
    NEE(22.5),
    NE(45.0),
    NNE(67.5),
    N(90.0),
    NNW(112.5),
    NW(135.0),
    NWW(157.5),
    W(180.0),
    SWW(202.5),
    SW(225.0),
    SSW(247.5),
    S(270.0),
    SSE(292.5),
    SE(315.0),
    SEE(337.5);

    private final double angleRadians;

    GolfDirection(double angleDegrees) {
        this.angleRadians = Math.toRadians(angleDegrees);
    }

    public double getAngleRadians() { return angleRadians; }
    public String getName() { return name(); }
}
