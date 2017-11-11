package info.btsland.app.ui.fragment;

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
import android.widget.TextView;

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
import info.btsland.app.model.MarketTicker;
import info.btsland.app.ui.activity.MarketDetailedActivity;

public class DetailedKFragment extends Fragment implements MarketStat.OnMarketStatUpdateListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MARKET = "market";
    private static final String TAG ="DetailedKFragment" ;
    private static final int SUCCESS=1;
    private long range= TimeUnit.MINUTES.toSeconds(5);//每条信息的间隔

    private long ago=TimeUnit.DAYS.toMillis(1);//距离现在时间

    private static String quote ="BTS";
    private static String base ="CNY";

    private SimpleDateFormat df = new SimpleDateFormat("HH:mm");
    private CandleDataSet candleDataSet;
    private CandleData candleData;//烛形图对象
    private CombinedData data;//图表总数据
    private String key;

    private CombinedChart simpleK;//图表
    private TextView tv5M;
    private TextView tv1H;
    private TextView tv1D;

    private static DetailedKFragment listener;
    private MarketTicker market;


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
        drawK();
        initChart();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        tvOnClickListener listener=new tvOnClickListener();

        tv5M.setOnClickListener(listener);
        tv1D.setOnClickListener(listener);
        tv1H.setOnClickListener(listener);

    }
    class tvOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tv_detailed_foot_5M:
                    range=TimeUnit.MINUTES.toSeconds(5);
                    ago=TimeUnit.DAYS.toMillis(1);
                    break;
                case R.id.tv_detailed_foot_1H:
                    range=TimeUnit.HOURS.toSeconds(1);
                    ago=TimeUnit.DAYS.toMillis(7);
                    break;
                case R.id.tv_detailed_foot_1D:
                    range=TimeUnit.DAYS.toSeconds(1);
                    ago=TimeUnit.DAYS.toMillis(90);
                    break;
            }
            startReceiveMarkets();

        }
    }
    private void init(View view) {
        simpleK=view.findViewById(R.id.cbc_detailed_K);
        simpleK.setNoDataText("数据正在读取中。。。");
        tv5M=view.findViewById(R.id.tv_detailed_foot_5M);
        tv1H=view.findViewById(R.id.tv_detailed_foot_1H);
        tv1D=view.findViewById(R.id.tv_detailed_foot_1D);
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
        String key=constructingKey(base,quote,range,ago);
        Log.i(TAG, "drawK: newKey:"+key);
        Log.i(TAG, String.valueOf("drawK: "+ BtslandApplication.dataKMap.get(key)!=null));
        if(BtslandApplication.dataKMap.get(key)!=null) {
            updateChartData();
        }else {
            startReceiveMarkets();
        }
    }

    private void updateChartData() {
        List<MarketStat.HistoryPrice> historyPriceList=BtslandApplication.dataKMap.get(key);
        initializeData(historyPriceList);
        IAxisValueFormatter xValue = new xAxisValueFormater(historyPriceList);
        simpleK.getXAxis().setValueFormatter(xValue);
    }
    private void initializeData(List<MarketStat.HistoryPrice> listHistoryPrice) {
        List<CandleEntry> candleEntryList = new ArrayList<>();
        List<BarEntry> barEntryList = new ArrayList<>();
        int i = 0;
        for (MarketStat.HistoryPrice price : listHistoryPrice) {
            int nPosition = i++;
            CandleEntry candleEntry = new CandleEntry(nPosition, (float)price.high, (float)price.low, (float)price.open, (float)price.close, price);
            candleEntryList.add(candleEntry);

            BarEntry barEntry = new BarEntry((float)nPosition, (float)price.volume);
            barEntryList.add(barEntry);
        }

        CandleDataSet set = new CandleDataSet(candleEntryList, "");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setShadowWidth(0.7f);
        set.setDecreasingPaintStyle(Paint.Style.FILL);
        int nColorGreen = ContextCompat.getColor(getActivity(), R.color.color_font_red);
        int nColorRed = ContextCompat.getColor(getActivity(), R.color.color_green);

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
        barDataSet.setColor(getResources().getColor(R.color.color_Grey), 25);
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
        simpleK.setVisibleXRangeMaximum(48);
        simpleK.moveViewToX(simpleK.getXChartMax() - 48);
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
                MarketStat.DEFAULT_UPDATE_SECS,
                getListener());
    }

    @Override
    public void onMarketStatUpdate(MarketStat.Stat stat) {
        Log.i(TAG, "onMarketStatUpdate: Thread:"+Thread.currentThread().getName());
        Log.i(TAG, String.valueOf("onMarketStatUpdate: stat:"+stat==null));
        if(stat!=null&&stat.prices!=null&&stat.prices.size()>0){
            MarketStat.HistoryPrice price=stat.prices.get(0);
            key=constructingKey(price.base,price.quote,stat.bucket,stat.ago);
            if(BtslandApplication.dataKMap.get(key)!=null){
                BtslandApplication.dataKMap.remove(BtslandApplication.dataKMap.get(key));
            }
            BtslandApplication.dataKMap.put(key,stat.prices);

            handler.sendEmptyMessage(SUCCESS);
        }
    }

    /**
     * 生成数据提取的键
     * @param base
     * @param quote
     * @param bucket
     * @param ago
     * @return
     */
    public String constructingKey(String base,String quote,long bucket,long ago){
        return ""+base+"/"+quote+":"+bucket+","+ago;
    }
    public Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if( msg.what==SUCCESS){
                updateChartData();
            }
        }
    };
}
