package dev.foxat.mc.pgr.game;

import lombok.Getter;
import net.minestom.server.MinecraftServer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class GameInstanceManager {

    @Getter
    private static final Map<String, PixelGliders> ffas;

    @Getter
    private static final Map<UUID, PixelGliders> matches;

    static {
        ffas = new HashMap<>();
        matches = new HashMap<>();
    }

    private GameInstanceManager() {

    }

    public static Optional<PixelGliders> findInstance(UUID instanceId) {
        if (matches.containsKey(instanceId)) {
            return Optional.of(matches.get(instanceId));
        }

        return ffas.values().stream()
                .filter(game -> game.getUniqueId().equals(instanceId))
                .findAny();
    }

    public static void registerFFA(PixelGliders game) {
        MinecraftServer.getInstanceManager().registerInstance(game);
        ffas.put(game.getName(), game);
    }

    public static void registerMatch(PixelGliders game) {
        MinecraftServer.getInstanceManager().registerInstance(game);
        matches.put(game.getUniqueId(), game);
    }

    public static void unregisterGame(PixelGliders game) {
        if (ffas.containsKey(game.getName())) {
            MinecraftServer.getInstanceManager().unregisterInstance(game);
            ffas.remove(game.getName());
        } else if (matches.containsKey(game.getUniqueId())) {
            MinecraftServer.getInstanceManager().unregisterInstance(game);
            matches.remove(game.getUniqueId());
        } else {
            throw new IllegalArgumentException("Game was not previously registered!");
        }
    }

}
