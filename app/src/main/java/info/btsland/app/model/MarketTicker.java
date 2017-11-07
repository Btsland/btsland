package info.btsland.app.model;


import java.io.Serializable;

public class MarketTicker implements Serializable {
    public String base;
    public String quote;
    public String latest;
    public String lowest_ask;
    public String highest_bid;
    public double percent_change;
    public double base_volume;
    public double quote_volume;

    public MarketTicker(String base, String quote) {
        this.base=base;
        this.quote=quote;
    }

    @Override
    public String toString() {
        return "MarketTicker{" +
                "base='" + base + '\'' +
                ", quote='" + quote + '\'' +
                ", latest=" + latest +
                ", lowest_ask=" + lowest_ask +
                ", highest_bid=" + highest_bid +
                ", percent_change='" + percent_change + '\'' +
                ", base_volume=" + base_volume +
                ", quote_volume=" + quote_volume +
                '}';
    }
}
