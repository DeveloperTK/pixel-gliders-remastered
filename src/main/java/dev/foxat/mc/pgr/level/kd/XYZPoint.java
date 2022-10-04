package dev.foxat.mc.pgr.level.kd;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

@Data
@SuppressWarnings("unused")
public class XYZPoint implements Comparable<XYZPoint> {

    protected final double x;
    protected final double y;
    protected final double z;

    /**
     * z is defaulted to zero.
     */
    public XYZPoint(double x, double y) {
        this.x = x;
        this.y = y;
        this.z = 0;
    }

    /**
     * Default constructor
     */
    public XYZPoint(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Does not use R to calculate x, y, and z. Where R is the approximate radius of earth (e.g. 6371KM).
     */
    public XYZPoint(Double latitude, Double longitude) {
        x = cos(Math.toRadians(latitude)) * cos(Math.toRadians(longitude));
        y = cos(Math.toRadians(latitude)) * sin(Math.toRadians(longitude));
        z = sin(Math.toRadians(latitude));
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getZ() {
        return z;
    }

    /**
     * Computes the Euclidean distance from this point to the other.
     *
     * @param o1
     *            other point.
     * @return euclidean distance.
     */
    public double euclideanDistance(XYZPoint o1) {
        return euclideanDistance(o1, this);
    }

    /**
     * Computes the Euclidean distance from one point to the other.
     *
     * @param o1
     *            first point.
     * @param o2
     *            second point.
     * @return euclidean distance.
     */
    private static double euclideanDistance(XYZPoint o1, XYZPoint o2) {
        return Math.sqrt(Math.pow((o1.x - o2.x), 2) + Math.pow((o1.y - o2.y), 2) + Math.pow((o1.z - o2.z), 2));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return 31 * (int)(this.x + this.y + this.z);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof XYZPoint xyzPoint))
            return false;

        if (Double.compare(this.x, xyzPoint.x)!=0)
            return false;
        if (Double.compare(this.y, xyzPoint.y)!=0)
            return false;
        return Double.compare(this.z, xyzPoint.z) == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(@NotNull XYZPoint o) {
        int xComp = KDTree.X_COMPARATOR.compare(this, o);
        if (xComp != 0)
            return xComp;
        int yComp = KDTree.Y_COMPARATOR.compare(this, o);
        if (yComp != 0)
            return yComp;
        return KDTree.Z_COMPARATOR.compare(this, o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}