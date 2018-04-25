package com.freddyheppell.cavegame.world;

import com.freddyheppell.cavegame.CaveGame;
import com.freddyheppell.cavegame.config.Config;
import com.freddyheppell.cavegame.save.SaveManager;
import com.freddyheppell.cavegame.world.coord.RegionCoordinate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Holds the currently active regions of the game
 */
public class RegionManager {
    /**
     * Cached results of checking if the region exists on disk
     */
    private HashMap<RegionCoordinate, Boolean> regionLookupCache;

    /**
     * The cache of loaded regions. This is a HashMap that also preserves order, with the eldest being removed when it
     * reaches the maximum size
     */
    private LinkedHashMap<RegionCoordinate, Region> regionCache = new LinkedHashMap<RegionCoordinate, Region>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            boolean doClear = size() > Config.getInt("iRegionCacheSize");
            if (doClear) {
                logger.debug("Clearing region cache");
            }
            return doClear;
        }
    };

    /**
     * The directory in which to put save data
     */
    private File saveDir;

    /**
     * The game's SeedManager
     */
    private SeedManager seedManager;

    private static final Logger logger = LogManager.getLogger();

    public RegionManager(String worldName, SeedManager seedManager) {
        saveDir = SaveManager.getSaveFolder(worldName);
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

    /**
     * Load a region from cache or disk
     *
     * @param saveDir          the directory to load from
     * @param regionCoordinate the coordinate of the region
     * @return the Region instance
     */
    private Region loadRegion(File saveDir, RegionCoordinate regionCoordinate) {
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
            // This is a new region
            // Create it...
            Region region = createRegion(regionCoordinate);
            // Add entities
            region.generateEntities();

            // Save all current regions
            // This can't just be the current region because events may modify other regions
            saveAllRegions();

            return region;
        }
    }

    /**
     * Create a region and save it
     *
     * @param regionCoordinate The location of the region
     * @return The Region instance
     */
    private Region createRegion(RegionCoordinate regionCoordinate) {
        // The Region needs to be created
        Region region = new Region(seedManager.getRegionSeed(regionCoordinate), regionCoordinate);
        // Populate the region with random cells
        region.populateRandomly();

        // Perform the required iterations
        for (int i = 0; i < Config.getInt("iRegionIterationCount"); i++) {
            region.iteration();
        }

        // Add chests randomly
        region.generateChests();

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

    /**
     * Save all regions with a modified flag
     */
    public void saveAllRegions() {
        logger.debug("Saving all");
        for (Map.Entry<RegionCoordinate, Region> entry :
                regionCache.entrySet()) {
            if (entry.getValue().modified) {
                logger.debug("Saving" + entry.getKey());
                resaveRegion(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Save a specific region to disk
     *
     * @param regionCoordinate the coordinates of the region
     * @param region           the instance of the region
     */
    public void resaveRegion(RegionCoordinate regionCoordinate, Region region) {
        try {
            SaveManager.saveRegion(region, SaveManager.getRegionFile(saveDir, regionCoordinate));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to resave file");
        }
    }

    public void resaveRegion(RegionCoordinate regionCoordinate) {
        Region region = CaveGame.game.getWorld().getRegion(regionCoordinate);

        resaveRegion(regionCoordinate, region);
    }
}
