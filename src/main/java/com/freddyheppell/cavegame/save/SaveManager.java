package com.freddyheppell.cavegame.save;

import com.freddyheppell.cavegame.entities.Entity;
import com.freddyheppell.cavegame.entities.Monster;
import com.freddyheppell.cavegame.entities.Player;
import com.freddyheppell.cavegame.items.Item;
import com.freddyheppell.cavegame.items.ItemRegistry;
import com.freddyheppell.cavegame.utility.Console;
import com.freddyheppell.cavegame.world.Region;
import com.freddyheppell.cavegame.world.World;
import com.freddyheppell.cavegame.world.cells.*;
import com.freddyheppell.cavegame.world.coord.RegionCoordinate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Interacts with the filesystem to save and load the game's data
 */
public class SaveManager {
    private static final Logger logger = LogManager.getLogger();

    /**
     * An instance of the Runtime Adapter Factory for the Cell class, which allows Gson to correctly serialise
     * and deserialise polymorphic classes
     */
    private static final RuntimeTypeAdapterFactory<Cell> CELL_ADAPTER_FACTORY = RuntimeTypeAdapterFactory
            .of(Cell.class, "t")
            .registerSubtype(EmptyCell.class, "e")
            .registerSubtype(FloorCell.class, "f")
            .registerSubtype(RockCell.class, "r")
            .registerSubtype(ChestCell.class, "c");

    /**
     * An instance of the Runtime Adapter Factory for the Item class, which allows Gson to correctly serialise
     * and deserialise polymorphic classes
     */
    private static final RuntimeTypeAdapterFactory<Item> ITEM_ADAPTER_FACTORY = ItemRegistry.getInstance().getItemAdapterFactory();

    /**
     * An instance of the Runtime Adapter Factory for the Entity class, which allows Gson to correctly serialise
     * and deserialise polymorphic classes
     */
    private static final RuntimeTypeAdapterFactory<Entity> ENTITY_ADAPTER_FACTORY = RuntimeTypeAdapterFactory
            .of(Entity.class, "t")
            .registerSubtype(Monster.class, "m");

    /**
     * The name of the file that stores player data
     */
    private static final String PLAYER_FILE_NAME = "player.json";

    /**
     * The name of the world information file
     */
    private static final String WORLD_FILE_NAME = "world.json";

    /**
     * Get the file name of a region at the given coordinates
     *
     * @param regionCoordinate The coordinates of the region
     * @return A string representation of the file in the format r(x)(y).json
     */
    private static String regionFileName(RegionCoordinate regionCoordinate) {
        return String.format("r%d,%d.json", regionCoordinate.rx, regionCoordinate.ry);
    }

    /**
     * Get the path to the directory where save folders are stored
     *
     * @return The path as a string
     */
    public static String getSavePath() {
        Console.OperatingSystem operatingSystem = Console.getOperatingSystem();

        switch (operatingSystem) {
            case WINDOWS:
                return System.getenv("APPDATA") + "\\CaveGame";
            case MAC:
                return System.getProperty("user.home") + "/Library/Application Support/CaveGame";
            case LINUX:
                return System.getProperty("user.dir") + ".CaveGame";
            case UNKNOWN:
                return "CaveGame";
            default:
                return "CaveGame";
        }
    }

    /**
     * Check if the folder exists, if not then create it
     *
     * @param directory The directory to be checked
     * @return If the operation was successful
     */
    public static boolean checkSaveFolder(File directory) {
        return directory.exists() || directory.mkdir();
    }

    /**
     * Get the File representation of the save folder for a world.
     * This creates the directory if it does not exist currently.
     *
     * @param saveName The name of the save
     * @return A File representation of the save folder
     */
    public static File getSaveFolder(String saveName) {
        // First check if the root folder (i.e. the CaveGame folder) exists
        File rootFolder = getRoot();
        // Then check if the specific save folder exists
        File saveFolder = new File(rootFolder, saveName);
        if (checkSaveFolder(rootFolder) && checkSaveFolder(saveFolder)) {
            return saveFolder;
        } else {
            throw new RuntimeException("Could not load save folder!");
        }
    }

    /**
     * Get a reference to the root folder where all data should be stored
     * This is in Application Support on macOS, APPDATA\Roaming on Windows
     *
     * @return the root folder
     */
    private static File getRoot() {
        return new File(getSavePath());
    }

    /**
     * Get the File representation of a region
     *
     * @param saveDir          The File representation of the save directory
     * @param regionCoordinate The coordinate of the region
     * @return A File representation of the region save file
     */
    public static File getRegionFile(File saveDir, RegionCoordinate regionCoordinate) {
        return new File(saveDir, regionFileName(regionCoordinate));
    }

