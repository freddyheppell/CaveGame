package com.freddyheppell.cavegame.world;

import com.freddyheppell.cavegame.entities.Entity;
import com.freddyheppell.cavegame.world.cells.Cell;
import com.freddyheppell.cavegame.world.coord.CoordinateProperties;
import com.freddyheppell.cavegame.world.coord.RegionCoordinate;
import com.freddyheppell.cavegame.world.coord.WorldCoordinate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class World {
    private RegionManager regionManager;
    private static final Logger logger = LogManager.getLogger();


    public World(SeedManager seedManager, String worldName) {
        this.regionManager = new RegionManager(worldName, seedManager);
    }


    public Region getRegion(RegionCoordinate regionCoordinate) {
        return regionManager.getRegion(regionCoordinate);
    }

    public Cell getCell(WorldCoordinate worldCoordinate) {
        Region region = getRegion(worldCoordinate.getRegionCoordinate());
        return region.getCellIfExists(worldCoordinate);
    }

    public WorldCoordinate getSpawnLocation() {
        int n = 0;
        boolean found = false;

        while (!found) {
            WorldCoordinate spawnCoordinate = CoordinateProperties.getSpiralCoordinate(n);
            logger.debug("Checking {} for spawn suitability", spawnCoordinate.toString());
            if (getCell(spawnCoordinate).isSpawnAllowed()) {
                return spawnCoordinate;
            }
            n += 1;
        }

        return null;
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }

    /**
     * Check if two coordinates have line of sight to each other
     *
     * @param coordinate0 The first coordinate
     * @param coordinate1 The second coordinate
     * @return If Line of Sight could be found
     */
    public boolean hasLineOfSight(WorldCoordinate coordinate0, WorldCoordinate coordinate1) {
        if (Math.abs(coordinate0.wy - coordinate1.wy) > Math.abs(coordinate0.wx - coordinate1.wx)) {
            // Swap the coordinates within each pair
            coordinate0 = new WorldCoordinate(coordinate0.wy, coordinate0.wx);
            coordinate1 = new WorldCoordinate(coordinate1.wy, coordinate1.wx);
        }
        if (coordinate0.wx > coordinate1.wx) {
            // Swap the coordinates
            WorldCoordinate tempC = coordinate0;
            coordinate0 = coordinate1;
            coordinate1 = tempC;
        }

        int E = Math.abs(coordinate1.wy - coordinate0.wy);
        int S;

        if (coordinate0.wy > coordinate1.wy) {
            S = -1;
        } else {
            S = 1;
        }

        int dx = coordinate1.wx - coordinate0.wx;
        int e = dx >> 1;
        int y = coordinate0.wy;

        for (int x = coordinate0.wx; x <= coordinate1.wx; x++) {
            WorldCoordinate checkCoordinate;
            if (Math.abs(coordinate0.wy - coordinate1.wy) > Math.abs(coordinate0.wx - coordinate1.wx)) {
                // Check (y,x)
                checkCoordinate = new WorldCoordinate(y, x);
            } else {
                // Check (x,y)
                checkCoordinate = new WorldCoordinate(x, y);
            }

            if (getCell(checkCoordinate).isBlocking()) {
                return false;
            }

            e -= E;

            if (e < 0) {
                y += S;
                e += dx;
            }
        }

        return true;
    }
}
