package dev.foxat.mc.pgr.commands;

import dev.foxat.mc.pgr.Lobby;
import dev.foxat.mc.pgr.game.GameInstanceManager;
import dev.foxat.mc.pgr.game.PixelGliders;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public class WhereamiCommand extends Command {

    public WhereamiCommand() {
        super("whereami");

        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("This command can only be executed by a player!");
                return;
            }

            Instance instance = player.getInstance();
            if (instance == null) {
                player.sendMessage("You are not assigned to an instance!");
            } else if (instance.getUniqueId().equals(Lobby.INSTANCE.getUniqueId())) {
                player.sendMessage("You are in the lobby");
            } else if (instance instanceof PixelGliders game) {
                player.sendMessage("You are in game instance " + game.getName());
            }

            player.sendMessage("=== INSTANCES ===");
            player.sendMessage("Lobby: " + Lobby.INSTANCE.getPlayers().size() + " Players");
            player.sendMessage("FFAs:");
            for (PixelGliders game : GameInstanceManager.getFfas().values()) {
                player.sendMessage(" - " + game.getName() + " (" + game.getPlayers().size() + " players)");
            }
            player.sendMessage("Matches:");
            for (PixelGliders game : GameInstanceManager.getMatches().values()) {
                player.sendMessage(" - " + game.getName() + " (" + game.getPlayers().size() + " players)");
            }
        });
    }

}
