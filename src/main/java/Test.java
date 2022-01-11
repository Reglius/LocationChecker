import Database.Database;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Test implements Keys{

    public static void main(String [] args) throws IOException, SQLException, ClassNotFoundException, SAXException, ParserConfigurationException {

//        URL url = (new RequestBuilder("american eagle","omaha")).build();
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




        //        String [] stockList = {"1", "2", "3", "4"};
////        createNewDatabase();
//        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
//        Runnable task1 = () -> System.out.printf("\n\nbreak\n");
//
//        for (String stock : stockList) {
//            Runnable task = () -> System.out.printf("Number : %s\n\tTime : %s\n", stock, LocalDateTime.now().toString());
//
//            ses.scheduleAtFixedRate(task, 0, 5, TimeUnit.SECONDS);
//        }
//        ses.scheduleAtFixedRate(task1, 0, 5, TimeUnit.SECONDS);
        System.out.print(LocalDate.now().toString());
    }

    public static void createNewDatabase() throws ClassNotFoundException, SQLException, IOException {
        Connection conn = null;
        Stock stock = YahooFinance.get("amtd");
        int locations = 12;
        try {
            Class.forName(dbClassName);

            // Properties for user and password. Here the user and password are both 'paulr'
            Properties p = new Properties();
            p.put("user", user);
            p.put("password",password);

            // Now try to connect
            conn = DriverManager.getConnection(CONNECTION,p);

            System.out.printf("Connected to %s\n", conn.getMetaData().getDriverName());
            //Database.create(stock, conn);
            Database.insert(stock, locations,conn);
            //Database.select(stock, conn);

            System.out.println("Created database");
        } finally {
            try {
                if (conn != null) {
                    System.err.println("Closing database");
                    conn.close();
                }
                else{
                    System.err.println("Connection NULL");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
