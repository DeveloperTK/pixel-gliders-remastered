package dev.foxat.mc.pgr;

import dev.foxat.mc.pgr.utils.FullbrightDimension;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.InstanceContainer;

import java.util.UUID;

public class Lobby extends InstanceContainer {

    public static final Lobby INSTANCE = new Lobby();

    static {
        MinecraftServer.getInstanceManager().registerInstance(INSTANCE);
    }

    private static final UUID zeroUUID = new UUID(0, 0);

    private Lobby() {
        super(zeroUUID, FullbrightDimension.INSTANCE);
        AnvilLoader anvilLoader = new AnvilLoader(Resources.LOBBY_ANVIL_WORLD_PATH.getPath());
        setChunkLoader(anvilLoader);
        setTimeRate(0);
    }

}
