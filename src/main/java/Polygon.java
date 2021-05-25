import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

public class Polygon {
    private List<Coordinate> coordinates;

    public Polygon(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return "\t- " + coordinates.size() + " coordinates\n";
    }

    public Element getXml(Document document) {
        Element polygon = document.createElement("Polygon");

        Element outer_boundary_is = document.createElement("outerBoundaryIs");
        polygon.appendChild(outer_boundary_is);

        Element linear_ring = document.createElement("LinearRing");
        outer_boundary_is.appendChild(linear_ring);

        Element coordinates = document.createElement("coordinates");
        coordinates.appendChild(document.createTextNode(stringCoordinates()));
        linear_ring.appendChild(coordinates);

        return polygon;
    }

    private String stringCoordinates() {
        StringBuilder result = new StringBuilder();
        for (Coordinate coordinate :
                coordinates) {
            result.append(coordinate).append(" ");
        }

        return result.toString();
    }
}
