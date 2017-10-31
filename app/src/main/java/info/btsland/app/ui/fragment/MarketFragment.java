package info.btsland.app.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.btsland.app.Adapter.MarketRowAdapter;
import info.btsland.app.R;
import info.btsland.app.api.MarketStat;
import info.btsland.app.api.websocket_api;
import info.btsland.app.model.Market;
import info.btsland.app.model.MarketTicker;
import info.btsland.app.service.Impl.MarketServiceImpl;
import info.btsland.app.service.MarketService;

public class MarketFragment extends Fragment implements MarketStat.OnMarketStatUpdateListener {
    private String TAG="MarketFragment";
    private MarketService marketService;
    private MarketSimpleKFragment simpleKFragment;
    private TextView tvMarketLeftCoin_1;
    private TextView tvMarketLeftCoin_2;
    private TextView tvMarketLeftCoin_3;
    private TextView tvMarketLeftCoin_4;
    private TextView tvMarketLeftCoin_5;
    private ListView lvMarketInfo;
    private List<MarketTicker> cnyMarket;
    private List<MarketTicker> btsMarket;
    private List<MarketTicker> usdMarket;
    private List<MarketTicker> btcMarket;
    private List<MarketTicker> ethMarket;
    private MarketRowAdapter cnyRowAdapter ;
    private MarketRowAdapter btsRowAdapter ;
    private MarketRowAdapter btcRowAdapter ;
    private MarketRowAdapter usdRowAdapter ;
    private MarketRowAdapter ethRowAdapter ;
    private MarketStat marketStat;
    public MarketFragment() {
        this.marketStat=new MarketStat();
    }
    @Override
    public void onMarketStatUpdate(MarketStat.Stat stat) {
        if(stat.MarketTickers==null){
            return;
        }
        if(stat.MarketTickers.get(0).base=="CNY"){
            if(cnyMarket!=null){
                cnyMarket.clear();
            }
            cnyMarket=stat.MarketTickers;
            cnyRowAdapter.markets=cnyMarket;
            cnyRowAdapter.notifyDataSetChanged();
            return;
        }
        if(stat.MarketTickers.get(0).base=="BTS"){
            if(btsMarket!=null){
                btsMarket.clear();
            }
            btsMarket=stat.MarketTickers;
            btsRowAdapter.notifyDataSetChanged();
        }
        if(stat.MarketTickers.get(0).base=="USD"){
            if(usdMarket!=null){
                usdMarket.clear();
            }
            usdMarket=stat.MarketTickers;
            usdRowAdapter.notifyDataSetChanged();
        }
        if(stat.MarketTickers.get(0).base=="BTC"){
            if(btcMarket!=null){
                btcMarket.clear();
            }
            btcMarket=stat.MarketTickers;
            btcRowAdapter.notifyDataSetChanged();
        }
        if(stat.MarketTickers.get(0).base=="ETH"){
            if(ethMarket!=null){
                ethMarket.clear();
            }
            ethMarket=stat.MarketTickers;
            ethRowAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market, container, false);
        return view;
    }
    //startGetTickerThread
    @Override
    public void onStart() {
        super.onStart();
        fillInSimpleK(null);
        init();
        touchColor(tvMarketLeftCoin_1);//交互特效
        setMarket(tvMarketLeftCoin_1);//设置数据
        websocket_api websocketApi=new websocket_api();
        websocketApi.connect();
//        marketStat.subscribe("CNY",MarketStat.STAT_TICKERS_BASE,this);
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

    private void init() {
        tvMarketLeftCoin_1 = getActivity().findViewById(R.id.tv_market_left_coin1);
        tvMarketLeftCoin_2 = getActivity().findViewById(R.id.tv_market_left_coin2);
        tvMarketLeftCoin_3 = getActivity().findViewById(R.id.tv_market_left_coin3);
        tvMarketLeftCoin_4 = getActivity().findViewById(R.id.tv_market_left_coin4);
        tvMarketLeftCoin_5 = getActivity().findViewById(R.id.tv_market_left_coin5);
        lvMarketInfo = getActivity().findViewById(R.id.lv_market_info);
        LeftCoinOnClickListener onClickListener = new LeftCoinOnClickListener();
        tvMarketLeftCoin_1.setOnClickListener(onClickListener);
        tvMarketLeftCoin_2.setOnClickListener(onClickListener);
        tvMarketLeftCoin_3.setOnClickListener(onClickListener);
        tvMarketLeftCoin_4.setOnClickListener(onClickListener);
        tvMarketLeftCoin_5.setOnClickListener(onClickListener);
        cnyRowAdapter = new MarketRowAdapter(simpleKFragment, getActivity(), cnyMarket);
        btsRowAdapter = new MarketRowAdapter(simpleKFragment, getActivity(), btsMarket);
        btcRowAdapter = new MarketRowAdapter(simpleKFragment, getActivity(), btcMarket);
        usdRowAdapter = new MarketRowAdapter(simpleKFragment, getActivity(), usdMarket);
        ethRowAdapter = new MarketRowAdapter(simpleKFragment, getActivity(), ethMarket);

    }

    /**
     * 装载简易K图
     */
    private void fillInSimpleK(Market market) {
        Log.i(TAG, "fillInSimpleK: ");
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (simpleKFragment == null) {
            simpleKFragment = MarketSimpleKFragment.newInstance(market);
            transaction.add(R.id.fra_market_simple, simpleKFragment);
        }
        transaction.commit();
    }



    class LeftCoinOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            touchColor((TextView) view);//交互特效
            setMarket(view);//设置数据
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
                setUpBack(tvMarketLeftCoin_5);
                break;
            case R.id.tv_market_left_coin2:
                setDownBack(tvMarketLeftCoin_2);
                setUpBack(tvMarketLeftCoin_1);
                setUpBack(tvMarketLeftCoin_3);
                setUpBack(tvMarketLeftCoin_4);
                setUpBack(tvMarketLeftCoin_5);
                break;
            case R.id.tv_market_left_coin3:
                setDownBack(tvMarketLeftCoin_3);
                setUpBack(tvMarketLeftCoin_2);
                setUpBack(tvMarketLeftCoin_1);
                setUpBack(tvMarketLeftCoin_4);
                setUpBack(tvMarketLeftCoin_5);
                break;
            case R.id.tv_market_left_coin4:
                setDownBack(tvMarketLeftCoin_4);
                setUpBack(tvMarketLeftCoin_2);
                setUpBack(tvMarketLeftCoin_3);
                setUpBack(tvMarketLeftCoin_1);
                setUpBack(tvMarketLeftCoin_5);
                break;
            case R.id.tv_market_left_coin5:
                setDownBack(tvMarketLeftCoin_5);
                setUpBack(tvMarketLeftCoin_2);
                setUpBack(tvMarketLeftCoin_3);
                setUpBack(tvMarketLeftCoin_4);
                setUpBack(tvMarketLeftCoin_1);
                break;
        }
    }

    /**
     * 设置选中时的背景样式
     *
     * @param TextView
     */
    private void setDownBack(TextView TextView) {
        TextView.setBackground(getView().getResources().getDrawable(R.drawable.tv_market_left_coin_touch, null));
        TextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.color_yellow, null));
    }

    /**
     * 设置未选中时的背景样式
     *
     * @param TextView
     */
    private void setUpBack(TextView TextView) {
        TextView.setBackground(getView().getResources().getDrawable(R.color.color_darkGrey, null));
        TextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.color_white, null));
    }

    private void setMarket(View baseView) {
        switch (baseView.getId()) {
            case R.id.tv_market_left_coin1:
                lvMarketInfo.setAdapter(cnyRowAdapter);
                break;
            case R.id.tv_market_left_coin2:
                lvMarketInfo.setAdapter(btsRowAdapter);
                break;
            case R.id.tv_market_left_coin3:
                lvMarketInfo.setAdapter(usdRowAdapter);
                break;
            case R.id.tv_market_left_coin4:
                lvMarketInfo.setAdapter(btcRowAdapter);
                break;
            case R.id.tv_market_left_coin5:
                lvMarketInfo.setAdapter(ethRowAdapter);
                break;
        }
    }

