package dev.foxat.mc.pgr;

import lombok.AllArgsConstructor;

import java.net.URL;

@AllArgsConstructor
public enum Resources {

    LOBBY_ANVIL_WORLD_PATH("/lobby"),
    DEBUG_ANVIL_WORLD_PATH("/levels/debug")

    ;

    private final String resourcePath;

    public String getPath() {
        URL resource = getClass().getResource(resourcePath);

        if (resource == null || resource.getFile().isBlank()) {
            throw new IllegalStateException("Built-in resource " + this.name() + " not found");
        }

        return resource.getPath();
    }

}
