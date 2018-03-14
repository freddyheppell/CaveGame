package com.freddyheppell.cavegame.world;

import com.freddyheppell.cavegame.config.Config;
import com.freddyheppell.cavegame.entities.Entity;
import com.freddyheppell.cavegame.world.cells.Cell;
import com.freddyheppell.cavegame.world.coord.CoordinateProperties;
import com.freddyheppell.cavegame.world.coord.RegionCoordinate;
import com.freddyheppell.cavegame.world.coord.WorldCoordinate;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class World {
    private SeedManager seedManager;
    private RegionManager regionManager;
    private HashMap<WorldCoordinate, Entity> entities = new HashMap<>();
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
    public WorldCoordinate getSpawnLocation() {
        int n = 0;
        boolean found = false;

        while (!found) {
            WorldCoordinate spawnCoordinate = CoordinateProperties.getSpiralCoordinate(n);
            System.out.println("Testing" + spawnCoordinate.toString());
            if (getCell(spawnCoordinate).isSpawnAllowed()) {
                return spawnCoordinate;
            }
            n += 1;
        }

        return null;
    }

    public void resaveRegion(RegionCoordinate regionCoordinate) {
        regionManager.resaveRegion(regionCoordinate);
    }
}
