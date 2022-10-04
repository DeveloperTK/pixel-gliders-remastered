package dev.foxat.mc.pgr.level.kd;

import lombok.Data;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * A k-d tree (short for k-dimensional tree) is a space-partitioning data
 * structure for organizing points in a k-dimensional space. k-d trees are a
 * useful data structure for several applications, such as searches involving a
 * multidimensional search key (e.g. range searches and nearest neighbor
 * searches). k-d trees are a special case of binary space partitioning trees.
 *
 * @author Justin Wetherell <phishman3579@gmail.com>
 * @see <a href="http://en.wikipedia.org/wiki/K-d_tree">K-d_tree (Wikipedia)</a>
 */
@Data
@SuppressWarnings("unused")
public class KDTree<T extends XYZPoint> implements Iterable<T> {

    private int k = 3;

    private KDNode root = null;

    public static final Comparator<XYZPoint> X_COMPARATOR = new Comparator<>() {
        /**
         * {@inheritDoc}
         */
        @Override
        public int compare(XYZPoint o1, XYZPoint o2) {
            return Double.compare(o1.x, o2.x);
        }
    };

    public static final Comparator<XYZPoint> Y_COMPARATOR = new Comparator<>() {
        /**
         * {@inheritDoc}
         */
        @Override
        public int compare(XYZPoint o1, XYZPoint o2) {
            return Double.compare(o1.y, o2.y);
        }
    };

    public static final Comparator<XYZPoint> Z_COMPARATOR = new Comparator<>() {
        /**
         * {@inheritDoc}
         */
        @Override
        public int compare(XYZPoint o1, XYZPoint o2) {
            return Double.compare(o1.z, o2.z);
        }
    };

    protected static final int X_AXIS = 0;
    protected static final int Y_AXIS = 1;
    protected static final int Z_AXIS = 2;

    /**
     * Default constructor.
     */
    public KDTree() { }

    /**
     * Constructor for creating a more balanced tree. It uses the
     * "median of points" algorithm.
     *
     * @param list
     *            of XYZPoints.
     */
    public KDTree(List<XYZPoint> list) {
        super();
        root = createNode(list, k, 0);
    }

    /**
     * Constructor for creating a more balanced tree. It uses the
     * "median of points" algorithm.
     *
     * @param list
     *            of XYZPoints.
     * @param k
     *            of the tree.
     */
    public KDTree(List<XYZPoint> list, int k) {
        super();
        root = createNode(list, k, 0);
    }

    /**
     * Creates node from list of XYZPoints.
     *
     * @param list
     *            of XYZPoints.
     * @param k
     *            of the tree.
     * @param depth
     *            depth of the node.
     * @return node created.
     */
    private static KDNode createNode(List<XYZPoint> list, int k, int depth) {
        if (list == null || list.size() == 0)
            return null;

        int axis = depth % k;
        if (axis == X_AXIS)
            list.sort(X_COMPARATOR);
        else if (axis == Y_AXIS)
            list.sort(Y_COMPARATOR);
        else
            list.sort(Z_COMPARATOR);

        KDNode node = null;
        List<XYZPoint> less = new ArrayList<>(list.size());
        List<XYZPoint> more = new ArrayList<>(list.size());
        if (list.size() > 0) {
            int medianIndex = list.size() / 2;
            node = new KDNode(list.get(medianIndex), k, depth);
            // Process list to see where each non-median point lies
            for (int i = 0; i < list.size(); i++) {
                if (i == medianIndex)
                    continue;
                XYZPoint p = list.get(i);
                // Cannot assume points before the median are less since they could be equal
                if (KDNode.compareTo(depth, k, p, node.getId()) <= 0) {
                    less.add(p);
                } else {
                    more.add(p);
                }
            }

            if ((medianIndex-1 >= 0) && less.size() > 0) {
                node.setLesser(createNode(less, k, depth + 1));
                node.getLesser().setParent(node);
            }

            if ((medianIndex <= list.size()-1) && more.size() > 0) {
                node.setGreater(createNode(more, k, depth + 1));
                node.getGreater().setParent(node);
            }
        }

        return node;
    }

