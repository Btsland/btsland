package info.btsland.app.api;


import android.os.Handler;
import android.os.HandlerThread;
import android.text.format.DateUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    public static final long DEFAULT_BUCKET_SECS = TimeUnit.DAYS.toSeconds(1);//每条信息的间隔

    public static final long DEFAULT_UPDATE_SECS = TimeUnit.MINUTES.toMillis(1);//信息刷新的间隔
    public static final long DEFAULT_AGO_SECS=TimeUnit.DAYS.toMillis(90);//距离现在时间
    public static final int STAT_MARKET_HISTORY = 0x01;
    public static final int STAT_MARKET_TICKER = 0x02;
    public static final int STAT_MARKET_ORDER_BOOK = 0x04;
    public static final int STAT_MARKET_OPEN_ORDER = 0x08;
    public static final int STAT_COUNECT=0x12;
    public static final int STAT_MARKET_ALL = 0xffff;
    public static final int STAT_TICKERS_BASE = 0x10;
    public static final int STAT_ACCENTS = 1;
    public Websocket_api mWebsocketApi=new Websocket_api();

    public HashMap<String, Subscription> subscriptionHashMap = new HashMap<>();
    public  HashMap<String, Connect> connectHashMap = new HashMap<>();
    private static boolean isDeserializerRegistered = false;
    
    private String[] quotes;
    
    public MarketStat() {
//        if (!isDeserializerRegistered) {
//            isDeserializerRegistered = true;
//            global_config_object.getInstance().getGsonBuilder().registerTypeAdapter(
//                    full_account_object.class, new full_account_object.deserializer());
//        }


    }

    /**
     *
     * @param base
     * @param quotes
     * @param stats
     * @param l
     */
    public void subscribe(String[] base,String[] quotes,int stats,long intervalMillis,
                          OnMarketStatUpdateListener l) {
        this.quotes=quotes;
        //Log.e(TAG, "subscribe: base:"+base+"stats:"+stats );
        for(int i=0;i<base.length;i++){
            unsubscribe(base[i], "",stats);
            Subscription subscription =new Subscription(base[i], stats, intervalMillis,l);
            subscriptionHashMap.put(makeMarketName(base[i], "",stats), subscription);
        }


    }
    public void connect(int stats,
                          OnMarketStatUpdateListener l) {
        Connect connect = new Connect(stats, l);
        connectHashMap.put("connect", connect);

    }
    public void subscribe(String base, String quote, int stats,long intervalMillis,
                          OnMarketStatUpdateListener l) {
        Log.i(TAG, "subscribe() called with: base = [" + base + "], quote = [" + quote + "], stats = [" + stats + "], intervalMillis = [" + intervalMillis + "], l = [" + l + "]");
        subscribe(base, quote, DEFAULT_BUCKET_SECS,DEFAULT_AGO_SECS, stats,intervalMillis, l);
    }

    public void subscribe(String base, String quote, long bucketSize,long ago, int stats,long intervalMillis,
                          OnMarketStatUpdateListener l) {
        Log.i(TAG, "subscribe: ago:"+ago);
        unsubscribe(base, quote,stats);
        Subscription subscription =
                new Subscription(base, quote, bucketSize, ago,stats ,intervalMillis, l);
        subscriptionHashMap.put(makeMarketName(base, quote,stats), subscription);
    }
    /*public void subscribe(List<String> name,String pwd, int stats,
                           OnMarketStatUpdateListener l) {
        Log.e(TAG, "subscribe: name:"+name.get(0) );
        Subscription subscription =
                new Subscription(name,pwd,stats,l);
        subscriptionHashMap.put("get_accounts", subscription);
    }*/
    public void subscribe(String base, String quote, int stats,
                          OnMarketStatUpdateListener l) {

        subscribe(base, quote, DEFAULT_BUCKET_SECS, stats, l);
    }

    private void subscribe(String base, String quote, long bucketSize, int stats,
                          OnMarketStatUpdateListener l) {
        unsubscribe(base, quote,stats);
        Subscription subscription =
                new Subscription(base, quote, bucketSize, stats, l);
        subscriptionHashMap.put(makeMarketName(base, quote,stats), subscription);
    }

    public void unsubscribe(String base, String quote,int stats) {
        String market = makeMarketName(base, quote,stats);
        Subscription subscription = subscriptionHashMap.get(market);
        if (subscription != null) {
            subscriptionHashMap.remove(market);
            subscription.cancel();
        }
    }

    public void updateImmediately(String base, String quote,int stats) {
        String market = makeMarketName(base, quote,stats);
        Subscription subscription = subscriptionHashMap.get(market);
        if (subscription != null) {
            subscription.updateImmediately();
        }
    }
    public void updateConnect(String name) {
        Connect connect = connectHashMap.get(name);
        if (connect != null) {
            connect.updateConnect();
        }
    }
    public static String makeMarketName(String base, String quote,int stats) {
        if((base==null||base=="")&&(quote==null||quote=="")){
            return "";
        }
        if((base!=null||base!="")&&(quote==null||quote=="")){
            return String.format("%s_%s_%s", base.toLowerCase(),"",stats);
        }
        if((base==null||base=="")&&(quote!=null||quote!="")){
            return String.format("%s_%s_%s", "",quote.toLowerCase(),stats);
        }else {
            return String.format("%s_%s_%s", base.toLowerCase(), quote.toLowerCase(),stats);
        }
    }


    public static class HistoryPrice {
        public String base;
        public String quote;

        public double high;
        public double low;
        public double open;
        public double close;
        public double volume;
        public Date date;

        @Override
        public String toString() {
            return "HistoryPrice{" +
                    "high=" + high +
                    ", low=" + low +
                    ", open=" + open +
                    ", close=" + close +
                    ", volume=" + volume +
                    ", date=" + date +
                    '}';
        }
    }

    public static class Stat {
        public long bucket;
        public long ago;
        public List<HistoryPrice> prices;
        public MarketTicker ticker;
        public Date latestTradeDate;
        public OrderBook orderBook;
        public List<OpenOrder> openOrders;
        public List<MarketTicker> MarketTickers;
        public MarketTicker MarketTicker;
        public account_object account_object;
        public int nRet;

        @Override
        public String toString() {
            return "Stat{" +
                    "prices=" + prices +
                    ", ticker=" + ticker +
                    ", latestTradeDate=" + latestTradeDate +
                    ", orderBook=" + orderBook +
                    ", openOrders=" + openOrders +
                    ", MarketTickers=" + MarketTickers +
                    ", MarketTicker=" + MarketTicker +
                    ", nRet=" + nRet +
                    '}';
        }
    }

    public interface OnMarketStatUpdateListener {
        void onMarketStatUpdate(Stat stat);
    }

    public class Connect implements Runnable {
        private int stats;
        private OnMarketStatUpdateListener listener;
        private HandlerThread statThread;
        private Handler statHandler;
        private Handler handler=new Handler();
        private Connect(int stats, OnMarketStatUpdateListener l) {
            this.stats = stats;
            this.listener = l;
            this.statThread = new HandlerThread("connect");
            this.statThread.start();
            this.statHandler = new Handler(this.statThread.getLooper());
            this.statHandler.post(this);

        }
        public void unConnect(String name) {
            Connect connect = connectHashMap.get(name);
            if (connect != null) {
                connectHashMap.remove(connect);
            }
        }

        @Override
        public void run() {
            Log.i(TAG, "run: "+Thread.currentThread().getName());
            //登录
            final Stat stat = new Stat();
            if ((stats & STAT_COUNECT) != 0) {
                stat.nRet= mWebsocketApi.connect();

                listener.onMarketStatUpdate(stat);
            }
        }

        public void updateConnect() {
            statHandler.post(this);
        }
    }

    public class Subscription implements Runnable {
        private  long ago;
        private String password;
        private List<String> accentName;
        private String base;
        private String quote;
        private long bucketSecs = DEFAULT_BUCKET_SECS;
        private int stats;
        private long intervalMillis=DEFAULT_UPDATE_SECS;
        private OnMarketStatUpdateListener listener;
        private asset_object baseAsset;
        private asset_object quoteAsset;
        private HandlerThread statThread;
        private Handler statHandler;
        private Handler handler=new Handler();
        private AtomicBoolean isCancelled = new AtomicBoolean(false);
        private Subscription(String base, String quote, long bucketSecs,long ago, int stats,long intervalMillis, OnMarketStatUpdateListener l) {
            this.base = base;
            this.quote = quote;
            this.bucketSecs = bucketSecs;
            this.ago=ago;
            this.stats = stats;
            this.intervalMillis=intervalMillis;
            this.listener = l;
            this.statThread = new HandlerThread(makeMarketName(base, quote,stats));
            this.statThread.start();
            this.statHandler = new Handler(this.statThread.getLooper());
            this.statHandler.post(this);
            Log.i(TAG, "Subscription: ago:"+ago);
        }
        private Subscription(String base, String quote, long bucketSecs, int stats, OnMarketStatUpdateListener l) {
            this.base = base;
            this.quote = quote;
            this.bucketSecs = bucketSecs;
            this.stats = stats;
            this.listener = l;
            this.statThread = new HandlerThread(makeMarketName(base, quote,stats));
            this.statThread.start();
            this.statHandler = new Handler(this.statThread.getLooper());
            this.statHandler.post(this);
        }
        private Subscription(String base, int stats,long intervalMillis, OnMarketStatUpdateListener l) {
            //Log.e(TAG, "Subscription: ");
            this.base = base;
            this.stats = stats;
            this.intervalMillis=intervalMillis;
            this.listener = l;
            this.statThread = new HandlerThread(makeMarketName(base, "",stats));
            this.statThread.start();
            this.statHandler = new Handler(this.statThread.getLooper());
            this.statHandler.post(this);
        }
        /*private Subscription(List<String> name, String pwd,int stats, OnMarketStatUpdateListener l) {
            //Log.e(TAG, "Subscription: ");
            this.accentName=name;
            this.password=pwd;
            this.stats = stats;
            this.listener = l;
            this.statThread = new HandlerThread("get_accounts");
            this.statThread.start();
            this.statHandler = new Handler(this.statThread.getLooper());
            this.statHandler.post(this);
        }*/

        private void cancel() {
            isCancelled.set(true);
        }

        public void updateImmediately() {
            statHandler.post(this);
        }

        @Override
        public  void run() {
            //mWebsocketApi.connect();
            Log.e(TAG, "run: "+Thread.currentThread().getName());
            Log.e(TAG, "run: stats==STAT_ACCENTS:"+String.valueOf(stats==STAT_ACCENTS) );
            //Log.e(TAG, "run: " );
            if(base==null||base==""){
                base="CNY";
            }
            if(quote==null||quote==""){
                quote="BTS";
            }
           // if (getAssets()) {
            if (true) {
                final Stat stat = new Stat();
                if ((stats & STAT_TICKERS_BASE) != 0){
                    try {
                        for(int i=0;i<quotes.length;i++){
                            stat.MarketTicker = mWebsocketApi.get_ticker_base(base,quotes[i]);
                            new dataHandling(listener,stat).start();
                        }
                        //this.updateImmediately();
                        return;
                    } catch (NetworkStatusException e) {
                        e.printStackTrace();
                    }
                }
                if ((stats & STAT_MARKET_HISTORY) != 0) {
                    Log.i(TAG, "run: ago:"+ago);
                    Log.i(TAG, "run: ago:"+TimeUnit.DAYS.toMillis(90));
                    Date startDate = new Date(
                            System.currentTimeMillis() -ago);
                    stat.prices = getMarketHistory(base,quote,(int)bucketSecs,startDate);//1
                    stat.bucket=bucketSecs;
                    stat.ago=ago;
                }
                Log.e(TAG, "run: stats==STAT_ACCENTS:"+String.valueOf(stats==STAT_ACCENTS) );

                /*//登录
                if (stats== STAT_ACCENTS) {
                    Log.e(TAG, "run: STAT_ACCENTS  accentName:"+accentName);
                    try {
                        stat.account_object= mWebsocketApi.get_account_by_name(accentName);//1
                    } catch (NetworkStatusException e) {
                        e.printStackTrace();
                    }
                }*/
                if ((stats & STAT_MARKET_TICKER) != 0) {
                    try {
                        stat.ticker = mWebsocketApi.get_ticker(base, quote);//2
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
                            stat.latestTradeDate = trades.get(0).date;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if ((stats & STAT_MARKET_ORDER_BOOK) != 0) {
                    stat.orderBook = getOrderBook();
                }
                if (isCancelled.get()) {
                    return;
                }
                //开启处理数据线程
                new dataHandling(listener,stat).start();
                //this.updateImmediately();
            } else if (!isCancelled.get()) {

            }
        }
        class dataHandling extends Thread{
            private final OnMarketStatUpdateListener listener;
            private final Stat stat;

            public dataHandling(OnMarketStatUpdateListener listener, Stat stat) {
                this.listener=listener;
                this.stat=stat;
            }

            @Override
            public void run() {
                listener.onMarketStatUpdate(stat);
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

        private List<HistoryPrice> getMarketHistory(String base,String quote,int bucketSecs,Date start) {
            Date endDate = new Date();
            List<bucket_object> buckets= getMarketHistory(base,quote,bucketSecs,start, endDate);

            List<HistoryPrice> prices=new ArrayList<>();

            if (buckets != null) {
                for (int i = 0; i < buckets.size(); i++) {
                    prices.add(priceFromBucket(base,quote,buckets.get(i)));
                }
            }
            return prices;
        }

        private List<bucket_object> getMarketHistory(String base,String quote,int bucketSecs, Date start, Date end) {
            try {
                baseAsset = mWebsocketApi.lookup_asset_symbols(base);
                Log.e(TAG, "getMarketHistory: base_object.id:"+baseAsset.id );
                quoteAsset = mWebsocketApi.lookup_asset_symbols(quote);
                Log.e(TAG, "getMarketHistory: quote_object.id:"+quoteAsset.id );
                return mWebsocketApi.get_market_history(
                        baseAsset.id, quoteAsset.id,bucketSecs, start, end);
            } catch (NetworkStatusException e) {
                e.printStackTrace();
                return null;
            } catch (Exception e) {
                return null;
            }
        }


        private HistoryPrice priceFromBucket(String base, String quote, bucket_object bucket) {
            HistoryPrice price = new HistoryPrice();
            price.base=base;
            price.quote=quote;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                price.date = df.parse(bucket.key.open);
            } catch (ParseException e) {
                e.printStackTrace();
            }
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
