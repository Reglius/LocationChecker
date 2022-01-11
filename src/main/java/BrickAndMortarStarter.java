import Database.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;
import java.util.concurrent.*;

public class BrickAndMortarStarter implements Keys{

    private static final Logger LOGGER = LoggerFactory.getLogger(BrickAndMortarStarter.class);
    private static Scanner scan = new Scanner(System.in);
    private static String stocks = "ANF,AEO,BOOT,BURL,CBKC,FL,EXPR,GPS,JWN,RL,ROST,BKE,URBN,ZUMZ,DDS,KSS,M,MIK,ASNA,GES,HOTT,SCVL,LB,DG,FIVE,YUM";
    public static ArrayList<StockInfo> ownership = new ArrayList<>();

    public static void main(String [] args) {

        String[] stockList = stocks.split(",");
        String path = BrickAndMortarStarter.class.getResource("CompanyOwnership.csv").getPath();

        try(Scanner companyOwnership = new Scanner(new FileReader(path))){
            while(companyOwnership.hasNextLine()){
                String line = companyOwnership.nextLine();
                //System.out.println(line);

                ArrayList<String> info = new ArrayList<>(Arrays.asList(line.split(",")));
                StockInfo current = new StockInfo(info.get(0));
                info.remove(0);

                for(String sub : info) {
                    current.addSub(sub);
                }

                ownership.add(current);
            }
        } catch  (FileNotFoundException fnfe){
            LOGGER.error("CompanyOwnership.csv not found");
            System.exit(-1);
        }

        //dont run, it gets expensive
        System.out.println("ran");
        System.exit(0);
        //dont run, it gets expensive

        //run if the databases have not been setup
        //        firstTimeStartup(stocks);
        ExecutorService ses = Executors.newCachedThreadPool();

        for (StockInfo stock : ownership) {
            Thread thread = new Thread(new StockChecker(stock));
            ses.execute(() -> thread.run());
        }

        while (true) {
            String line = scan.next();

            if (line.equals("exit")){
                ses.shutdownNow();
                return;
            }
        }

    }

    static void firstTimeStartup(String [] stockList) {
        try {
            Class.forName(dbClassName);

            // Properties for user and password. Here the user and password are both 'paulr'
            Properties p = new Properties();
            p.put("user", user);
            p.put("password", password);

            // Now try to connect
            Connection conn = DriverManager.getConnection(CONNECTION, p);

            for (String name : stockList) {
                Database.create(name, conn);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
