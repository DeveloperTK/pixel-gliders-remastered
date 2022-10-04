package dev.foxat.mc.pgr.game;

import dev.foxat.mc.pgr.level.Level;
import dev.foxat.mc.pgr.math.Vector;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.event.instance.AddEntityToInstanceEvent;
import net.minestom.server.event.instance.RemoveEntityFromInstanceEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerStopSneakingEvent;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.world.DimensionType;

import java.util.UUID;

public class PixelGliders extends InstanceContainer {

    @Getter
    private final String name;

    @Getter
    private final Level level;

    public PixelGliders(String name, Level level) {
        super(UUID.randomUUID(), DimensionType.OVERWORLD);
        this.name = name;
        this.level = level;

        setChunkLoader(new AnvilLoader(getClass().getResource("/levels/debug").getPath()));
        eventNode().addListener(AddEntityToInstanceEvent.class, event -> {
            if (event.getEntity() instanceof Player player) {
                player.setRespawnPoint(level.getSpawnPoint().toPos());
                MinecraftServer.getSchedulerManager().scheduleNextTick(() ->
                        player.teleport(level.getSpawnPoint().toPos()));
            }
        });

        eventNode().addListener(RemoveEntityFromInstanceEvent.class, event -> {
            if (event.getInstance().getUniqueId().equals(getUniqueId())) {
                if (event.getEntity() instanceof Player && getPlayers().size() <= 1) {
                    MinecraftServer.getSchedulerManager().scheduleNextTick(() ->
                            GameInstanceManager.unregisterGame(this));
                }
            }
        });

        final Vector center = new Vector(23, -7.5, 15.5);
        final Vector normal = new Vector(1, 0, 0);

        eventNode().addListener(PlayerMoveEvent.class, event -> {
            if (!event.getPlayer().isFlyingWithElytra()) {
                if (event.getPlayer().isOnGround()) {
                    if (!this.level.getStartRegion().isInside(event.getPlayer().getPosition())) {
                        reset(event.getPlayer());
                    }
                }
                return;
            } else {
                double[] oldPosition = new double[]{
                        event.getPlayer().getPosition().x(),
                        event.getPlayer().getPosition().y(),
                        event.getPlayer().getPosition().z()
                };

                double[] newPosition = new double[]{
                        event.getNewPosition().x(),
                        event.getNewPosition().y(),
                        event.getNewPosition().z()
                };

                Vector oldVector = new Vector(oldPosition);
                Vector newVector = new Vector(newPosition);
                Vector lineDirection = oldVector.plus(newVector.scale(-1));

                if (true) {
                    Vector intersection = lineIntersection(center, normal, oldVector, lineDirection);

                    if (intersection != null) {
                        event.getPlayer().sendMessage("distance: " + center.plus(intersection.scale(-1)).length());
                    }
                }
            }
        });

        eventNode().addListener(PlayerStopSneakingEvent.class, event -> {
            event.getPlayer().teleport(event.getPlayer().getPosition().add(0, 2, 0));
            event.getPlayer().setFlyingWithElytra(true);
        });
    }

    public static Vector lineIntersection(Vector planePoint, Vector planeNormal, Vector linePoint, Vector lineDirection) {
        // https://stackoverflow.com/questions/5666222/3d-line-plane-intersection
        if (planeNormal.dot(lineDirection.normalize()) == 0) {
            return null;
        }

        double t = (planeNormal.dot(planePoint) - planeNormal.dot(linePoint)) / planeNormal.dot(lineDirection.normalize());
        return linePoint.plus(lineDirection.normalize().scale(t));
    }


    private void reset(Player player) {
        player.sendMessage("Reset!");
        player.teleport(level.getSpawnPoint().toPos());
    }

}
