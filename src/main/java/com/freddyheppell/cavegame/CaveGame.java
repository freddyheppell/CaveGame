package com.freddyheppell.cavegame;

import com.freddyheppell.cavegame.world.SeedManager;
import com.freddyheppell.cavegame.world.World;

import java.util.Scanner;

public class CaveGame {

    public static void main(String[] args) {
        Game game = new Game();
//        while (true) {
        game.gameLoop();
//        }
    }
}
