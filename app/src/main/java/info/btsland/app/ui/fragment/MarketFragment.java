package info.btsland.app.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.btsland.app.Adapter.MarketRowAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.MarketStat;
import info.btsland.app.model.MarketTicker;
import info.btsland.app.service.MarketService;
import info.btsland.app.util.ArrayUtils;
import info.btsland.app.util.InternetUtil;

public class MarketFragment extends Fragment implements MarketStat.OnMarketStatUpdateListener {
    private String TAG="MarketFragment";
    private MarketService marketService;
    private MarketSimpleKFragment simpleKFragment;
    private TextView tvMarketLeftCoin_1;
    private TextView tvMarketLeftCoin_2;
    private TextView tvMarketLeftCoin_3;
    private TextView tvMarketLeftCoin_4;
//    private TextView tvMarketLeftCoin_5;
    private ListView lvMarketInfo;

//    private Map<String,MarketTicker> ethMarket=new HashMap<>();
    private MarketRowAdapter cnyAdapter ;
    private MarketRowAdapter btsAdapter ;
    private MarketRowAdapter usdAdapter ;
    private MarketRowAdapter btcAdapter ;
//    private MarketRowAdapter ethRowAdapter ;
    private MarketStat marketStat;
    public static int NOTIFY_CNY=1;
    public static int NOTIFY_BTS=2;
    public static int NOTIFY_USD=3;
    public static int NOTIFY_BTC=4;
    public static int NOTIFY_ETH=5;
    private String[] bases;
    private String[] quotes;
    public MarketFragment() {


    }
    @Override
    public void onMarketStatUpdate(MarketStat.Stat stat) {

        if(stat.MarketTicker==null){
            return;
        }
        if(stat.MarketTicker.base==null||stat.MarketTicker.quote==null){
            return;
        }
        //Log.e(TAG, "onMarketStatUpdate: marketStat.MarketTicker："+stat.MarketTicker);
        Message message=Message.obtain();
        switch (stat.MarketTicker.base){
            case "CNY":
                if (stat.MarketTicker.quote.equals("CNY")) {
                    return;
                }
                if (BtslandApplication.marketMap.get("CNY") != null && BtslandApplication.marketMap.get("CNY").size() > quotes.length) {
                    BtslandApplication.marketMap.get("CNY").clear();
                }
                if (!replaceMarket(BtslandApplication.marketMap.get("CNY"), stat.MarketTicker)) {
                    //Log.i(TAG, "handleMessage: ");
                    return;
                }
                if(!isAdded()){
                    return;
                }
                message.what = NOTIFY_CNY;
                break;
            case "BTS":
                if (stat.MarketTicker.quote.equals("BTS")) {
                    return;
                }
                if (BtslandApplication.marketMap.get("BTS") != null && BtslandApplication.marketMap.get("BTS").size() > quotes.length) {
                    BtslandApplication.marketMap.get("BTS").clear();
                }
                if (!replaceMarket(BtslandApplication.marketMap.get("BTS"), stat.MarketTicker)) {
                    //Log.i(TAG, "handleMessage: ");
                    return;
                }
                if(!isAdded()){
                    return;
                }
                message.what = NOTIFY_BTS;
                break;
            case "USD":
                if (stat.MarketTicker.quote.equals("USD")) {
                    return;
                }
                if (BtslandApplication.marketMap.get("USD") != null && BtslandApplication.marketMap.get("USD").size() > quotes.length) {
                    BtslandApplication.marketMap.get("USD").clear();
                }
                if (!replaceMarket(BtslandApplication.marketMap.get("USD"), stat.MarketTicker)) {
                    //Log.i(TAG, "handleMessage: ");
                    return;
                }
                if(!isAdded()){
                    return;
                }
                message.what = NOTIFY_USD;
                break;
            case "BTC":
                if (stat.MarketTicker.quote.equals("BTC")) {
                    return;
                }
                if (BtslandApplication.marketMap.get("BTC") != null && BtslandApplication.marketMap.get("BTC").size() > quotes.length) {
                    BtslandApplication.marketMap.get("BTC").clear();
                }
                if (!replaceMarket(BtslandApplication.marketMap.get("BTC"), stat.MarketTicker)) {
                    //Log.i(TAG, "handleMessage: ");
                    return;
                }
                if(!isAdded()){
                    return;
                }
                message.what = NOTIFY_BTC;
                break;
        }
        mHandler.sendMessage(message);
    }
    public boolean replaceMarket(List<MarketTicker> oldMarkets,MarketTicker newMarket){
        for(int i=0;i<oldMarkets.size();i++) {
            if(oldMarkets.get(i)!=null){
                if(oldMarkets.get(i).quote.equals(newMarket.quote)){
                    oldMarkets.remove(i);
                    oldMarkets.add(i,newMarket);
                    return true;
                }
            }else {
                oldMarkets.add(newMarket);
                if(oldMarkets.size()>quotes.length){
                    oldMarkets.clear();
                }
                return true;
            }
        }
        return true;

    }
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //Log.i(TAG, "handleMessage: msg.what:" + msg.what);
            if (msg.what == NOTIFY_CNY) {
                cnyAdapter.setMarkets(BtslandApplication.marketMap.get("CNY"));
            } else if (msg.what == NOTIFY_BTS) {
                btsAdapter.setMarkets(BtslandApplication.marketMap.get("BTS"));
            } else if (msg.what == NOTIFY_USD) {
                usdAdapter.setMarkets(BtslandApplication.marketMap.get("USD"));
            } else if (msg.what == NOTIFY_BTC) {
                btcAdapter.setMarkets(BtslandApplication.marketMap.get("BTC"));
            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.e(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market, container, false);
        this.marketStat= BtslandApplication.getMarketStat();
        bases=BtslandApplication.bases;
        quotes=BtslandApplication.quotes2;
        if(InternetUtil.isConnected(BtslandApplication.getInstance())){
            marketStat.subscribe(bases,quotes,MarketStat.STAT_TICKERS_BASE,MarketStat.DEFAULT_UPDATE_MARKE_SECS,this);
        }
        //Log.e(TAG, "onCreateView: ");
        fillInSimpleK(null);
        init(view);
        touchColor(tvMarketLeftCoin_1);//交互特效
        lvMarketInfo.setAdapter(cnyAdapter);
        return view;
    }
    //startGetTickerThread
    @Override
    public void onStart() {
        super.onStart();
//        websocket_api websocketApi=new websocket_api();
//        websocketApi.connect();
//        marketStat.subscribe("BTS",MarketStat.STAT_TICKERS_BASE,this);
//        marketStat.subscribe("BTC",MarketStat.STAT_TICKERS_BASE,this);
//        marketStat.subscribe("USD",MarketStat.STAT_TICKERS_BASE,this);
//        marketStat.subscribe("ETH",MarketStat.STAT_TICKERS_BASE,this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        private MarketSimpleKFragment simpleKFragment;
//        private TextView tvMarketLeftCoin_1;
//        private TextView tvMarketLeftCoin_2;
//        private TextView tvMarketLeftCoin_3;
//        private TextView tvMarketLeftCoin_4;
//        private TextView tvMarketLeftCoin_5;
//        private ListView lvMarketInfo;
//        private Map<String, List<Market>> market;

    }

