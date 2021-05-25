import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * @author Da Silva Marques Fabio, Forestier Quentin
 * @date 25.05.2021
 */
public class Main {

    public static void main(String[] args) {
        try {
            FileReader reader = new FileReader("src/main/resources/countries.geojson");
            GeoJson geoJson = new GeoJson(reader);
            geoJson.generateKML("src/main/resources/countries.kml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
