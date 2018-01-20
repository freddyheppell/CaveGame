package com.freddyheppell.cavegame.world;

import com.freddyheppell.cavegame.config.Config;
import com.freddyheppell.cavegame.save.SaveManager;
import com.freddyheppell.cavegame.world.coord.RegionCoordinate;
import com.google.gson.Gson;

import java.io.*;
import java.util.HashMap;

public class RegionManager {
    /**
     * Cached results of checking if the region exists on disk
     */
    private HashMap<RegionCoordinate, Boolean> regionLookupCache;
    /**
     * The directory in which to put savedata
     */
    private File saveDir;
    /**
     * The game's SeedManager
     */
    private SeedManager seedManager;
    private RegionCoordinate regionCoordinate;

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
            return regionLookupCache.get(regionCoordinate);
        }

        boolean regionExists = SaveManager.getRegionFile(saveDir, regionCoordinate).exists();
        regionLookupCache.put(regionCoordinate, regionExists);

        return regionExists;
    }

    /**
     * Get the region at the coordinates
     *
     * @param regionCoordinate The region coordinate
     * @return The specified region
     */
    public Region getRegion(RegionCoordinate regionCoordinate) {
        this.regionCoordinate = regionCoordinate;
        if (regionExists(regionCoordinate)) {
            // The Region already exists
            try {
               return SaveManager.loadRegion(saveDir, regionCoordinate);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException("Save file could not be loaded");
            }

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
