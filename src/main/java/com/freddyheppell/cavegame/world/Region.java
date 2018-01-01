package com.freddyheppell.cavegame.world;

import com.freddyheppell.cavegame.config.Config;
import com.freddyheppell.cavegame.world.coord.WorldCoordinate;
import com.freddyheppell.cavegame.world.coord.CoordinateProperties;

import java.util.Arrays;
import java.util.Random;

public class Region {
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
     * @param worldCoordinate The worldCoordinate pair to get
     * @return The Cell at those coordinates
     */
    public Cell getCellIfExists(WorldCoordinate worldCoordinate) {
        if (worldCoordinate.wx > Config.REGION_SIZE - 1 ||
                worldCoordinate.wy > Config.REGION_SIZE - 1 ||
                worldCoordinate.wx < 0 ||
                worldCoordinate.wy < 0) {
            return new Cell(EnumCellType.ROCK);
        }

        return cells[worldCoordinate.wx][worldCoordinate.wy];
    }

    /**
     * Get the Moore neighbourhood of a cell excluding the cell itself
     *
     * @param center The coordinate of the home cell
     * @return A list of cells in the Moore neighbourhood
     */
    private Cell[] getMooreNeighbourhood(WorldCoordinate center) {
        Cell[] neighbours = new Cell[8];

        for (int i = 0; i < 8; i++) {
            WorldCoordinate worldCoordinate = center.addTransform(CoordinateProperties.adjacentTransforms[i]);
            neighbours[i] = getCellIfExists(worldCoordinate);
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
                Cell[] neighbours = getMooreNeighbourhood(new WorldCoordinate(x, y));
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

    public Cell[][] getCells() {
        return cells;
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
}
