package dev.foxat.mc.pgr;

import dev.foxat.mc.pgr.game.PixelGliders;
import dev.foxat.mc.pgr.math.Vector;
import org.junit.jupiter.api.Test;

class VectorCalculationTests {

    @Test
    void intersectionTest() {
        final Vector M = new Vector(23, -7.5, 15.5);
        final Vector n = new Vector(1, 0, 0);
        final double r = 3;

        final Vector A = new Vector(22, -7, 15);
        final Vector B = new Vector(24, -8, 16);

        // i)
        final double d = -1 * n.dot(M);
        final double i = (n.get(0) * A.get(0)) + (n.get(1) * A.get(1)) + (n.get(2) * A.get(2)) + d;
        final double j = (n.get(0) * B.get(0)) + (n.get(1) * B.get(1)) + (n.get(2) * B.get(2)) + d;

        if (sgn(i) == sgn(j)) {
            System.out.println("No intersection! Failed at i)");
            return;
        }

        // ii)
        Vector AB = B.plus(A.scale(-1));
        Vector I = lineIntersection(M, n, A, AB);

        if (I == null) {
            System.out.println("No intersection! Failed at ii)");
            return;
        }

        Vector IM = M.plus(I.scale(-1));
        if (IM.length() > r) {
            System.out.println("Intersection in plane but outside of circle!");
        } else {
            System.out.println("Intersection through circle!");
        }
    }

    static int sgn(double x) {
        if (Math.abs(x) != x) {
            return -1;
        } else {
            return 1;
        }
    }

    static Vector lineIntersection(Vector planePoint, Vector planeNormal, Vector linePoint, Vector lineDirection) {
        // https://stackoverflow.com/questions/5666222/3d-line-plane-intersection
        if (planeNormal.dot(lineDirection.normalize()) == 0) {
            return null;
        }

        double t = (planeNormal.dot(planePoint) - planeNormal.dot(linePoint)) / planeNormal.dot(lineDirection.normalize());
        return linePoint.plus(lineDirection.normalize().scale(t));
    }

}
