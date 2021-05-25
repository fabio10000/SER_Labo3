import org.jdom2.Element;

import java.util.List;

/**
 * @author Da Silva Marques Fabio, Forestier Quentin
 * @date 25.05.2021
 */
public class Country {
    private String name;
    private String iso_name;
    private List<Polygon> border;

    Country(String name, String iso_name, List<Polygon> border) {
        this.name = name;
        this.iso_name = iso_name;
        this.border = border;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("(" + iso_name + ") " + name + "\n");
        for (Polygon polygon : border) {
            result.append(polygon);
        }
        return result.toString();
    }

    /**
     * @brief Obtient le pays sous forme d'Element pour le fichier KML
     * @param styleName Nom du style dans lequel doit s'afficher le pays
     * @return Element Jdom2 représentant le pays
     */
    public Element getXml(String styleName) {
        Element place_mark = new Element("Placemark");

        Element style_url = new Element("styleUrl");
        style_url.setText(styleName);
        place_mark.addContent(style_url);

        Element extended_data = new Element("ExtendedData");
        place_mark.addContent(extended_data);

        Element name = new Element("name");
        name.setText(this.name);
        place_mark.addContent(name);

        Element polygon_root = place_mark;
        if (border.size() > 1) {
            Element multi_geometry = new Element("MultiGeometry");
            place_mark.addContent(multi_geometry);
            polygon_root = multi_geometry;
        }

        for (Polygon polygon : border) {
            polygon_root.addContent(polygon.getXml());
        }

        return place_mark;
    }
}
