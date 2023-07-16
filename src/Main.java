import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // Set initial values
        int mapWidth = 10;
        int mapHeight = 9;
        Coordinate start = new Coordinate(2, 2);
        Coordinate target = new Coordinate(4, 0);
        ArrayList<Coordinate> walls = new ArrayList<>() {{
            add(new Coordinate(1, 1));
            add(new Coordinate(2, 1));
            add(new Coordinate(0, 1));
        }};

        // Create a map and interface
        Map map = new Map(mapWidth, mapHeight, walls, start, target);
        Display display = new Display(map);
    }


    public static void delay(int milliseconds) {
        try {Thread.sleep(milliseconds);}
        catch (InterruptedException ignored) {}
    }
}
