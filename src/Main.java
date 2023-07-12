public class Main {
    private static Map map;
    private static Coordinate start = new Coordinate(2, 2);
    private static Coordinate target = new Coordinate(4, 0);
    private static Coordinate[] walls = new Coordinate[]{new Coordinate(0, 1), new Coordinate(0, 2)};
    public static void main(String[] args){
        map = new Map(5, 5, walls, start, target);
        System.out.println(map);
    }
}