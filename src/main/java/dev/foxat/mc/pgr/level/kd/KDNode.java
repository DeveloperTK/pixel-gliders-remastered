package dev.foxat.mc.pgr.level.kd;

import lombok.Data;

@Data
public class KDNode implements Comparable<KDNode> {

    private final XYZPoint id;
    private final int k;
    private final int depth;

    private KDNode parent = null;
    private KDNode lesser = null;
    private KDNode greater = null;

    public KDNode(XYZPoint id) {
        this.id = id;
        this.k = 3;
        this.depth = 0;
    }

    public KDNode(XYZPoint id, int k, int depth) {
        this.id = id;
        this.k = k;
        this.depth = depth;
    }

    public static int compareTo(int depth, int k, XYZPoint o1, XYZPoint o2) {
        int axis = depth % k;
        if (axis == KDTree.X_AXIS)
            return KDTree.X_COMPARATOR.compare(o1, o2);
        if (axis == KDTree.Y_AXIS)
            return KDTree.Y_COMPARATOR.compare(o1, o2);
        return KDTree.Z_COMPARATOR.compare(o1, o2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return 31 * (this.k + this.depth + this.id.hashCode());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof KDNode kdNode))
            return false;

        return this.compareTo(kdNode) == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(KDNode o) {
        return compareTo(depth, k, this.id, o.id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "k=" + k + " depth=" + depth + " id=" + id.toString();
    }
}