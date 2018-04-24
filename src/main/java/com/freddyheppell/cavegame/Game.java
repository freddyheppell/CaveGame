package com.freddyheppell.cavegame;

import com.freddyheppell.cavegame.config.Config;
import com.freddyheppell.cavegame.input.EnumKey;
import com.freddyheppell.cavegame.entities.Player;
import com.freddyheppell.cavegame.items.ItemRegistry;
import com.freddyheppell.cavegame.save.SaveManager;
import com.freddyheppell.cavegame.utility.Console;
import com.freddyheppell.cavegame.world.OutputFrame;
import com.freddyheppell.cavegame.world.SeedManager;
import com.freddyheppell.cavegame.world.World;
import com.freddyheppell.cavegame.world.coord.CoordinateProperties;
import com.freddyheppell.cavegame.world.coord.WorldCoordinate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class Game {
    public World world;
    private Player player;
    private SeedManager seedManager;
    private String gameName;

    private static final Logger logger = LogManager.getLogger();

    public Game() throws IOException {
        ItemRegistry.getInstance().doRegister();

        mainMenu();
    }

    /**
     * Show the user the main menu
     *
     * @throws IOException when a filesystem exception is encountered
     */
    public void mainMenu() throws IOException {
        Console.clearScreen();
        System.out.println("CaveGame");

        int menuChoice = Console.menu(new String[]{"Load Game", "Create Game"});

        Console.clearScreen();

        if (menuChoice == 0) {
            // List Existing Games
            System.out.println("Select a world to load\n");
            String[] worlds = SaveManager.getWorlds();
            int choice = Console.menu(worlds);
            this.gameName = worlds[choice];
        } else if (menuChoice == 1) {
            // Create a new game
            System.out.println("Enter the game name");
            System.out.print(">");
            this.gameName = Console.readLine();
        }

        // Get the save folder
        // This will create the folder if it doesn't exist
        File saveDir = SaveManager.getSaveFolder(gameName);
        // This will be null if the world doesn't have a JSON file, possibly because the world was just created
        Map<String, String> configMap = SaveManager.loadWorld(saveDir);

        if (configMap != null) {
            // This world has previously been created
            // Retrieve the seed
            this.seedManager = new SeedManager(Config.getString(configMap.get("worldSeed")));

            // Check if the configuration matches
            // If the hash doesn't match, alert the user
            if (!configMap.get("configurationHash").equals(Config.getConfigurationHash())) {
                System.out.println("!!! WARNING !!!");
                System.out.println("The current configuration has changed since this world was created.");
                System.out.println("This could cause world generation issues, or prevent the world from loading at all");
                System.out.println("Press ENTER to accept this");
                Console.requestEnter();
            }
        } else {
            // The world hasn't previously been created, so ask the user for the seed
            System.out.println("Enter the world seed");
            System.out.print("> ");
            String seedString = Console.readLine();
            this.seedManager = new SeedManager(seedString);
        }

        // Now create the world with that data
        // A new world is always created because the only data that's persisted is the two parameters passed through
        // Everything else doesn't need to be saved
        this.world = new World(this.seedManager, this.gameName);

        // Now overwrite the world.json file with these parameters
        saveWorld();
    }

    public void initPlayer() throws IOException {
        // Loading Player
        File saveDir = SaveManager.getSaveFolder(gameName);
        if (SaveManager.getPlayerFile(saveDir).exists()) {
            // The player file already exists, load it
            this.player = SaveManager.loadPlayer(saveDir);
        } else {
            // There is no player file, make one
            this.player = new Player(world.getSpawnLocation());
            // ... and save it
            SaveManager.savePlayer(saveDir, player);
        }
    }

    /**
     * Save the player instance
     */
    public void savePlayer() {
        File saveDir = SaveManager.getSaveFolder(gameName);

        try {
            SaveManager.savePlayer(saveDir, this.player);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to resave player!");
        }
    }

    private void saveWorld() {
        File saveDir = SaveManager.getSaveFolder(gameName);

        try {
            SaveManager.saveWorld(saveDir, this.world);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to save world!");
        }
    }

    public World getWorld() {
        return world;
    }

    /**
     * Register an entity activation event
     *
     * @param triggerSource   The entity that must be alerted
     * @param triggerLocation The cell that causes the trigger
     */
    public void registerEvent(WorldCoordinate triggerSource, WorldCoordinate triggerLocation) {
        logger.debug("Registering event in world at" + triggerLocation);
        logger.debug(triggerLocation == null);
        // Set the listener of this cell to the entity
        world.getCell(triggerLocation).listener = triggerSource;
        world.getRegion(triggerLocation.getRegionCoordinate()).modified = true;
    }

    /**
     * Run one iteration of the game
     *
     * @return Should the game quit
     * @throws IOException If an exception is encountered
     */
    public boolean gameLoop() throws IOException {
        // Check if the player has died
        if (!player.alive) {
            // The player has died

            // Place them near the middle of the current region
            player.setLocation(getWorld().getRegion(player.getLocation().getRegionCoordinate()).getEntitySpawnCell());

            // Resurrect them
            player.alive = true;

            Console.requestEnter();
        }

        OutputFrame outputFrame = new OutputFrame(world, player);
        String output = outputFrame.toString();
        Console.clearScreen();
        System.out.println(output);


        int consoleInput = Console.readChar();
        EnumKey key = EnumKey.valueOf(consoleInput);

        switch (key) {
            case DIR_NORTH:
                player.move(CoordinateProperties.UP, world);
                break;
            case DIR_SOUTH:
                player.move(CoordinateProperties.DOWN, world);
                break;
            case DIR_EAST:
                player.move(CoordinateProperties.LEFT, world);
                break;
            case DIR_WEST:
                player.move(CoordinateProperties.RIGHT, world);
                break;
            case OPEN_INV:
                player.showInventory();
                break;
            case QUIT:
                return false;
            case UNKNOWN:
                break;
        }


        try {
            Thread.sleep((long) Config.getFloat("fFrameSleepTime") * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted during frame sleep");
        }

        return true;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
