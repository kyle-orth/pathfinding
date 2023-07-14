import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import javax.swing.SwingConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class Interface {
    protected static final HashMap<String, Color> colors = new HashMap<>(){{
        put("start", Color.green);
        put("target", Color.orange);
        put("wall", Color.gray);
        put("empty", Color.white);
        put("cursor", Color.cyan);
    }};

    private final Map map;

    private final JFrame f;
    private final JButton[][] colorMap;
    private final JLabel editor;

    private ArrayList<JButton> legend = new ArrayList<>();

    private String cursorType = "cursor";

    /**
     * Creates an Interface object to render the map based on the given Map object's current values. The
     * map can have any dimensions. A JFrame application window will appear upon calling this constructor.
     */
    public Interface(Map map) {
        this.map = map;
        // Set the window dimensions, and the resulting tile dimensions
        int WINDOW_WIDTH = 1200;
        int WINDOW_HEIGHT = 700;
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
        colorMap = new JButton[map.getHeight()][map.getWidth()];
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                JButton b = new JButton();
                b.setBounds(1 + j * tileWidth, 1 + i * tileHeight, tileWidth-1, tileHeight-1);
                b.setBackground(Color.white);
                b.setOpaque(true);
                int x = j;
                int y = i;
                b.addActionListener(e -> colorMapClick(b, x, y));
                colorMap[i][j] = b;
                f.add(b);
            }
        }
        // Set the JLabel colors based on actual map values
        updateColorMap();

        // Position editor label
         editor = new JLabel("Editor");
         editor.setFont(new Font("TimesNewRoman", Font.PLAIN, 30));
         editor.setBounds(WINDOW_WIDTH * 3/4, 0, WINDOW_WIDTH / 4, WINDOW_HEIGHT / 15);
         editor.setHorizontalAlignment(SwingConstants.CENTER);
         editor.setVerticalAlignment(SwingConstants.BOTTOM);
         editor.setBackground(Color.lightGray);
         f.add(editor);

        // Initialize the legend
        String[] legendElements = new String[]{"cursor", "wall", "start", "target"};
        for (int i=0; i<legendElements.length; i++) {
            String tileType = legendElements[i];
            JButton b = new JButton(tileType);
            b.addActionListener(e -> cursorType = tileType);
            b.setFont(new Font("TimesNewRoman", Font.PLAIN, 18));
            b.setBackground(colors.get(tileType));
            b.setBounds(WINDOW_WIDTH * 13 / 16, WINDOW_HEIGHT / 15 + i * WINDOW_HEIGHT / 5 + WINDOW_HEIGHT / 12, WINDOW_WIDTH / 8, WINDOW_HEIGHT / 6);
            f.add(b);
            legend.add(b);
        }

        // Render the JFrame window
        f.setLayout(null);
        f.setVisible(true);
    }

    /**
     * Called when a JButton in the colorMap is clicked. Based on the current cursorType, this updates
     * the underlying map, as well as the JButton's corresponding color.
     */
    private void colorMapClick(JButton b, int x, int y){
        // Cursor Mode: toggle empty tiles back and forth from cursor color. Ignore other tiles.
        if (Objects.equals(cursorType, "cursor")) {
            if (b.getBackground() == colors.get("empty"))
                b.setBackground(colors.get("cursor"));
            else if (!new ArrayList<Color>(){{add(colors.get("wall")); add(colors.get("start")); add(colors.get("target"));}}.contains(b.getBackground())){
                b.setBackground(colors.get("empty"));
            }
        }
        // Wall Mode: Toggle walls and empty tiles with one another. Ignore start and target tiles.
        else if (Objects.equals(cursorType, "wall") && !new ArrayList<Color>(){{add(colors.get("start")); add(colors.get("target"));}}.contains(b.getBackground())) {
            if (b.getBackground() == colors.get("wall")) {
                map.setEmpty(new Coordinate(x, y));
                b.setBackground(colors.get("empty"));
            }
            else {
                map.setWall(new Coordinate(x, y));
                b.setBackground(colors.get("wall"));
            }
        }
        // Start Mode: Move the start tile to the selected empty tile. Ignore other tiles.
        else if (Objects.equals(cursorType, "start") && !new ArrayList<Color>(){{add(colors.get("wall")); add(colors.get("target"));}}.contains(b.getBackground())) {
            colorMap[map.getStartCoord().y()][map.getStartCoord().x()].setBackground(colors.get("empty"));
            map.setStartCoord(new Coordinate(x, y));
            b.setBackground(colors.get("start"));
        }

        // Target Mode: Move the target tile to the selected empty tile. Ignore other tiles.
        else if (Objects.equals(cursorType, "target") && !new ArrayList<Color>(){{add(colors.get("start")); add(colors.get("wall"));}}.contains(b.getBackground())) {
            colorMap[map.getTargetCoord().y()][map.getTargetCoord().x()].setBackground(colors.get("empty"));
            map.setTargetCoord(new Coordinate(x, y));
            b.setBackground(colors.get("target"));
        }
    }

    /**
     * Called automatically by the ComponentListener. Resizes the components if the JFrame is resized
     * by a user.
     */
    public void windowResize() {
        // Set the window dimensions, and the resulting tile dimensions
        int WINDOW_WIDTH = f.getWidth();
        int WINDOW_HEIGHT = f.getHeight();
        int tileWidth = (WINDOW_WIDTH * 3 / 4) / map.getWidth();
        int tileHeight = (WINDOW_HEIGHT - 40) / map.getHeight();

        // Update the colorMap dimensions
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                colorMap[i][j].setBounds(1 + j * tileWidth, 1 + i * tileHeight, tileWidth - 1, tileHeight - 1);
            }
        }

        // Update the editor dimensions
        editor.setBounds(WINDOW_WIDTH * 3/4, 0, WINDOW_WIDTH / 4, WINDOW_HEIGHT / 15);

        // Update the legend dimensions
        for (int i = 0; i < legend.size(); i++) {
            JButton b = legend.get(i);
            b.setBounds(WINDOW_WIDTH * 13 / 16, WINDOW_HEIGHT / 15 + i * WINDOW_HEIGHT / 5 + WINDOW_HEIGHT / 12, WINDOW_WIDTH / 8, WINDOW_HEIGHT / 6);
        }
    }

    /**
     * Uses the provided Map dimensions and values to update the color of each JLabel in the colorMap.
     */
    public void updateColorMap() {
        int[][] mapValues = map.getMap();
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                for(String key : Map.legend.keySet()){
                    if (mapValues[i][j] != Map.legend.get("empty") && mapValues[i][j] == Map.legend.get(key))
                        colorMap[i][j].setBackground(Interface.colors.get(key));
                }
            }
        }
    }
}
