package com.freddyheppell.cavegame.world;

import com.freddyheppell.cavegame.config.Config;
import com.freddyheppell.cavegame.save.SaveManager;
import com.freddyheppell.cavegame.world.coord.RegionCoordinate;
import com.google.gson.Gson;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class RegionManager {
    /**
     * Cached results of checking if the region exists on disk
     */
    private HashMap<RegionCoordinate, Boolean> regionLookupCache;

    private LinkedHashMap<RegionCoordinate, Region> regionCache = new LinkedHashMap<RegionCoordinate, Region>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > Config.REGION_CACHE_SIZE;
        }
    };

    /**
     * The directory in which to put savedata
     */
    private File saveDir;
    /**
     * The game's SeedManager
     */
    private SeedManager seedManager;

    public RegionManager(String worldName, SeedManager seedManager) {
        saveDir = SaveManager.getSaveFolder(worldName);
        String worldName1 = worldName;
        this.seedManager = seedManager;
        this.regionLookupCache = new HashMap<>();
    }

    /**
     * Check if the region exists, using the cache if possible
     *
     * @param regionCoordinate The region coordinate
     * @return If the region exists
     */
    private boolean regionExists(RegionCoordinate regionCoordinate) {
        if (regionLookupCache.containsKey(regionCoordinate)) {
            // If the region is already in the cache, retrieve it
            return regionLookupCache.get(regionCoordinate);
        } else {
            // Otherwise, get the region
            boolean regionExists = SaveManager.getRegionFile(saveDir, regionCoordinate).exists();
            // and put it in the cache
            regionLookupCache.put(regionCoordinate, regionExists);

            return regionExists;
        }
    }

    public Region loadRegion(File saveDir, RegionCoordinate regionCoordinate) {
        if (regionCache.containsKey(regionCoordinate)) {
            return regionCache.get(regionCoordinate);
        } else {
            try {
                Region region = SaveManager.loadRegion(saveDir, regionCoordinate);
                regionCache.put(regionCoordinate, region);
                return region;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException("Save file could not be loaded");
            }
        }
    }

    /**
     * Get the region at the coordinates
     *
     * @param regionCoordinate The region coordinate
     * @return The specified region
     */
    public Region getRegion(RegionCoordinate regionCoordinate) {
        if (regionExists(regionCoordinate)) {
            // The Region already exists

            return loadRegion(saveDir, regionCoordinate);

        } else {
            // The Region needs to be created
            Region region = new Region(seedManager.getRegionSeed(regionCoordinate));
            // Populate the region with random cells
            region.populateRandomly();

            // Perform the required iterations
            for (int i = 0; i < Config.ITERATION_COUNT; i++) {
                region.iteration();
            }

            try {
                // Attempt to save the region to disk
                SaveManager.saveRegion(region, SaveManager.getRegionFile(saveDir, regionCoordinate));
                regionLookupCache.put(regionCoordinate, true);

                return region;

            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Could not load file");
            }
        }
    }
}
