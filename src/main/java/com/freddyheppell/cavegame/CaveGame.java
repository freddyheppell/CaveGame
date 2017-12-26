package com.freddyheppell.cavegame;

import com.freddyheppell.cavegame.world.SeedManager;
import com.freddyheppell.cavegame.world.World;

import java.util.Scanner;

public class CaveGame {

    public static void main(String[] args) {
        System.out.print("Enter Seed: ");
        Scanner scanner = new Scanner(System.in);

        SeedManager seedManager = new SeedManager(scanner.next());

        World world = new World(seedManager);

    }
}
