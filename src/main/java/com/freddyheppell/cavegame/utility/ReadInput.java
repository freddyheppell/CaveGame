package com.freddyheppell.cavegame.utility;

import java.util.Scanner;

public class ReadInput {
    public static int readInput() {
        Scanner sc = new Scanner(System.in);

        return sc.next().charAt(0);
        }
}
