package info.btsland.app.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.MarketStat;
import info.btsland.app.model.DataK;
import info.btsland.app.model.Market;
import info.btsland.app.model.MarketTicker;
import info.btsland.app.ui.activity.MarketDetailedActivity;
import info.btsland.app.ui.view.IViewPager;
import info.btsland.app.util.KeyUtil;

public class DetailedKFragment extends Fragment implements MarketStat.OnMarketStatUpdateListener,MarketDetailedActivity.RefurbishK {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MARKET = "market";
    private static final String TAG ="DetailedKFragment" ;
    private static final int SUCCESS=1;
    private static final long INTERVAL5M=TimeUnit.MINUTES.toSeconds(5);
    private static final long INTERVAL1H=TimeUnit.HOURS.toSeconds(1);
    private static final long INTERVAL1D=TimeUnit.DAYS.toSeconds(1);
    private static final long INTERVAL4H=TimeUnit.HOURS.toSeconds(4);
    private static final long SPAN1D=TimeUnit.DAYS.toMillis(1);
    private static final long SPAN1W=TimeUnit.DAYS.toMillis(7);
    private static final long SPAN1M=TimeUnit.DAYS.toMillis(30);
    private static final long SPAN3M=TimeUnit.DAYS.toMillis(90);

    public static long range= TimeUnit.MINUTES.toSeconds(5);//每条信息的间隔

    public static long ago=TimeUnit.DAYS.toMillis(1);//距离现在时间

    private static String quote ="BTS";
    private static String base ="CNY";

    private SimpleDateFormat df = new SimpleDateFormat("HH:mm");
    private CandleDataSet candleDataSet;
    private CandleData candleData;//烛形图对象
    private CombinedData data;//图表总数据
    private MarketStat.HistoryPrice price=new MarketStat.HistoryPrice();

    private CombinedChart simpleK;//图表
    private NumberProgressBar progressBar;
    private TextView tvInterval5M;
    private TextView tvInterval1H;
    private TextView tvInterval4H;
    private TextView tvInterval1D;
    private TextView tvSpan1D;
    private TextView tvSpan1W;
    private TextView tvSpan1M;
    private TextView tvSpan3M;
    private LinearLayout layout1;
    private IViewPager iViewPager;

    private TextView tvDateNum;
    private TextView tvOpenNum;
    private TextView tvHighNum;
    private TextView tvVolumeNum;
    private TextView tvCloseNum;
    private TextView tvLowNum;

    private TextView tvInfoBase;
    private TextView tvInfoQuote;
    private TextView tvInfoNewPrice;
    private TextView tvInfoFluctuation;
    private TextView tvInfoBestBid;
    private TextView tvInfoBestAsk;
    private TextView tvInfoOpen;
    private TextView tvInfoClose;
    private TextView tvInfoHighReach;
    private TextView tvInfoLowReach;

    private static DetailedKFragment listener;
    private MarketTicker market;

    private Double highPrice=0.0;
    private Double lowPrice=0.0;


