import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeoJson {
    private List<Country> countries;
    GeoJson(FileReader geojsonFile) {
        JSONParser jsonParser = new JSONParser();
        countries = new ArrayList<>();

        try {
            JSONObject obj = (JSONObject) jsonParser.parse(geojsonFile);
            JSONArray features = (JSONArray) obj.get("features");

            for (JSONObject feature : (Iterable<JSONObject>) features) {
                JSONObject properties = (JSONObject) feature.get("properties");
                JSONObject geometry = (JSONObject) feature.get("geometry");
                
                List<Polygon> polygones = getPolygones((JSONArray) geometry.get("coordinates"), (String) geometry.get("type"));
                Country country = new Country((String) properties.get("ADMIN"), (String) properties.get("ISO_A3"), polygones);
                countries.add(country);

                System.out.println(country);
                
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private List<Polygon> getPolygones(JSONArray coordinates_json, String type) {
        List<Polygon> polygons = new ArrayList<>();

        if (type.equals("Polygon")) {
            polygons.add(getPolygon((JSONArray) coordinates_json.get(0)));
        } else {
            for (JSONArray jsonArray : (Iterable<JSONArray>) coordinates_json) {
                polygons.add(getPolygon((JSONArray) jsonArray.get(0)));
            }
        }
        
        return polygons;
    }

    private Polygon getPolygon(JSONArray polygon_json) {
        return new Polygon(getCoordinates(polygon_json));
    }


    private List<Coordinate> getCoordinates(JSONArray polygon_json) {
        List<Coordinate> coordinates = new ArrayList<>();
        for (JSONArray coordinate_json : (Iterable<JSONArray>) polygon_json) {
            coordinates.add(new Coordinate((double) coordinate_json.get(0), (double) coordinate_json.get(1)));
        }
        return coordinates;
    }

    public void generateKML(String filename) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element kml = document.createElement("kml");
            document.appendChild(kml);

            Element doc = document.createElement("Document");
            kml.appendChild(doc);

            Element schema = document.createElement("Schema");
            schema.setAttribute("id", "countries");
            doc.appendChild(schema);

            Element simple_field = document.createElement("SimpleField");
            simple_field.setAttribute("name", "ADMIN");
            simple_field.setAttribute("type", "string");
            schema.appendChild(simple_field);

            simple_field = document.createElement("SimpleField");
            simple_field.setAttribute("name", "ISO_A3");
            simple_field.setAttribute("type", "string");
            schema.appendChild(simple_field);

            for (Country country : countries) {
                doc.appendChild(country.getXml(document));
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(filename));

            document.setXmlStandalone(true);
            document.setXmlVersion("1.0");

            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(source, result);

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }


    }
}
