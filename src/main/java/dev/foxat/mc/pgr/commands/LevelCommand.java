package dev.foxat.mc.pgr.commands;

import dev.foxat.mc.pgr.game.GameInstanceManager;
import dev.foxat.mc.pgr.game.PixelGliders;
import dev.foxat.mc.pgr.level.Level;
import dev.foxat.mc.pgr.level.LevelManager;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Player;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LevelCommand extends Command {

    public LevelCommand() {
        super("level");

        setDefaultExecutor((sender, context) -> {
            if (sender instanceof Player player && player.getInstance() != null) {
                UUID instanceId = player.getInstance().getUniqueId();
                Optional<PixelGliders> instance = GameInstanceManager.findInstance(instanceId);
                if (instance.isPresent()) {
                    Level level = instance.get().getLevel();
                    player.sendMessage("Current Level: " + level.getName()
                            + " (" + level.getDifficulty().name()
                            + ") by " + level.getCreatedBy());
                    return;
                }
            }

            String levels = LevelManager.getAll().stream().map(Level::getName).collect(Collectors.joining(", "));
            sender.sendMessage("Available Levels: " + levels);
        });

        ArgumentString levelArgument = ArgumentType.String("level");
        levelArgument.setSuggestionCallback((sender, context, suggestion) -> {
            String typed = context.get(levelArgument);
            boolean blank = typed == null || typed.isBlank();

            LevelManager.getAll().stream()
                    .map(Level::getName)
                    .filter(s -> blank || s.startsWith(typed))
                    .map(SuggestionEntry::new)
                    .forEach(suggestion::addEntry);
        });

        setArgumentCallback((sender, exception) -> {
            sender.sendMessage(exception.getInput() + " is not a level name!");
        }, levelArgument);

        addSyntax((sender, context) -> {
            String levelName = context.get(levelArgument);
            if (!LevelManager.contains(levelName)) {
                sender.sendMessage("The level " + levelName + " does not exist");
            } else {
                Level level = LevelManager.get(levelName);
                sender.sendMessage(level.getName() + " (" + level.getDifficulty().name()
                        + ") by " + level.getCreatedBy());
            }
        }, levelArgument);
    }

}
