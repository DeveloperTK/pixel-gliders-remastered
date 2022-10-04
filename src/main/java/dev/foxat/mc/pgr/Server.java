package dev.foxat.mc.pgr;

import dev.foxat.mc.pgr.commands.FreeForAllCommand;
import dev.foxat.mc.pgr.commands.LevelCommand;
import dev.foxat.mc.pgr.commands.LobbyCommand;
import dev.foxat.mc.pgr.commands.WhereamiCommand;
import dev.foxat.mc.pgr.level.LevelManager;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.world.Difficulty;

public class Server {

    @Getter
    private static Server instance;

    @Getter
    private final MinecraftServer minecraftServer;

    private Server() {
        this.minecraftServer = MinecraftServer.init();
    }

    private void start() {
        MinecraftServer.getInstanceManager().registerInstance(Lobby.INSTANCE);

        MinecraftServer.getCommandManager().register(new WhereamiCommand());
        MinecraftServer.getCommandManager().register(new LobbyCommand());
        MinecraftServer.getCommandManager().register(new FreeForAllCommand());
        MinecraftServer.getCommandManager().register(new LevelCommand());

        MinecraftServer.getGlobalEventHandler().addListener(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(Lobby.INSTANCE);
            player.setRespawnPoint(new Pos(4.5, 21.0, 4.5));
        });

        MinecraftServer.setBrandName("PixelGliders");
        MinecraftServer.setDifficulty(Difficulty.PEACEFUL);

        minecraftServer.start("127.0.0.1", 25565);
    }

    public static void main(String[] args) {
        instance = new Server();
        LevelManager.load();
        MojangAuth.init();
        instance.start();
    }
}