    /**
     * Adds value to the tree. Tree can contain multiple equal values.
     *
     * @param value
     *            T to add to the tree.
     * @return True if successfully added to tree.
     */
    public boolean add(T value) {
        if (value == null)
            return false;

        if (root == null) {
            root = new KDNode(value);
            return true;
        }

        KDNode node = root;
        while (true) {
            if (KDNode.compareTo(node.getDepth(), node.getK(), value, node.getId()) <= 0) {
                // Lesser
                if (node.getLesser() == null) {
                    KDNode newNode = new KDNode(value, k, node.getDepth() + 1);
                    newNode.setParent(node);
                    node.setLesser(newNode);
                    break;
                }
                node = node.getLesser();
            } else {
                // Greater
                if (node.getGreater() == null) {
                    KDNode newNode = new KDNode(value, k, node.getDepth() + 1);
                    newNode.setParent(node);
                    node.setGreater(newNode);
                    break;
                }
                node = node.getGreater();
            }
        }

        return true;
    }

    /**
     * Does the tree contain the value.
     *
     * @param value
     *            T to locate in the tree.
     * @return True if tree contains value.
     */
    public boolean contains(T value) {
        if (value == null || root == null)
            return false;

        KDNode node = getNode(this, value);
        return (node != null);
    }

    /**
     * Locates T in the tree.
     *
     * @param tree
     *            to search.
     * @param value
     *            to search for.
     * @return KdNode or NULL if not found
     */
    private static <T extends XYZPoint> KDNode getNode(KDTree<T> tree, T value) {
        if (tree == null || tree.root == null || value == null)
            return null;

        KDNode node = tree.root;
        while (true) {
            if (node.getId().equals(value)) {
                return node;
            } else if (KDNode.compareTo(node.getDepth(), node.getK(), value, node.getId()) <= 0) {
                // Lesser
                if (node.getLesser() == null) {
                    return null;
                }
                node = node.getLesser();
            } else {
                // Greater
                if (node.getGreater() == null) {
                    return null;
                }
                node = node.getGreater();
            }
        }
    }

    /**
     * Removes first occurrence of value in the tree.
     *
     * @param value
     *            T to remove from the tree.
     * @return True if value was removed from the tree.
     */
    public boolean remove(T value) {
        if (value == null || root == null)
            return false;

        KDNode node = getNode(this, value);
        if (node == null)
            return false;

        KDNode parent = node.getParent();
        List<XYZPoint> nodes = getTree(node);
        if (parent != null) {
            if (parent.getLesser() != null && node.equals(parent.getLesser())) {
                if (nodes.size() > 0) {
                    parent.setLesser(createNode(nodes, node.getK(), node.getDepth()));
                    if (parent.getLesser() != null) {
                        parent.getLesser().setParent(parent);
                    }
                } else {
                    parent.setLesser(null);
                }
            } else {
                if (nodes.size() > 0) {
                    parent.setGreater(createNode(nodes, node.getK(), node.getDepth()));
                    if (parent.getGreater() != null) {
                        parent.getGreater().setParent(parent);
                    }
                } else {
                    parent.setGreater(null);
                }
            }
        } else {
            // root
            if (nodes.size() > 0)
                root = createNode(nodes, node.getK(), node.getDepth());
            else
                root = null;
        }

        return true;
    }

    /**
     * Gets the (sub) tree rooted at root.
     *
     * @param root
     *            of tree to get nodes for.
     * @return points in (sub) tree, not including root.
     */
    private static List<XYZPoint> getTree(KDNode root) {
        List<XYZPoint> list = new ArrayList<>();
        if (root == null)
            return list;

        if (root.getLesser() != null) {
            list.add(root.getLesser().getId());
            list.addAll(getTree(root.getLesser()));
        }
        if (root.getGreater() != null) {
            list.add(root.getGreater().getId());
            list.addAll(getTree(root.getGreater()));
        }

        return list;
    }

