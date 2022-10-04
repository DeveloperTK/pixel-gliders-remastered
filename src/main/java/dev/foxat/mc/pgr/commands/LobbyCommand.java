package dev.foxat.mc.pgr.commands;

import dev.foxat.mc.pgr.Lobby;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;

public class LobbyCommand extends Command {

    public LobbyCommand() {
        super("lobby", "l", "hub", "h");

        setDefaultExecutor(this::defaultExecutor);
    }

    private void defaultExecutor(CommandSender sender, CommandContext context) {
        if (sender instanceof Player player) {
            if (player.getInstance() == null) {
                player.sendMessage("Please try again in a few seconds");
            } else if (Lobby.INSTANCE.getUniqueId().equals(player.getInstance().getUniqueId())) {
                player.sendMessage("You are already inside the lobby");
            } else {
                player.setInstance(Lobby.INSTANCE);
            }
        } else {
            sender.sendMessage("You need to execute this command as player");
        }
    }

}
