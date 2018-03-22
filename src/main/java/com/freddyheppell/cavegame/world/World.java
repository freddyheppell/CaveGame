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
    private SeedManager seedManager;
    private RegionManager regionManager;
    private HashMap<WorldCoordinate, Entity> entities = new HashMap<>();
    public Region region;

    private static final Logger logger = LogManager.getLogger();


    public World(SeedManager seedManager) {
        this.seedManager = seedManager;
        this.regionManager = new RegionManager("Test", seedManager);
    }


    public Region getRegion(RegionCoordinate regionCoordinate) {
        return regionManager.getRegion(regionCoordinate);
    }

    public Cell getCell(WorldCoordinate worldCoordinate) {
        Region region = getRegion(worldCoordinate.getRegionCoordinate());
        return region.getCellIfExists(worldCoordinate);
    }

    // TODO Finish implementing the cell finding logic
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
}
