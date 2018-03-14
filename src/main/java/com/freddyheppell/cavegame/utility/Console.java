package com.freddyheppell.cavegame.utility;

import com.freddyheppell.cavegame.config.Config;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.Scanner;

public class Console {
    /**
     * If the computer is running Windows
     * The OS name of all versions of Windows will begin with "Windows"
     */
    private static final boolean isWindows = System.getProperty("os.name").startsWith("Windows");
    /**
     * String to clear screen on *nix systems (mac OS/Linux etc.)
     * First character clears screen, second returns cursor to beginning of screen
     */
    private static final String unixClearString = "\033[H\033[2J";

    public static void clearScreen() {
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

    public static int readInput() {
        Scanner sc = new Scanner(System.in);

        return sc.next().charAt(0);
    }

    private static Terminal getTerminal() {
        try {
            return TerminalBuilder.builder().build();
        } catch (IOException e) {
            throw new RuntimeException("Unable to get terminal instance!");
        }
    }

    public static int getWidth() {
        // This number of cells appears to the left and right of the player (divide by 2)
        // and each cell consists of 3 characters (divide by 3). Therefore divide by 6.
        // In some cases one character overflows, so subtract 1 to be safe
        return Math.floorDiv(getTerminal().getWidth() - 1, 6) - 1;
    }

    public static int getHeight() {
        // This number of cells appears above and below the player (divide by 2)
        // Subtract two lines for the input prompt and the line created upon pressing enter
        return Math.floorDiv(getTerminal().getHeight() - 1, 2) - 2;
    }
}
