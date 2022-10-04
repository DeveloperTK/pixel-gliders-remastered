package dev.foxat.mc.pgr.commands;

import dev.foxat.mc.pgr.game.GameInstanceManager;
import dev.foxat.mc.pgr.game.PixelGliders;
import dev.foxat.mc.pgr.level.Level;
import dev.foxat.mc.pgr.level.LevelManager;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

import java.util.Objects;

public class FreeForAllCommand extends Command {

    public FreeForAllCommand() {
        super("join-ffa", "ffa");

        setDefaultExecutor((sender, context) -> sender.sendMessage("Please select a level!"));

        ArgumentString levelArgument = ArgumentType.String("level");

        addSyntax(this::executor, levelArgument);
    }

    private void executor(CommandSender sender, CommandContext context) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be executed by a player!");
            return;
        }

        String levelName = context.get("level");
        if (!LevelManager.contains(levelName)) {
            player.sendMessage("This level does not exist!");
            return;
        }

        if (GameInstanceManager.getFfas().containsKey(levelName)) {
            PixelGliders game = GameInstanceManager.getFfas().get(levelName);
            if (Objects.equals(player.getInstance().getUniqueId(), game.getUniqueId())) {
                player.sendMessage("You are already playing ffa on " + levelName);
            } else {
                player.setInstance(game);
            }
        } else {
            Level level = LevelManager.get(levelName);
            PixelGliders game = new PixelGliders(levelName, level);
            GameInstanceManager.registerFFA(game);
            player.setInstance(game);
        }
    }
}
