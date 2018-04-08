package com.freddyheppell.cavegame.utility;

import com.freddyheppell.cavegame.config.Config;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.Scanner;

public class Console {
    /**
     * String to clear screen on *nix systems (mac OS/Linux etc.)
     * First character clears screen, second returns cursor to beginning of screen
     */
    private static final String unixClearString = "\033[H\033[2J";

    public enum OperatingSystem {WINDOWS, MAC, LINUX, UNKNOWN}
    private static OperatingSystem operatingSystem;

    /**
     * Get the user's operating system
     *
     * @return OperatingSystem enum of the user's OS
     */
    public static OperatingSystem getOperatingSystem() {
        String os = System.getProperty("os.name").toUpperCase();

        if (operatingSystem == null) {
            if (os.contains("WINDOWS")) {
                operatingSystem = OperatingSystem.WINDOWS;
            } else if (os.contains("MAC")) {
                operatingSystem = OperatingSystem.MAC;
            } else if (os.contains("NUX")) {
                operatingSystem = OperatingSystem.LINUX;
            } else {
                operatingSystem = OperatingSystem.UNKNOWN;
            }
        }

        return operatingSystem;
    }

    /**
     * Clear the screen using the correct method for this OS
     */
    public static void clearScreen() {
        if (!Config.getBoolean("bShouldClearScreen")) {
            // If screen clearing is disabled in the configuration,
            // don't clear it
            return;
        }

        if (getOperatingSystem() == OperatingSystem.WINDOWS) {
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

    /**
     * Read an single character from Stdin
     *
     * @return The integer representation of the character
     */
    public static int readInput() {
        Scanner sc = new Scanner(System.in);

        return sc.next().charAt(0);
    }

    /**
     * @return The instance of the terminal
     */
    private static Terminal getTerminal() {
        try {
            return TerminalBuilder.builder().build();
        } catch (IOException e) {
            throw new RuntimeException("Unable to get terminal instance!");
        }
    }

    /**
     * Get the usable width of the terminal
     *
     * @return The usable width of the terminal
     */
    public static int getWidth() {
        // This number of cells appears to the left and right of the player (divide by 2)
        // and each cell consists of 3 characters (divide by 3). Therefore divide by 6.
        // Some terminals will round up partial character widths, so subtract 1 to be safe
        return Math.floorDiv(getTerminal().getWidth() - 1, 6) - 1;
    }

    /**
     * Get the usable height of the terminal
     *
     * @return The usable width of the terminal
     */
    public static int getHeight() {
        // This number of cells appears above and below the player (divide by 2)
        // Subtract two lines for the input prompt and the line created upon pressing enter
        return Math.floorDiv(getTerminal().getHeight() - 1, 2) - 2;
    }
}
