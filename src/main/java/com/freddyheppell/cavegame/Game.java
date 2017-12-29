package com.freddyheppell.cavegame;

import com.freddyheppell.cavegame.config.Config;
import com.freddyheppell.cavegame.input.EnumKey;
import com.freddyheppell.cavegame.player.Player;
import com.freddyheppell.cavegame.utility.ClearScreen;
import com.freddyheppell.cavegame.utility.ConsoleInput;
import com.freddyheppell.cavegame.world.OutputFrame;
import com.freddyheppell.cavegame.world.SeedManager;
import com.freddyheppell.cavegame.world.World;
import com.freddyheppell.cavegame.world.coord.Coordinate;
import com.freddyheppell.cavegame.world.coord.CoordinateProperties;
import com.freddyheppell.cavegame.world.coord.Transform;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Game {
    private World world;
    private Player player;
    private SeedManager seedManager;

    public Game() {
//        System.out.println("Hello there");
        this.seedManager = new SeedManager(Config.SEED);
        this.world = new World(seedManager);
        player = new Player(new Coordinate(16, 16));
        world.createRegion(new Coordinate(0, 0));
    }

    public void gameLoop() {
        OutputFrame outputFrame = new OutputFrame(world, player);
        String output = outputFrame.toString();
        ClearScreen.clear();
        System.out.println(output);


        try {
            int consoleInput = ConsoleInput.read(true);
            System.out.println((char)consoleInput);

            EnumKey key = EnumKey.valueOf(consoleInput);

            System.out.println(key);

            switch (key) {
                case DIR_NORTH:
                    player.moveCellCoordinate(CoordinateProperties.NORTH);
                    break;
                case DIR_SOUTH:
                    player.moveCellCoordinate(CoordinateProperties.SOUTH);
                    break;
                case DIR_EAST:
                    player.moveCellCoordinate(CoordinateProperties.EAST);
                    break;
                case DIR_WEST:
                    player.moveCellCoordinate(CoordinateProperties.WEST);
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
