package dungeon;

import org.jspecify.annotations.NonNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Student implementation of the dungeon.
 *
 * @author Justin Spadone
 */
public class Dungeon implements IDungeon {
    // TODO
    private static int rows;
    private static int cols;
    private final LinkedHashMap<Coordinates, LinkedHashSet<Coordinates>> graph = new LinkedHashMap<>();
    private final ArrayList<Treasure> treasures = new ArrayList<>();
    private final Coordinates home;

    /**
     * Create a new dungeon from a file.
     *
     * @param filename the name of the dungeon file
     * @throws IOException      if a problem is encountered reading the file
     * @throws DungeonException if there is an error in the dungeon file
     */
    public Dungeon(String filename) throws IOException, DungeonException {
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            // first line is: rows cols
            String line = in.readLine();
            String[] fields = line.split("\\s+");
            rows = Integer.parseInt(fields[0]);
            cols = Integer.parseInt(fields[1]);
            if (rows <= 0 && cols <= 0)
                throw new DungeonException("rows and columns must be greater than zero: " + rows + " " + cols);
            else if (rows <= 0) throw new DungeonException("rows must be greater than zero: " + rows);
            else if (cols <= 0) throw new DungeonException("columns must be greater than zero: " + cols);
            // e.g. to read number of rows: Integer.parseInt(fields[0]);
            // e.g. to read number of columns: Integer.parseInt(fields[1]);
            for (int row = 0; row < rows; row++) {
                String[] cells = in.readLine().split("\\s+");
                for (int col = 0; col < cols; col++) {
                    LinkedHashSet<Coordinates> neighbors = getNeighbors(cells, row, col);
                    graph.put(new Coordinates(row, col), neighbors);
                }
            }
            String homeLine = in.readLine();
            if (!homeLine.contains("*")) throw new DungeonException("home position not specified!");
            String homeCoord = homeLine.split(" ")[1];
            home = new Coordinates(homeCoord);
            int numTreasures = Integer.parseInt(in.readLine());
            for (int i = 0; i < numTreasures; i++) {
                String[] treasure = in.readLine().split(" ");
                treasures.add(new Treasure(treasure[0], new Coordinates(treasure[1])));
            }
            // TODO
        } // any exceptions generated will get thrown to the main program
    }

    private static @NonNull LinkedHashSet<Coordinates> getNeighbors(String[] cells, int row, int col) throws DungeonException {
        String cell = cells[col];
        LinkedHashSet<Coordinates> neighbors = new LinkedHashSet<>();
        if (cell.contains("N")) {
            if (row == 0) throw new DungeonException("cannot have a north neighbor: (%d,%d)".formatted(row, col));
            neighbors.add(new Coordinates(row - 1, col));
        }
        if (cell.contains("S")) {
            if (row == rows - 1)
                throw new DungeonException("cannot have a south neighbor: (%d,%d)".formatted(row, col));
            neighbors.add(new Coordinates(row + 1, col));
        }
        if (cell.contains("E")) {
            if (col == cols - 1)
                throw new DungeonException("cannot have an east neighbor: (%d,%d)".formatted(row, col));
            neighbors.add(new Coordinates(row, col + 1));
        }
        if (cell.contains("W")) {
            if (col == 0) throw new DungeonException("cannot have a west neighbor: (%d,%d)".formatted(row, col));
            neighbors.add(new Coordinates(row, col - 1));
        }
        return neighbors;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getCols() {
        return cols;
    }

    @Override
    public boolean hasCoordinates(Coordinates location) {
        // TODO
        return graph.containsKey(location);
    }

    @Override
    public boolean isNeighbor(Coordinates src, Coordinates dest) {
        // TODO
        return graph.get(src).contains(dest);
    }

    @Override
    public String getCell(Coordinates location) {
        // TODO
        if (home.equals(location)) return HOME;
        for (Treasure treasure : treasures) {
            if (treasure.getLocation().equals(location)) {
                if (treasure.isCollected()) return CELL;
                return treasure.getName();
            }
        }
        return CELL;
    }

    @Override
    public Coordinates getHome() {
        return home;
    }

    @Override
    public boolean hasTreasure(Coordinates location) {
        // TODO
        return treasures.stream().anyMatch(obj -> Objects.equals(location, obj.getLocation()));
    }

    @Override
    public Collection<Treasure> getTreasures() {
        // TODO
        return treasures;
    }

    @Override
    public Collection<Coordinates> getNeighbors(Coordinates location) {
        return graph.get(location);
    }

    @Override
    public String toString() {
        return display();
    }
}