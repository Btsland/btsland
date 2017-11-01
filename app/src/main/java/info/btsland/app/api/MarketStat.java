package info.btsland.app.api;


import android.os.Handler;
import android.os.HandlerThread;
import android.text.format.DateUtils;
import android.util.Log;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import info.btsland.app.exception.NetworkStatusException;
import info.btsland.app.model.MarketTicker;
import info.btsland.app.model.MarketTrade;
import info.btsland.app.model.OpenOrder;
import info.btsland.app.model.Order;
import info.btsland.app.model.OrderBook;


public class MarketStat {
    private static final String TAG = "MarketStat";
    private static final long DEFAULT_BUCKET_SECS = TimeUnit.MINUTES.toSeconds(5);
    
    public static final int STAT_MARKET_HISTORY = 0x01;
    public static final int STAT_MARKET_TICKER = 0x02;
    public static final int STAT_MARKET_ORDER_BOOK = 0x04;
    public static final int STAT_MARKET_OPEN_ORDER = 0x08;
    public static final int STAT_MARKET_ALL = 0xffff;
    public static final int STAT_TICKERS_BASE = 0x10;
    private websocket_api mWebsocketApi=new websocket_api();

    private HashMap<String, Subscription> subscriptionHashMap = new HashMap<>();
    private static boolean isDeserializerRegistered = false;
    
    private String[] quotes={"BTC", "ETH","BTS","LTC","OMG","STEEM","VEN","HPB","OCT","YOYOW","DOGE","HASH"};
    
    public MarketStat() {
//        if (!isDeserializerRegistered) {
//            isDeserializerRegistered = true;
//            global_config_object.getInstance().getGsonBuilder().registerTypeAdapter(
//                    full_account_object.class, new full_account_object.deserializer());
//        }
    }
    public int initialize() {
        int nRet = mWebsocketApi.connect();

        return nRet;
    }
    public void subscribe(String base,int stats,
                          OnMarketStatUpdateListener l) {
        //Log.e(TAG, "subscribe: base:"+base+"stats:"+stats );
        unsubscribe(base, "");
        Subscription subscription =new Subscription(base, stats, l);
        subscriptionHashMap.put(makeMarketName(base, ""), subscription);
    }
    public void subscribe(String base, String quote, int stats, long intervalMillis,
                          OnMarketStatUpdateListener l) {
        subscribe(base, quote, DEFAULT_BUCKET_SECS, stats, intervalMillis, l);
    }

    public void subscribe(String base, String quote, long bucketSize, int stats,
                          long intervalMillis, OnMarketStatUpdateListener l) {
        unsubscribe(base, quote);
        Subscription subscription =
                new Subscription(base, quote, bucketSize, stats, intervalMillis, l);
        subscriptionHashMap.put(makeMarketName(base, quote), subscription);
    }

    public void unsubscribe(String base, String quote) {
        String market = makeMarketName(base, quote);
        Subscription subscription = subscriptionHashMap.get(market);
        if (subscription != null) {
            subscriptionHashMap.remove(market);
            subscription.cancel();
        }
    }

    public void updateImmediately(String base, String quote) {
        String market = makeMarketName(base, quote);
        Subscription subscription = subscriptionHashMap.get(market);
        if (subscription != null) {
            subscription.updateImmediately();
        }
    }

    private static String makeMarketName(String base, String quote) {
        if((base==null||base=="")&&(quote==null||quote=="")){
            return "";
        }
        if((base!=null||base!="")&&(quote==null||quote=="")){
            return String.format("%s_%s", base.toLowerCase(),"");
        }
        if((base==null||base=="")&&(quote!=null||quote!="")){
            return String.format("%s_%s", "",quote.toLowerCase());
        }else {
            return String.format("%s_%s", base.toLowerCase(), quote.toLowerCase());
        }
    }


    public static class HistoryPrice {
        public double high;
        public double low;
        public double open;
        public double close;
        public double volume;
        public Date date;
    }

    public static class Stat {
        public HistoryPrice[] prices;
        public MarketTicker ticker;
        public Date latestTradeDate;
        public OrderBook orderBook;
        public List<OpenOrder> openOrders;
        public List<MarketTicker> MarketTickers;

        @Override
        public String toString() {
            return "Stat{" +
                    "prices=" + Arrays.toString(prices) +
                    ", ticker=" + ticker +
                    ", latestTradeDate=" + latestTradeDate +
                    ", orderBook=" + orderBook +
                    ", openOrders=" + openOrders +
                    ", MarketTickers=" + MarketTickers +
                    '}';
        }
    }

    public interface OnMarketStatUpdateListener {
        void onMarketStatUpdate(Stat stat);
    }

