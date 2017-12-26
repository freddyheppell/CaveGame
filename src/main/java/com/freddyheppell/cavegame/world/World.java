package com.freddyheppell.cavegame.world;

import com.freddyheppell.cavegame.config.Config;
import com.freddyheppell.cavegame.world.coord.Coordinate;

import java.util.Arrays;
import java.util.Scanner;

public class World {
    private Coordinate loadedRegionCoordinate;
    private Region loadedRegion;
    private SeedManager seedManager;

    public World(SeedManager seedManager) {
        this.seedManager = seedManager;

        loadedRegionCoordinate = new Coordinate(0, 0);
        loadedRegion = new Region(seedManager.getRegionSeed(loadedRegionCoordinate));


        loadedRegion.populateRandomly();

        for (int i = 0; i < Config.ITERATION_COUNT; i++) {
            System.out.println(i);
            loadedRegion.iteration();
        }

        loadedRegion.output();

        System.out.println(loadedRegion.getSaveData());
    }

    public void createRegion(Coordinate coordinate) {

    }
}
