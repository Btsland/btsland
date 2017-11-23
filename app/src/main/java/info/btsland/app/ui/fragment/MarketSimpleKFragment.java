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
import com.github.mikephil.charting.utils.ViewPortHandler;

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
import info.btsland.app.service.Impl.MarketServiceImpl;
import info.btsland.app.service.MarketService;
import info.btsland.app.ui.activity.MarketDetailedActivity;
import info.btsland.app.util.KeyUtil;


public class MarketSimpleKFragment extends Fragment implements MarketStat.OnMarketStatUpdateListener {
    private static String TAG="MarketSimpleKFragment";
    public static String key;
    private static MarketSimpleKFragment listener;
    private static final int SUCCESS=1;
    private static String quote ="BTS";
    private static String base ="CNY";
    private SimpleDateFormat df = new SimpleDateFormat("HH:mm");
    private CandleDataSet candleDataSet;
    private CandleData candleData;//烛形图对象
    private CombinedData data;//图表总数据

    private CombinedChart simpleK;//图表
    private MarketTicker market;

    public MarketSimpleKFragment() {
        // Required empty public constructor
    }

    public static MarketSimpleKFragment getListener() {
        return listener;
    }
//    public static MarketSimpleKFragment newInstance(Market market) {
//        MarketSimpleKFragment simpleKFragment = new MarketSimpleKFragment();
//        Bundle args = new Bundle();
//        args.putSerializable("market", market);
//        simpleKFragment.setArguments(args);
//        return simpleKFragment;
//    }

    private void init(View view) {
        simpleK=view.findViewById(R.id.cbc_market_simple_K);
        simpleK.setNoDataText("数据正在读取中。。。");
    }

    @Override
    public void onMarketStatUpdate(MarketStat.Stat stat) {
        if(stat!=null&&stat.prices!=null&&stat.prices.size()>0){
            MarketStat.HistoryPrice price=stat.prices.get(0);
            String newkey=KeyUtil.constructingDateKKey(price.base,price.quote,stat.bucket,stat.ago);

            if(BtslandApplication.dataKMap.get(newkey)!=null){
                BtslandApplication.dataKMap.remove(BtslandApplication.dataKMap.get(newkey));
            }
            BtslandApplication.dataKMap.put(newkey,stat.prices);
            if(newkey.equals(key)){
                handler.sendEmptyMessage(SUCCESS);
            }

        }
    }

    public static MarketSimpleKFragment newInstance(MarketTicker market) {

        MarketSimpleKFragment simpleKFragment=new MarketSimpleKFragment();
        if(market!=null){
            Bundle bundle=new Bundle();
            bundle.putSerializable("MarketTicker",market);
            simpleKFragment.setArguments(bundle);
        }
        return simpleKFragment;
    }

    public static void startReceiveMarkets(MarketTicker market) {
        Log.i(TAG, "startReceiveMarkets: market:"+market);
        String base=MarketSimpleKFragment.base;
        String quote=MarketSimpleKFragment.quote;
        if (market != null) {
            base=market.base;
            quote=market.quote;
        }
        //判断线程是否存在，存在则重启，不在则重开
            BtslandApplication.getMarketStat().subscribe(
                    base,
                    quote,
                    MarketStat.STAT_MARKET_HISTORY,
                    MarketStat.DEFAULT_UPDATE_SECS,
                    getListener());

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

    public void drawK(MarketTicker market) {
        if(market!=null){
            base=market.base;
            quote=market.quote;
        }
        key= KeyUtil.constructingDateKKey(base,quote,MarketStat.DEFAULT_BUCKET_SECS, MarketStat.DEFAULT_AGO_SECS);
        if(BtslandApplication.dataKMap.get(key)!=null) {
            updateChartData();
        }else {
            startReceiveMarkets(market);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("", "onCreateView: ");
        View view = null;
        view = inflater.inflate(R.layout.fragment_market_simple_k, container, false);
        if (getArguments() != null) {
            market= (MarketTicker) getArguments().getSerializable("MarketTicker");
        }
        init(view);
        initChart();
        listener=this;
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        drawK(market);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void updateChartData() {
        List<MarketStat.HistoryPrice> historyPriceList=BtslandApplication.dataKMap.get(key);
        initializeData(historyPriceList);
        IAxisValueFormatter xValue = new xAxisValueFormater(historyPriceList);
        simpleK.getXAxis().setValueFormatter(xValue);
    }
    private void initChart() {
        int colorHomeBg = getResources().getColor(android.R.color.transparent);

        simpleK.setDescription(null);
        simpleK.setDrawGridBackground(true);
        //simpleK.setBackgroundColor(colorHomeBg);
        simpleK.setGridBackgroundColor(colorHomeBg);
        simpleK.setScaleYEnabled(false);
        simpleK.setPinchZoom(true);

        simpleK.setNoDataText("数据正在读取中。。。");
        simpleK.setAutoScaleMinMaxEnabled(true);
        simpleK.setDragEnabled(true);
        simpleK.setDoubleTapToZoomEnabled(false);

        XAxis xAxis = simpleK.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        //xAxis.setGridColor(colorLine);
        //xAxis.setTextColor(colorText);
        xAxis.setLabelCount(4);
        //xAxis.setSpaceBetweenLabels(4);

        YAxis leftAxis = simpleK.getAxisLeft();
        leftAxis.setLabelCount(4, false);
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawAxisLine(true);
        //leftAxis.setGridColor(colorLine);
        //leftAxis.setTextColor(colorText);

        YAxis rightAxis = simpleK.getAxisRight();
        rightAxis.setTextSize(8);
        rightAxis.setLabelCount(5, false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawAxisLine(true);
//        rightAxis.setGridColor(colorLine);
//        rightAxis.setTextColor(colorText);
        rightAxis.setAxisMinimum(0);
    }
    private void initializeData(List<MarketStat.HistoryPrice> listHistoryPrice) {
        if(listHistoryPrice==null){
            return;
        }
        List<CandleEntry> candleEntryList = new ArrayList<>();
        List<BarEntry> barEntryList = new ArrayList<>();
        int i = 0;

        for (MarketStat.HistoryPrice price : listHistoryPrice) {
            int nPosition = i++;
            CandleEntry candleEntry = new CandleEntry(nPosition, (float)price.high, (float)price.low, (float)price.open, (float)price.close, price.date);
            candleEntryList.add(candleEntry);

            BarEntry barEntry = new BarEntry((float)nPosition, (float)price.volume);
            barEntryList.add(barEntry);
        }

        CandleDataSet set = new CandleDataSet(candleEntryList, "");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setShadowWidth(0.7f);
        set.setDecreasingPaintStyle(Paint.Style.FILL);
        int nColorGreen = ContextCompat.getColor(getActivity(), R.color.color_green);
        int nColorRed = ContextCompat.getColor(getActivity(), R.color.color_font_red);

        //设置绿涨红跌
        set.setDecreasingColor(nColorRed);
        set.setIncreasingColor(nColorGreen);
        set.setNeutralColor(nColorGreen);


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
        simpleK.setVisibleXRangeMaximum(45);
        simpleK.moveViewToX(simpleK.getXChartMax() - 45);
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
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd");

                return simpleDateFormat.format(date);
            } else {
                return "";
            }
        }
    }

}
