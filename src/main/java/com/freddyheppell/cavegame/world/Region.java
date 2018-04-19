package com.freddyheppell.cavegame.world;

import com.freddyheppell.cavegame.CaveGame;
import com.freddyheppell.cavegame.config.Config;
import com.freddyheppell.cavegame.entities.Entity;
import com.freddyheppell.cavegame.entities.Monster;
import com.freddyheppell.cavegame.world.cells.*;
import com.freddyheppell.cavegame.world.coord.CellCoordinate;
import com.freddyheppell.cavegame.world.coord.RegionCoordinate;
import com.freddyheppell.cavegame.world.coord.WorldCoordinate;
import com.freddyheppell.cavegame.world.coord.CoordinateProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Region {
    private Cell[][] cells = new Cell[Config.getInt("iRegionSize")][Config.getInt("iRegionSize")];
    private RegionCoordinate regionCoordinate;
    private Random random;

    private static final Logger logger = LogManager.getLogger();

    // Due to serialisation constraints, the entities in the region must be stored as two lists
    // These variables are treated as a hashmap
    private ArrayList<WorldCoordinate> entityCoordinates = new ArrayList<>();
    private ArrayList<Entity> entities = new ArrayList<>();

    /**
     * Has the world been modified since the last save?
     */
    public transient boolean modified = true;

    public Region(long seed, RegionCoordinate regionCoordinate) {
        random = new Random(seed);
        this.regionCoordinate = regionCoordinate;
    }

    /**
     * Fill the grid with rocks and floor randomly
     */
    public void populateRandomly() {
        for (int x = 0; x < Config.getInt("iRegionSize"); x++) {

            cells[x] = new Cell[Config.getInt("iRegionSize")];

            for (int y = 0; y < cells[x].length; y++) {
                float randomValue = random.nextFloat();

                if (randomValue < Config.getFloat("fRandomBoundary")) {
                    cells[x][y] = new FloorCell();
                } else {
                    cells[x][y] = new RockCell();
                }
            }
        }
    }

    public int entityCount() {
        return entityCoordinates.size();
    }

    public void generateChests() {
        for (int x = 0; x < Config.getInt("iRegionSize"); x++) {
            for (int y = 0; y < Config.getInt("iRegionSize"); y++) {
                float randomValue = random.nextFloat();

                if ((randomValue > Config.getFloat("fChestSpawnBoundary")) && (cells[x][y].getClass() == FloorCell.class)) {
                    ChestCell chestCell = new ChestCell();
                    chestCell.generate();
                    cells[x][y] = chestCell;
                }
            }
        }
    }

    public void generateEntities() {
        WorldCoordinate entitySpawnCell = getEntitySpawnCell();

        if (entitySpawnCell != null) {
            Entity entity = new Monster();
            entities.add(entity);
            entityCoordinates.add(entitySpawnCell);
            CaveGame.game.getWorld().getRegionManager().resaveRegion(entitySpawnCell.getRegionCoordinate(), this);

            entity.calculateVisibleCells(entitySpawnCell);


            // Now mark the region as changed
            modified = true;
        }
    }

    public WorldCoordinate getEntitySpawnCell() {
        boolean found = false;
        while (!found) {
            int x = random.nextInt(Config.getInt("iRegionSize"));
            int y = random.nextInt(Config.getInt("iRegionSize"));

            if (!cells[x][y].canSpawn()) {
                CellCoordinate cellCoordinate = new CellCoordinate(x, y);
                return WorldCoordinate.fromRegionAndCell(regionCoordinate, cellCoordinate);
            }
        }
        // If one can't be found, just return null
        return null;
    }

    /**
     * Get a cell if it exists, or return rock if it does not
     *
     * @param worldCoordinate The worldCoordinate pair to get
     * @return The Cell at those coordinates
     */
    public Cell getCellIfExists(WorldCoordinate worldCoordinate) {
        if (worldCoordinate.cx > Config.getInt("iRegionSize") - 1 ||
                worldCoordinate.cy > Config.getInt("iRegionSize") - 1 ||
                worldCoordinate.cx < 0 ||
                worldCoordinate.cy < 0) {
            return new EmptyCell();
        } else {
            return cells[worldCoordinate.cx][worldCoordinate.cy];
        }
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
    private int countMatchingCells(Cell[] cells, Class<?> type) {
        int count = 0;
        for (Cell cell : cells) {
            if (cell.getClass() == type) {
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
        Cell[][] nextCells = new Cell[Config.getInt("iRegionSize")][Config.getInt("iRegionSize")];

        for (int x = 0; x < cells.length; x++) {
            Arrays.fill(nextCells[x], new EmptyCell());

            for (int y = 0; y < cells[x].length; y++) {
                Cell[] neighbours = getMooreNeighbourhood(new WorldCoordinate(x, y));
                int count = countMatchingCells(neighbours, RockCell.class);

                if (count >= Config.getInt("iCellDeathThreshold")) {
                    // If the count is higher than the threshold, the cell will become rock
                    nextCells[x][y] = new RockCell();
                } else {
                    // but if it is lower, it will become floor
                    nextCells[x][y] = new FloorCell();
                }
            }
        }

        // Replace the cell grid with the new grid
        cells = nextCells;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public boolean hasEventForCoordinate(WorldCoordinate worldCoordinate) {
        logger.debug("Checking events for " + worldCoordinate);

        if (getCellIfExists(worldCoordinate).listener != null) {
            logger.debug("Event found");
            return true;
        }
        logger.debug("No event found");
        return false;
    }

    public boolean isEntityAt(WorldCoordinate worldCoordinate) {
        return entityCoordinates.contains(worldCoordinate);
    }

    public Entity getEntityAt(WorldCoordinate worldCoordinate) {
        logger.debug("Getting entity at " + worldCoordinate);
        logger.debug(regionCoordinate);
        logger.debug(entities);
        return entities.get(entityCoordinates.indexOf(worldCoordinate));
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
