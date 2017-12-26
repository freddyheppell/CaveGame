package com.freddyheppell.cavegame.world;

import com.freddyheppell.cavegame.config.Config;
import com.freddyheppell.cavegame.world.coord.Coordinate;

import java.util.LinkedHashMap;
import java.util.Map;

public class RegionCache {
    private LinkedHashMap<Coordinate, Region> cache = new LinkedHashMap<Coordinate, Region>()
    {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Coordinate, Region> eldest)
        {
            return this.size() > Config.REGION_CACHE_SIZE;
        }
    };

    public Region getByCoordinate(Coordinate regionCoordinate) {
        return cache.get(regionCoordinate);
    }

    public boolean checkCoordinate(Coordinate regionCoordinate) {
        return cache.containsKey(regionCoordinate);
    }

    public void add(Coordinate regionCoordinate, Region region) {
        if (checkCoordinate(regionCoordinate)) {
            return;
        }

        cache.put(regionCoordinate, region);

    }

    public void addToQueue(Coordinate coordinate, Region region) {

    }
}