//        private void createCol(View leftCoin,LinearLayout Linear,List<Market> markets){
//            for (int i = 0; i < markets.size(); i++) {
//                market=markets.get(i);
//                //生成一行
//                RowLinearLayout row = new RowLinearLayout(getActivity());
//                LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getActivity(),70f));
//                rowParams.setMargins(DensityUtil.dip2px(getActivity(),6f),DensityUtil.dip2px(getActivity(),2f),DensityUtil.dip2px(getActivity(),6f),DensityUtil.dip2px(getActivity(),2f));
//                row.setOrientation(LinearLayout.HORIZONTAL);
//                row.setLayoutParams(rowParams);
//                row.setBackground(getActivity().getDrawable(R.drawable.ll_market_info_row));
//                //生成左侧linear
//                LinearLayout left = new LinearLayout(getActivity());
//                left.setOrientation(LinearLayout.VERTICAL);
//                LinearLayout.LayoutParams leftParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.5f);
//                left.setLayoutParams(leftParams);
//                TextView fluct = new TextView(getActivity());
//                LinearLayout.LayoutParams fluctParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f);
//                fluct.setLayoutParams(fluctParams);
//                //货币名称
//                TextView coinName = new TextView(getActivity());
//                LinearLayout.LayoutParams coinNameParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 2f);
//                coinName.setLayoutParams(coinNameParams);
//                coinName.setGravity(Gravity.CENTER);
//                coinName.setTextSize(18);
//                coinName.setLines(1);
//
//
//
//                //涨跌幅
//                TextView fluctuation = new TextView(getActivity());
//                LinearLayout.LayoutParams fluctuationParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f);
//                fluctuation.setLayoutParams(fluctuationParams);
//                fluctuation.setGravity(Gravity.CENTER);
//                fluctuation.setTextSize(14);
//                fluctuation.setLines(1);
//
//
//                //生成中间textView
//                TextView price = new TextView(getActivity());
//                LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 2.3f);
//                price.setLayoutParams(priceParams);
//                price.setGravity(Gravity.CENTER);
//                price.setTextSize(26);
//                price.setLines(1);
//                price.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
//                price.setSingleLine(true);
//
//                //生成右侧linear
//                LinearLayout right = new LinearLayout(getActivity());
//                right.setOrientation(LinearLayout.VERTICAL);
//                LinearLayout.LayoutParams rightParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.8f);
//                right.setLayoutParams(rightParams);
//                //最低卖价
//                TextView bestAsk = new TextView(getActivity());
//                LinearLayout.LayoutParams bestBidParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f);
//                bestBidParams.setMargins(DensityUtil.dip2px(getActivity(),6f),DensityUtil.dip2px(getActivity(),10f),0,0);
//                bestAsk.setLayoutParams(bestBidParams);
//                bestAsk.setGravity(Gravity.LEFT);
//                bestAsk.setTextSize(12);
//                bestAsk.setLines(1);
//                bestAsk.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
//                bestAsk.setSingleLine(true);
//
//
//                //最高买价
//                TextView bestBid = new TextView(getActivity());
//                LinearLayout.LayoutParams bestAskParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f);
//                bestAskParams.setMargins(DensityUtil.dip2px(getActivity(),6f),DensityUtil.dip2px(getActivity(),10f),0,0);
//                bestBid.setLayoutParams(bestAskParams);
//                bestBid.setGravity(Gravity.LEFT);
//                bestBid.setTextSize(12);
//                bestBid.setLines(1);;
//                bestBid.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
//                bestBid.setSingleLine(true);
//
//                //设定值
//                coinName.setText(Html.fromHtml("<font color='"+getResources().getString(R.string.font_color_white)+"'>"+market.getLeftCoin()+"</font>"));
//                float fluctuat=market.getFluctuation();
//                if (fluctuat>0){
//                    price.setText(Html.fromHtml("<font color='"+getResources().getString(R.string.font_color_green)+"'>"+market.getNewPrice()+"</font>"));
//                    fluctuation.setText(Html.fromHtml("<font color='"+getResources().getString(R.string.font_color_green)+"'>"+market.getFluctuation()+"%</font>"));
//                }else {
//                    price.setText(Html.fromHtml("<font color='"+getResources().getString(R.string.font_color_red)+"'>"+market.getNewPrice()+"</font>"));
//                    fluctuation.setText(Html.fromHtml("<font color='"+getResources().getString(R.string.font_color_red)+"'>"+market.getFluctuation()+"%</font>"));
//                }
//
//                String buy=getResources().getString(R.string.buy);
//                String sell=getResources().getString(R.string.sell);
//                bestAsk.setText(Html.fromHtml("<font color='"+getResources().getString(R.string.font_color_gray)+"'>"+sell+":&nbsp;&nbsp;</font>"+ "<font color='"+getResources().getString(R.string.font_color_lightGrey)+"'>"+market.getBestAsk()+"</font>"));
//                bestBid.setText(Html.fromHtml("<font color='"+getResources().getString(R.string.font_color_gray)+"'>"+buy+":&nbsp;&nbsp;</font>"+ "<font color='"+getResources().getString(R.string.font_color_lightGrey)+"'>"+market.getBestBid()+"</font>"));
//                left.addView(fluct);
//                left.addView(coinName);
//                left.addView(fluctuation);
//                right.addView(bestAsk);
//                right.addView(bestBid);
//
//                row.addView(left);
//                row.addView(price);
//                row.addView(right);
//                row.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        RowLinearLayout layout= (RowLinearLayout) view;
//                        String leftCoin = layout.getLeftCoin();
//                        String rightCoin = layout.getRightCoin();
//                    }
//                });
//                Linear.addView(row);
//            }

//        }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