    private void init(View view) {
        tvMarketLeftCoin_1 = view.findViewById(R.id.tv_market_left_coin1);
        tvMarketLeftCoin_2 = view.findViewById(R.id.tv_market_left_coin2);
        tvMarketLeftCoin_3 = view.findViewById(R.id.tv_market_left_coin3);
        tvMarketLeftCoin_4 = view.findViewById(R.id.tv_market_left_coin4);
        //tvMarketLeftCoin_5 = getActivity().findViewById(R.id.tv_market_left_coin5);
        lvMarketInfo = view.findViewById(R.id.lv_market_info);
        LeftCoinOnClickListener onClickListener = new LeftCoinOnClickListener();
        tvMarketLeftCoin_1.setOnClickListener(onClickListener);
        tvMarketLeftCoin_2.setOnClickListener(onClickListener);
        tvMarketLeftCoin_3.setOnClickListener(onClickListener);
        tvMarketLeftCoin_4.setOnClickListener(onClickListener);

        cnyAdapter = new MarketRowAdapter(simpleKFragment, view.getContext());
        cnyAdapter.setMarkets(BtslandApplication.marketMap.get("CNY"));
        btsAdapter = new MarketRowAdapter(simpleKFragment, view.getContext());
        btsAdapter.setMarkets(BtslandApplication.marketMap.get("BTS"));
        usdAdapter = new MarketRowAdapter(simpleKFragment, view.getContext());
        usdAdapter.setMarkets(BtslandApplication.marketMap.get("USD"));
        btcAdapter = new MarketRowAdapter(simpleKFragment, view.getContext());
        btcAdapter.setMarkets(BtslandApplication.marketMap.get("BTC"));
        //ethRowAdapter = new MarketRowAdapter(simpleKFragment, getActivity(), ArrayUtils.remove(quotes,"ETH"), ethMarket);

    }


