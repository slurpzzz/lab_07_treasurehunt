package dungeon;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Student implementation of the dungeon.
 *
 * @author YOUR NAME HERE
 */
public class Dungeon implements IDungeon {
    // TODO

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
            // e.g. to read number of rows: Integer.parseInt(fields[0]);
            // e.g. to read number of columns: Integer.parseInt(fields[1]);

            // TODO
        } // any exceptions generated will get thrown to the main program
    }

    @Override
    public int getRows() {
        // TODO
        return 0;
    }

    @Override
    public int getCols() {
        // TODO
        return 0;
    }

    @Override
    public boolean hasCoordinates(Coordinates location) {
        // TODO
        return false;
    }

    @Override
    public boolean isNeighbor(Coordinates src, Coordinates dest) {
        // TODO
        return false;
    }

    @Override
    public String getCell(Coordinates location) {
        // TODO
        return null;
    }

    @Override
    public Coordinates getHome() {
        // TODO
        return null;
    }

    @Override
    public boolean hasTreasure(Coordinates location) {
        // TODO
        return false;
    }

    @Override
    public Collection<Treasure> getTreasures() {
        // TODO
        return new ArrayList<>();
    }

    @Override
    public Collection<Coordinates> getNeighbors(Coordinates location) {
        // TODO
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return display();
    }
}