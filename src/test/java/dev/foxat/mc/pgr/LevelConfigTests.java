package dev.foxat.mc.pgr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.foxat.mc.pgr.level.position.LevelBoxRegion;
import dev.foxat.mc.pgr.level.Level;
import dev.foxat.mc.pgr.level.position.LevelPos;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LevelConfigTests {

    static final Level exampleConfig = Level.builder()
            .name("ExampleLevel")
            .spawnPoint(new LevelPos(0.5, 65, 0.5, 0, 0))
            .startRegion(new LevelBoxRegion(
                    new LevelPos(-5, 60, -5, 0, 0),
                    new LevelPos(5, 70, 5, 0, 0)
            ))
            .endRegion(new LevelBoxRegion(
                    new LevelPos(200, 20, 200, 0, 0),
                    new LevelPos(220, 22, 220, 0, 0)
            ))
            .build();

    static final String exampleConfigString = """
            {
                "levelName":"ExampleLevel",
                "spawnPoint":{ "x":0.5, "y":65.0, "z":0.5, "yaw":0.0, "pitch":0.0 },
                "startRegion":{
                    "@type":"Box",
                    "a":{ "x":-5.0, "y":60.0, "z":-5.0, "yaw":0.0, "pitch":0.0 },
                    "b":{ "x":5.0, "y":70.0, "z":5.0, "yaw":0.0, "pitch":0.0 }
                },
                "endRegion":{
                    "@type":"Box",
                    "a":{ "x":200.0, "y":20.0, "z":200.0, "yaw":0.0, "pitch":0.0 },
                    "b":{ "x":220.0, "y":22.0, "z":220.0, "yaw":0.0, "pitch":0.0 }
                }
            }
            """;

    @Test
    void serializationTest() throws JsonProcessingException {
        String string = new ObjectMapper().writeValueAsString(exampleConfig);
    }

    @Test
    void deserializationTest() throws JsonProcessingException {
        Level level = new ObjectMapper().readValue(exampleConfigString, Level.class);
        Assertions.assertEquals(level.getName(), exampleConfig.getName());
    }

}
