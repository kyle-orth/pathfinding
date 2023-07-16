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

    public static void showSearch(Display display) {
        display.showSearch(Algorithms.lastSearched, "searched");
        for(Coordinate frontier : Algorithms.newFrontier)
            display.showSearch(frontier, "frontier");
    }

    private static int getAlgorithmTimer(Display display){
        String delayString = "" + display.algorithmSpeed.getSelectedItem();
        delayString = delayString.substring(0, delayString.length() - 2);
        return Integer.parseInt(delayString);
    }

    public static Coordinate showFinalRoute(Coordinate lastShown, Display display){
        String dirName = Algorithms.directions[lastShown.y()][lastShown.x()];
        int[] direction = Algorithms.dirCodes.get(dirName);
        Coordinate nextInPath = new Coordinate(lastShown.x() + direction[0], lastShown.y() + direction[1]);
        display.showSearch(nextInPath, "path");
        return nextInPath;
    }

    public static void delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
