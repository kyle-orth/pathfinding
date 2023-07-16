import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interface {
    protected static String cursorType = "highlight";
    protected static String menuType = "editor";
    protected static String[] menuOptions = {"editor", "algorithms", "load/save [Future]", "ai pathfinder [Future]"};
    protected static String[] algorithmSpeedOptions = {"10ms", "100ms", "500ms", "1000ms"};

    private static Timer timer;
    private static Coordinate lastPathShown;

    /**
     * Called when a JButton in the colorMap is clicked. Based on the current cursorType, this updates
     * the underlying map, as well as the JButton's corresponding color. If the map is changed beyond the given
     * JButton, then a Coordinate is returned for the Display to update.
     */
    public static void colorMapClick(Map map, Coordinate tile, Display display) {
        if (!menuType.equals("editor"))
            return;
        Coordinate additionalToUpdate = null;
        // Use the map and Coordinate to find the tileType at that tile
        String tileType = "";
        for (String key : Map.legend.keySet())
            if (Map.legend.get(key).equals(map.getMap()[tile.y()][tile.x()]))
                tileType = key;

        // Cursor Mode: toggles tile from empty to highlight and back. Ignore other tiles.
        if (cursorType.equals("highlight")) {
            if (tileType.equals("empty"))
                map.setHighlight(tile);

            else if (tileType.equals("highlight")) {
                map.setEmpty(tile);
            }
        }
        // Wall Mode: Toggle walls and empty tiles with one another. Ignore start and target tiles.
        else if (cursorType.equals("wall")) {
            if (tileType.equals("wall")) {
                map.setEmpty(tile);
            } else if (tileType.equals("empty") || tileType.equals("highlight")) {
                map.setWall(tile);
            }
        }
        // Start Mode: Move the start tile to the selected empty or highlight tile. Ignore other tiles.
        else if (cursorType.equals("start") && (tileType.equals("empty") || tileType.equals("highlight"))) {
            additionalToUpdate = map.getStartCoord();
            map.setStartCoord(tile);
        }

        // Target Mode: Move the target tile to the selected empty or highlight tile. Ignore other tiles.
        else if (cursorType.equals("target") && tileType.equals("empty") || tileType.equals("highlight")) {
            additionalToUpdate = map.getTargetCoord();
            map.setTargetCoord(tile);
        }

        // Reflect changes onto the Display
        display.updateColorMap(tile);
        if (additionalToUpdate != null)
            display.updateColorMap(additionalToUpdate);
    }

    /**
     * Performs a breadth-first search on the given map, updating the given display. Implements step-by-step
     * searching, while updating the display every frame and waiting for an amount of time designated by
     * the Display's algorithmSpeed variable.
     * @param map Map containing start, target, and wall structure to pathfind in
     * @param display Display in which to show the process, which also controls the speed.
     */
    public static void breadthFirstSearch(Map map, Display display) {
        // Reset the colorMap and clear highlighted squares
        display.updateColorMap();
        display.clearHighlighted();

        Algorithms.setupBreadthFirstSearch(map);
        lastPathShown = map.getTargetCoord();
        // The timer acts as the loop, performing one time-step each loop, then delaying for the selected time
        // as controlled by the user via the Display's algorithm timer.
        timer = new Timer(getAlgorithmTimer(display), e -> {
            if (Algorithms.done) {
                // Increment the shown path until complete
                lastPathShown = showFinalRoute(lastPathShown, display);
                if(lastPathShown.equals(map.getStartCoord()))
                    timer.stop();
            }
            else {
                Algorithms.stepBreadthFirstSearch(map);
                showSearch(display);
                timer.setDelay(getAlgorithmTimer(display));
            }
        });
        timer.setInitialDelay(0);
        timer.start();
    }

    /**
     * Uses the stored data in Algorithms to efficiently update the display based on the past algorithm step
     * @param display Display on which to show the search
     */
    public static void showSearch(Display display) {
        display.showSearch(Algorithms.lastSearched, "searched");
        for(Coordinate frontier : Algorithms.newFrontier)
            display.showSearch(frontier, "frontier");
    }

    /**
     * @return the int representing milliseconds of the selected option on the Algorithm menu of the Display
     */
    private static int getAlgorithmTimer(Display display){
        String delayString = "" + display.algorithmSpeed.getSelectedItem();
        delayString = delayString.substring(0, delayString.length() - 2);
        return Integer.parseInt(delayString);
    }

    /**
     * Shows the next step of the path (backwards) in the route found by Algorithms. Once it reaches the
     * start tile, it should not be called again.
     * @param lastShown Coordinate holding the location of the last tile shown (the output of this function's previous call)
     * @param display The Display on which to show the path
     * @return The tile that was just displayed.
     */
    public static Coordinate showFinalRoute(Coordinate lastShown, Display display){
        String dirName = Algorithms.directions[lastShown.y()][lastShown.x()];
        int[] direction = Algorithms.dirCodes.get(dirName);
        Coordinate nextInPath = new Coordinate(lastShown.x() + direction[0], lastShown.y() + direction[1]);
        display.showSearch(nextInPath, "path");
        return nextInPath;
    }
}
