package com.freddyheppell.cavegame;

import com.freddyheppell.cavegame.config.Config;
import com.freddyheppell.cavegame.input.EnumKey;
import com.freddyheppell.cavegame.entities.Player;
import com.freddyheppell.cavegame.utility.ClearScreen;
import biz.source_code.utils.RawConsoleInput;
import com.freddyheppell.cavegame.world.OutputFrame;
import com.freddyheppell.cavegame.world.SeedManager;
import com.freddyheppell.cavegame.world.World;
import com.freddyheppell.cavegame.world.coord.WorldCoordinate;
import com.freddyheppell.cavegame.world.coord.CoordinateProperties;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Game {
    private World world;
    private Player player;
    private SeedManager seedManager;

    public Game() {
//        System.out.println("Hello there");
        this.seedManager = new SeedManager(Config.SEED);
        this.world = new World(seedManager);
        player = new Player(new WorldCoordinate(16, 16));
        world.createRegion(new WorldCoordinate(0, 0));
    }

    public void gameLoop() {
        OutputFrame outputFrame = new OutputFrame(world, player);
        String output = outputFrame.toString();
        ClearScreen.clear();
        System.out.println(output);


        try {
            int consoleInput = RawConsoleInput.read(true);
            EnumKey key = EnumKey.valueOf(consoleInput);

            switch (key) {
                case DIR_NORTH:
                    player.move(CoordinateProperties.NORTH);
                    break;
                case DIR_SOUTH:
                    player.move(CoordinateProperties.SOUTH);
                    break;
                case DIR_EAST:
                    player.move(CoordinateProperties.EAST);
                    break;
                case DIR_WEST:
                    player.move(CoordinateProperties.WEST);
                    break;
                case QUIT:
                    break;
                default:
                    throw new RuntimeException("Bad input!" + (char)consoleInput);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
