package com.freddyheppell.cavegame.world;

import com.freddyheppell.cavegame.config.Config;
import com.freddyheppell.cavegame.storage.ISaveable;
import com.freddyheppell.cavegame.world.coord.Coordinate;
import com.freddyheppell.cavegame.world.coord.CoordinateProperties;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Random;

public class Region implements ISaveable{
    private Cell[][] cells = new Cell[Config.REGION_SIZE][Config.REGION_SIZE];
    private Random random;

    public Region(long seed) {
        random = new Random(seed);
    }

    /**
     * Fill the grid with rocks and floor randomly
     */
    public void populateRandomly() {
        for (int x = 0; x < Config.REGION_SIZE; x++) {

            cells[x] = new Cell[Config.REGION_SIZE];

            for (int y = 0; y < cells[x].length; y++) {
                float randomValue = random.nextFloat();

                if (randomValue < Config.RANDOM_BOUNDARY) {
                    cells[x][y] = new Cell(EnumCellType.FLOOR);
                } else {
                    cells[x][y] = new Cell(EnumCellType.ROCK);
                }
            }
        }
    }

    /**
     * Get a cell if it exists, or return rock if it does not
     *
     * @param coordinate The coordinate pair to get
     * @return The Cell at those coordinates
     */
    private Cell getCellIfExists(Coordinate coordinate) {
        if (coordinate.x > Config.REGION_SIZE - 1 ||
                coordinate.y > Config.REGION_SIZE - 1 ||
                coordinate.x < 0 ||
                coordinate.y < 0) {
            return new Cell(EnumCellType.ROCK);
        }

        return cells[coordinate.x][coordinate.y];
    }

    /**
     * Get the Moore neighbourhood of a cell excluding the cell itself
     *
     * @param center The coordinate of the home cell
     * @return A list of cells in the Moore neighbourhood
     */
    private Cell[] getMooreNeighbourhood(Coordinate center) {
        Cell[] neighbours = new Cell[8];

        for (int i = 0; i < 8; i++) {
            Coordinate coordinate = center.addTransform(CoordinateProperties.adjacentTransforms[i]);
            neighbours[i] = getCellIfExists(coordinate);
        }

        return neighbours;
    }

    /**
     * Count the number of cells matching the type
     *
     * @param cells An array of cells
     * @param type  The type to check
     * @return The number of matching cells
     */
    private int countMatchingCells(Cell[] cells, EnumCellType type) {
        int count = 0;
        for (Cell cell : cells) {
            if (cell.type == type) {
                count++;
            }
        }

        return count;
    }

    /**
     * Run an iteration of the cellular model
     */
    public void iteration() {
        // Iterations should be applied simultaneously, so modifications are placed onto a new grid
        Cell[][] nextCells = new Cell[Config.REGION_SIZE][Config.REGION_SIZE];

        for (int x = 0; x < cells.length; x++) {
            Arrays.fill(nextCells[x], new Cell(EnumCellType.UNSET));

            for (int y = 0; y < cells[x].length; y++) {
                Cell[] neighbours = getMooreNeighbourhood(new Coordinate(x, y));
                int count = countMatchingCells(neighbours, EnumCellType.ROCK);

                if (count >= Config.CELL_DEATH_THRESHOLD) {
                    // If the count is higher than the threshold, the cell will become rock
                    nextCells[x][y] = new Cell(EnumCellType.ROCK);
                } else {
                    // but if it is lower, it will become floor
                    nextCells[x][y] = new Cell(EnumCellType.FLOOR);
                }
            }
        }

        // Replace the cell grid with the new grid
        cells = nextCells;
    }

    /**
     * DEBUG: Output a cell to System.out
     */
    public void output() {
        for (Cell[] cell : cells) {
            for (Cell aCell : cell) {
                System.out.print(aCell);
            }
            System.out.print("\n");
        }
        System.out.println("********************************");
    }

    /**
     * Get the save data representation of the region
     *
     * @return A JSON-string of the
     */
    @Override
    public String getSaveData() {
        Gson gson = new Gson();

        return gson.toJson(this);
    }
}
