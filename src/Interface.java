import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.HashMap;

public class Interface {
    protected static final HashMap<String, Color> colors = new HashMap<>(){{
        put("start", Color.green);
        put("target", Color.orange);
        put("wall", Color.gray);
        put("empty", Color.white);
        put("cursor", Color.cyan);
    }};

    private final JFrame f;
    private final JLabel[][] colorMap;
    private final JLabel editor;

    /**
     * Creates an Interface object to render the map based on the given Map object's current values. The
     * map can have any dimensions. A JFrame application window will appear upon calling this constructor.
     */
    public Interface(Map map) {
        // Set the window dimensions, and the resulting tile dimensions
        int WINDOW_WIDTH = 1500;
        int WINDOW_HEIGHT = 200;
        int tileWidth = (WINDOW_WIDTH * 3 / 4) / map.getWidth();
        int tileHeight = (WINDOW_HEIGHT - 40) / map.getHeight();

        // Initialize the frame and set default values
        f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        f.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                windowResize();
            }
        });

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

        // Position editor label
         editor = new JLabel("Editor");
         editor.setFont(new Font("TimesNewRoman", Font.PLAIN, 30));
         editor.setBounds(WINDOW_WIDTH * 3/4, 0, WINDOW_WIDTH / 4, WINDOW_HEIGHT / 15);
         editor.setHorizontalAlignment(SwingConstants.CENTER);
         editor.setVerticalAlignment(SwingConstants.BOTTOM);
         editor.setBackground(Color.lightGray);
         f.add(editor);

        // Initialize the legend


        // Render the JFrame window
        f.setLayout(null);
        f.setVisible(true);
    }

    public void windowResize() {
        // Set the window dimensions, and the resulting tile dimensions
        int WINDOW_WIDTH = f.getWidth();
        int WINDOW_HEIGHT = f.getHeight();
        int tileWidth = (WINDOW_WIDTH * 3 / 4) / colorMap.length;
        int tileHeight = (WINDOW_HEIGHT - 40) / colorMap[0].length;

        // Update the colorMap dimensions
        for (int i = 0; i < colorMap.length; i++) {
            for (int j = 0; j < colorMap[0].length; j++) {
                colorMap[i][j].setBounds(1 + i * tileWidth, 1 + j * tileHeight, tileWidth - 1, tileHeight - 1);
            }
        }

        // Update the editor dimensions
        editor.setBounds(WINDOW_WIDTH * 3/4, 0, WINDOW_WIDTH / 4, WINDOW_HEIGHT / 15);

        // Update the legend dimensions
    }

    /**
     * Uses the provided Map dimensions and values to update the color of each JLabel in the colorMap.
     */
    public void update(Map map) {
        int[][] mapValues = map.getMap();
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                for(String key : Map.legend.keySet()){
                    if (mapValues[i][j] == Map.legend.get(key))
                        colorMap[i][j].setBackground(Interface.colors.get(key));
                }
            }
        }
    }
}
