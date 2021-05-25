import org.jdom2.Element;

import java.util.List;

/**
 * @author Da Silva Marques Fabio, Forestier Quentin
 * @date 25.05.2021
 */
public class Polygon
{
    private List<Coordinate> coordinates;

    public Polygon(List<Coordinate> coordinates)
    {
        this.coordinates = coordinates;
    }

    @Override
    public String toString()
    {
        return "\t- " + coordinates.size() + " coordinates\n";
    }

    /**
     * @brief Obtient le Polygon sous forme d'Element pour le fichier KML
     * @return Element Jdom2 représentant le polygon
     */
    public Element getXml()
    {
        Element polygon = new Element("Polygon");

        Element outer_boundary_is = new Element("outerBoundaryIs");
        polygon.addContent(outer_boundary_is);

        Element linear_ring = new Element("LinearRing");
        outer_boundary_is.addContent(linear_ring);

        Element coords = new Element("coordinates");
        coords.setText(stringCoordinates());

        linear_ring.addContent(coords);


        return polygon;
    }

    /**
     * @brief Converti la liste de coordonées en string
     * @return String représentant les coordonées dans la liste
     */
    private String stringCoordinates()
    {
        StringBuilder result = new StringBuilder();
        for (Coordinate coordinate :
                coordinates)
        {
            result.append(coordinate).append(" ");
        }

        return result.toString();
    }
}
