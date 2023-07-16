import java.util.ArrayList;
import java.util.HashMap;

public class Algorithms {
    private final static HashMap<String, Integer> statusCodes= new HashMap<>(){{
        put("unsearched", 0);
        put("searched", 1);
        put("frontier", 2);
    }};
    protected final static HashMap<String, int[]> dirCodes = new HashMap<>(){{
        put("N", new int[]{0, -1});
        put("NE", new int[]{1, -1});
        put("E", new int[]{1, 0});
        put("SE", new int[]{1, 1});
        put("S", new int[]{0, 1});
        put("SW", new int[]{-1, 1});
        put("W", new int[]{-1, 0});
        put("NW", new int[]{-1, -1});
    }};
    private final static HashMap<String, String> oppositeDir = new HashMap<>(){{
        put("N", "S");
        put("NE", "SW");
        put("E", "W");
        put("SE", "NW");
        put("S", "N");
        put("SW", "NE");
        put("W", "E");
        put("NW", "SE");
    }};
    protected static boolean done;
    private static Coordinate target;
    private static int[][] values;
    protected static int[][] searchStatus;
    protected static String[][] directions;

    protected static boolean routeExists;


    protected static Coordinate lastSearched;
    protected static ArrayList<Coordinate> newFrontier = new ArrayList<>();

    public static void setupBreadthFirstSearch(Map map) {
        done = false;
        searchStatus = new int[map.getHeight()][map.getWidth()];
        values = new int[map.getHeight()][map.getWidth()];
        directions = new String[map.getHeight()][map.getWidth()];

        // Enter the start and target coordinates
        Coordinate start = map.getStartCoord();
        searchStatus[start.y()][start.x()] = statusCodes.get("frontier");
        values[start.y()][start.x()] = 0;

        target = map.getTargetCoord();
        values[target.y()][target.x()] = Integer.MAX_VALUE;
    }

    /**
     * Progresses the breadth-first search by one step. Sets done to true when finished.
     */
    public static void stepBreadthFirstSearch(Map map) {
        Coordinate toSearch = findLowestFrontier();
        // Return true if best route has been found
        if(values[toSearch.y()][toSearch.x()] >= values[target.y()][target.x()]) {
            routeExists = true;
            done = true;
        }

        // Complete one step of the search
        searchStatus[toSearch.y()][toSearch.x()] = statusCodes.get("searched");
        breadthExpandFrontier(map, toSearch);

        // If the frontier is empty, the search has failed
        if (frontierEmpty()) {
            routeExists = false;
            done = true;
        }

        lastSearched = toSearch;
    }

    private static boolean frontierEmpty(){
        for(int[] row : searchStatus)
            for(int status : row)
                if(status == statusCodes.get("frontier"))
                    return false;
        return true;
    }

    /**
     * Returns the Coordinate of the frontier tile with the lowest value.
     */
    private static Coordinate findLowestFrontier(){
        Coordinate lowest = null;
        int low = Integer.MAX_VALUE;
        for(int r = 0; r<values.length; r++)
            for(int c=0; c<values[0].length; c++){
                if(!(searchStatus[r][c] == statusCodes.get("frontier")))
                    continue;
                if (values[r][c] < low){
                    low = values[r][c];
                    lowest = new Coordinate(c, r);
                }
            }
        return lowest;
    }

    /**
     * Uses the Map to explore the frontier around the given Coordinate. Walls and out of bounds tiles are disregarded.
     * All other tiles are given the frontier status, with their values and directions being updated if necessary.
     */
    private static void breadthExpandFrontier(Map map, Coordinate searched){
        newFrontier.clear();
        for(String key : dirCodes.keySet()){
            int[] dir = dirCodes.get(key);
            Coordinate possibleFrontier = new Coordinate(searched.x() + dir[0], searched.y() + dir[1]);
            if (!(map.isInBounds(possibleFrontier) && !map.isWall(possibleFrontier) && searchStatus[possibleFrontier.y()][possibleFrontier.x()] != statusCodes.get("searched")))
                continue;
            // Sets dirDistance to 10 if cardinal, 14 if diagonal (14 representing square root of 2)
            int dirDistance = 6 + key.length()*4;
            int totalDistance = values[searched.y()][searched.x()] + dirDistance;
            if (searchStatus[possibleFrontier.y()][possibleFrontier.x()] == statusCodes.get("unsearched")){
                searchStatus[possibleFrontier.y()][possibleFrontier.x()] = statusCodes.get("frontier");
                newFrontier.add(possibleFrontier);
                values[possibleFrontier.y()][possibleFrontier.x()] = totalDistance;
                directions[possibleFrontier.y()][possibleFrontier.x()] = oppositeDir.get(key);
            }
            else if (values[possibleFrontier.y()][possibleFrontier.x()] > totalDistance) {
                    values[possibleFrontier.y()][possibleFrontier.x()] = totalDistance;
                    directions[possibleFrontier.y()][possibleFrontier.x()] = oppositeDir.get(key);
            }
        }
    }
}
