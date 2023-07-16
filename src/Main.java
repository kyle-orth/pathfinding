import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // Set initial values
        int mapWidth = 10;
        int mapHeight = 9;
        Coordinate start = new Coordinate(7,0);
        Coordinate target = new Coordinate(7, 5);

        // Create an example wall array
        ArrayList<Coordinate> walls = new ArrayList<>() {{
            add(new Coordinate(4, 1));
            add(new Coordinate(5, 1));
            add(new Coordinate(6, 1));
            add(new Coordinate(7, 1));
            add(new Coordinate(0, 2));
            add(new Coordinate(8, 2));
            add(new Coordinate(1, 3));
            add(new Coordinate(2, 3));
            add(new Coordinate(8, 3));
            add(new Coordinate(8, 4));
            add(new Coordinate(8, 5));
            add(new Coordinate(3, 6));
            add(new Coordinate(8, 6));
            add(new Coordinate(3, 7));
            add(new Coordinate(4, 7));
            add(new Coordinate(5, 7));
            add(new Coordinate(6, 7));
            add(new Coordinate(7, 7));
        }};

        Map map = new Map(mapWidth, mapHeight, walls, start, target);

        // Create a display, which then follows user input until it is closed
        new Display(map);
    }
}