    /**
     * 装载简易K图
     */
    private void fillInSimpleK(MarketTicker market) {
        //Log.i(TAG, "fillInSimpleK: ");
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (simpleKFragment == null) {
            simpleKFragment = MarketSimpleKFragment.newInstance(market);
            transaction.add(R.id.fra_market_simple, simpleKFragment);
        }
        transaction.commit();
    }



    class LeftCoinOnClickListener implements View.OnClickListener {
//        int theme = getSharedPreferences("cons", MODE_PRIVATE).getInt("theme",R.style.SwitchTheme1);
        @Override
        public synchronized void onClick(View view) {
            touchColor((TextView) view);//交互特效
            switch (view.getId()) {
                case R.id.tv_market_left_coin1:
                    lvMarketInfo.setAdapter(cnyAdapter);
                    cnyAdapter.setMarkets(BtslandApplication.marketMap.get("CNY"));
                    break;
                case R.id.tv_market_left_coin2:
                    lvMarketInfo.setAdapter(btsAdapter);
                    btsAdapter.setMarkets(BtslandApplication.marketMap.get("BTS"));
                    break;
                case R.id.tv_market_left_coin3:
                    lvMarketInfo.setAdapter(usdAdapter);
                    usdAdapter.setMarkets(BtslandApplication.marketMap.get("USD"));
                    break;
                case R.id.tv_market_left_coin4:
                    lvMarketInfo.setAdapter(btcAdapter);
                    btcAdapter.setMarkets(BtslandApplication.marketMap.get("BTC"));
                    break;
            }
        }

    }

    /**
     * 左侧导航栏交互特效
     *
     * @param facingTextView 当前的控件
     */
    protected void touchColor(TextView facingTextView) {

        switch (facingTextView.getId()) {
            case R.id.tv_market_left_coin1:
                setDownBack(tvMarketLeftCoin_1);
                setUpBack(tvMarketLeftCoin_2);
                setUpBack(tvMarketLeftCoin_3);
                setUpBack(tvMarketLeftCoin_4);
                //setUpBack(tvMarketLeftCoin_5);
                break;
            case R.id.tv_market_left_coin2:
                setDownBack(tvMarketLeftCoin_2);
                setUpBack(tvMarketLeftCoin_1);
                setUpBack(tvMarketLeftCoin_3);
                setUpBack(tvMarketLeftCoin_4);
                //setUpBack(tvMarketLeftCoin_5);
                break;
            case R.id.tv_market_left_coin3:
                setDownBack(tvMarketLeftCoin_3);
                setUpBack(tvMarketLeftCoin_2);
                setUpBack(tvMarketLeftCoin_1);
                setUpBack(tvMarketLeftCoin_4);
                //setUpBack(tvMarketLeftCoin_5);
                break;
            case R.id.tv_market_left_coin4:
                setDownBack(tvMarketLeftCoin_4);
                setUpBack(tvMarketLeftCoin_2);
                setUpBack(tvMarketLeftCoin_3);
                setUpBack(tvMarketLeftCoin_1);
                //setUpBack(tvMarketLeftCoin_5);
                break;
//            case R.id.tv_market_left_coin5:
//                setDownBack(tvMarketLeftCoin_5);
//                setUpBack(tvMarketLeftCoin_2);
//                setUpBack(tvMarketLeftCoin_3);
//                setUpBack(tvMarketLeftCoin_4);
//                setUpBack(tvMarketLeftCoin_1);
//                break;
        }
    }

    /**
     * 设置选中时的背景样式
     *
     * @param TextView
     */
    private void  setDownBack(TextView TextView) {

        TextView.setBackground(getContext().getResources().getDrawable(R.drawable.tv_market_left_coin_touch, null));
        TextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.color_font_red, null));
    }

    /**
     * 设置未选中时的背景样式
     *
     * @param TextView
     */
    private void setUpBack(TextView TextView) {
        TextView.setBackground(getContext().getResources().getDrawable(R.color.color_white, null));
        TextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.color_darkGrey, null));
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
