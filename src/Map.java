import java.util.ArrayList;

/**
 * This class holds information about a two-dimensional map, with size width, height.
 * It holds the locations of empty coordinates, wall coordinates, the start coordinate, and the target coordinate.
 * There is exactly one start coordinate and one target coordinate at all times
 * Every other coordinate is either an empty space or a wall.
 */

public class Map {
    private final int width;
    private final int height;

    private int[][] map;
    private Coordinate startCoord;
    private Coordinate targetCoord;

    private static final int empty = 0;
    private static final int wall = 1;
    private static final int start = 2;
    private static final int target = 3;

    Map(int w, int h, Coordinate[] walls, Coordinate startCoord, Coordinate targetCoord) {
        width = w;
        height = h;
        map = new int[h][w];
        this.startCoord = startCoord;
        this.targetCoord = targetCoord;

        //Initiate map values
        for(int i = 0; i < h; i++) {
            for(int j = 0; j < w; j++) {
                map[i][j] = Map.empty;
            }
        }
        for(Coordinate wall : walls) {
            map[wall.x()][wall.y()] = Map.wall;
        }
        map[startCoord.x()][startCoord.y()] = Map.start;
        map[targetCoord.x()][targetCoord.y()] = Map.target;
    }

    /**
     * Replace the current startCoord with an empty tile, before adding the new startCoord to the map.
     * @param startCoord is the new start coord
     * @precondition startCoord must be in bounds
     */
    public void setStartCoord(Coordinate startCoord) {
        map[startCoord.y()][startCoord.x()] = Map.empty;
        this.startCoord = startCoord;
        map[startCoord.y()][startCoord.x()] = Map.start;
    }

    /**
     * Replace the current targetCoord with an empty tile, before adding the new targetCoord to the map.
     * @param targetCoord is the new start coord
     * @precondition targetCoord must be in bounds
     */
    public void setTargetCoord(Coordinate targetCoord) {
        map[targetCoord.y()][targetCoord.x()] = Map.empty;
        this.targetCoord = targetCoord;
        map[targetCoord.y()][targetCoord.x()] = Map.target;
    }

    /**
     * Sets the map at the provided Coordinate to empty, if and only if it is currently a wall
     * @param empty is the Coordinate to be set as empty
     * @precondition empty is in bounds
     */
    public void setEmpty(Coordinate empty){
        if(map[empty.y()][empty.x()] == Map.wall)
            map[empty.y()][empty.x()] = Map.empty;
    }

    /**
     * Sets the map at the provided Coordinate to a wall, if and only if it is currently empty
     * @param wall is the Coordinate to be set as a wall
     * @precondition wall is in bounds
     */
    public void setWall(Coordinate wall){
        if(map[wall.y()][wall.x()] == Map.empty)
            map[wall.y()][wall.x()] = Map.wall;
    }

    public boolean isWall(Coordinate coord){
        return map[coord.y()][coord.x()] == Map.wall;
    }

    public boolean isEmpty(Coordinate coord){
        return map[coord.y()][coord.x()] == Map.empty;
    }

    public boolean isStart(Coordinate coord){
        return map[coord.y()][coord.x()] == Map.start;
    }

    public boolean isTarget(Coordinate coord){
        return map[coord.y()][coord.x()] == Map.target;
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
        String s = "";
        for(int i=0; i<height; i++){
            s += "[";
            for(int j=0; j<width; j++){
                s += map[i][j];
                s += ", ";
            }
            s = s.substring(0, s.length()-2) + "]\n";
        }
        return s;
    }
}
