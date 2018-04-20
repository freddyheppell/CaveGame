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
import java.util.Scanner;

public class Game {
    public World world;
    private Player player;
    private SeedManager seedManager;
    private String gameName;

    private static final Logger logger = LogManager.getLogger();

    public Game() {
        ItemRegistry.getInstance().doRegister();
        this.seedManager = new SeedManager(Config.getString("sWorldSeed"));

        System.out.println("CaveGame Temporary Main Menu");
        System.out.println("Enter the name of the world to create or load");
        Scanner scanner = new Scanner(System.in);
        System.out.print(">");
        this.gameName = scanner.nextLine();

        this.world = new World(seedManager, this.gameName);
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

    public World getWorld() {
        return world;
    }

    /**
     * Register an entity activation event
     *
     * @param triggerSource The entity that must be alerted
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
            default:
                throw new RuntimeException("Bad input!" + (char) consoleInput);
        }


        try {
            Thread.sleep((long)Config.getFloat("fFrameSleepTime") * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted during frame sleep");
        }

        return true;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