    /**
     * Searches the K nearest neighbor.
     *
     * @param K
     *            Number of neighbors to retrieve. Can return more than K, if
     *            last nodes are equal distances.
     * @param value
     *            to find neighbors of.
     * @return Collection of T neighbors.
     */
    @SuppressWarnings("unchecked")
    public Collection<T> nearestNeighbourSearch(int K, T value) {
        if (value == null || root == null)
            return Collections.EMPTY_LIST;

        // Map used for results
        TreeSet<KDNode> results = new TreeSet<>(new EuclideanComparator(value));

        // Find the closest leaf node
        KDNode prev = null;
        KDNode node = root;
        while (node != null) {
            if (KDNode.compareTo(node.getDepth(), node.getK(), value, node.getId()) <= 0) {
                // Lesser
                prev = node;
                node = node.getLesser();
            } else {
                // Greater
                prev = node;
                node = node.getGreater();
            }
        }
        KDNode leaf = prev;

        // Used to not re-examine nodes
        Set<KDNode> examined = new HashSet<>();

        // Go up the tree, looking for better solutions
        node = leaf;
        while (node != null) {
            // Search node
            searchNode(value, node, K, results, examined);
            node = node.getParent();
        }

        // Load up the collection of the results
        Collection<T> collection = new ArrayList<>(K);
        for (KDNode kdNode : results)
            collection.add((T) kdNode.getId());
        return collection;
    }

    private static <T extends XYZPoint> void searchNode(T value, KDNode node, int K, TreeSet<KDNode> results, Set<KDNode> examined) {
        examined.add(node);

        // Search node
        KDNode lastNode = null;
        double lastDistance = Double.MAX_VALUE;
        if (results.size() > 0) {
            lastNode = results.last();
            lastDistance = lastNode.getId().euclideanDistance(value);
        }
        Double nodeDistance = node.getId().euclideanDistance(value);
        if (nodeDistance.compareTo(lastDistance) < 0) {
            if (results.size() == K && lastNode != null)
                results.remove(lastNode);
            results.add(node);
        } else if (nodeDistance.equals(lastDistance)) {
            results.add(node);
        } else if (results.size() < K) {
            results.add(node);
        }
        lastNode = results.last();
        lastDistance = lastNode.getId().euclideanDistance(value);

        int axis = node.getDepth() % node.getK();
        KDNode lesser = node.getLesser();
        KDNode greater = node.getGreater();

        // Search children branches, if axis aligned distance is less than
        // current distance
        if (lesser != null && !examined.contains(lesser)) {
            examined.add(lesser);

            double nodePoint;
            double valuePlusDistance;
            if (axis == X_AXIS) {
                nodePoint = node.getId().x;
                valuePlusDistance = value.x - lastDistance;
            } else if (axis == Y_AXIS) {
                nodePoint = node.getId().y;
                valuePlusDistance = value.y - lastDistance;
            } else {
                nodePoint = node.getId().z;
                valuePlusDistance = value.z - lastDistance;
            }
            boolean lineIntersectsCube = (valuePlusDistance <= nodePoint);

            // Continue down lesser branch
            if (lineIntersectsCube)
                searchNode(value, lesser, K, results, examined);
        }
        if (greater != null && !examined.contains(greater)) {
            examined.add(greater);

            double nodePoint;
            double valuePlusDistance;
            if (axis == X_AXIS) {
                nodePoint = node.getId().x;
                valuePlusDistance = value.x + lastDistance;
            } else if (axis == Y_AXIS) {
                nodePoint = node.getId().y;
                valuePlusDistance = value.y + lastDistance;
            } else {
                nodePoint = node.getId().z;
                valuePlusDistance = value.z + lastDistance;
            }
            boolean lineIntersectsCube = (valuePlusDistance >= nodePoint);

            // Continue down greater branch
            if (lineIntersectsCube)
                searchNode(value, greater, K, results, examined);
        }
    }

    /**
     * Adds, in a specified queue, a given node and its related nodes (lesser, greater).
     *
     * @param node
     *              Node to check. May be null.
     *
     * @param results
     *              Queue containing all found entries. Must not be null.
     */
    @SuppressWarnings("unchecked")
    private static <T extends XYZPoint> void search(final KDNode node, final Deque<T> results) {
        if (node != null) {
            results.add((T) node.getId());
            search(node.getGreater(), results);
            search(node.getLesser(), results);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return TreePrinter.getString(this);
    }

    /**
     * Searches all entries from the first to the last entry.
     *
     * @return Iterator
     *                  allowing to iterate through a collection containing all found entries.
     */
    public Iterator<T> iterator() {
        final Deque<T> results = new ArrayDeque<>();
        search(root, results);
        return results.iterator();
    }

    /**
     * Searches all entries from the last to the first entry.
     *
     * @return Iterator
     *                  allowing to iterate through a collection containing all found entries.
     */
    public Iterator<T> reverseIterator() {
        final Deque<T> results = new ArrayDeque<>();
        search(root, results);
        return results.descendingIterator();
    }

}