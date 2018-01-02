package com.freddyheppell.cavegame.world;

import com.freddyheppell.cavegame.config.Config;
import com.freddyheppell.cavegame.world.cells.Cell;
import com.freddyheppell.cavegame.world.coord.RegionCoordinate;
import com.freddyheppell.cavegame.world.coord.WorldCoordinate;
import com.google.gson.Gson;

import java.util.HashMap;

public class World {
    private SeedManager seedManager;
    //    private RegionCache regionCache;
    public Region region;

    public World(SeedManager seedManager) {
        this.seedManager = seedManager;
//        this.regionCache = new RegionCache();
    }

    /**
     * Create a region at the specified coordinates
     *
     * @param regionCoordinate The coordinates of the region
     */
    public void createRegion(RegionCoordinate regionCoordinate) {
        Region region = new Region(seedManager.getRegionSeed(regionCoordinate));

        region.populateRandomly();

        for (int i = 0; i < Config.ITERATION_COUNT; i++) {
            region.iteration();
        }

        this.region = region;
    }

    public Region getRegion(RegionCoordinate regionCoordinate) {
        return region;
    }

    public Cell getCell(WorldCoordinate worldCoordinate) {
        Region region = getRegion(worldCoordinate.getRegionCoordinate());
        return region.getCellIfExists(worldCoordinate);
    }

    public WorldCoordinate getEmptyCellNear(WorldCoordinate worldCoordinate) {
        // First, check if the supplied cell is empty
        if (getCell(worldCoordinate).isNonBlocking()) {
            return worldCoordinate;
        }

        return WorldCoordinate.origin();
    }


    /**
     * Get the JSON representation of the world
     *
     * @return
     */
    public String getSaveData() {
        HashMap<String, Object> saveData = new HashMap<>();
        saveData.put("seed", seedManager);

        Gson gson = new Gson();
        return gson.toJson(saveData);
    }

}
