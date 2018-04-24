package com.freddyheppell.cavegame.save;

import com.freddyheppell.cavegame.config.Config;
import com.freddyheppell.cavegame.world.World;
import com.google.gson.*;

import java.lang.reflect.Type;

public class WorldSerialiser implements JsonSerializer<World> {
    @Override
    public JsonElement serialize(World src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("worldName", new JsonPrimitive(src.getWorldName()));
        result.add("configurationHash", new JsonPrimitive(Config.getConfigurationHash()));
        result.add("worldSeed", new JsonPrimitive(src.getWorldSeed()));
        return result;
    }
}
