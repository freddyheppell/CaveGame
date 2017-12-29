package com.freddyheppell.cavegame.utility;

import com.freddyheppell.cavegame.config.Config;

import java.io.IOException;

public class ClearScreen {
    public static void clear() {
        if (Config.SHOULD_CLEAR_SCREEN) {
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error clearing screen");
            }
        }
    }
}
