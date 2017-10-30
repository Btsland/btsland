package info.btsland.app.model;


import java.util.Date;


public class MarketTrade {
    public Date date;
    public double price;
    public double amount;
    public double value;

    @Override
    public String toString() {
        return "MarketTrade{" +
                "date=" + date +
                ", price=" + price +
                ", amount=" + amount +
                ", value=" + value +
                '}';
    }
}
