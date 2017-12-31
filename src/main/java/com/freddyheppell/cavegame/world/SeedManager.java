package com.freddyheppell.cavegame.world;

import com.freddyheppell.cavegame.world.coord.RegionCoordinate;
import com.freddyheppell.cavegame.world.coord.WorldCoordinate;

public class SeedManager {
    private String worldSeed;


    public SeedManager(String worldSeed) {
        this.worldSeed = worldSeed;
    }

    /**
     * Encode each value of the string as a long value
     *
     * @param string The string to be encoded
     * @return A long representation
     */
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

    /**
     * Get the global seed
     *
     * @return
     */
    public long getWorldSeed() {
        return strToLong(worldSeed);
    }

    /**
     * Get the region's individual seed
     *
     * @param regionCoordinate The coordinates of the region
     * @return The region's seed
     */
    public long getRegionSeed(RegionCoordinate regionCoordinate) {
        char regionSeedSeparationChar = '|';
        String regionSeed = worldSeed + regionSeedSeparationChar + regionCoordinate.rx + regionSeedSeparationChar + regionCoordinate.ry;

        return strToLong(regionSeed);
    }

}
