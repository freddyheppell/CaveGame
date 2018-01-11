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
    //    private RegionCache regionCache;
    private Map<RegionCoordinate, Region> regions;
    public Region region;

    public World(SeedManager seedManager) {
        this.seedManager = seedManager;
        this.regions = new HashMap<>();
//        this.regionCache = new RegionCache();
    }

    /**
     * Create a region at the specified coordinates
     *
     * @param regionCoordinate The coordinates of the region
     */
    public void createRegion(RegionCoordinate regionCoordinate) {
//        System.out.println("Now creating region " + regionCoordinate);
        System.out.println(regions.keySet());
        if (regions.containsKey(regionCoordinate)) {
            throw new RuntimeException("Attempted to generate existing region");
        }

        Region region = new Region(seedManager.getRegionSeed(regionCoordinate));

        region.populateRandomly();

        for (int i = 0; i < Config.ITERATION_COUNT; i++) {
            region.iteration();
        }

        regions.put(regionCoordinate, region);
    }

    public Region getOrGenerateRegion(RegionCoordinate regionCoordinate) {
        if (regions.containsKey(regionCoordinate)) {
//            System.out.println("Already has " + regionCoordinate);
            return regions.get(regionCoordinate);
        } else {
//            System.out.println("Creating " + regionCoordinate);
            createRegion(regionCoordinate);
            return regions.get(regionCoordinate);
        }
    }

    public Region getRegion(RegionCoordinate regionCoordinate) {
        return getOrGenerateRegion(regionCoordinate);
    }

    public Cell getCell(WorldCoordinate worldCoordinate) {
        Region region = getRegion(worldCoordinate.getRegionCoordinate());
        return region.getCellIfExists(worldCoordinate);
    }

    public WorldCoordinate getEmptyCellNear(WorldCoordinate worldCoordinate) {
        // First, check if the supplied cell is empty
        if (!getCell(worldCoordinate).isBlocking()) {
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