    public DetailedKFragment() {
        // Required empty public constructor
    }
    public static DetailedKFragment newInstance(MarketTicker market) {
        DetailedKFragment fragment = new DetailedKFragment();
        Bundle args = new Bundle();
        args.putSerializable(MARKET, market);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_detailed_k, container, false);
        this.market=(MarketTicker) getArguments().getSerializable(MARKET);
        if (market != null) {
            base=market.base;
            quote=market.quote;
        }
        listener=this;
        init(view);
        fillIn();
        initChart();
        MarketDetailedActivity.refurbishK=this;
        return view;
    }

    private void fillIn() {
        tvInfoBase.setText(MarketDetailedActivity.market.base);
        tvInfoQuote.setText(MarketDetailedActivity.market.quote);
        tvInfoNewPrice.setText(MarketDetailedActivity.market.latest);
        tvInfoFluctuation.setText(String.format("%.2f", MarketDetailedActivity.market.percent_change)+"%");
        tvInfoBestBid.setText(MarketDetailedActivity.market.highest_bid);
        tvInfoBestAsk.setText(MarketDetailedActivity.market.lowest_ask);

        tvInfoOpen.setText(String.valueOf(price.open));
        tvInfoClose.setText(String.valueOf(price.close));
        tvInfoHighReach.setText(String.valueOf(highPrice));
        tvInfoLowReach.setText(String.valueOf(lowPrice));
        ((MarketDetailedActivity) getActivity()).isRefK=true;
        if(((MarketDetailedActivity) getActivity()).isRefOrder&&((MarketDetailedActivity) getActivity()).isRefK){
            ((MarketDetailedActivity) getActivity()).headFragment.hidePBar();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        drawK();
        tvOnClickListener listener=new tvOnClickListener();

        tvInterval5M.setOnClickListener(listener);
        tvInterval1D.setOnClickListener(listener);
        tvInterval1H.setOnClickListener(listener);
        tvInterval4H.setOnClickListener(listener);
        tvSpan1D.setOnClickListener(listener);
        tvSpan1M.setOnClickListener(listener);
        tvSpan1W.setOnClickListener(listener);
        tvSpan3M.setOnClickListener(listener);


        simpleK.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                layout1.setVisibility(View.VISIBLE);
//                e.getData();
//                CandleEntry candleEntry = (CandleEntry)e;
                if(e.getData()!=null) {
                    MarketStat.HistoryPrice historyPrice = (MarketStat.HistoryPrice) e.getData();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd HH:mm");

                    if (historyPrice.date != null) {
                        tvDateNum.setText(simpleDateFormat.format(historyPrice.date));
                    } else {
                        tvDateNum.setText("");
                    }
                    tvHighNum.setText(String.valueOf(historyPrice.high));
                    tvLowNum.setText(String.valueOf(historyPrice.low));
                    tvOpenNum.setText(String.valueOf(historyPrice.open));
                    tvCloseNum.setText(String.valueOf(historyPrice.close));
                    tvVolumeNum.setText(String.valueOf(num(historyPrice.volume)));
                    Log.e(TAG, "onValueSelected: "+num(historyPrice.volume) );
                }
            }
            private String num(double n){
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                double b=0.0;
                String str="";
                if(n < 1000) {
                    str = decimalFormat.format(n);
                } else if(n >=1000 && n < 1000000){
                    b = n/1000;
                    str=decimalFormat.format(b)+"K";
                }else if(n >=1000000 && n < 1000000000){
                    b = n/1000000;
                    str = decimalFormat.format(b)+"M";
                }
                return str;
            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    @Override
    public void refurbish() {
        this.market=MarketDetailedActivity.market;
        base=market.base;
        quote=market.quote;
        drawK();
    }

    class tvOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tv_foot_interval_5M:
                    range=INTERVAL5M;
                    ago=SPAN1D;
                    break;
                case R.id.tv_foot_interval_1H:
                    range=INTERVAL1H;
                    if(ago==SPAN3M||ago==SPAN1M){
                        ago=SPAN1W;
                    }
                    break;
                case R.id.tv_foot_interval_4H:
                    range=INTERVAL4H;
                    break;
                case R.id.tv_foot_interval_1D:
                    range=INTERVAL1D;
                    break;
                case R.id.tv_foot_span_1D:
                    ago=SPAN1D;
                    break;
                case R.id.tv_foot_span_1W:
                    ago = SPAN1W;
                    if(range==INTERVAL5M) {
                        range=INTERVAL1H;
                    }
                    break;
                case R.id.tv_foot_span_1M:
                    ago=SPAN1M;
                    if(range==INTERVAL5M||range==INTERVAL1H) {
                        range=INTERVAL4H;
                    }
                    break;
                case R.id.tv_foot_span_3M:
                    if(range==INTERVAL5M||range==INTERVAL1H){
                        range=INTERVAL4H;
                    }
                    ago=SPAN3M;
                    break;
            }
            Log.i(TAG, "onClick: ago:"+TimeUnit.MILLISECONDS.toDays(ago));
            Log.i(TAG, "onClick: range:"+TimeUnit.SECONDS.toMinutes(range));
            drawK();
            setBtnBack();
        }
    }

    private void setBtnBack() {
        if(range==INTERVAL1D){
            setIntervalBack(tvInterval1D,tvInterval5M,tvInterval1H,tvInterval4H);
        }else if(range==INTERVAL1H){
            setIntervalBack(tvInterval1H,tvInterval1D,tvInterval5M,tvInterval4H);
        }else if(range==INTERVAL5M){
            setIntervalBack(tvInterval5M,tvInterval1H,tvInterval1D,tvInterval4H);
        }else if(range==INTERVAL4H){
            setIntervalBack(tvInterval4H,tvInterval1H,tvInterval1D,tvInterval5M);
        }
        if(ago==SPAN1D){
            setSpanlBack(tvSpan1D,tvSpan1M,tvSpan1W,tvSpan3M);
        }else if(ago==SPAN1W){
            setSpanlBack(tvSpan1W,tvSpan1M,tvSpan1D,tvSpan3M);
        }else if(ago==SPAN1M){
            setSpanlBack(tvSpan1M,tvSpan1D,tvSpan1W,tvSpan3M);
        }else if(ago==SPAN3M){
            setSpanlBack(tvSpan3M,tvSpan1M,tvSpan1W,tvSpan1D);
        }
    }
    private void setIntervalBack(TextView textView1,TextView textView2,TextView textView3,TextView textView4){
        textView1.setBackground(getActivity().getDrawable(R.drawable.tv_detai_touch));
        textView2.setBackground(getActivity().getDrawable(R.drawable.tv_detai));
        textView3.setBackground(getActivity().getDrawable(R.drawable.tv_detai));
        textView4.setBackground(getActivity().getDrawable(R.drawable.tv_detai));
    }
    private void setSpanlBack(TextView textView1,TextView textView2,TextView textView3,TextView textView4){
        textView1.setBackground(getActivity().getDrawable(R.drawable.tv_detai_touch));
        textView2.setBackground(getActivity().getDrawable(R.drawable.tv_detai));
        textView3.setBackground(getActivity().getDrawable(R.drawable.tv_detai));
        textView4.setBackground(getActivity().getDrawable(R.drawable.tv_detai));
    }

    private void init(View view) {
        simpleK=view.findViewById(R.id.cbc_detailed_K);
        simpleK.setNoDataText("数据正在读取中。。。");
        progressBar=view.findViewById(R.id.npb_detailed_K_ProgressBar);
        progressBar.setMax(100);
        tvInterval5M=view.findViewById(R.id.tv_foot_interval_5M);
        tvInterval1H=view.findViewById(R.id.tv_foot_interval_1H);
        tvInterval1D=view.findViewById(R.id.tv_foot_interval_1D);
        tvInterval4H=view.findViewById(R.id.tv_foot_interval_4H);
        tvSpan1D=view.findViewById(R.id.tv_foot_span_1D);
        tvSpan1W=view.findViewById(R.id.tv_foot_span_1W);
        tvSpan1M=view.findViewById(R.id.tv_foot_span_1M);
        tvSpan3M=view.findViewById(R.id.tv_foot_span_3M);
        layout1=view.findViewById(R.id.ll_detailed1);
        tvDateNum=view.findViewById(R.id.tv_date_num);
        tvOpenNum=view.findViewById(R.id.tv_open_num);
        tvHighNum=view.findViewById(R.id.tv_high_num);
        tvVolumeNum=view.findViewById(R.id.tv_volume_num);
        tvCloseNum=view.findViewById(R.id.tv_close_num);
        tvLowNum=view.findViewById(R.id.tv_low_num);
        iViewPager=getActivity().findViewById(R.id.vp_detailed_page);

        tvInfoBase=view.findViewById(R.id.tv_detailed_info_baseText);
        tvInfoQuote=view.findViewById(R.id.tv_detailed_info_quoteText);
        tvInfoNewPrice=view.findViewById(R.id.tv_detailed_info_newPriceText);
        tvInfoFluctuation=view.findViewById(R.id.tv_detailed_info_fluctuationText);
        tvInfoBestBid=view.findViewById(R.id.tv_detailed_info_bestBidText);
        tvInfoBestAsk=view.findViewById(R.id.tv_detailed_info_bestAskText);
        tvInfoOpen=view.findViewById(R.id.tv_detailed_info_openText);
        tvInfoClose=view.findViewById(R.id.tv_detailed_info_closeText);
        tvInfoHighReach=view.findViewById(R.id.tv_detailed_info_highReachText);
        tvInfoLowReach=view.findViewById(R.id.tv_detailed_info_lowReachText);

        tvInterval5M.setBackground(getActivity().getDrawable(R.drawable.tv_detai_touch));
        tvSpan1D.setBackground(getActivity().getDrawable(R.drawable.tv_detai_touch));
    }

    private void initChart() {
        int colorHomeBg = getResources().getColor(android.R.color.transparent);

        simpleK.setDescription(null);
        simpleK.setDrawGridBackground(true);
        simpleK.setBackgroundColor(colorHomeBg);
        simpleK.setGridBackgroundColor(colorHomeBg);
        simpleK.setScaleYEnabled(false);
        simpleK.setPinchZoom(true);

        simpleK.setNoDataText("数据正在读取中。。。");
        simpleK.setAutoScaleMinMaxEnabled(true);
        simpleK.setDragEnabled(true);
        simpleK.setDoubleTapToZoomEnabled(false);
//        simpleK.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                iViewPager.setScanScroll(false);
//            }
//        });

        XAxis xAxis = simpleK.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        //xAxis.setGridColor(colorLine);
        //xAxis.setTextColor(colorText);
        xAxis.setLabelCount(3);
        //xAxis.setSpaceBetweenLabels(4);

        YAxis leftAxis = simpleK.getAxisLeft();
        leftAxis.setLabelCount(4, false);
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawAxisLine(true);
        //leftAxis.setGridColor(colorLine);
        //leftAxis.setTextColor(colorText);
    }
    public static DetailedKFragment getListener() {
        return listener;
    }
    public void drawK() {
        Log.e(TAG, "drawK:1111111111111111111111111111111111111111111111111111111 ");
        MarketDetailedActivity.dataKey=KeyUtil.constructingDateKKey(base,quote,range,ago);
        Log.i(TAG, "drawK: newKey:"+MarketDetailedActivity.dataKey);
        Log.e(TAG, String.valueOf("drawK: "+ BtslandApplication.dataKMap.get(MarketDetailedActivity.dataKey)!=null));
        if(BtslandApplication.dataKMap.get(MarketDetailedActivity.dataKey)!=null) {
            updateChartData();
        }else {
            startReceiveMarkets();
        }
    }

    private void updateChartData() {
        List<MarketStat.HistoryPrice> historyPriceList=BtslandApplication.dataKMap.get(MarketDetailedActivity.dataKey);
        if(historyPriceList!=null&&historyPriceList.size()>0){
            price=historyPriceList.get(historyPriceList.size()-1);
            initializeData(historyPriceList);
            IAxisValueFormatter xValue = new xAxisValueFormater(historyPriceList);
            simpleK.getXAxis().setValueFormatter(xValue);
        }
        fillInHandler.sendEmptyMessage(1);
    }
    private void initializeData(List<MarketStat.HistoryPrice> listHistoryPrice) {
        Log.i(TAG, "initializeData: listHistoryPrice:"+listHistoryPrice.size());
        List<CandleEntry> candleEntryList = new ArrayList<>();
        List<BarEntry> barEntryList = new ArrayList<>();
        highPrice=listHistoryPrice.get(0).high;
        lowPrice=listHistoryPrice.get(0).low;
        for (int i=0;i<listHistoryPrice.size(); i++) {
            MarketStat.HistoryPrice pri=listHistoryPrice.get(i);
            if (pri.high>highPrice){
                highPrice=pri.high;
            }
            if (pri.low<lowPrice){
                lowPrice=pri.low;
            }
            CandleEntry candleEntry = new CandleEntry(i+1, (float)pri.high, (float)pri.low, (float)pri.open, (float)pri.close, pri);
            candleEntryList.add(candleEntry);

            BarEntry barEntry = new BarEntry((float)i+1, (float)pri.volume);
            barEntryList.add(barEntry);
        }

        CandleDataSet set = new CandleDataSet(candleEntryList, "");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setShadowWidth(0.7f);
        set.setDecreasingPaintStyle(Paint.Style.FILL);


        //设置绿涨红跌
        set.setDecreasingColor(BtslandApplication.goDown);
        set.setIncreasingColor(BtslandApplication.goUp);
        set.setNeutralColor(BtslandApplication.suspend);


        set.setIncreasingPaintStyle(Paint.Style.FILL);
        set.setShadowColorSameAsCandle(true);
        set.setHighlightLineWidth(0.5f);
        set.setHighLightColor(Color.WHITE);
        set.setDrawValues(false);
        set.setForm(Legend.LegendForm.EMPTY);

        BarDataSet barDataSet = new BarDataSet(barEntryList, "");
        barDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        barDataSet.setColor(getResources().getColor(R.color.color_Grey), 45);
        barDataSet.setDrawValues(false);
        barDataSet.setForm(Legend.LegendForm.EMPTY);

        CombinedData combinedData = simpleK.getCombinedData();
        if (combinedData == null) {
            combinedData = new CombinedData();
        }

        CandleData candleData = combinedData.getCandleData();
        if (candleData == null) {
            candleData = new CandleData();
        }
        if (candleData.getDataSetCount() > 0) {
            candleData.removeDataSet(0);
            candleData.notifyDataChanged();
            simpleK.notifyDataSetChanged();
        }
        candleData.addDataSet(set);
        candleData.notifyDataChanged();
        combinedData.setData(candleData);

        BarData barData = combinedData.getBarData();
        if (barData == null) {
            barData = new BarData();
        }

        if (barData.getDataSetCount() > 0) {
            barData.removeDataSet(0);
            barData.notifyDataChanged();
            simpleK.notifyDataSetChanged();
        }
        barData.addDataSet(barDataSet);
        barData.notifyDataChanged();
        combinedData.setData(barData);

        simpleK.setData(combinedData);
        simpleK.notifyDataSetChanged();
        simpleK.fitScreen();
        //simpleK.setVisibleXRangeMaximum(100);
        simpleK.moveViewToX(simpleK.getXChartMax() - 48);
        simpleK.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }
    class xAxisValueFormater implements IAxisValueFormatter {
        private List<MarketStat.HistoryPrice> mListPrices;

        public xAxisValueFormater(List<MarketStat.HistoryPrice> listPrices) {
            mListPrices = listPrices;
        }
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            int nValue = (int)value;
            if (nValue < mListPrices.size()) {
                Date date = mListPrices.get(nValue).date;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd HH:mm");

                return simpleDateFormat.format(date);
            } else {
                return "";
            }
        }
    }

    /**
     * 启动查询数据线程
     */
    public void startReceiveMarkets() {
        Log.i(TAG, "startReceiveMarkets: age:"+ago);
        BtslandApplication.getMarketStat().subscribe(
                base,
                quote,
                range,
                ago,
                MarketStat.STAT_MARKET_HISTORY,
                MarketStat.DEFAULT_UPDATE_K_SECS,
                getListener(),barhandler);
        barhandler.sendEmptyMessage(2);
    }

    @Override
    public void onMarketStatUpdate(MarketStat.Stat stat) {
        Log.e(TAG, "onMarketStatUpdate: Thread:"+Thread.currentThread().getName());
        Log.i(TAG, String.valueOf("onMarketStatUpdate: stat:"+stat==null));
        Log.i(TAG, "onMarketStatUpdate: "+stat.prices.size());
        if(stat!=null&&stat.prices!=null&&stat.prices.size()>0){
            MarketStat.HistoryPrice price=stat.prices.get(0);
            String newkey= KeyUtil.constructingDateKKey(price.base,price.quote,stat.bucket,stat.ago);
            if(BtslandApplication.dataKMap.get(newkey)!=null){
                BtslandApplication.dataKMap.remove(newkey);
            }
            BtslandApplication.dataKMap.put(newkey,stat.prices);
            if(newkey.equals(MarketDetailedActivity.dataKey)){
                Log.i(TAG, "onMarketStatUpdate: success");
                handler.sendEmptyMessage(SUCCESS);
            }

        }
    }

    public Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
        if( msg.what==SUCCESS){
            if(isAdded()){
                updateChartData();
            }
        }
        }
    };
    public Handler fillInHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            fillIn();
        }
    };
    public Handler barhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG, "handleMessage: ");
            if(msg.what==1){
                Bundle bundle=msg.getData();
                String key=bundle.getString("name");
                int a=bundle.getInt("bar",0);
                Log.e(TAG, "handleMessage: "+a );
                if(MarketDetailedActivity.dataKey.equals(key)){
                    progressBar.setProgress(a);
                }
            }else if(msg.what==2) {
                simpleK.setVisibility(View.INVISIBLE);
                progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    };
}
