package info.btsland.app.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    private long range= TimeUnit.MINUTES.toSeconds(5);//每条信息的间隔

    private long ago=TimeUnit.DAYS.toMillis(1);//距离现在时间

    private static String quote ="BTS";
    private static String base ="CNY";

    private SimpleDateFormat df = new SimpleDateFormat("HH:mm");
    private CandleDataSet candleDataSet;
    private CandleData candleData;//烛形图对象
    private CombinedData data;//图表总数据

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

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        drawK();
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

    public static DetailedKFragment getListener() {
        return listener;
    }
    public void drawK() {
        String key=market.quote+"/"+market.base;
        Log.i(TAG, "drawK: newKey:"+key);
        Log.i(TAG, String.valueOf("drawK: "+ BtslandApplication.dataKMap.get(key)!=null));
        if(BtslandApplication.dataKMap.get(key)!=null) {
            DataK dataK = BtslandApplication.dataKMap.get(key);
            List<CandleEntry> candleEntries = dataK.candleEntries;
            candleDataSet = new CandleDataSet(candleEntries, "");//烛形图图形
            simpleKInit(candleDataSet, dataK);
            List<String> xVals = new ArrayList<>();
            for (int i = 0; i < candleEntries.size(); i++) {
                xVals.add(i, df.format(candleEntries.get(i).getData()));
            }
            Log.i(TAG, "handleMessage: xVals:" + xVals.size());
            candleData = new CandleData(xVals, candleDataSet);
            Log.i(TAG, "handleMessage: candleDataSet.getEntryCount():" + candleDataSet.getEntryCount());
            Log.i(TAG, "handleMessage: candleData.getDataSets().size():" + candleData.getDataSets().size());
            Log.i(TAG, "handleMessage: candleData.getXValCount():" + candleData.getXValCount());
            data = new CombinedData(xVals);
            data.setData(candleData);
            simpleK.clear();
            simpleK.setData(data);
            simpleK.setDescription("");
            simpleK.notifyDataSetChanged();
            simpleK.invalidate();
            Log.i(TAG, "getAxisLeft: max:" + simpleK.getAxisLeft().getAxisMaxValue());
            Log.i(TAG, "getAxisLeft: min:" + simpleK.getAxisLeft().getAxisMinValue());
        }else {
            startReceiveMarkets();
        }
    }
    private void simpleKInit(CandleDataSet candleDataSet,DataK dataK) {
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
        xAxis.setValueFormatter(new DetailedKFragment.MyXAxisValueFormatter(dataK.prices));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴标签显示位置
        xAxis.setDrawGridLines(false);//绘制格网线
        YAxis leftAxis = simpleK.getAxisLeft();//取得左侧y轴
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);//y轴标签绘制的位置
        leftAxis.setDrawGridLines(true);//绘制y轴格网线
        leftAxis.setDrawLabels(true);//显示坐标轴上的值, ...其他样式
        leftAxis.setStartAtZero(false);
        leftAxis.setAxisMaxValue((float) (dataK.max*1.1));
        leftAxis.setAxisMinValue((float) (dataK.min*0.9));


        YAxis rightAxis = simpleK.getAxisRight();//取得左侧y轴
        rightAxis.setDrawGridLines(false);//不绘制y轴格网线
        rightAxis.setDrawLabels(false);//不显示坐标轴上的值, ...其他样式
    }
    class MyXAxisValueFormatter implements XAxisValueFormatter {
        private final List<MarketStat.HistoryPrice> prices;

        public MyXAxisValueFormatter(List<MarketStat.HistoryPrice> prices) {
            this.prices=prices;
        }

        @Override
        public String getXValue(String original, int index, ViewPortHandler viewPortHandler) {
            return df.format(prices.get(index).date);
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
            DataK dataK=new DataK();
            dataK.prices=stat.prices;
            String newKey=stat.prices.get(0).quote+"/"+stat.prices.get(0).base;
            List<CandleEntry> candleEntries=new ArrayList<>();
            float max=(float)stat.prices.get(0).high;
            float min=(float)stat.prices.get(0).low;
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
                candleEntries.add(entry);
            }

            dataK.max=max;
            dataK.min=min;
            dataK.candleEntries=candleEntries;
            if(BtslandApplication.dataKMap.get(newKey)!=null){
                BtslandApplication.dataKMap.remove(BtslandApplication.dataKMap.get(newKey));
            }
            BtslandApplication.dataKMap.put(newKey,dataK);

            Message message=Message.obtain();
            message.obj=new MarketTicker(stat.prices.get(0).base,stat.prices.get(0).quote);
            Log.e(TAG, "onMarketStatUpdate:sendMessage:newKey"+newKey);
            handler.sendMessage(message);
        }
    }
    public Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "handleMessage: Thread:"+Thread.currentThread().getName());
            market=(MarketTicker) msg.obj;
            drawK();
        }
    };
}
