import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class CityCoordinates {

    private static final Logger LOGGER = LoggerFactory.getLogger(CityCoordinates.class);

    public static void refresh() throws IOException, ParserConfigurationException, SAXException {

        PrintWriter printWriter = new PrintWriter(CityCoordinates.class.getResource("CityCoordinates.csv").getPath());

        Scanner sc = new Scanner(new FileReader(CityCoordinates.class.getResource("Cities.csv").getPath()));

        sc.useDelimiter(",");

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String [] split = line.split(",");

            String city = split[0];
            city = city.replaceAll(" ", "+");
            String state = split[1];
            state = state.replaceAll(" ", "+");

            URL url = new URL(String.format("https://maps.googleapis.com/maps/api/geocode/xml?address=%s+%s&key=%s",city, state, Keys.google));

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) { // success

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();

                Document doc = builder.parse(con.getInputStream());

                //todo file write the document to a date organized file system

                String name = doc.getElementsByTagName("long_name").item(0).getTextContent();
                String lat = doc.getElementsByTagName("lat").item(0).getTextContent();
                String lng = doc.getElementsByTagName("lng").item(0).getTextContent();
//                System.out.printf("%20s,%20s,%20s\n", name, lat, lng);

                printWriter.append(String.format("%s,%s,%s\n", name, lat, lng));

//                System.out.println(new XMLDocument(doc).toString());
            } else {
                LOGGER.error("GET request did not work for: " + con.getURL().toString());
                return;
            }
        }
        printWriter.close();
    }

    public static List<City> getList() {
        List<City> ret = new LinkedList<>();
        try {
            //System.out.println(CityCoordinates.class.getResource("CityCoordinates.csv").getPath());
            Scanner sc = new Scanner(new FileReader(CityCoordinates.class.getResource("CityCoordinates.csv").getPath()));

            sc.useDelimiter(",");

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] split = line.split(",");

                ret.add(new City(split[0], split[1], split[2]));

            }
        }catch (FileNotFoundException fnfe) {
            LOGGER.error("City list file not found CityCoordinates");
        }
        return ret;
    }
}
