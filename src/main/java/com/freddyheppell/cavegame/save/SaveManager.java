package com.freddyheppell.cavegame.save;

import com.freddyheppell.cavegame.entities.Player;
import com.freddyheppell.cavegame.items.Item;
import com.freddyheppell.cavegame.items.ItemRegistry;
import com.freddyheppell.cavegame.utility.Console;
import com.freddyheppell.cavegame.world.Region;
import com.freddyheppell.cavegame.world.cells.*;
import com.freddyheppell.cavegame.world.coord.RegionCoordinate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

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

    private static final RuntimeTypeAdapterFactory<Item> ITEM_ADAPTER_FACTORY = ItemRegistry.getInstance().getItemAdapterFactory();

    private static final String PLAYER_FILE_NAME = "player.json";

    /**
     * Get the file name of a region at the given coordinates
     *
     * @param regionCoordinate The coordinates of the region
     * @return A string represnetation of the file in the format r(x)(y).json
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
    private static boolean checkSaveFolder(File directory) {
        return directory.exists() || directory.mkdir();
    }

    /**
     * Get the File representation of the save folder
     *
     * @param saveName The name of the save
     * @return A File representation of the save folder
     */
    public static File getSaveFolder(String saveName) {
        String path = getSavePath();
        // First check if the root folder (i.e. the CaveGame folder) exists
        File rootFolder = new File(path);
        // Then check if the specific save folder exists
        File saveFolder = new File(path, saveName);
        if (checkSaveFolder(rootFolder) && checkSaveFolder(saveFolder)) {
            return saveFolder;
        } else {
            throw new RuntimeException("Could not load save folder!");
        }
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


    /**
     * Save a region to disk
     *
     * @param region       The Region instance
     * @param saveLocation The File location to save the Region to
     * @throws IOException If the file was unable to be saved
     */
    public static void saveRegion(Region region, File saveLocation) throws IOException {
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CELL_ADAPTER_FACTORY)
                .registerTypeAdapterFactory(ITEM_ADAPTER_FACTORY).create();
        logger.info("Starting region save");
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
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CELL_ADAPTER_FACTORY)
                .registerTypeAdapterFactory(ITEM_ADAPTER_FACTORY).create();
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
        logger.info("Saving player");
        FileReader playerReader = new FileReader(getPlayerFile(saveDir));
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(ITEM_ADAPTER_FACTORY).create();
        Player player = gson.fromJson(new BufferedReader(playerReader), Player.class);
        logger.info("Player saved");
        return player;
    }
}
