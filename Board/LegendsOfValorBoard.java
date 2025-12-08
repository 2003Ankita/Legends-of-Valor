package Board;

import java.util.Random;

import Items.Position;

/**
 * Board implementation dedicated to Legends of Valor.
 *
 * Layout (8x8) per PDF:
 * - 3 lanes of width 2 (TOP, MIDDLE, BOTTOM)
 * - Columns 2 and 5 are INACCESSIBLE walls between lanes
 * - Row 0: Monsters' Nexus, row 7: Heroes' Nexus
 * - Lane cells in rows 1..6 are random Plain/Bush/Cave/Koulou/Obstacle
 */
public class LegendsOfValorBoard {

    public static final int SIZE = 8;

    private final LegendsTile[][] grid;

    private LegendsOfValorBoard(LegendsTile[][] grid) {
        this.grid = grid;
    }

    public int getSize() {
        return SIZE;
    }

    public LegendsTile getTile(Position p) {
        return getTile(p.row, p.col);
    }

    public LegendsTile getTile(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            throw new IndexOutOfBoundsException("Out of bounds: " + row + "," + col);
        }
        return grid[row][col];
    }

    public boolean inBounds(Position p) {
        return p.row >= 0 && p.row < SIZE && p.col >= 0 && p.col < SIZE;
    }

    /** Heroes spawn in the left cell of their lane's Nexus (bottom row). */
    public Position heroNexusForLane(Lane lane) {
        return new Position(SIZE - 1, lane.getLeftColumn());
    }

    /** Monsters spawn in the right cell of their lane's Nexus (top row). */
    public Position monsterNexusForLane(Lane lane) {
        return new Position(0, lane.getRightColumn());
    }

    /**
     * Builder for LegendsOfValorBoard (Builder pattern).
     * Lets you tweak terrain distribution if needed.
     */
    public static class Builder {
        private double bushRate = 0.20;
        private double caveRate = 0.20;
        private double koulouRate = 0.20;
        private double obstacleRate = 0.05;
        private long randomSeed = System.currentTimeMillis();

        public Builder withBushRate(double bushRate) {
            this.bushRate = bushRate;
            return this;
        }

        public Builder withCaveRate(double caveRate) {
            this.caveRate = caveRate;
            return this;
        }

        public Builder withKoulouRate(double koulouRate) {
            this.koulouRate = koulouRate;
            return this;
        }

        public Builder withObstacleRate(double obstacleRate) {
            this.obstacleRate = obstacleRate;
            return this;
        }

        public Builder withRandomSeed(long seed) {
            this.randomSeed = seed;
            return this;
        }

        public LegendsOfValorBoard build() {
            LegendsTile[][] grid = new LegendsTile[SIZE][SIZE];
            Random rand = new Random(randomSeed);

            // Default everything to PLAIN
            for (int r = 0; r < SIZE; r++) {
                for (int c = 0; c < SIZE; c++) {
                    grid[r][c] = new LegendsTile(TerrainType.PLAIN);
                }
            }

            // Walls between lanes: columns 2 and 5 (INACCESSIBLE)
            for (int r = 0; r < SIZE; r++) {
                grid[r][2] = new LegendsTile(TerrainType.INACCESSIBLE);
                grid[r][5] = new LegendsTile(TerrainType.INACCESSIBLE);
            }

            // Nexus rows
            for (Lane lane : Lane.values()) {
                // Monster Nexus at top row
                grid[0][lane.getLeftColumn()] = new LegendsTile(TerrainType.MONSTER_NEXUS);
                grid[0][lane.getRightColumn()] = new LegendsTile(TerrainType.MONSTER_NEXUS);

                // Hero Nexus at bottom row
                grid[SIZE - 1][lane.getLeftColumn()] = new LegendsTile(TerrainType.HERO_NEXUS);
                grid[SIZE - 1][lane.getRightColumn()] = new LegendsTile(TerrainType.HERO_NEXUS);
            }

            // Lane cells (rows 1..SIZE-2) get random terrain
            for (int r = 1; r < SIZE - 1; r++) {
                for (int c = 0; c < SIZE; c++) {
                    if (c == 2 || c == 5)
                        continue; // wall columns
                    TerrainType type = randomLaneTerrain(rand);
                    grid[r][c] = new LegendsTile(type);
                }
            }

            return new LegendsOfValorBoard(grid);
        }

        private TerrainType randomLaneTerrain(Random rand) {
            double roll = rand.nextDouble();
            if (roll < obstacleRate) {
                return TerrainType.OBSTACLE;
            }
            roll -= obstacleRate;
            if (roll < bushRate)
                return TerrainType.BUSH;
            roll -= bushRate;
            if (roll < caveRate)
                return TerrainType.CAVE;
            roll -= caveRate;
            if (roll < koulouRate)
                return TerrainType.KOULOU;
            return TerrainType.PLAIN;
        }
    }
}
