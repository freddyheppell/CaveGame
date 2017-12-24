package com.freddyheppell.cavegame.world;

import com.freddyheppell.cavegame.config.Config;

import java.util.Arrays;
import java.util.Scanner;

public class World {
    private int loadedRegionX;
    private int loadedRegionY;
    private Region loadedRegion;
    private long seed;

    public World(long seed) {
        this.seed = seed;
        loadedRegion = new Region(seed);
        loadedRegionX = 0;
        loadedRegionY = 0;

        loadedRegion.populateRandomly();

        for (int i = 0; i < Config.ITERATION_COUNT; i++) {
            System.out.println(i);
            loadedRegion.output();
            loadedRegion.iteration();
        }

        loadedRegion.output();
    }
}
