package dev.foxat.mc.pgr.level.position;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.minestom.server.coordinate.Pos;

public record LevelPos(
        @JsonProperty(value = "x", required = true)
        double x,

        @JsonProperty(value = "y", required = true)
        double y,

        @JsonProperty(value = "z", required = true)
        double z,

        @JsonProperty(value = "yaw", defaultValue = "0")
        float yaw,

        @JsonProperty(value = "pitch", defaultValue = "0")
        float pitch
) {

    public Pos toPos() {
        return new Pos(x, y, z, yaw, pitch);
    }

}
