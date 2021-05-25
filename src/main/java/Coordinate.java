/**
 * @author Da Silva Marques Fabio, Forestier Quentin
 * @date 25.05.2021
 */
public class Coordinate {
    private double x;
    private double y;

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return x+","+y;
    }
}
