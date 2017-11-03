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
    private String TAG="MarketSimpleKFragment";
    private static final long DEFAULT_BUCKET_SECS = TimeUnit.MINUTES.toSeconds(5);
    private MarketStat marketStat;
    private TextView deal;
    private TextView high;
    private TextView low;
    private TextView count;

    private final String REFURBISH="refurbish";

    private String quote ="BTS";
    private String base ="CNY";

    private MarketStat.HistoryPrice[] prices;
    private String highStr;
    private String lowStr;
    private String countStr;
    private SimpleDateFormat df = new SimpleDateFormat("HH:mm");
    private CandleDataSet candleDataSet;
    private CandleData candleData;//烛形图对象
    private CombinedData data;//图表总数据

    private CombinedChart simpleK;//图表

    private float max;
    private float min;

    public MarketSimpleKFragment() {
        this.marketStat=BtslandApplication.getMarketStat();
        // Required empty public constructor
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
    public void onMarketStatUpdate(MarketStat.Stat stat) {
        Log.i(TAG, String.valueOf("onMarketStatUpdate: stat:"+stat==null));
        if(stat!=null&&stat.prices!=null&&stat.prices.size()>0){
            Log.i(TAG, "onMarketStatUpdate: stat.prices.length:"+stat.prices.size());
            if(BtslandApplication.prices!=null){
                BtslandApplication.prices.clear();
                BtslandApplication.candleEntries.clear();
            }
            BtslandApplication.prices=stat.prices;
            Log.i(TAG, "onMarketStatUpdate: BtslandApplication.prices.length:"+BtslandApplication.prices.size());
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
                BtslandApplication.candleEntries.add(entry);
            }
            Log.i(TAG, "onMarketStatUpdate: max:"+max);
            Log.i(TAG, "onMarketStatUpdate: min:"+min);

            Message message=Message.obtain();
            message.obj=REFURBISH;
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
        @Override
        public String getXValue(String original, int index, ViewPortHandler viewPortHandler) {
            return df.format(BtslandApplication.prices.get(index).date);
        }
    }

    public void startReceiveMarkets(MarketTicker market) {
        if (market != null) {
            if (market.base.equals(base) && market.quote.equals(quote)) {
                Intent intent = new Intent(getActivity(), MarketDetailedActivity.class);
                intent.putExtra("MarketTicker", market);
                getActivity().startActivity(intent);
                return;
            }else {
                this.base=market.base;
                this.quote=market.quote;
               // deal.setText(quote+":"+base);
            }
        }
        marketStat.subscribe(
                base,
                quote,
                MarketStat.DEFAULT_BUCKET_SECS,
                MarketStat.STAT_MARKET_HISTORY,
                DEFAULT_BUCKET_SECS,this);

    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.obj.equals(REFURBISH)){
                Log.i(TAG, "handleMessage: max:"+max);
                Log.i(TAG, "handleMessage: min:"+min);
                Log.i(TAG, "handleMessage: candleEntries:"+BtslandApplication.candleEntries.size());
                candleDataSet=new CandleDataSet(BtslandApplication.candleEntries,"Data Set");//烛形图图形
                simpleKInit(candleDataSet);
                List<String> xVals=new ArrayList<>();
                for(int i=0;i<BtslandApplication.candleEntries.size();i++){
                    xVals.add(i,df.format(BtslandApplication.candleEntries.get(i).getData()));
                }
                Log.i(TAG, "handleMessage: xVals:"+xVals.size());
                candleData=new CandleData(xVals,candleDataSet);
                Log.i(TAG, "handleMessage: candleDataSet.getEntryCount():"+candleDataSet.getEntryCount());
                Log.i(TAG, "handleMessage: candleData.getDataSets().size():"+candleData.getDataSets().size());
                Log.i(TAG, "handleMessage: candleData.getXValCount():"+candleData.getXValCount());
                data=new CombinedData(xVals);
                data.setData(candleData);
                simpleK.setData(data);
                simpleK.setDescription(quote+"/"+base);
                simpleK.setDescriptionTextSize(14);
                simpleK.notifyDataSetChanged();
                simpleK.invalidate();
                Log.i(TAG, "getAxisLeft: max:"+simpleK.getAxisLeft().getAxisMinValue());
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
        candleDataSet.setLabel("label");//图表名称
        XAxis xAxis=simpleK.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴标签显示位置
        xAxis.setDrawGridLines(false);//绘制格网线
        YAxis leftAxis = simpleK.getAxisLeft();//取得左侧y轴
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);//y轴标签绘制的位置
        leftAxis.setDrawGridLines(true);//绘制y轴格网线
        leftAxis.setDrawLabels(true);//显示坐标轴上的值, ...其他样式
        leftAxis.setStartAtZero(false);
        leftAxis.setAxisMaxValue((float) (max*1.05));
        leftAxis.setAxisMinValue((float) (min*0.95));


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
        MarketTicker market=null;
        if (getArguments() != null) {
            market= (MarketTicker) getArguments().getSerializable("MarketTicker");
            if (market!= null) {
                base = market.base;
                quote = market.quote;
            }

        }
        init(view);
        startReceiveMarkets(market);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
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
