package com.freddyheppell.cavegame;


public class CaveGame {

    public static void main(String[] args) {
        Game game = new Game();
        while (true) {
            game.gameLoop();
        }
    }
}
