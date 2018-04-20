package com.freddyheppell.cavegame;


import com.freddyheppell.cavegame.config.Config;
import com.freddyheppell.cavegame.utility.Console;

import java.io.IOException;

public class CaveGame {

    public static final Game game;

    static {
        try {
            Config.loadConfiguration();
            game = new Game();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialise game!");
        }
    }

    public static void main(String[] args) {
        boolean shouldContinue = true;
        try {
            game.initPlayer();
            while (shouldContinue) {
                shouldContinue = game.gameLoop();
            }
            Console.clearScreen();
            System.out.println("Quitting game");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error getting input");
        }
    }
}
