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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MarketTicker that = (MarketTicker) o;

        if (Double.compare(that.percent_change, percent_change) != 0) return false;
        if (Double.compare(that.base_volume, base_volume) != 0) return false;
        if (Double.compare(that.quote_volume, quote_volume) != 0) return false;
        if (!base.equals(that.base)) return false;
        if (!quote.equals(that.quote)) return false;
        if (!latest.equals(that.latest)) return false;
        if (!lowest_ask.equals(that.lowest_ask)) return false;
        return highest_bid.equals(that.highest_bid);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = base.hashCode();
        result = 31 * result + quote.hashCode();
        result = 31 * result + latest.hashCode();
        result = 31 * result + lowest_ask.hashCode();
        result = 31 * result + highest_bid.hashCode();
        temp = Double.doubleToLongBits(percent_change);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(base_volume);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(quote_volume);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
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
