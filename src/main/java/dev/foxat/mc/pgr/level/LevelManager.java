package dev.foxat.mc.pgr.level;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.foxat.mc.pgr.Resources;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
public class LevelManager {

    @Getter
    private static boolean initialized;

    private static final Map<String, Level> levels;

    static {
        levels = new HashMap<>();
    }

    private LevelManager() {

    }

    public static boolean contains(String levelName) {
        if (!initialized) throw new IllegalStateException("LevelManager needs to be initialized");
        return levels.containsKey(levelName);
    }

    public static Level get(String levelName) {
        if (!initialized) throw new IllegalStateException("LevelManager needs to be initialized");
        return levels.get(levelName);
    }

    public static Collection<Level> getAll() {
        if (!initialized) throw new IllegalStateException("LevelManager needs to be initialized");
        return levels.values();
    }

    public static void load() {
        try (Stream<Path> walkStream = Files.walk(Path.of(Resources.BUILTIN_LEVELS_BASE_PATH.getPath()))) {
            walkStream.map(Path::toFile)
                    .filter(File::isFile)
                    .filter(file -> file.getName().equals(Resources.LEVEL_FILE_NAME))
                    .forEach(LevelManager::loadLevel);
        } catch (IOException exception) {
            log.error("Could not read all level files", exception);
        }

        initialized = true;
    }

    private static void loadLevel(File levelFile) {
        try {
            Level level = new ObjectMapper().readValue(levelFile, Level.class);
            levels.put(level.getName(), level);
            log.info("Loaded level {}", level.getName());
        } catch (JsonProcessingException exception) {
            log.error("Could not read level config " + levelFile.getPath(), exception);
        } catch (IOException exception) {
            log.error("Error while reading level file" + levelFile.getPath(), exception);
        }
    }

}
