package dungeon;

/**
 * A custom exception class for problems encountered while running
 * the TreasureHunt application.
 *
 * @author RIT CS
 */
public class DungeonException extends Exception {
    /**
     * Construct a new instance.
     *
     * @param msg the associated error message
     */
    public DungeonException(String msg) {
        super("DungeonException: " + msg);
    }
}
