import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.awt.Color;
import java.awt.Font;

import java.util.ArrayList;
import java.util.HashMap;

public class Display {
    /**
     * Static hashmap containing java.awt.Color codes corresponding to each tile type. Tile type names are the keys, with
     * the values holding the colors.
     */
    protected static final HashMap<String, Color> colors = new HashMap<>(){{
        put("start", Color.green);
        put("target", Color.yellow);
        put("wall", Color.gray);
        put("empty", Color.white);
        put("highlight", Color.cyan);

        put("searched", Color.cyan);
        put("frontier", Color.blue);
        put("path", Color.magenta);
    }};

    // Underlying data source
    private final Map map;

    // Display elements
    private final JFrame f;
    private final JButton[][] colorMap;
    private final JComboBox<String> menu;

    // Editor elements
    private final ArrayList<JComponent> editorMenu;
    private final JLabel editor;
    private final ArrayList<JButton> editorElements;

    // Algorithm elements
    private final ArrayList<JComponent> algorithmMenu;
    private final JLabel algorithms;
    protected final JComboBox<String> algorithmSpeed;
    private final ArrayList<JButton> algorithmElements;


    /**
     * Creates a Display object to render a map based on the given Map object's current values. The
     * map can have any dimensions. A JFrame application window will appear upon calling this constructor.
     */
    public Display(Map map) {
        this.map = map;

        // Frame
        f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                windowResize();
            }
        });

        // ColorMap
        colorMap = new JButton[map.getHeight()][map.getWidth()];
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                initializeColorMapButton(i, j);
            }
        }

        // Menu selection dropdown
        menu = new JComboBox<>(Interface.menuOptions);
        setupMenuSelection();

        // Editor Menu
        editorMenu = new ArrayList<>();
        editorElements = new ArrayList<>();
        editor = new JLabel("Editor");
        setupEditorMenu();

        // Algorithm Menu
        algorithmMenu = new ArrayList<>();
        algorithmElements = new ArrayList<>();
        algorithms = new JLabel("Algorithms");
        algorithmSpeed = new JComboBox<>(Interface.algorithmSpeedOptions);
        setupAlgorithmMenu();

        // Render the JFrame window
        f.setSize(1200, 800);
        f.setLayout(null);
        f.setVisible(true);
    }

    /**
     * Initializes a JButton in the given location in colorMap
     */
    private void initializeColorMapButton(int i, int j) {
        JButton b = new JButton();
        b.setOpaque(true);
        // Set color based on the map
        for(String key : Map.legend.keySet())
            if (map.getMap()[i][j] == Map.legend.get(key))
                b.setBackground(colors.get(key));

        Coordinate tile = new Coordinate(j, i);
        // When the button gets pressed, call Interface.colorMapClick to change the map, and then update the necessary colorMap tiles.
        b.addActionListener(e -> Interface.colorMapClick(map, tile, this));
        colorMap[i][j] = b;
        f.add(b);
    }

    /**
     * Sets up the menu dropdown display
     */
    private void setupMenuSelection(){
        menu.setFont(new Font("TimesNewRoman", Font.PLAIN, 14));
        menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String) menu.getSelectedItem();
                if (!Interface.menuType.equals(selected)) {
                    changeMenu(selected);
                }
            }
        });
        f.add(menu);
    }

    /**
     * Sets up the components for the editor menu, leaves it visible as the default menu.
     */
    private void setupEditorMenu(){
        // Position editor label and add it to the JFrame
        editor.setFont(new Font("TimesNewRoman", Font.PLAIN, 30));
        editor.setHorizontalAlignment(SwingConstants.CENTER);
        editor.setBackground(Color.lightGray);

        editorMenu.add(editor);
        f.add(editor);

        // Initialize the editorElements and add to the JFrame
        String[] elements = new String[]{"highlight", "wall", "start", "target"};
        for (String tileType : elements) {
            JButton b = new JButton(tileType);
            b.addActionListener(e -> Interface.cursorType = tileType);
            b.setFont(new Font("TimesNewRoman", Font.PLAIN, 18));
            b.setBackground(colors.get(tileType));
            f.add(b);
            editorMenu.add(b);
            editorElements.add(b);
        }
    }

    /**
     * Sets up the components for the algorithm menu, hides it from view.
     */
    private void setupAlgorithmMenu(){
        // Position algorithm label and add it to the JFrame
        algorithms.setFont(new Font("TimesNewRoman", Font.PLAIN, 30));
        algorithms.setHorizontalAlignment(SwingConstants.CENTER);

        algorithms.setBackground(Color.lightGray);
        algorithmMenu.add(algorithms);
        f.add(algorithms);

        // Initialize the algorithmElements and add them to the JFrame
        JButton breadth = new JButton("Breadth-First");
        breadth.setFont(new Font("TimesNewRoman", Font.PLAIN, 14));
        breadth.setBackground(colors.get("target"));
        breadth.addActionListener(e -> Interface.breadthFirstSearch(map, this));
        f.add(breadth);
        algorithmElements.add(breadth);
        algorithmMenu.add(breadth);

        // Speed Selection
        algorithmSpeed.setSelectedIndex(1);
        algorithmSpeed.setFont(new Font("TimesNewRoman", Font.PLAIN, 14));
        algorithmMenu.add(algorithmSpeed);
        f.add(algorithmSpeed);

        // Hide from view
        showAlgorithmMenu(false);
    }

    /**
     * changeMenu hides the current menu and sets the selected option to an active state. Automatically called when
     * the JComboBox is used.
     * @param menu Name of selected menu type. All options can be found in the static Interface.menuOptions.
     */
    public void changeMenu(String menu){
        // First loop hides menu, second loop shows the new menu
        for(Boolean bool : new boolean[]{false, true}) {
            if (Interface.menuType.equals("editor")) {
                updateColorMap();
                showEditorMenu(bool);
            }
            else if (Interface.menuType.equals("algorithms"))
                showAlgorithmMenu(bool);
            // After first loop, change the menuType so the next loop activates the new menu
            Interface.menuType = menu;
        }
    }

    /**
     * Displays or hides the editor menu. Does not impact resizing while hidden.
     * @param bool True for visible, false for hidden
     */
    public void showEditorMenu(boolean bool) {
        for(JComponent j : editorMenu)
            j.setVisible(bool);
    }

    /**
     * Displays or hides the algorithm menu. Does not impact resizing while hidden.
     * @param bool True for visible, false for hidden
     */
    public void showAlgorithmMenu(boolean bool) {
        for(JComponent j : algorithmMenu)
            j.setVisible(bool);
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

        // Menu dropdown
        menu.setBounds(WINDOW_WIDTH * 4 / 5, 5, WINDOW_WIDTH * 3 / 20, WINDOW_HEIGHT / 20);

        // Editor Menu
        editor.setBounds(WINDOW_WIDTH * 3/4, WINDOW_HEIGHT / 18, WINDOW_WIDTH / 4, WINDOW_HEIGHT / 15);
        // Update the editorElements dimensions
        for (int i = 0; i < editorElements.size(); i++) {
            JButton b = editorElements.get(i);
            b.setBounds(WINDOW_WIDTH * 13 / 16, WINDOW_HEIGHT * 2 / 15 + i * WINDOW_HEIGHT / 5, WINDOW_WIDTH / 8, WINDOW_HEIGHT / 6);
        }

        // Algorithm Menu
        algorithms.setBounds(WINDOW_WIDTH * 3/4, WINDOW_HEIGHT / 18, WINDOW_WIDTH / 4, WINDOW_HEIGHT / 15);
        // Update the algorithmElements dimensions
        algorithmElements.get(0).setBounds(WINDOW_WIDTH * 13/16, WINDOW_HEIGHT / 3, WINDOW_WIDTH / 8, WINDOW_HEIGHT / 6);
        algorithmSpeed.setBounds(WINDOW_WIDTH * 13/16, WINDOW_HEIGHT * 9 / 10, WINDOW_WIDTH / 8, WINDOW_HEIGHT / 20);

    }

    /**
     * Updates the entire colormap to match what is in the Map object. This removes any pathfinding colors, but
     * will preserve highlighted squares.
     */
    public void updateColorMap() {
        int[][] mapValues = map.getMap();
        for(int row=0; row<mapValues.length; row++)
            for(int col=0; col<mapValues[0].length; col++) {
                updateColorMap(new Coordinate(col, row));
            }
    }

    /**
     * Updates the color of the specific Coordinate passed
     * @param toUpdate tile to update color of
     */
    public void updateColorMap(Coordinate toUpdate) {
        int[][] mapValues = map.getMap();
        for(String key : Map.legend.keySet()) {
            if (mapValues[toUpdate.y()][toUpdate.x()] == Map.legend.get(key)) {
                colorMap[toUpdate.y()][toUpdate.x()].setBackground(colors.get(key));
                return;
            }
        }
    }

    /**
     * Changes the color of the tile to either searched or frontier, based on searchStatus. Only applies
     * if the tile is an empty square. Coordinate must be in bounds.
     * @param tile Coordinate to display color
     * @param searchStatus string code, either "frontier" or "searched"
     */
    public void showSearch(Coordinate tile, String searchStatus) {
        if (map.getMap()[tile.y()][tile.x()] == Map.legend.get("empty"))
            colorMap[tile.y()][tile.x()].setBackground(colors.get(searchStatus));
    }

    /**
     * Removes all highlighted tiles (makes them empty tiles instead)
     */
    public void clearHighlighted() {
        int[][] mapValues = map.getMap();
        for(int row = 0; row<mapValues.length; row++)
            for(int col=0; col<mapValues[0].length; col++)
                if(mapValues[row][col] == Map.legend.get("highlight")) {
                    Coordinate highlightedTile = new Coordinate(col, row);
                    map.setEmpty(highlightedTile);
                    updateColorMap(highlightedTile);
                }
    }
}
