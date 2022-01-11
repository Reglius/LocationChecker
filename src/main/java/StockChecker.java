import Database.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class StockChecker implements Runnable, Keys
{
    private static final Logger LOGGER = LoggerFactory.getLogger(StockChecker.class);

    private Stock parent;
    private StockInfo stock;
    private int locations;

    public StockChecker(StockInfo stockName) {
        try {
            parent = YahooFinance.get(stockName.getName());
            stock = stockName;

            LOGGER.info("Name: " + stock.getName());
            LOGGER.info("    " + parent.getSymbol());
            LOGGER.info("    $" + parent.getQuote().getPrice().toString());
        } catch (IOException ioe){
            LOGGER.error("Stock not found in StockChecker");
        }
    }

    public void run() {

        locations = 0;
        for (String company : stock.getSubs()) {
            locations += allLocations(company);
        }
        LOGGER.info("Number of locations: " + locations);

        try {
            // Properties for user and password. Here the user and password are both 'paulr'
            Properties p = new Properties();
            p.put("user", user);
            p.put("password", password);

            // Now try to connect
            Connection conn = DriverManager.getConnection(CONNECTION, p);
            //todo: percent growth? growth compared to S&P?
            Database.insert(parent, locations, conn);

        }
        catch (SQLException sql) {
            sql.printStackTrace();
        }

//
//        URL url = (new RequestBuilder(stock.getName(),"omaha")).build();
//        System.out.println(url.toString());
//
//        HttpURLConnection con = (HttpURLConnection) url.openConnection();
//        con.setRequestMethod("GET");
//        int responseCode = con.getResponseCode();
//        if (responseCode == HttpURLConnection.HTTP_OK) { // success
//
//            DocumentBuilderFactory factory =
//                    DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = factory.newDocumentBuilder();
//
//            Document doc = builder.parse(con.getInputStream());
//
//            NodeList addresses = doc.getElementsByTagName("formatted_address");
//            System.out.println("Number of locations: " + addresses.getLength());
//            for (int index = 0; index < addresses.getLength(); index++){
//                System.out.println("\t" + addresses.item(index).getTextContent());
//            }
//        } else {
//            System.out.println("GET request not worked");
//        }
    }

    private static int allLocations(String stock) {
        int ret = 0;
        for (City city : CityCoordinates.getList()) {
            URL url = (new RequestBuilder(stock, city.getCity())).build();
//        System.out.println(url.toString());

            try {
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) { // success

//                System.out.print(city.getCity() + ",");
                    ret += parse(con);
//                System.out.println(ret);

                } else {
                    LOGGER.error("GET request didn't work for \'"+ url.toString() + "\'");
                    LOGGER.error(con.getResponseMessage());
                }
            } catch (IOException ioe){
                LOGGER.error("IOException in Stock Checker when connecting to: ", url.toString());
            }
        }
        return ret;
    }

    public static void printResponse(HttpURLConnection con) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine + "\n");
        }
        in.close();

        LOGGER.info(response.toString());
    }

    public static int parse(HttpURLConnection con) throws IOException {
        try {
            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(con.getInputStream());

//            String xml = new XMLDocument(doc).toString();
//            System.out.println(xml);

            NodeList addresses = doc.getElementsByTagName("formatted_address");
//            System.out.println("Number of locations: " + addresses.getLength());
//            for (int index = 0; index < addresses.getLength(); index++){
//                System.out.println("\t" + addresses.item(index).getTextContent());
//            }

//            System.out.println(addresses.getLength());
             return addresses.getLength();

        } catch (SAXException saxe) {
            LOGGER.error("Error parsing return from google in StockChecker");
        } catch (ParserConfigurationException pce) {
            LOGGER.error("Error creating parser in StockChecker");
        }
        return 0;
    }
}