    /**
     * Get the File representation of the location of the player file
     *
     * @param saveDir The File representation of the save directory
     * @return A File representation of the player file
     */
    public static File getPlayerFile(File saveDir) {
        return new File(saveDir, PLAYER_FILE_NAME);
    }

    private static File getWorldFile(File saveDir) {
        return new File(saveDir, WORLD_FILE_NAME);
    }

    /**
     * Save a region to disk
     *
     * @param region       The Region instance
     * @param saveLocation The File location to save the Region to
     * @throws IOException If the file was unable to be saved
     */
    public static void saveRegion(Region region, File saveLocation) throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(CELL_ADAPTER_FACTORY)
                .registerTypeAdapterFactory(ITEM_ADAPTER_FACTORY)
                .registerTypeAdapterFactory(ENTITY_ADAPTER_FACTORY)
                .create();
        logger.info("Starting region save");
        logger.info("Entity count: " + region.entityCount());
        Writer writer = new FileWriter(saveLocation, false);
        gson.toJson(region, writer);
        writer.close();
        logger.info("Region save completed");
    }

    /**
     * Load a region from disk
     *
     * @param saveDir          The directory to load from
     * @param regionCoordinate The coordinate of the region
     * @return The Region instance
     * @throws FileNotFoundException If the file could not be found
     */
    public static Region loadRegion(File saveDir, RegionCoordinate regionCoordinate) throws FileNotFoundException {
        logger.info("Loading region from disk {}", regionCoordinate.toString());
        FileReader regionReader = new FileReader(SaveManager.getRegionFile(saveDir, regionCoordinate));
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(CELL_ADAPTER_FACTORY)
                .registerTypeAdapterFactory(ITEM_ADAPTER_FACTORY)
                .registerTypeAdapterFactory(ENTITY_ADAPTER_FACTORY)
                .create();
        Region region = gson.fromJson(new BufferedReader(regionReader), Region.class);
        logger.info("Loaded region {}", regionCoordinate.toString());
        return region;
    }

    /**
     * Save the player file
     *
     * @param saveLocation The folder to store the player file in
     * @param player       The current instance of the player
     * @throws IOException If an error is encountered saving the file
     */
    public static void savePlayer(File saveLocation, Player player) throws IOException {
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(ITEM_ADAPTER_FACTORY).create();
        logger.info("Saving player");
        Writer writer = new FileWriter(getPlayerFile(saveLocation), false);
        gson.toJson(player, writer);
        writer.close();
        logger.info("Player saved");
    }

    /**
     * Load the player from disk
     *
     * @param saveDir The directory the player is located in
     * @return The Player instance
     * @throws FileNotFoundException If the player file does not exist
     */
    public static Player loadPlayer(File saveDir) throws FileNotFoundException {
        logger.info("Loading player");
        FileReader playerReader = new FileReader(getPlayerFile(saveDir));
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(ITEM_ADAPTER_FACTORY).create();
        Player player = gson.fromJson(new BufferedReader(playerReader), Player.class);
        logger.info("Player loaded");
        return player;
    }

    /**
     * Save the world information file
     *
     * @param saveLocation The world's save folder
     * @param world        The instance of the world
     * @throws IOException if an exception is encountered saving the file
     */
    public static void saveWorld(File saveLocation, World world) throws IOException {
        Gson gson = new GsonBuilder().registerTypeAdapter(World.class, new WorldSerialiser()).disableHtmlEscaping().create();
        Writer writer = new FileWriter(getWorldFile(saveLocation), false);
        gson.toJson(world, writer);
        writer.close();
    }

    /**
     * Load the world information file
     *
     * @param saveDir The world's save folder
     * @return A String,String map of the properties
     */
    public static Map<String, String> loadWorld(File saveDir) {
        logger.info("Loading world");
        // Get a reference to the type of a String,String map
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        try {
            FileReader worldReader = new FileReader(getWorldFile(saveDir));
            // HTML Escaping is disabled because it attempts to escape some base64 control characters
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            Map<String, String> worldMap = gson.fromJson(new BufferedReader(worldReader), type);
            logger.info("World loaded");

            return worldMap;
        } catch (FileNotFoundException e) {
            // If the file doesn't exist, just return null
            logger.info("World file does not exist");
            return null;
        }
    }

    /**
     * Finds all valid worlds on disk
     *
     * @return A string array of all valid worlds
     */
    public static String[] getWorlds() {
        return getRoot().list((current, name) -> {
            File thisItem = new File(current, name);

            if (thisItem.isDirectory()) {
                // If it's a directory, check if it's a valid world with a world.json file

                return new File(thisItem, "world.json").exists();
            }

            return false;
        });
    }
}
