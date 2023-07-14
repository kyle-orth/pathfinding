import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class Display {
    protected static final HashMap<String, Color> colors = new HashMap<>(){{
        put("start", Color.green);
        put("target", Color.orange);
        put("wall", Color.gray);
        put("empty", Color.white);
        put("highlighted", Color.cyan);
    }};

    // Underlying data source
    private final Map map;

    // Display elements
    private final JFrame f;
    private final JButton[][] colorMap;
    private final JLabel editor;
    private ArrayList<JButton> editorElements;

    /**
     * Creates a Display object to render a map based on the given Map object's current values. The
     * map can have any dimensions. A JFrame application window will appear upon calling this constructor.
     */
    public Display(Map map) {
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
                b.setOpaque(true);
                // Set color based on the map
                for(String key : Map.legend.keySet())
                    if (map.getMap()[i][j] == Map.legend.get(key))
                        b.setBackground(colors.get(key));

                Coordinate tile = new Coordinate(j, i);
                // When the button gets pressed, call Interface.colorMapClick to change the map, and then update the necessary colorMap tiles.
                b.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Coordinate additionalToUpdate = Interface.colorMapClick(map, tile);
                        updateColorMap(tile);
                        if (additionalToUpdate != null)
                            updateColorMap(additionalToUpdate);
                    }});
                // Add the completed JButton to both the colorMap to store, and the JFrame to display
                colorMap[i][j] = b;
                f.add(b);
            }
        }

        // Position editor label and add it to the JFrame
        editor = new JLabel("Editor");
        editor.setFont(new Font("TimesNewRoman", Font.PLAIN, 30));
        editor.setBounds(WINDOW_WIDTH * 3/4, 0, WINDOW_WIDTH / 4, WINDOW_HEIGHT / 15);
        editor.setHorizontalAlignment(SwingConstants.CENTER);
        editor.setVerticalAlignment(SwingConstants.BOTTOM);
        editor.setBackground(Color.lightGray);
        f.add(editor);

        // Initialize the editorElements and add to the JFrame
        editorElements = new ArrayList<>();
        String[] elements = new String[]{"cursor", "wall", "start", "target"};
        for (int i=0; i<elements.length; i++) {
            String tileType = elements[i];
            JButton b = new JButton(tileType);
            b.addActionListener(e -> Interface.cursorType = tileType);
            b.setFont(new Font("TimesNewRoman", Font.PLAIN, 18));
            b.setBackground(colors.get(tileType));
            b.setBounds(WINDOW_WIDTH * 13 / 16, WINDOW_HEIGHT / 15 + i * WINDOW_HEIGHT / 5 + WINDOW_HEIGHT / 12, WINDOW_WIDTH / 8, WINDOW_HEIGHT / 6);
            f.add(b);
            editorElements.add(b);
        }

        // Render the JFrame window
        f.setLayout(null);
        f.setVisible(true);
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

        // Update the editorElements dimensions
        for (int i = 0; i < editorElements.size(); i++) {
            JButton b = editorElements.get(i);
            b.setBounds(WINDOW_WIDTH * 13 / 16, WINDOW_HEIGHT / 15 + i * WINDOW_HEIGHT / 5 + WINDOW_HEIGHT / 12, WINDOW_WIDTH / 8, WINDOW_HEIGHT / 6);
        }
    }

    private void updateColorMap(Coordinate toUpdate) {
        int[][] mapValues = map.getMap();
        for(String key : Map.legend.keySet()) {
            if (mapValues[toUpdate.y()][toUpdate.x()] == Map.legend.get(key))
                colorMap[toUpdate.y()][toUpdate.x()].setBackground(colors.get(key));
        }
    }
}
