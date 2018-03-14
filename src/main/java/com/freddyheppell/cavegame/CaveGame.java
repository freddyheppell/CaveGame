package com.freddyheppell.cavegame;


import java.io.IOException;

public class CaveGame {

    public static final Game game = new Game();

    public static void main(String[] args) {
        while (true) {
            try {
                game.gameLoop();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error getting input");
            }
        }
    }
}
