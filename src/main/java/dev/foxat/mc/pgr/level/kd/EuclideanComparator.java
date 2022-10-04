package dev.foxat.mc.pgr.level.kd;

import java.util.Comparator;

@SuppressWarnings("ClassCanBeRecord")
public class EuclideanComparator implements Comparator<KDNode> {

    private final XYZPoint point;

    public EuclideanComparator(XYZPoint point) {
        this.point = point;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(KDNode o1, KDNode o2) {
        Double d1 = point.euclideanDistance(o1.getId());
        Double d2 = point.euclideanDistance(o2.getId());
        if (d1.compareTo(d2) < 0)
            return -1;
        else if (d2.compareTo(d1) < 0)
            return 1;
        return o1.getId().compareTo(o2.getId());
    }
}
