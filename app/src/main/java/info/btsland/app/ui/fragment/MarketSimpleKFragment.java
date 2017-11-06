package info.btsland.app.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.util.TimeUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.MarketStat;
import info.btsland.app.model.Market;
import info.btsland.app.model.MarketTicker;
import info.btsland.app.service.Impl.MarketServiceImpl;
import info.btsland.app.service.MarketService;
import info.btsland.app.ui.activity.MarketDetailedActivity;


public class MarketSimpleKFragment extends Fragment implements MarketStat.OnMarketStatUpdateListener {
    private static String TAG="MarketSimpleKFragment";
    private static final long DEFAULT_BUCKET_SECS = TimeUnit.MINUTES.toSeconds(5);
    private static String key;
    private TextView deal;
    private TextView high;
    private TextView low;
    private TextView count;
    private static MarketSimpleKFragment listener;

    private final String REFURBISH="refurbish";

    private static String quote ="BTS";
    private static String base ="CNY";
    private String highStr;
    private String lowStr;
    private String countStr;
    private SimpleDateFormat df = new SimpleDateFormat("HH:mm");
    private CandleDataSet candleDataSet;
    private CandleData candleData;//烛形图对象
    private CombinedData data;//图表总数据

    private CombinedChart simpleK;//图表
    private MarketTicker market;
    private float max;
    private float min;

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
//        deal = view.findViewById(R.id.tv_market_simple_deal);
//        high = view.findViewById(R.id.tv_market_simple_high);
//        low = view.findViewById(R.id.tv_market_simple_low);
//        count = view.findViewById(R.id.tv_market_simple_count);
        simpleK=view.findViewById(R.id.cbc_market_simple_K);
        simpleK.setNoDataText("数据正在读取中。。。");
//        deal.setText(quote + ":" + base);
    }

    @Override
    public synchronized void onMarketStatUpdate(MarketStat.Stat stat) {
        Log.i(TAG, "handleMessage: Thread:"+Thread.currentThread().getName());
        Log.i(TAG, String.valueOf("onMarketStatUpdate: stat:"+stat==null));
        if(stat!=null&&stat.prices!=null&&stat.prices.size()>0){
            BtslandApplication.prices.put(key,stat.prices);
            List<CandleEntry> pric=new ArrayList<>();
            max=(float)stat.prices.get(0).high;
            min=(float)stat.prices.get(0).low;
            //处理数据
            for(int i=0;i<stat.prices.size();i++){
                CandleEntry entry=new CandleEntry(i,
                        (float) stat.prices.get(i).high, (float) stat.prices.get(i).low,
                        (float) stat.prices.get(i).open, (float) stat.prices.get(i).close,
                        stat.prices.get(i).date);

                if((float)stat.prices.get(i).high>max){
                    max=(float)stat.prices.get(i).high;
                }
                if((float)stat.prices.get(i).low<min){
                    min=(float)stat.prices.get(i).low;
                }
                pric.add(entry);
            }
            String key=stat.prices.get(0).quote+"/"+stat.prices.get(0).base;
            if(BtslandApplication.candleEntries.get(key)!=null){
                BtslandApplication.candleEntries.get(key).clear();
            }

            BtslandApplication.candleEntries.put(key,pric);
            Log.i(TAG, "onMarketStatUpdate: max:"+max);
            Log.i(TAG, "onMarketStatUpdate: min:"+min);

            Message message=Message.obtain();
            message.obj=key;
            handler.sendMessage(message);
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
    class MyXAxisValueFormatter implements XAxisValueFormatter {
        private final String key;

        public MyXAxisValueFormatter(String key) {
            this.key=key;
        }

        @Override
        public String getXValue(String original, int index, ViewPortHandler viewPortHandler) {
            return df.format(BtslandApplication.prices.get(key).get(index).date);
        }
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
        if(BtslandApplication.getMarketStat().subscriptionHashMap
                .get(MarketStat.makeMarketName(
                        base,quote,MarketStat.STAT_MARKET_HISTORY))!=null){
            BtslandApplication.getMarketStat().subscriptionHashMap
                    .get(MarketStat.makeMarketName(
                            base,quote,MarketStat.STAT_MARKET_HISTORY)).updateImmediately();
        }else {
            BtslandApplication.getMarketStat().subscribe(
                    base,
                    quote,
                    MarketStat.STAT_MARKET_HISTORY,
                    MarketStat.DEFAULT_UPDATE_SECS,
                    getListener());
        }
    }


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "handleMessage: Thread:"+Thread.currentThread().getName());
            String newkey= (String) msg.obj;

            if(BtslandApplication.candleEntries.get(key)!=null){
                if(!MarketSimpleKFragment.key.equals(key)){
                    return;
                }
                List<CandleEntry> candleEntries=BtslandApplication.candleEntries.get(key);
                Log.i(TAG, "handleMessage: max:"+max);
                Log.i(TAG, "handleMessage: min:"+min);
                Log.i(TAG, "handleMessage: candleEntries:"+BtslandApplication.candleEntries.size());
                candleDataSet=new CandleDataSet(candleEntries,"");//烛形图图形
                simpleKInit(candleDataSet);
                List<String> xVals=new ArrayList<>();
                for(int i=0;i<BtslandApplication.candleEntries.get(key).size();i++){
                    xVals.add(i,df.format(candleEntries.get(i).getData()));
                }
                Log.i(TAG, "handleMessage: xVals:"+xVals.size());
                candleData=new CandleData(xVals,candleDataSet);
                Log.i(TAG, "handleMessage: candleDataSet.getEntryCount():"+candleDataSet.getEntryCount());
                Log.i(TAG, "handleMessage: candleData.getDataSets().size():"+candleData.getDataSets().size());
                Log.i(TAG, "handleMessage: candleData.getXValCount():"+candleData.getXValCount());
                data=new CombinedData(xVals);
                data.setData(candleData);
                simpleK.setData(data);
                key=newkey;
                simpleK.setDescription(key);
                simpleK.setDescriptionTextSize(14);
                simpleK.notifyDataSetChanged();
                simpleK.invalidate();
                Log.i(TAG, "getAxisLeft: max:"+simpleK.getAxisLeft().getAxisMaxValue());
                Log.i(TAG, "getAxisLeft: min:"+simpleK.getAxisLeft().getAxisMinValue());
            }
        }
    };

    private ArrayList<Entry> toEntry(List<Market> markets) {
        if (markets == null || markets.size() == 0) {
            return null;
        }
        ArrayList<Entry> Entrys = new ArrayList<Entry>();
        float high = 0;
        float low = 0;
        float count;
        for (int i = 0; i < markets.size(); i++) {
            Market market = markets.get(i);
            if (i == 0) {
            }
//            将market生成entry对象
            Entry entry = new Entry(market.getNewPrice(), i);
            Entrys.add(entry);
        }

        return Entrys;
    }

    private void simpleKInit(CandleDataSet candleDataSet) {
        candleDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        candleDataSet.setShadowColor(Color.DKGRAY);//影线颜色
        candleDataSet.setShadowColorSameAsCandle(true);//影线颜色与实体一致
        candleDataSet.setShadowWidth(0.7f);//影线
        candleDataSet.setDecreasingColor(Color.RED);
        candleDataSet.setDecreasingPaintStyle(Paint.Style.FILL);//红涨，实体
        candleDataSet.setIncreasingColor(Color.GREEN);
        candleDataSet.setIncreasingPaintStyle(Paint.Style.FILL);//绿跌，空心
        //candleDataSet.setNeutralColor(Color.RED);//当天价格不涨不跌（一字线）颜色
        candleDataSet.setColor(Color.RED);
        candleDataSet.setHighlightLineWidth(1f);//选中蜡烛时的线宽
        candleDataSet.setDrawValues(false);//在图表中的元素上面是否显示数值
        XAxis xAxis=simpleK.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(key));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴标签显示位置
        xAxis.setDrawGridLines(false);//绘制格网线
        YAxis leftAxis = simpleK.getAxisLeft();//取得左侧y轴
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);//y轴标签绘制的位置
        leftAxis.setDrawGridLines(true);//绘制y轴格网线
        leftAxis.setDrawLabels(true);//显示坐标轴上的值, ...其他样式
        leftAxis.setStartAtZero(false);
        leftAxis.setAxisMaxValue(max);
        leftAxis.setAxisMinValue(min);


        YAxis rightAxis = simpleK.getAxisRight();//取得左侧y轴
        rightAxis.setDrawGridLines(false);//不绘制y轴格网线
        rightAxis.setDrawLabels(false);//不显示坐标轴上的值, ...其他样式


        Log.i(TAG, "simpleKInit: max:"+max);
        Log.i(TAG, "simpleKInit: min:"+min);
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
        listener=this;
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        key=quote+"/"+base;
        startReceiveMarkets(market);


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
