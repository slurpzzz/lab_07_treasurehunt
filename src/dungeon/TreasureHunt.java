package dungeon;

import java.io.IOException;
import java.util.*;


/**
 * The main program for the dungeon crawler.  It is run on the command
 * line with the dungeon file, e.g.:
 * <p>
 * $ java TreasureHunt data/dungeon-3.txt
 *
 * @author RIT CS
 * @author Justin Spadone
 */
public class TreasureHunt {
    /**
     * the dungeon
     */
    private final IDungeon dungeon;

    /**
     * Create the instance and construct the dungeon from the file.  Then print the
     * initial dungeon.
     *
     * @param filename the dungeon file
     * @throws IOException      if there is a problem reading the file
     * @throws DungeonException if there is an error in the dungeon file
     */
    public TreasureHunt(String filename) throws IOException, DungeonException {
        this.dungeon = new Dungeon(filename);
        System.out.println(this.dungeon);
    }

    /**
     * Uses BFS to find the shortest path between given coordinates
     * @param start starting coordinate of BFS
     * @param finish ending coordinate of BFS
     * @return The list of coordinates of the shortest path from start to finish. Null if no path exists.
     */
    private List<Coordinates> getShortestPath(Coordinates start, Coordinates finish) {
        Map<Coordinates, Coordinates> predecessorMap = new HashMap<>();
        Queue<Coordinates> queue = new ArrayDeque<>();
        Coordinates current = start;
        queue.add(current);
        predecessorMap.put(current, null);
        while (!queue.contains(finish) && !queue.isEmpty()) {
            current = queue.remove();
            Collection<Coordinates> neighbors = dungeon.getNeighbors(current);
            for (Coordinates neighbor : neighbors) {
                if (!predecessorMap.containsKey(neighbor)) {
                    queue.add(neighbor);
                    predecessorMap.put(neighbor, current);
                }
            }
        }
        if (queue.isEmpty()) return null;
        current = finish;
        List<Coordinates> shortestPath = new LinkedList<>();
        shortestPath.addFirst(finish);
        while (predecessorMap.get(current) != null) {
            shortestPath.addFirst(predecessorMap.get(current));
            current = predecessorMap.get(current);
        }
        return shortestPath;
    }

    /**
     * Find all the treasures in the dungeon, starting at the home position
     * find the shortest path to the first treasure.  For each additional treasure,
     * find the shortest path from the last collected treasure to the next uncollected one.
     * When the last treasure is collected, find the shortest path from it to the
     * home position.  All along we the way we keep track of the total number of steps taken.
     */
    public void findTreasures() {
        int totalSteps = 0;
        Coordinates current = dungeon.getHome();
        List<Coordinates> path;
        for (Treasure treasure : dungeon.getTreasures()) {
            System.out.println("Collecting: " + treasure);
            path = getShortestPath(current, treasure.getLocation());
            if (path == null) {
                System.out.printf("\tNo path from %s to %s!%n", current, treasure.getLocation());
            } else {
                System.out.printf("\tPath from %s to %s: %s%n", current, treasure.getLocation(), path);
                System.out.println("\tSteps: " + (path.size() - 1));
                totalSteps += path.size() - 1;
                current = treasure.getLocation();
                treasure.collect();
            }
            System.out.println(dungeon.display());
        }
        System.out.printf("Returning home from %s to %s%n", current, dungeon.getHome());
        path = getShortestPath(current, dungeon.getHome());
        assert path != null;
        System.out.printf("\tPath from %s to %s: %s%n", current, dungeon.getHome(), path);
        System.out.println("\tSteps: " + (path.size() - 1));
        totalSteps += path.size() - 1;
        System.out.println("Total Steps: " + totalSteps);
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
