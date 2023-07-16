import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class holds and manipulates information about a two-dimensional array of tiles.
 * All information is mutable, but the original width and height cannot be changed. Accessors make it straightforward
 * to transfer information into a new Map object of different dimensions.
 */

public class Map {
    protected static final HashMap<String, Integer> legend = new HashMap<>(){{
        put("start", 0);
        put("target", 1);
        put("empty", 2);
        put("highlight", 3);
        put("wall", 4);
    }};

    private final int width;
    private final int height;

    private final int[][] map;
    private Coordinate startCoord;
    private Coordinate targetCoord;

    /**
     * Constructs a Map object from the provided information
     * @param w width of the map
     * @param h height of the map
     * @param walls ArrayList of Coordinates representing wall locations
     * @param startCoord Coordinate of the start location
     * @param targetCoord Coordinate of the target location
     */
    Map(int w, int h, ArrayList<Coordinate> walls, Coordinate startCoord, Coordinate targetCoord) {
        width = w;
        height = h;
        map = new int[h][w];
        if(isInBounds(startCoord))
            this.startCoord = startCoord;
        else
            this.startCoord = new Coordinate(0, 0);
        if(isInBounds(targetCoord))
            this.targetCoord = targetCoord;
        else
            this.targetCoord = new Coordinate(4, 4);

        //Initiate map values
        for(int i = 0; i < h; i++) {
            for(int j = 0; j < w; j++) {
                map[i][j] = Map.legend.get("empty");
            }
        }
        map[startCoord.y()][startCoord.x()] = Map.legend.get("start");
        map[targetCoord.y()][targetCoord.x()] = Map.legend.get("target");

        for(Coordinate wall : walls) {
            if(isInBounds(wall))
                setWall(wall);
        }
    }

    /**
     * Replace the current startCoord with an empty tile, before adding the new startCoord to the map.
     * The Coordinate must be within the bounds of the Map.
     */
    public void setStartCoord(Coordinate startCoord) {
        map[this.startCoord.y()][this.startCoord.x()] = Map.legend.get("empty");
        this.startCoord = startCoord;
        map[startCoord.y()][startCoord.x()] = Map.legend.get("start");
    }

    /**
     * Replace the current targetCoord with an empty tile, before adding the new targetCoord to the map.
     * The Coordinate must be within the bounds of the Map.
     */
    public void setTargetCoord(Coordinate targetCoord) {
        map[this.targetCoord.y()][this.targetCoord.x()] = Map.legend.get("empty");
        this.targetCoord = targetCoord;
        map[targetCoord.y()][targetCoord.x()] = Map.legend.get("target");
    }

    /**
     * Sets the map at the provided Coordinate to empty, provided it is not the start or target.
     * The Coordinate must be within the bounds of the Map.
     */
    public void setEmpty(Coordinate empty){
        if(!new ArrayList<Integer>(){{add(legend.get("start")); add(legend.get("target"));}}.contains(map[empty.y()][empty.x()]))
            map[empty.y()][empty.x()] = Map.legend.get("empty");
    }

    /**
     * Sets the map at the provided Coordinate to highlight, if and only if it is currently empty.
     * The Coordinate must be within the bounds of the Map.
     */
    public void setHighlight(Coordinate highlight){
        if(map[highlight.y()][highlight.x()] == Map.legend.get("empty"))
            map[highlight.y()][highlight.x()] = Map.legend.get("highlight");
    }

    /**
     * Sets the map at the provided Coordinate to a wall, provided it is not the start or target.
     * The Coordinate must be within the bounds of the Map.
     */
    public void setWall(Coordinate wall){
        if(!new ArrayList<Integer>(){{add(legend.get("start")); add(legend.get("target"));}}.contains(map[wall.y()][wall.x()]))
            map[wall.y()][wall.x()] = Map.legend.get("wall");
    }

    /**
     * Returns the boolean representing whether the given Coordinate is a wall tile.
     * The Coordinate must be within the bounds of the Map.
     */
    public boolean isWall(Coordinate tile){
        return map[tile.y()][tile.x()] == Map.legend.get("wall");
    }

    /**
     * Returns the boolean representing whether the given Coordinate is within the Map's bounds.
     */
    public boolean isInBounds(Coordinate tile) {
        return (tile.x() >= 0 && tile.y() >= 0 && tile.x() < width && tile.y() < height);
    }

    public int[][] getMap(){
        return map;
    }

    /**
     * Ease of Access method, designed to transfer existing data to a new map of different dimensions.
     * @return An ArrayList of all walls in the current state of map.
     */
    public ArrayList<Coordinate> getWalls() {
        ArrayList<Coordinate> walls = new ArrayList<>();
        for(int row=0; row<height; row++){
            for(int col=0; col<width; col++){
                if (map[row][col] == legend.get("wall"))
                    walls.add(new Coordinate(col, row));
            }
        }
        return walls;
    }

    public Coordinate getStartCoord() {
        return startCoord;
    }

    public Coordinate getTargetCoord() {
        return targetCoord;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String toString(){
        StringBuilder s = new StringBuilder();
        for(int i=0; i<height; i++){
            s.append("[");
            for(int j=0; j<width; j++){
                s.append(map[i][j]);
                s.append(", ");
            }
            s = new StringBuilder(s.substring(0, s.length() - 2) + "]\n");
        }
        return s.toString();
    }
}
