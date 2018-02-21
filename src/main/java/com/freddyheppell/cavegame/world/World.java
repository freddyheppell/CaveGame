package com.freddyheppell.cavegame.world;

import com.freddyheppell.cavegame.config.Config;
import com.freddyheppell.cavegame.world.cells.Cell;
import com.freddyheppell.cavegame.world.coord.RegionCoordinate;
import com.freddyheppell.cavegame.world.coord.WorldCoordinate;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class World {
    private SeedManager seedManager;
    private RegionManager regionManager;
    public Region region;

    public World(SeedManager seedManager) {
        this.seedManager = seedManager;
        this.regionManager = new RegionManager("Testing World", seedManager);
    }


    public Region getRegion(RegionCoordinate regionCoordinate) {
        return regionManager.getRegion(regionCoordinate);
    }

    public Cell getCell(WorldCoordinate worldCoordinate) {
        Region region = getRegion(worldCoordinate.getRegionCoordinate());
        return region.getCellIfExists(worldCoordinate);
    }

    // TODO Finish implementing the cell finding logic
    public WorldCoordinate getEmptyCellNear(WorldCoordinate worldCoordinate) {
        // First, check if the supplied cell is empty
        if (!getCell(worldCoordinate).isBlocking()) {
            return worldCoordinate;
        }

        return WorldCoordinate.origin();
    }
}
