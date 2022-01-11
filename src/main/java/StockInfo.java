import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.util.ArrayList;

public class StockInfo {
    private String name;
    private ArrayList<String> subs;

    public StockInfo(String name){
        this.name = name;
        subs = new ArrayList<>();

    }

    public void addSub(String name){ subs.add(name); }

    public ArrayList<String> getSubs() { return subs; }

    public String getName() { return name; }
}
