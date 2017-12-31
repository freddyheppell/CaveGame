package com.freddyheppell.cavegame.world;

import com.freddyheppell.cavegame.world.coord.WorldCoordinate;

public class SeedManager {
    private String worldSeed;


    public SeedManager(String worldSeed) {
        this.worldSeed = worldSeed;
    }

    private long strToLong(String string) {
        if (string == null) {
            return 0;
        }
        long hash = 0;
        for (char c : string.toCharArray()) {
            hash = 31L * hash + c;
        }
        return hash;
    }

    public long getWorldSeed() {
        return strToLong(worldSeed);
    }

    public long getRegionSeed(WorldCoordinate worldCoordinate) {
        char regionSeedSeparationChar = '|';
        String regionSeed = worldSeed + regionSeedSeparationChar + worldCoordinate.wx + regionSeedSeparationChar + worldCoordinate.wy;

        return strToLong(regionSeed);
    }

}
