package dungeon;

import java.io.IOException;


/**
 * The main program for the dungeon crawler.  It is run on the command
 * line with the dungeon file, e.g.:
 *
 * $ java TreasureHunt data/dungeon-3.txt
 *
 * @author RIT CS
 * @author YOUR NAME HERE
 */
public class TreasureHunt {
    /** the dungeon */
    private final IDungeon dungeon;

    /**
     * Create the instance and construct the dungeon from the file.  Then print the 
     * initial dungeon.
     *
     * @param filename the dungeon file
     * @throws IOException if there is a problem reading the file
     * @throws DungeonException if there is an error in the dungeon file
     */
    public TreasureHunt(String filename) throws IOException, DungeonException {
        this.dungeon = new Dungeon(filename);
        System.out.println(this.dungeon);
    }
    

    /**
     * Find all the treasures in the dungeon, starting at the home position
     * find the shortest path to the first treasure.  For each additional treasure,
     * find the shortest path from the last collected treasure to the next uncollected one.
     * When the last treasure is collected, find the shortest path from it to the
     * home position.  All along we the way we keep track of the total number of steps taken.
     */
    public void findTreasures() {
        // TODO
    }

    /**
     * The main method.
     *
     * @param args command line arguments (dungeon file)
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TreasureHunt dungeon-file");
        } else {
            try {
                // construct the dungeon here
                TreasureHunt hunt = new TreasureHunt(args[0]);
                // perform searches over our dungeon
                hunt.findTreasures();
            } catch (IOException | DungeonException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
