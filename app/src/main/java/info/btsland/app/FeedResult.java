package info.btsland.app;

import java.util.List;

import info.btsland.app.api.price;

public class FeedResult
{
    public String id;

    public List<List<Object>> feeds;

    public current_feed current_feed;

    public String current_feed_publication_time;

    public Options options;

    public int force_settled_volume;

    public boolean is_prediction_market;

    public price settlement_price;

    public int settlement_fund;

    @Override
    public String toString() {
        return "FeedResult{" +
                "id='" + id + '\'' +
                ", feeds=" + feeds +
                ", current_feed=" + current_feed +
                ", current_feed_publication_time='" + current_feed_publication_time + '\'' +
                ", options=" + options +
                ", force_settled_volume=" + force_settled_volume +
                ", is_prediction_market=" + is_prediction_market +
                ", settlement_price=" + settlement_price +
                ", settlement_fund=" + settlement_fund +
                '}';
    }

    class current_feed
    {
        public price settlement_price;

        public int maintenance_collateral_ratio;

        public int maximum_short_squeeze_ratio;

        public price core_exchange_rate;


    }

    class Options
    {
        public int feed_lifetime_sec;

        public int minimum_feeds;

        public int force_settlement_delay_sec;

        public int force_settlement_offset_percent;

        public int maximum_force_settlement_volume;

        public String short_backing_asset;

        public List<String> extensions;


    }
}






