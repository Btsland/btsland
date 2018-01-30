package info.btsland.app.ui.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import info.btsland.app.Adapter.CoinsAdapter;
import info.btsland.app.Adapter.MarketRowAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.MarketStat;
import info.btsland.app.model.Market;
import info.btsland.app.model.MarketTicker;
import info.btsland.app.ui.activity.MarketDetailedActivity;
import info.btsland.app.ui.activity.SettingDealActivity;
import info.btsland.app.util.ArrayUtils;
import info.btsland.app.util.InternetUtil;

public class MarketFragment extends Fragment implements MarketStat.OnMarketStatUpdateListener {
    private String TAG="MarketFragment";
    private MarketSimpleKFragment simpleKFragment;
//    private TextView tvMarketLeftCoin_5;
    private ListView lvMarketInfo;
    public static String base ="CNY";

    private ListView lvMarketCoin;

    private MarketStat marketStat;

    private MarketRowAdapter rowAdapter;
    private CoinsAdapter coinsAdapter;
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
        Message message=Message.obtain();
        for (String base:BtslandApplication.listMap.keySet()){
            if(base.equals(stat.MarketTicker.base)){
                if (stat.MarketTicker.quote.equals(base)) {
                    return;
                }
                if(BtslandApplication.marketMap.get(base)!=null){
                    if (!replaceMarket(BtslandApplication.marketMap.get(base), stat.MarketTicker)) {
                        //Log.i(TAG, "handleMessage: ");
                        return;
                    }
                }

                if(!isAdded()){
                    return;
                }
                message.obj=base;
            }
        }
    }

    public boolean replaceMarket(Map<String,MarketTicker> oldMarkets, MarketTicker newMarket){
        if(oldMarkets.get(newMarket.quote)!=null){
            synchronized (oldMarkets.get(newMarket.quote)){
                oldMarkets.get(newMarket.quote).replase(newMarket);
                return true;
            }
        }else {
            oldMarkets.put(newMarket.quote,newMarket);
        }
        return true;

    }
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            rowAdapter.notifyDataSetChanged();
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
        if(InternetUtil.isConnected(BtslandApplication.getInstance())){
            String[] bases = BtslandApplication.listMap.keySet().toArray(new String[]{});
            for(String base : bases){
                String[] quotes= BtslandApplication.listMap.get(base).toArray(new String[]{});
                marketStat.subscribe(base,quotes,MarketStat.STAT_TICKERS_BASE, TimeUnit.SECONDS.toMillis(5),this);
            }

        }
        //Log.e(TAG, "onCreateView: ");
        fillInSimpleK(null);
        init(view);
        fillInMarket(view);
       // touchColor(tvMarketLeftCoin_1);//交互特效
        return view;
    }
    //startGetTickerThread
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void init(View view) {
        lvMarketInfo = view.findViewById(R.id.lv_market_info);
        lvMarketCoin = view.findViewById(R.id.lv_market_coins);
    }
    private void fillInMarket(View view){
        coinsAdapter=new CoinsAdapter(view.getContext());
        lvMarketCoin.setAdapter(coinsAdapter);
        coinsAdapter.setCoins(new ArrayList<String>(BtslandApplication.listMap.keySet()));
        coinsAdapter.setSelectorItem(base);//设置默认
        lvMarketCoin.setOnItemClickListener(new CoinItemClickListener());

        rowAdapter=new MarketRowAdapter(simpleKFragment,view.getContext());
        lvMarketInfo.setAdapter(rowAdapter);
        List<MarketTicker> tickers =new ArrayList<MarketTicker>(BtslandApplication.marketMap.get(BtslandApplication.baseList.get(0)).values());
        rowAdapter.setMarkets(tickers);//设置默认
        rowAdapter.setMarketItemClickListener(new IMarketItemClickListener());
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    mHandler.sendEmptyMessage(1);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 基础货币点击监听器
     */
    class CoinItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            coinsAdapter.setSelectorItem(i);//设置选定的特效
            base=new ArrayList<String>(BtslandApplication.listMap.keySet()).get(i);
            Log.e(TAG, "onItemClick:coin: "+base );
            List<MarketTicker> tickers = new ArrayList<MarketTicker>(BtslandApplication.marketMap.get(base).values());
            Log.e(TAG, "onItemClick: marketMap: "+BtslandApplication.marketMap.get(base).values().size() );
            Log.e(TAG, "onItemClick: tickers:"+tickers.size() );
            rowAdapter.setMarkets(tickers);
            rowAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 交易对点击监听器
     */
    class IMarketItemClickListener implements MarketRowAdapter.MarketItemClickListener {
        /**
         * 双击
         * @param market
         */
        @Override
        public void dblClick(int i,MarketTicker market) {
            MarketDetailedActivity.startAction(getActivity(),market,1);
        }

        /**
         * 单击
         * @param market
         */
        @Override
        public void onClick(int i,MarketTicker market) {
            if(market!=null){
                simpleKFragment.drawK(market);
            }
        }
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

//    /**
//     * 左侧导航栏交互特效
//     *
//     * @param facingTextView 当前的控件
//     */
//    protected void touchColor(TextView facingTextView) {
//
//        switch (facingTextView.getId()) {
//            case R.id.tv_market_left_coin1:
//                setDownBack(tvMarketLeftCoin_1);
//                setUpBack(tvMarketLeftCoin_2);
//                setUpBack(tvMarketLeftCoin_3);
//                setUpBack(tvMarketLeftCoin_4);
//                //setUpBack(tvMarketLeftCoin_5);
//                break;
//            case R.id.tv_market_left_coin2:
//                setDownBack(tvMarketLeftCoin_2);
//                setUpBack(tvMarketLeftCoin_1);
//                setUpBack(tvMarketLeftCoin_3);
//                setUpBack(tvMarketLeftCoin_4);
//                //setUpBack(tvMarketLeftCoin_5);
//                break;
//            case R.id.tv_market_left_coin3:
//                setDownBack(tvMarketLeftCoin_3);
//                setUpBack(tvMarketLeftCoin_2);
//                setUpBack(tvMarketLeftCoin_1);
//                setUpBack(tvMarketLeftCoin_4);
//                //setUpBack(tvMarketLeftCoin_5);
//                break;
//            case R.id.tv_market_left_coin4:
//                setDownBack(tvMarketLeftCoin_4);
//                setUpBack(tvMarketLeftCoin_2);
//                setUpBack(tvMarketLeftCoin_3);
//                setUpBack(tvMarketLeftCoin_1);
//                //setUpBack(tvMarketLeftCoin_5);
//                break;
////            case R.id.tv_market_left_coin5:
////                setDownBack(tvMarketLeftCoin_5);
////                setUpBack(tvMarketLeftCoin_2);
////                setUpBack(tvMarketLeftCoin_3);
////                setUpBack(tvMarketLeftCoin_4);
////                setUpBack(tvMarketLeftCoin_1);
////                break;
//        }
//    }

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
