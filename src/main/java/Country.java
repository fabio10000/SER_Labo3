import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

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

    public Element getXml(Document document) {
        Element place_mark = document.createElement("Placemark");

        Element extended_data = document.createElement("ExtendedData");
        place_mark.appendChild(extended_data);

        Element schema_data = document.createElement("SchemaData");
        schema_data.setAttribute("schemaUrl", "#countries");
        extended_data.appendChild(schema_data);

        Element simple_data = document.createElement("SimpleData");
        simple_data.setAttribute("name", "ADMIN");
        simple_data.appendChild(document.createTextNode(name));
        schema_data.appendChild(simple_data);

        simple_data = document.createElement("SimpleData");
        simple_data.setAttribute("name", "ISO_A3");
        simple_data.appendChild(document.createTextNode(iso_name));
        schema_data.appendChild(simple_data);

        Element polygon_root = place_mark;
        if (border.size() > 1) {
            Element multi_geometry = document.createElement("MultiGeometry");
            place_mark.appendChild(multi_geometry);
            polygon_root = multi_geometry;
        }

        for (Polygon polygon : border) {
            polygon_root.appendChild(polygon.getXml(document));
        }

        return place_mark;
    }
}
