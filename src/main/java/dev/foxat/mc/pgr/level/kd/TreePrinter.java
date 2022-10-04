package dev.foxat.mc.pgr.level.kd;

import java.util.ArrayList;
import java.util.List;

public class TreePrinter {

    public static <T extends XYZPoint> String getString(KDTree<T> tree) {
        if (tree.getRoot() == null)
            return "Tree has no nodes.";
        return getString(tree.getRoot(), "", true);
    }

    private static String getString(KDNode node, String prefix, boolean isTail) {
        StringBuilder builder = new StringBuilder();

        if (node.getParent() != null) {
            String side = "left";
            if (node.getParent().getGreater() != null && node.getId().equals(node.getParent().getGreater().getId()))
                side = "right";
            builder.append(prefix).append(isTail ? "└── " : "├── ").append("[").append(side).append("] ")
                    .append("depth=").append(node.getDepth()).append(" id=").append(node.getId()).append("\n");
        } else {
            builder.append(prefix).append(isTail ? "└── " : "├── ").append("depth=").append(node.getDepth())
                    .append(" id=").append(node.getId()).append("\n");
        }
        List<KDNode> children = null;
        if (node.getLesser() != null || node.getGreater() != null) {
            children = new ArrayList<>(2);
            if (node.getLesser() != null)
                children.add(node.getLesser());
            if (node.getGreater() != null)
                children.add(node.getGreater());
        }
        if (children != null) {
            for (int i = 0; i < children.size() - 1; i++) {
                builder.append(getString(children.get(i), prefix + (isTail ? "    " : "│   "), false));
            }
            if (children.size() >= 1) {
                builder.append(getString(children.get(children.size() - 1), prefix + (isTail ? "    " : "│   "),
                        true));
            }
        }

        return builder.toString();
    }
}