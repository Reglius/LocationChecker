package Database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yahoofinance.Stock;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Database {
    private static final Logger LOGGER = LoggerFactory.getLogger(Database.class);

//    public static void main(String [] args) {
//
//        Scanner scan = new Scanner(System.in);
//        System.out.println("Type 'help' for commands");
//        System.out.print(">>"); String input = scan.next();
//
//        if(input.contains("create")){create();}
//
//        if(input.contains("help")){help();}
//
//        if(input.contains("insert")) {
//            System.out.println("Name:		");
//            String name = scan.next();
//
//            System.out.println("Date (YYYY-MM-DD):	");
//            String date = scan.next();
//
//            System.out.println("Location:		");
//            int loc  = scan.nextInt();
//
//            System.out.println("Growth:		");
//            int growth = scan.nextInt();
//
//            insert(name, date, loc, growth);}
//
//        if(input.contains("select")){select();}
//
//        if(!input.contains("exit")){main(null);}
//
//    }

    static void help() {
        LOGGER.info("Commands are:\n" +
                "create:    creates the table\n" +
                "help:      lists commands\n" +
                "insert:    inserts a value\n" +
                "select:    pulls values out of table\n");
    }
    public static void create(String stock, Connection connection) throws SQLException {
        LOGGER.info("Creating database for %s\n", stock);
        // Statements allow to issue SQL queries to the database
        Statement statement = connection.createStatement();
        // Result set get the result of the SQL query
        statement.executeUpdate(String.format("CREATE TABLE %s " +
                        "(ID    INT NOT NULL    AUTO_INCREMENT," +
                        "NAME	TEXT	NOT NULL," +
                        "DATE	TEXT	NOT NULL, " +
                        "LOC	INT	NOT NULL, " +
                        "GROWTH    INT	NOT NULL, " +
			            "PRIMARY KEY (ID)", stock));

        LOGGER.info("Table created successfully");
    }
    public static void insert(Stock stock, int loc, Connection connection) throws SQLException {
        //System.out.println("Opened database successfully");

        Statement statement = connection.createStatement();

        statement.execute("INSERT INTO LOCATIONS (NAME,DATE,LOC) " +
                String.format("VALUES ('%s', '%s', %d );", stock.getSymbol(), LocalDate.now().toString(), loc));
        //System.out.println("Records created successfully");
    }
    public static void select(Stock stock, Connection connection) throws SQLException {
            //System.out.println("Opened database successfully");

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(String.format("SELECT * FROM %s;",stock.getSymbol()));

            while ( rs.next() ) {

                String id = rs.getString("ID");
                String name = rs.getString("NAME");
                String  date = rs.getString("DATE");
                int loc = rs.getInt("LOC");
                int  growth = rs.getInt("GROWTH");

                LOGGER.info( "ID = " + id );
                LOGGER.info( "Name = " + name );
                LOGGER.info( "Date = " + date );
                LOGGER.info( "Locations = " + loc );
                LOGGER.info( "Growth = " + growth + "\n");
            }
            rs.close();
        LOGGER.info("Operation done successfully");
    }

    public static int getLastLocations(Stock stock, Connection connection) {
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(String.format("SELECT * FROM %s ORDER BY ID DESC LIMIT 1", stock.getSymbol()));
             ){

            while ( rs.next() ) {
                return rs.getInt("LOC");
            }
        }
        catch (SQLException sqle){
            sqle.printStackTrace();
        }
        return 0;
    }
}
