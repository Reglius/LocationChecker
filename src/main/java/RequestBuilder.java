import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class RequestBuilder implements Keys {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestBuilder.class);
    private String company;
    private String city;
    private String state;

    public RequestBuilder(String company, String city, String state) {
        this.company = company;
        this.city = city;
        this.state = state;
    }

    public RequestBuilder(String company, String city) {
        this.company = company;
        this.city = city;
    }

    public URL build() {
        try {
            if (state == null) {
                return new URL(String.format("https://maps.googleapis.com/maps/api/place/textsearch/xml?query=%s&location=%s&radius=5000&key=%s", shorten(company), coordLookup(), google));
            }
            return new URL(String.format("https://maps.googleapis.com/maps/api/place/textsearch/xml?query=%s+near+%s+%s&key=%s", shorten(company), shorten(city), shorten(state), google));
        } catch (MalformedURLException mue){
            LOGGER.error("Cannot create URL in RequestBuilder");
        }
        return null;
    }

    static String shorten(String s) {
        if (s.contains(",")) {
            s = s.substring(0, s.indexOf(","));
        }

        String[] split = s.split(" ");
        String splitter = "";
        for (int index = 0; index < split.length; index++) {
            splitter += "+" + split[index];
        }

        splitter = splitter.replaceAll("[^A-Za-z\\s+]+", "");

        return splitter.substring(1);
    }

    private String coordLookup() {
        String line = "";

        try {
            String path = path();
            Scanner sc = new Scanner(new FileReader(RequestBuilder.class.getResource("CityCoordinates.csv").getPath()));
            sc.useDelimiter(",");
            ArrayList<City> list = new ArrayList<>();
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                String [] split = line.split(",");
//                System.out.printf("%s: %s,%s \n",split[0], split[1], split[2]);
                list.add(new City(split[0], split[1], split[2]));
            }

            for (City city : list) {
//                System.out.println(city.getCity().toLowerCase() + " : " + this.city.toLowerCase());
                if (city.getCity().toLowerCase().equals(this.city.toLowerCase())) {
                    return String.format("%s,%s", city.getLat(), city.getLng());
                }
            }
            throw new Error(line + " not found, ");
        }catch (FileNotFoundException fnfe) {
            throw new Error("City list file not found");
        }
        catch(ArrayIndexOutOfBoundsException aioobe){
            LOGGER.error("Index out of bounds spliting %s using delimiter \",\"\n", line);
            return "";
        }
    }

    private String path() {
        String currentDirectory;
        File file = new File(".");
        currentDirectory = file.getAbsolutePath();
        return currentDirectory;
    }

}
