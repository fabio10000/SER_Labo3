import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Da Silva Marques Fabio, Forestier Quentin
 * @date 25.05.2021
 */
public class GeoJson
{
    private List<Country> countries;

    private static String styleName = "borderStyle";

    GeoJson(FileReader geojsonFile)
    {
        JSONParser jsonParser = new JSONParser();
        countries = new ArrayList<>();

        try
        {
            JSONObject obj = (JSONObject) jsonParser.parse(geojsonFile);
            JSONArray features = (JSONArray) obj.get("features");

            for (JSONObject feature : (Iterable<JSONObject>) features)
            {
                JSONObject properties = (JSONObject) feature.get("properties");
                JSONObject geometry = (JSONObject) feature.get("geometry");

                List<Polygon> polygones =
                        getPolygones((JSONArray) geometry.get("coordinates"),
                                (String) geometry.get("type"));
                Country country =
                        new Country((String) properties.get("ADMIN"),
                                (String) properties.get("ISO_A3"), polygones);
                countries.add(country);

                System.out.println(country);

            }

        }
        catch (IOException | ParseException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @brief Obtient la liste des polygons appartenant à un Element
     * représentant un pays
     * @param coordinates_json Array contenant la liste des frontières du pays
     * @param type Type de frontière du pays
     * @return Liste de polygones représentant les frontières du pays
     *
     *
     */
    private List<Polygon> getPolygones(JSONArray coordinates_json, String type)
    {
        List<Polygon> polygons = new ArrayList<>();

        if (type.equals("Polygon"))
        {
            polygons.add(getPolygon((JSONArray) coordinates_json.get(0)));
        }
        else
        {
            for (JSONArray jsonArray : (Iterable<JSONArray>) coordinates_json)
            {
                polygons.add(getPolygon((JSONArray) jsonArray.get(0)));
            }
        }

        return polygons;
    }

    /**
     * @brief Obtient la frontière spécifique d'un pays
     * @param polygon_json pays souhaité
     * @return Polygon représentant la frontière du pays
     */
    private Polygon getPolygon(JSONArray polygon_json)
    {
        return new Polygon(getCoordinates(polygon_json));
    }

    /**
     * @brief Obtient la liste coordonnées d'une frontière
      * @param polygon_json Frontière souhaitée
     * @return Liste de coordonées de la frontière
     */
    private List<Coordinate> getCoordinates(JSONArray polygon_json)
    {
        List<Coordinate> coordinates = new ArrayList<>();
        for (JSONArray coordinate_json : (Iterable<JSONArray>) polygon_json)
        {
            coordinates.add(new Coordinate((double) coordinate_json.get(0),
                    (double) coordinate_json.get(1)));
        }
        return coordinates;
    }

    /**
     * @brief Génère le fichier KML en récupérant les informations des pays
     * parsés dans la liste countries
     * @param filename Nom du fichier de sortie
     */
    public void generateKML(String filename)
    {
        Document doc = new Document();

        Element kml = new Element("kml", Namespace.getNamespace("http://www" +
                ".opengis.net/kml/2.2"));
        doc.setRootElement(kml);

        Element document = new Element("Document");
        kml.addContent(document);

        document.addContent(createBorderStyle());

        for (Country c : countries)
        {
            document.addContent(c.getXml("#" + styleName));
        }

        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        try
        {
            xmlOutputter.output(doc, new FileOutputStream(filename));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


    }

    /**
     * @brief Crée l'Element de style affichant les frontières des pays.
     * @return Element représentant le style d'affichage des pays
     */
    private static Element createBorderStyle()
    {

        Element style = new Element("Style");
        style.setAttribute("id", styleName);

        Element lineStyle = new Element("LineStyle");
        Element color = new Element("color");
        color.setText("ffffffff");
        Element width = new Element("width");
        width.setText("2");
        lineStyle.addContent(color);
        lineStyle.addContent(width);
        style.addContent(lineStyle);

        Element polyStyle = new Element("PolyStyle");
        Element fill = new Element("fill");
        fill.setText("0");
        Element outline = new Element("outline");
        outline.setText("1");
        polyStyle.addContent(fill);
        polyStyle.addContent(outline);
        style.addContent(polyStyle);

        return style;
    }
}
