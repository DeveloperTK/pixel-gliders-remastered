package dev.foxat.mc.pgr.level;

import dev.foxat.mc.pgr.level.position.LevelPos;
import dev.foxat.mc.pgr.level.position.LevelRegion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Level {

    private String name;
    private String createdBy;
    private LevelDifficulty difficulty;

    private LevelPos spawnPoint;
    private LevelRegion startRegion;
    private LevelRegion endRegion;

    private List<Object> rings;

}
