package dev.foxat.mc.pgr;

import lombok.AllArgsConstructor;

import java.net.URL;

@AllArgsConstructor
public enum Resources {

    LOBBY_ANVIL_WORLD_PATH("/lobby"),
    BUILTIN_LEVELS_BASE_PATH("/levels");

    ;

    public static final String LEVEL_FILE_NAME = "level.json";

    private final String resourcePath;

    public String getPath() {
        URL resource = getClass().getResource(resourcePath);

        if (resource == null || resource.getFile().isBlank()) {
            throw new IllegalStateException("Built-in resource " + this.name() + " not found");
        }

        return resource.getPath();
    }

}
