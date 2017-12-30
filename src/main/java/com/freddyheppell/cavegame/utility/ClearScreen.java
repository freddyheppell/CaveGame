package com.freddyheppell.cavegame.utility;

import com.freddyheppell.cavegame.config.Config;

import java.io.IOException;

public class ClearScreen {

    /**
     * Is the computer a Windows computer?
     */
    private static final boolean isWindows = System.getProperty("os.name").startsWith("Windows");

    /**
     * String to clear screen on *nix systems (mac OS/Linux etc.)
     * First character clears screen, second returns cursor to beginning of screen
     */
    private static final String unixClearString = "\033[H\033[2J";

    public static void clear() {
        if (!Config.SHOULD_CLEAR_SCREEN) {
            // If screen clearing is disabled in the configuration,
            // don't clear it
            return;
        }

        if (isWindows) {
            // On windows, run `cls` in the command prompt
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error clearing screen");
            }
        } else {
            // On *nix systems, use the escape characters
            System.out.print(unixClearString);
        }
    }
}
