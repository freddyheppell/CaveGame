package com.freddyheppell.cavegame.utility;

import biz.source_code.utils.RawConsoleInput;
import com.freddyheppell.cavegame.config.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Console {
    /**
     * String to clear screen on *nix systems (mac OS/Linux etc.)
     * First character clears screen, second returns cursor to beginning of screen
     */
    private static final String unixClearString = "\033[H\033[2J";

    public enum OperatingSystem {WINDOWS, MAC, LINUX, UNKNOWN}

    private static OperatingSystem operatingSystem;

    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

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
    public static int readChar() throws IOException {
        if (getOperatingSystem() == OperatingSystem.WINDOWS) {
            // On Windows, use the RawConsoleInput library
            // This support enter-less controls, which *NIX does not
            return RawConsoleInput.read(true);
        } else {
            // Use normal scanner
            String entry = br.readLine();

            if (entry.length() > 0) {
                // If at least one character was entered, pick the first
                return entry.charAt(0);
            }

            // If nothing was entered, pick an invalid character
            return Character.MIN_VALUE;
        }
    }

    /**
     * Read an entire line of input
     *
     * @return The input line
     * @throws IOException when an exception is encountered reading
     */
    public static String readLine() throws IOException {
        // Use normal scanner
        return br.readLine();
    }

    /**
     * Get the usable width of the terminal
     *
     * @return The usable width of the terminal
     */
    public static int getWidth() {
        // Return a hardcoded value to avoid issues with detection library
        return 24;
    }

    /**
     * Get the usable height of the terminal
     *
     * @return The usable width of the terminal
     */
    public static int getHeight() {
        // Return a hardcoded value to avoid issues with detection library
        return 15;
    }

    /**
     * Await the user pressing ENTER, discarding any input
     */
    public static void requestEnter() {
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
    }

    /**
     * Get the user to enter an interger in this range
     *
     * @param min The lower (inclusive) bound of the range
     * @param max The upper (inclusive) bound of the range
     * @return The integer the user entered
     * @throws IOException If an exception was encountered binding to stdin
     */
    private static int getIntRange(int min, int max) throws IOException {
        int enteredInt = min - 1;

        while (enteredInt < min || enteredInt > max) {
            String character = readLine();

            enteredInt = Integer.valueOf(character);
        }

        return enteredInt;
    }

    /**
     * Present the user with a menu of options and ask that they pick one
     *
     * @param options An array of options
     * @return The index within the array that the user selcted
     * @throws IOException If an error is encountered binding to stdin
     */
    public static int menu(String[] options) throws IOException {
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.println();
        System.out.print("Enter your choice: ");

        // Subtract one because the displayed list starts at 1 but the actual list starts at 0
        return getIntRange(1, options.length) - 1;
    }
}
