package com.freddyheppell.cavegame.config;

/**
 * Loads and manages the game's configuration
 */
public class Config {
    public static final int REGION_SIZE = 50;
    public static final int REGION_CACHE_SIZE = 8;
    public static final int ITERATION_COUNT = 2;
    public static final int CELL_DEATH_THRESHOLD = 5;
    public static final String SEED = "70";
    public static final float RANDOM_BOUNDARY = 0.5f;
    public static final float CHEST_SPAWN_BOUNDARY = 0.995f;
    public static final boolean SHOULD_CLEAR_SCREEN = true;
    public static final long FRAME_SLEEP_TIME = 3 / 4;
    public static final char PLAYER_INDICATOR = 'P';
    public static final int CHEST_MAX_ITEMS = 5;
}
