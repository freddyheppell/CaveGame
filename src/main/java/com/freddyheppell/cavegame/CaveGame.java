package com.freddyheppell.cavegame;


import com.freddyheppell.cavegame.config.Config;
import com.freddyheppell.cavegame.utility.Console;

import java.io.IOException;

public class CaveGame {

    public static final Game game;

    static {
        try {
            // Load the application's configuration
            Config.loadConfiguration();
            // and create the game instance
            game = new Game();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialise game!");
        }
    }

    public static void main(String[] args) {
        // Should the game loop again
        boolean shouldContinue = true;
        try {
            // Initialise the player instance
            game.initPlayer();
            while (shouldContinue) {
                // Loop the game until it returns a false value, which means the user wants to quit
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
