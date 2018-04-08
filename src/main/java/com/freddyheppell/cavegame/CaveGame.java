package com.freddyheppell.cavegame;


import com.freddyheppell.cavegame.config.Config;

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
        try {
            game.initPlayer();
            while (true) {
                game.gameLoop();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error getting input");
        }
    }
}
