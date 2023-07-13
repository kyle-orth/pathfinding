import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.*;
import java.util.HashMap;
import javax.swing.WindowConstants;

public class Interface {
    protected static final HashMap<String, Color> colors = new HashMap<>(){{
        put("start", Color.green);
        put("target", Color.orange);
        put("wall", Color.gray);
        put("empty", Color.white);
    }};

    private final JFrame f;
    private final JLabel[][] colorMap;

    /**
     * Creates an Interface object to render the map based on the given Map object's current values. The
     * map can have any dimensions. A JFrame application window will appear upon calling this constructor.
     */
    public Interface(Map map) {
        // Set the window dimensions, and the resulting tile dimensions
        int WINDOW_WIDTH = 1500;
        int WINDOW_HEIGHT = 850;
        int tileWidth = (WINDOW_WIDTH * 3 / 4) / map.getWidth();
        int tileHeight = (WINDOW_HEIGHT - 40) / map.getHeight();

        // Initialize the frame and set default values
        f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        // Initialize the colorMap and create white JLabels, while adding them to the JFrame
        colorMap = new JLabel[map.getWidth()][map.getHeight()];
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                JLabel l = new JLabel();
                l.setBounds(1 + i * tileWidth, 1 + j * tileHeight, tileWidth-1, tileHeight-1);
                l.setBackground(Color.white);
                l.setOpaque(true);
                colorMap[i][j] = l;
                f.add(l);
            }
        }

        // Set the JLabel colors based on actual map values
        update(map);

        // Render the JFrame window
        f.setLayout(null);
        f.setVisible(true);
    }

    /**
     * Uses the provided Map dimensions and values to update the color of each JLabel in the colorMap.
     */
    public void update(Map map) {
        int[][] mapValues = map.getMap();
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                if (mapValues[i][j] == Map.empty)
                    colorMap[i][j].setBackground(Interface.colors.get("empty"));
                else if (mapValues[i][j] == Map.wall)
                    colorMap[i][j].setBackground(Interface.colors.get("wall"));
                else if (mapValues[i][j] == Map.start)
                    colorMap[i][j].setBackground(Interface.colors.get("start"));
                else if (mapValues[i][j] == Map.target)
                    colorMap[i][j].setBackground(Interface.colors.get("target"));
            }
        }
    }
}
