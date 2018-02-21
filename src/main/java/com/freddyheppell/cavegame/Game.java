package com.freddyheppell.cavegame;

import com.freddyheppell.cavegame.config.Config;
import com.freddyheppell.cavegame.input.EnumKey;
import com.freddyheppell.cavegame.entities.Player;
import com.freddyheppell.cavegame.items.ItemRegistry;
import com.freddyheppell.cavegame.utility.ClearScreen;
import com.freddyheppell.cavegame.utility.ReadInput;
import com.freddyheppell.cavegame.world.OutputFrame;
import com.freddyheppell.cavegame.world.SeedManager;
import com.freddyheppell.cavegame.world.World;
import com.freddyheppell.cavegame.world.coord.RegionCoordinate;
import com.freddyheppell.cavegame.world.coord.WorldCoordinate;
import com.freddyheppell.cavegame.world.coord.CoordinateProperties;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Game {
    private World world;
    private Player player;
    private SeedManager seedManager;
    private String gameName;

    public Game() {
        this.seedManager = new SeedManager(Config.SEED);
        this.world = new World(seedManager);
        this.player = new Player(new WorldCoordinate(16, 14));
        this.gameName = "Test";
        ItemRegistry.doRegister();
    }

    public void gameLoop() throws IOException {
        OutputFrame outputFrame = new OutputFrame(world, player);
        String output = outputFrame.toString();
        ClearScreen.clear();
        System.out.println(output);


        int consoleInput = ReadInput.readInput();
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
            case QUIT:
                break;
            default:
                throw new RuntimeException("Bad input!" + (char) consoleInput);
        }


        try {
            TimeUnit.SECONDS.sleep(Config.FRAME_SLEEP_TIME);
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted during frame sleep");
        }
    }
}
