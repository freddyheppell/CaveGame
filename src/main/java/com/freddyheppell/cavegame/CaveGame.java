package com.freddyheppell.cavegame;


import java.io.IOException;

public class CaveGame {

    public static void main(String[] args) {
        Game game = new Game();
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
