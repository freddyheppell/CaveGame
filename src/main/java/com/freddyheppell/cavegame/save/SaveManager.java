package com.freddyheppell.cavegame.save;

import com.freddyheppell.cavegame.world.Region;
import com.freddyheppell.cavegame.world.cells.*;
import com.freddyheppell.cavegame.world.coord.RegionCoordinate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import java.io.*;

public class SaveManager {

    /**
     * An instance of the Runtime Adapter Factory for the Cell class, which allows GSON to correctly serialise
     * and deserialise polymorphic classes
     */
    private static final RuntimeTypeAdapterFactory<Cell> CELL_ADAPTER_FACTORY = RuntimeTypeAdapterFactory
            .of(Cell.class, "t")
            .registerSubtype(EmptyCell.class, "e")
            .registerSubtype(FloorCell.class, "f")
            .registerSubtype(RockCell.class, "r")
            .registerSubtype(ChestCell.class, "c");

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
    private static String getSavePath() {
        String os = System.getProperty("os.name").toUpperCase();
        String path;

        if (os.contains("WINDOWS")) {
            System.out.println("Windows");
            path = System.getenv("APPDATA") + "\\CaveGame";
        } else if (os.contains("MAC")) {
            System.out.println("Mac");
            path = System.getProperty("user.home") + "/Library/Application Support/CaveGame";
        } else if (os.contains("NUX")) {
            System.out.println("Linux");
            path = System.getProperty("user.dir") + ".CaveGame";
        } else {
            path = "CaveGame";
        }

        return path;
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
     * Get a FileReader for a region file
     *
     * @param regionFile The File instance for the region file
     * @return The FileReader
     * @throws FileNotFoundException If the file is not valid
     */
    public static FileReader getRegionReader(File regionFile) throws FileNotFoundException {
        return new FileReader(regionFile);
    }

    /**
     * Save a region to disk
     *
     * @param region       The Region instance
     * @param saveLocation The File location to save the Region to
     * @throws IOException If the file was unable to be saved
     */
    public static void saveRegion(Region region, File saveLocation) throws IOException {
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CELL_ADAPTER_FACTORY).create();
        System.out.println("STARTING SAVE");
        Writer writer = new FileWriter(saveLocation);
        gson.toJson(region, writer);
        writer.close();
        System.out.println("ENDING SAVE");
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
        System.out.println(regionCoordinate);
        FileReader regionReader = SaveManager.getRegionReader(SaveManager.getRegionFile(saveDir, regionCoordinate));
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(CELL_ADAPTER_FACTORY).create();
        return gson.fromJson(new BufferedReader(regionReader), Region.class);
    }
}
