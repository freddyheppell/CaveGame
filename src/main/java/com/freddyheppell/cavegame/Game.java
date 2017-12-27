package com.freddyheppell.cavegame;

import com.freddyheppell.cavegame.config.Config;
import com.freddyheppell.cavegame.player.Player;
import com.freddyheppell.cavegame.world.OutputFrame;
import com.freddyheppell.cavegame.world.SeedManager;
import com.freddyheppell.cavegame.world.World;
import com.freddyheppell.cavegame.world.coord.Coordinate;
import com.freddyheppell.cavegame.world.coord.Transform;

import java.util.Scanner;

public class Game {
    private World world;
    private Player player;
    private SeedManager seedManager;

    public Game() {
        this.seedManager = new SeedManager(Config.SEED);
        this.world = new World(seedManager);
        player = new Player(new Coordinate(0, 0));
        player.visibleCells();
        world.createRegion(new Coordinate(0, 0));
    }

    public void gameLoop() {
        OutputFrame outputFrame = new OutputFrame(world, player);
        System.out.print(outputFrame.toString());

        Scanner reader = new Scanner(System.in);  // Reading from System.in
        String command = reader.nextLine();

        if (command.equals("w")) {
            player.moveCellCoordinate(new Transform(-1, 0));
        }
    }
}
