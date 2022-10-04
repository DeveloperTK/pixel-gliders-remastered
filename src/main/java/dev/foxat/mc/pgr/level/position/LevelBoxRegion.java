package dev.foxat.mc.pgr.level.position;

import net.minestom.server.coordinate.Point;

/**
 * Example:
 * <code><pre>
 * {
 *     "@type": "Box",
 *     "a": { x: 0, y: 0, z: 0, yaw: 0, pitch: 0 },
 *     "b": { x: 10, y: 0, z: 10, yaw: 0, pitch: 0 }
 * }
 * </pre></code>
 *
 * @param a
 * @param b
 */
public record LevelBoxRegion(LevelPos a, LevelPos b) implements LevelRegion {

    @Override
    public boolean isInside(Point c) {
        return a.x() <= c.x() && b.x() >= c.x() &&
                a.y() <= c.y() && b.y() >= c.y() &&
                a.z() <= c.z() && b.z() >= c.z();
    }
}