    private class Subscription implements Runnable {
        private String base;
        private String quote;
        private long bucketSecs = DEFAULT_BUCKET_SECS;
        private int stats;
        private long intervalMillis;
        private OnMarketStatUpdateListener listener;
        private asset_object baseAsset;
        private asset_object quoteAsset;
        private AtomicBoolean isCancelled = new AtomicBoolean(false);

        private Subscription(String base, String quote, long bucketSecs, int stats,
                             long intervalMillis, OnMarketStatUpdateListener l) {
            this.base = base;
            this.quote = quote;
            this.bucketSecs = bucketSecs;
            this.stats = stats;
            this.intervalMillis = intervalMillis;
            this.listener = l;
        }
        private Subscription(String base, int stats, OnMarketStatUpdateListener l) {
            //Log.e(TAG, "Subscription: ");
            this.base = base;
            this.stats = stats;
            this.listener = l;
        }

        private void cancel() {
            isCancelled.set(true);
        }

        private void updateImmediately() {
        }

        @Override
        public void run() {
            //mWebsocketApi.connect();
            Log.e("websocket", "run: "+Thread.currentThread().getName());
            //Log.e(TAG, "run: " );
            if(base==null||base==""){
                base="CNY";
            }
            if(quote==null||quote==""){
                quote="BTS";
            }
           // if (getAssets()) {
            if (true) {
                final Stat marketStat = new Stat();
                if ((stats & STAT_TICKERS_BASE) != 0){
                    try {
                        marketStat.MarketTickers = mWebsocketApi.get_ticker_base(base,quotes);
                    } catch (NetworkStatusException e) {
                        e.printStackTrace();
                    }
                }
                if ((stats & STAT_MARKET_HISTORY) != 0) {
                    marketStat.prices = getMarketHistory();//1
                }
                if ((stats & STAT_MARKET_TICKER) != 0) {
                    try {
                        marketStat.ticker = mWebsocketApi.get_ticker(base, quote);//2
                        //Log.e(TAG, "run: "+"base"+base+"quote"+ quote);
                        Date start = new Date(System.currentTimeMillis());
                        Date end = new Date(
                                System.currentTimeMillis() - DateUtils.DAY_IN_MILLIS);
                        List<MarketTrade> trades =
                                mWebsocketApi.get_trade_history(base, quote, start, end, 1);//3
                        if (trades == null || trades.isEmpty()) {
                            end = new Date(0);
                            trades = mWebsocketApi.get_trade_history(base, quote, start, end, 1);
                        }
                        if (trades != null && trades.size() > 0) {
                            marketStat.latestTradeDate = trades.get(0).date;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if ((stats & STAT_MARKET_ORDER_BOOK) != 0) {
                    marketStat.orderBook = getOrderBook();
                }
                if (isCancelled.get()) {
                    return;
                }
                listener.onMarketStatUpdate(marketStat);
            } else if (!isCancelled.get()) {

            }
        }

        private boolean getAssets() {
            if (baseAsset != null && quoteAsset != null) {
                return true;
            }
            try {
                baseAsset = mWebsocketApi.lookup_asset_symbols(base);
                quoteAsset = mWebsocketApi.lookup_asset_symbols(quote);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        private HistoryPrice[] getMarketHistory() {
            // 服务器每次最多返回200个bucket对象
            final int maxBucketCount = 200;
            Date startDate1 = new Date(
                    System.currentTimeMillis() - bucketSecs * maxBucketCount * 1000);
            Date startDate2 = new Date(
                    System.currentTimeMillis() - bucketSecs * maxBucketCount * 2000);
            Date endDate = new Date(System.currentTimeMillis() + DateUtils.DAY_IN_MILLIS);
            List<bucket_object> buckets1 = getMarketHistory(startDate2, startDate1);
            List<bucket_object> buckets2 = getMarketHistory(startDate1, endDate);
            int numBuckets = (buckets1 != null ? buckets1.size() : 0) +
                    (buckets2 != null ? buckets2.size() : 0);
            HistoryPrice[] prices = new HistoryPrice[numBuckets];
            int priceIndex = 0;
            if (buckets1 != null) {
                for (int i = 0; i < buckets1.size(); i++) {
                    bucket_object bucket = buckets1.get(i);
                    prices[priceIndex++] = priceFromBucket(bucket);
                }
            }
            if (buckets2 != null) {
                for (int i = 0; i < buckets2.size(); i++) {
                    bucket_object bucket = buckets2.get(i);
                    prices[priceIndex++] = priceFromBucket(bucket);
                }
            }
            return prices;
        }

        private List<bucket_object> getMarketHistory(Date start, Date end) {
            try {
                return mWebsocketApi.get_market_history(
                        baseAsset.id, quoteAsset.id, (int) bucketSecs, start, end);
            } catch (Exception e) {
                return null;
            }
        }

        private HistoryPrice priceFromBucket(bucket_object bucket) {
            HistoryPrice price = new HistoryPrice();
            price.date = bucket.key.open;
            if (bucket.key.quote.equals(quoteAsset.id)) {
                price.high = utils.get_asset_price(bucket.high_base, baseAsset,
                        bucket.high_quote, quoteAsset);
                price.low = utils.get_asset_price(bucket.low_base, baseAsset,
                        bucket.low_quote, quoteAsset);
                price.open = utils.get_asset_price(bucket.open_base, baseAsset,
                        bucket.open_quote, quoteAsset);
                price.close = utils.get_asset_price(bucket.close_base, baseAsset,
                        bucket.close_quote, quoteAsset);
                price.volume = utils.get_asset_amount(bucket.quote_volume, quoteAsset);
            } else {
                price.low = utils.get_asset_price(bucket.high_quote, baseAsset,
                        bucket.high_base, quoteAsset);
                price.high = utils.get_asset_price(bucket.low_quote, baseAsset,
                        bucket.low_base, quoteAsset);
                price.open = utils.get_asset_price(bucket.open_quote, baseAsset,
                        bucket.open_base, quoteAsset);
                price.close = utils.get_asset_price(bucket.close_quote, baseAsset,
                        bucket.close_base, quoteAsset);
                price.volume = utils.get_asset_amount(bucket.base_volume, quoteAsset);
            }
            if (price.low == 0) {
                price.low = findMin(price.open, price.close);
            }
            if (price.high == Double.NaN || price.high == Double.POSITIVE_INFINITY) {
                price.high = findMax(price.open, price.close);
            }
            if (price.close == Double.POSITIVE_INFINITY || price.close == 0) {
                price.close = price.open;
            }
            if (price.open == Double.POSITIVE_INFINITY || price.open == 0) {
                price.open = price.close;
            }
            if (price.high > 1.3 * ((price.open + price.close) / 2)) {
                price.high = findMax(price.open, price.close);
            }
            if (price.low < 0.7 * ((price.open + price.close) / 2)) {
                price.low = findMin(price.open, price.close);
            }
            return price;
        }

        private OrderBook getOrderBook() {
            try {
                List<limit_order_object> orders =
                        mWebsocketApi.get_limit_orders(baseAsset.id, quoteAsset.id, 200);
                if (orders != null) {
                    OrderBook orderBook = new OrderBook();
                    orderBook.base = baseAsset.symbol;
                    orderBook.quote = quoteAsset.symbol;
                    orderBook.bids = new ArrayList<>();
                    orderBook.asks = new ArrayList<>();
                    for (int i = 0; i < orders.size(); i++) {
                        limit_order_object o = orders.get(i);
                        if (o.sell_price.base.asset_id.equals(baseAsset.id)) {
                            Order ord = new Order();
                            ord.price = priceToReal(o.sell_price);
                            ord.quote = ((double)o.for_sale * (double)o.sell_price.quote.amount)
                                    / (double)o.sell_price.base.amount
                                    / Math.pow(10, quoteAsset.precision);
                            ord.base = o.for_sale / Math.pow(10, baseAsset.precision);
                            orderBook.bids.add(ord);
                        } else {
                            Order ord = new Order();
                            ord.price = priceToReal(o.sell_price);
                            ord.quote = o.for_sale / Math.pow(10, quoteAsset.precision);
                            ord.base = (double)o.for_sale * (double)o.sell_price.quote.amount
                                    / o.sell_price.base.amount
                                    / Math.pow(10, baseAsset.precision);
                            orderBook.asks.add(ord);
                        }
                    }
                    Collections.sort(orderBook.bids, new Comparator<Order>() {
                        @Override
                        public int compare(Order o1, Order o2) {
                            return (o1.price - o2.price) < 0 ? 1 : -1;
                        }
                    });
                    Collections.sort(orderBook.asks, new Comparator<Order>() {
                        @Override
                        public int compare(Order o1, Order o2) {
                            return (o1.price - o2.price) < 0 ? -1 : 1;
                        }
                    });
                    return orderBook;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        private double assetToReal(asset a, long p) {
            return (double)a.amount / Math.pow(10, p);
        }

        private double priceToReal(price p) {
            if (p.base.asset_id.equals(baseAsset.id)) {
                return assetToReal(p.base, baseAsset.precision)
                        / assetToReal(p.quote, quoteAsset.precision);
            } else {
                return assetToReal(p.quote, baseAsset.precision )
                        / assetToReal(p.base, quoteAsset.precision);
            }
        }
    }

    private static double findMax(double a, double b) {
        if (a != Double.POSITIVE_INFINITY && b != Double.POSITIVE_INFINITY) {
            return Math.max(a, b);
        } else if (a == Double.POSITIVE_INFINITY) {
            return b;
        } else {
            return a;
        }
    }

    private static double findMin(double a, double b) {
        if (a != 0 && b != 0) {
            return Math.min(a, b);
        } else if (a == 0) {
            return b;
        } else {
            return a;
        }
    }
}
