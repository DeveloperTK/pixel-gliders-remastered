package dev.foxat.mc.pgr.level.ring;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.foxat.mc.pgr.game.PixelGliders;
import dev.foxat.mc.pgr.level.position.LevelPos;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LevelRegularRing.class, name = "LevelRegularRing"),
        @JsonSubTypes.Type(value = LevelBoostRing.class, name = "LevelBoostRing"),
        @JsonSubTypes.Type(value = LevelCheckpointRing.class, name = "LevelCheckpointRing"),
        @JsonSubTypes.Type(value = LevelPowerupRing.class, name = "LevelPowerupRing"),
})
public abstract class LevelRing {

    private int order;
    private LevelPos center;
    private float radius;

    public boolean lineIntersection(Point lineStart, Point lineEnd) {
        throw new IllegalStateException("TODO");
    }

    public abstract void pass(PixelGliders game, Player player);

}
