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
import info.btsland.app.model.MarketTicker;
import info.btsland.app.ui.activity.MarketDetailedActivity;
import info.btsland.app.ui.view.IViewPager;

public class DetailedKFragment extends Fragment implements MarketStat.OnMarketStatUpdateListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MARKET = "market";
    private static final String TAG ="DetailedKFragment" ;
    private static final int SUCCESS=1;
    private static final long INTERVAL5M=TimeUnit.MINUTES.toSeconds(5);
    private static final long INTERVAL1H=TimeUnit.HOURS.toSeconds(1);
    private static final long INTERVAL1D=TimeUnit.DAYS.toSeconds(1);
    private static final long SPAN1D=TimeUnit.DAYS.toMillis(1);
    private static final long SPAN1W=TimeUnit.DAYS.toMillis(7);
    private static final long SPAN1M=TimeUnit.DAYS.toMillis(30);
    private static final long SPAN3M=TimeUnit.DAYS.toMillis(90);
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
    private TextView tvInterval5M;
    private TextView tvInterval1H;
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

        tvInterval5M.setOnClickListener(listener);
        tvInterval1D.setOnClickListener(listener);
        tvInterval1H.setOnClickListener(listener);
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
    private TextView a;
    private TextView b;
    class tvOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tv_foot_interval_5M:
                    a.setBackground(getActivity().getDrawable(R.drawable.tv_detai));
                    range=INTERVAL5M;
                    a=tvInterval5M;
                    break;
                case R.id.tv_foot_interval_1H:
                    a.setBackground(getActivity().getDrawable(R.drawable.tv_detai));
                    range=INTERVAL1H;
                    a=tvInterval1H;
                    break;
                case R.id.tv_foot_interval_1D:
                    a.setBackground(getActivity().getDrawable(R.drawable.tv_detai));
                    range=INTERVAL1D;
                    a=tvInterval1D;
                    break;
                case R.id.tv_foot_span_1D:
                    b.setBackground(getActivity().getDrawable(R.drawable.tv_detai));
                    ago=SPAN1D;
                    b=tvSpan1D;
                    break;
                case R.id.tv_foot_span_1W:
                    b.setBackground(getActivity().getDrawable(R.drawable.tv_detai));
                    ago=SPAN1W;
                    b=tvSpan1W;
                    break;
                case R.id.tv_foot_span_1M:
                    b.setBackground(getActivity().getDrawable(R.drawable.tv_detai));
                    ago=SPAN1M;
                    b=tvSpan1M;
                    break;
                case R.id.tv_foot_span_3M:
                    b.setBackground(getActivity().getDrawable(R.drawable.tv_detai));
                    ago=SPAN3M;
                    b=tvSpan3M;
                    break;
            }
            Log.i(TAG, "onClick: ago:"+TimeUnit.MILLISECONDS.toDays(ago));
            Log.i(TAG, "onClick: range:"+TimeUnit.SECONDS.toMinutes(range));
            a.setBackground(getActivity().getDrawable(R.drawable.tv_detai_touch));
            b.setBackground(getActivity().getDrawable(R.drawable.tv_detai_touch));
            startReceiveMarkets();

        }
    }
    private void init(View view) {
        simpleK=view.findViewById(R.id.cbc_detailed_K);
        simpleK.setNoDataText("数据正在读取中。。。");
        tvInterval5M=view.findViewById(R.id.tv_foot_interval_5M);
        tvInterval1H=view.findViewById(R.id.tv_foot_interval_1H);
        tvInterval1D=view.findViewById(R.id.tv_foot_interval_1D);
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

        tvInterval5M.setBackground(getActivity().getDrawable(R.drawable.tv_detai_touch));
        tvSpan1D.setBackground(getActivity().getDrawable(R.drawable.tv_detai_touch));
        a=tvInterval5M;
        b=tvSpan1D;
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
        if(historyPriceList!=null&&historyPriceList.size()>0){
            initializeData(historyPriceList);
            IAxisValueFormatter xValue = new xAxisValueFormater(historyPriceList);
            simpleK.getXAxis().setValueFormatter(xValue);
        }
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
        int nColorGreen = BtslandApplication.getInstance().getResources().getColor(R.color.color_font_red);
        int nColorRed = BtslandApplication.getInstance().getResources().getColor(R.color.color_green);

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
                if(isAdded()){
                    updateChartData();
                }
            }
        }
    };
}
