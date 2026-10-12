package test;

import dungeon.*;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestDungeon {
    /** Used to test that expected System.out print's happen */
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(System.out);
    }

    // used for neighbor checking
    private Map<Coordinates, Set<Coordinates>> neighbors;
    private Map<Coordinates, Set<Coordinates>> invalidNeighbors;

    /**
     * This helper function builds the complete collection of neighbors
     * for every cell in the 3x4 dungeon from dungeon3.txt.
     * @param dungeon the dungeon
     * @return full collection of neighbors
     */
    private void generateNeighbors(IDungeon dungeon) {
        // build a 2-D grid of coordinates for easy access
        Coordinates[][] grid = new Coordinates[dungeon.getRows()][dungeon.getCols()];
        for (int row=0; row<dungeon.getRows(); ++row) {
            for (int col=0; col<dungeon.getCols(); ++col) {
                grid[row][col] = new Coordinates(row, col);
            }
        }

        // build the adjacency list that associates a cell with neighbor cells
        this.neighbors = new LinkedHashMap<>();
        // row 0
        this.neighbors.put(grid[0][0], Stream.of(grid[1][0])
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        this.neighbors.put(grid[0][1], Stream.of(grid[0][2], grid[1][1])
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        this.neighbors.put(grid[0][2], Stream.of(grid[0][1], grid[0][3])
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        this.neighbors.put(grid[0][3], Stream.of(grid[0][2])
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        // row 1
        this.neighbors.put(grid[1][0], Stream.of(grid[0][0], grid[2][0])
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        this.neighbors.put(grid[1][1], Stream.of(grid[0][1], grid[1][2], grid[2][1])
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        this.neighbors.put(grid[1][2], Stream.of(grid[1][1])
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        this.neighbors.put(grid[1][3], Stream.of(grid[2][3])
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        // row 2
        this.neighbors.put(grid[2][0], Stream.of(grid[1][0], grid[2][1])
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        this.neighbors.put(grid[2][1], Stream.of(grid[1][1], grid[2][0], grid[2][2])
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        this.neighbors.put(grid[2][2], Stream.of(grid[2][1])
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        this.neighbors.put(grid[2][3], Stream.of(grid[1][3])
                .collect(Collectors.toCollection(LinkedHashSet::new)));

        // now generate some invalid neighbors
        this.invalidNeighbors = new LinkedHashMap<>();
        // row 0
        this.invalidNeighbors.put(grid[0][0], Stream.of(grid[2][0], grid[0][3], grid[0][1])
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        this.invalidNeighbors.put(grid[0][1], Stream.of(grid[0][0], grid[2][1])
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        this.invalidNeighbors.put(grid[0][2], Stream.of(grid[2][2], grid[1][2])
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        this.invalidNeighbors.put(grid[0][3], Stream.of(grid[0][0], grid[2][3], grid[1][3])
                .collect(Collectors.toCollection(LinkedHashSet::new)));

    }

    @Test
    @Order(1)
    public void testDimensions() throws DungeonException {
        try {
            IDungeon dungeon = new Dungeon("data/dungeon-3.txt");
            assertEquals(3, dungeon.getRows());
            assertEquals(4, dungeon.getCols());
        } catch (IOException ioe) {
            System.err.println("Error: " + ioe);
        }

    }

    @Test
    @Order(2)
    public void testHasCoordinates() throws DungeonException {
        try {
            IDungeon dungeon = new Dungeon("data/dungeon-3.txt");
            for (int row=0; row<dungeon.getRows(); ++row) {
                for (int col=0; col<dungeon.getCols(); ++col) {
                    assertTrue(dungeon.hasCoordinates(new Coordinates(row, col)));
                }
            }
        } catch (IOException ioe) {
            System.err.println("Error: " + ioe);
        }
    }

    @Test
    @Order(3)
    public void testNeighbors() throws DungeonException {
        try {
            IDungeon dungeon = new Dungeon("data/dungeon-3.txt");
            generateNeighbors(dungeon);
            // all these neighbors are valid and should exist
            for (Coordinates src : this.neighbors.keySet()) {
                for (Coordinates dest : this.neighbors.get(src)) {
                    assertTrue(dungeon.isNeighbor(src, dest));
                }
            }

            // all these neighbors are invalid and should not exist
            for (Coordinates src : this.invalidNeighbors.keySet()) {
                for (Coordinates dest : this.invalidNeighbors.get(src)) {
                    assertFalse(dungeon.isNeighbor(src, dest));
                }
            }

            // now check getNeighbors
            for (Coordinates src : this.neighbors.keySet()) {
                for (Coordinates dest : this.neighbors.get(src)) {
                    assertTrue(dungeon.getNeighbors(src).contains(dest));
                }
            }
        } catch (IOException ioe) {
            System.err.println("Error: " + ioe);
        }
    }

    @Test
    @Order(4)
    public void testHome()  throws DungeonException {
        try {
            IDungeon dungeon = new Dungeon("data/dungeon-3.txt");
            assertEquals(new Coordinates(0,0), dungeon.getHome());
        } catch (IOException ioe) {
            System.err.println("Error: " + ioe);
        }
    }

    @Test
    @Order(5)
    public void testTreasures()  throws DungeonException {
        try {
            IDungeon dungeon = new Dungeon("data/dungeon-3.txt");

            Set<Treasure> treasures = new LinkedHashSet<>();
            treasures.add(new Treasure("A", new Coordinates(0, 3)));
            treasures.add(new Treasure("B", new Coordinates(1, 2)));
            treasures.add(new Treasure("C", new Coordinates(2, 3)));

            // getTreasures
            for (Treasure treasure : dungeon.getTreasures()) {
                assertTrue(treasures.contains(treasure));
            }

            // hasTreasures - check cells for treasure or no treasure
            // row 0
            assertFalse(dungeon.hasTreasure(new Coordinates(0, 0)));
            assertFalse(dungeon.hasTreasure(new Coordinates(0, 1)));
            assertFalse(dungeon.hasTreasure(new Coordinates(0, 2)));
            assertTrue(dungeon.hasTreasure(new Coordinates(0, 3)));
            // row 1
            assertFalse(dungeon.hasTreasure(new Coordinates(1, 0)));
            assertFalse(dungeon.hasTreasure(new Coordinates(1, 1)));
            assertTrue(dungeon.hasTreasure(new Coordinates(1, 2)));
            assertFalse(dungeon.hasTreasure(new Coordinates(1, 3)));
            // row 2
            assertFalse(dungeon.hasTreasure(new Coordinates(2, 0)));
            assertFalse(dungeon.hasTreasure(new Coordinates(2, 1)));
            assertFalse(dungeon.hasTreasure(new Coordinates(2, 2)));
            assertTrue(dungeon.hasTreasure(new Coordinates(2, 3)));
        } catch (IOException ioe) {
            System.err.println("Error: " + ioe);
        }
    }

    @Test
    @Order(6)
    public void testCells()  throws DungeonException {
        try {
            IDungeon dungeon = new Dungeon("data/dungeon-3.txt");
            // row 0
            assertEquals(IDungeon.HOME, dungeon.getCell(new Coordinates(0,0)));
            assertEquals(IDungeon.CELL, dungeon.getCell(new Coordinates(0,1)));
            assertEquals(IDungeon.CELL, dungeon.getCell(new Coordinates(0,2)));
            assertEquals("A", dungeon.getCell(new Coordinates(0,3)));
            // row 1
            assertEquals(IDungeon.CELL, dungeon.getCell(new Coordinates(1,0)));
            assertEquals(IDungeon.CELL, dungeon.getCell(new Coordinates(1,1)));
            assertEquals("B", dungeon.getCell(new Coordinates(1,2)));
            assertEquals(IDungeon.CELL, dungeon.getCell(new Coordinates(1,3)));
            // row 2
            assertEquals(IDungeon.CELL, dungeon.getCell(new Coordinates(2,0)));
            assertEquals(IDungeon.CELL, dungeon.getCell(new Coordinates(2,1)));
            assertEquals(IDungeon.CELL, dungeon.getCell(new Coordinates(2,2)));
            assertEquals("C", dungeon.getCell(new Coordinates(2,3)));

        } catch (IOException ioe) {
            System.err.println("Error: " + ioe);
        }
    }

    @Test
    @Order(7)
    public void testDisplay()  throws DungeonException {
        try {
            IDungeon dungeon = new Dungeon("data/dungeon-3.txt");
            String expected =
                    "  0 1 2 3 " + System.lineSeparator() +
                            "  -------" + System.lineSeparator() +
                            "0|*|. . A|" + System.lineSeparator() +
                            "      - - " + System.lineSeparator() +
                            "1|.|. B|.|" + System.lineSeparator() +
                            "      -   " + System.lineSeparator() +
                            "2|. . .|C|" + System.lineSeparator() +
                            "  -------";
            assertEquals(expected, dungeon.display());

            // when a treasure is collected, it should display as CELL
            for (Treasure treasure : dungeon.getTreasures()) {
                treasure.collect();
            }
            expected =
                    "  0 1 2 3 " + System.lineSeparator() +
                            "  -------" + System.lineSeparator() +
                            "0|*|. . .|" + System.lineSeparator() +
                            "      - - " + System.lineSeparator() +
                            "1|.|. .|.|" + System.lineSeparator() +
                            "      -   " + System.lineSeparator() +
                            "2|. . .|.|" + System.lineSeparator() +
                            "  -------";
            outContent.reset();
            dungeon.display();
            assertEquals(expected, dungeon.display());
        } catch (IOException ioe) {
            System.err.println("Error: " + ioe);
        }
    }
}