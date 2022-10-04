package dev.foxat.mc.pgr;

import dev.foxat.mc.pgr.utils.FullbrightDimension;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.instance.AddEntityToInstanceEvent;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.InstanceContainer;

import java.util.UUID;

public class Lobby extends InstanceContainer {

    public static final Lobby INSTANCE = new Lobby(UUID.randomUUID());

    private Lobby(UUID uniqueId) {
        super(uniqueId, FullbrightDimension.INSTANCE);
        AnvilLoader anvilLoader = new AnvilLoader(Resources.LOBBY_ANVIL_WORLD_PATH.getPath());
        setChunkLoader(anvilLoader);
        setTimeRate(0);

        eventNode().addListener(AddEntityToInstanceEvent.class, event -> {
            if (event.getEntity() instanceof Player player) {
                MinecraftServer.getSchedulerManager().scheduleNextTick(() ->
                        player.teleport(new Pos(4.5, 21, 4.5)));
            }
        });
    }

}
