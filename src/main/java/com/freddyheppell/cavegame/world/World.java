package com.freddyheppell.cavegame.world;

import com.freddyheppell.cavegame.config.Config;
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
