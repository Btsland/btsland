package info.btsland.app.api;

import java.util.Date;


public class bucket_object {
    // static const uint8_t space_id = ACCOUNT_HISTORY_SPACE_ID, 5
    // static const uint8_t type_id  = 1; // market_history_plugin type, referenced from account_history_plugin.hpp

    public class bucket_key {
        public object_id<asset_object>      base;
        public object_id<asset_object>      quote;
        public long                         seconds = 0;
        public Date open;
    }

    public price high() {
        // // TODO: 06/09/2017 完善该逻辑用于交易
        //return asset( high_base, key.base ) / asset( high_quote, key.quote );
        return null;
    }
    public price low() {
        //return asset( low_base, key.base ) / asset( low_quote, key.quote );
        return null;
    }

    public bucket_key    key;
    public long          high_base;
    public long          high_quote;
    public long          low_base;
    public long          low_quote;
    public long          open_base;
    public long          open_quote;
    public long          close_base;
    public long          close_quote;
    public long          base_volume;
    public long          quote_volume;

    @Override
    public String toString() {
        return "bucket_object{" +
                "key=" + key +
                ", high_base=" + high_base +
                ", high_quote=" + high_quote +
                ", low_base=" + low_base +
                ", low_quote=" + low_quote +
                ", open_base=" + open_base +
                ", open_quote=" + open_quote +
                ", close_base=" + close_base +
                ", close_quote=" + close_quote +
                ", base_volume=" + base_volume +
                ", quote_volume=" + quote_volume +
                '}';
    }
}
