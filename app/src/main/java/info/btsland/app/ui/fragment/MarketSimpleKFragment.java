package info.btsland.app.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.btsland.app.R;
import info.btsland.app.model.Market;
import info.btsland.app.model.MarketTicker;
import info.btsland.app.service.Impl.MarketServiceImpl;
import info.btsland.app.service.MarketService;
import info.btsland.app.ui.activity.MarketDetailedActivity;


public class MarketSimpleKFragment extends Fragment {

    private TextView deal;
    private TextView high;
    private TextView low;
    private TextView count;
    private LineChart simpleK;

    private String leftCoin = "BTS";
    private String rightCoin = "btsCNY";


    private String highStr;
    private String lowStr;
    private String countStr;

    private Market freshMarket;

    public MarketSimpleKFragment() {
        // Required empty public constructor
    }

    public static MarketSimpleKFragment newInstance(Market market) {
        Log.i("newInstance", "newInstance: ");
        MarketSimpleKFragment simpleKFragment = new MarketSimpleKFragment();
        Bundle args = new Bundle();
        args.putSerializable("market", market);
        simpleKFragment.setArguments(args);
        return simpleKFragment;
    }

    private void init(View view) {
        Log.i("init", "init: ");
        deal = view.findViewById(R.id.tv_market_simple_deal);
        high = view.findViewById(R.id.tv_market_simple_high);
        low = view.findViewById(R.id.tv_market_simple_low);
        count = view.findViewById(R.id.tv_market_simple_count);
        simpleK = view.findViewById(R.id.lc_market_simple_K);
        Log.i("init", "init: leftCoin+\":\"+rightCoin" + leftCoin + ":" + rightCoin);
        deal.setText(leftCoin + ":" + rightCoin);
        startReceiveMarkets(null);
//        simpleK.setDescription("BTC:CNY");
//        simpleK.setDescriptionTextSize(DensityUtil.dip2px(getActivity(),20f));
//        simpleK.setDrawGridBackground(false);
//        XAxis xAxis=simpleK.getXAxis();
//        xAxis.setDrawAxisLine(true);
//        xAxis.setSpaceBetweenLabels(-4);
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        List<String> values=new ArrayList<String>();
//        values.add("11：00");
//        values.add("13：00");
//        values.add("15：00");
//        values.add("17：00");
//        xAxis.setValueFormatter(new MyXAxisValueFormatter(0f,59f,values));
//        ArrayList<Entry> valsComp1 = new ArrayList<Entry>();
//        for(int i=0;i<14;i++){
//            int max=100;
//            int min=0;
//            Random random = new Random();
//            int s= random.nextInt(max)%(max-min+1) + min;
//            Entry c1e = new Entry(s, i*5);
//            valsComp1.add(c1e);
//        }
//
//        LineDataSet setComp1 = new LineDataSet(valsComp1, "最新成交价");
//        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
//        setComp1.setColor(Color.RED);
//        setComp1.setCircleSize(0f);//设置焦点圆心的大小
//        setComp1.setLineWidth(1f);
//        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
//        dataSets.add(setComp1);
//
//        ArrayList<String> xVals = new ArrayList<String>();
//        for (int i=0;i<72;i++){
//            xVals.add(i+"");
//        }
//        LineData data = new LineData(xVals,dataSets);
//        data.setDrawValues(false);
//        simpleK.setData(data);
//        //simpleK.setVisibleXRangeMaximum(30f);
//        simpleK.invalidate(); // refresh

    }

    class MyXAxisValueFormatter implements XAxisValueFormatter {
        List<String> mValues;
        float start;
        float to;
        float length;
        float step;
        Map<String, String> tags;

        /**
         * X轴标签
         *
         * @param start  起始
         * @param to     结束
         * @param values 需要设置的标签
         */
        public MyXAxisValueFormatter(float start, float to, List<String> values) {
            this.mValues = values;
            this.start = start;
            this.to = to;
            if (start < to) {
                this.length = to - start + 1;
            }
            Log.i("length", "MyXAxisValueFormatter+length: " + length);
            step = length / values.size();
            Log.i("step", "MyXAxisValueFormatter+step: " + step);
            tags = new HashMap<String, String>();
            for (int i = 0; i < values.size(); i++) {
                String value = values.get(i);
                int index = (int) (i * step);
                Log.i("index", "MyXAxisValueFormatter+index: " + index);
                tags.put(String.valueOf(index), value);
            }
        }

        @Override
        public String getXValue(String original, int index, ViewPortHandler viewPortHandler) {
            Log.i("index", "getXValue+index: " + index);
            String XValue = "";
            if (index == 30) {
                XValue = "中间";
            }
            if (tags.get("" + index) != null) {
                XValue = tags.get("" + index);
            }
//            for (int i=0;i<tags.size();i++ ) {
//                Map<String,String> tag= tags.get(i);
//                XValue = tag.get(String.valueOf(index));
//            }
            return XValue;
        }
    }

    public void startReceiveMarkets(MarketTicker market) {
        if (market != null) {
            //Log.i("startReceiveMarkets", "startReceiveMarkets: market1:" + market.getLeftCoin() + ":" + market.getRightCoin());
            if (market.base == rightCoin && market.quote == leftCoin) {
                Log.i("startReceiveMarkets", "startReceiveMarkets: 111111111111");
                Intent intent = new Intent(getActivity(), MarketDetailedActivity.class);
                intent.putExtra("market", market);
                getActivity().startActivity(intent);
                return;
            }
        }
        ReceiveMarkets receiveMarkets = new ReceiveMarkets(market);
        receiveMarkets.start();
    }

    public class ReceiveMarkets extends Thread {
        private String left;
        private String right;

        public ReceiveMarkets(MarketTicker market) {
            Log.i("ReceiveMarkets", "ReceiveMarkets: ");
            if (market != null) {
                left = market.quote;
                right = market.base;
            } else {
                left = leftCoin;
                right = rightCoin;
            }
        }

        @Override
        public void run() {
            Log.i("run", "run: ");
            Log.i("run", "run: Left:" + left + "+right:" + right);
            MarketService marketService = new MarketServiceImpl();
            List<Market> markets = marketService.queryMarkets(left, right, "");
            Log.i("run", "run: markets.size():" + markets.size());
            Message message = Message.obtain();
            message.what = 1;
            message.obj = markets;
            handler.sendMessage(message);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            List<Market> markets = (List<Market>) msg.obj;
            draw(markets);
            leftCoin = markets.get(0).getLeftCoin();
            rightCoin = markets.get(0).getRightCoin();
            deal.setText(markets.get(0).getLeftCoin() + ":" + markets.get(0).getRightCoin());
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

    private void draw(final List<Market> markets) {
        ArrayList<Entry> entries = toEntry(markets);
        LineDataSet setComp1 = new LineDataSet(entries, "最新成交价");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp1.setColor(Color.RED);
        setComp1.setCircleSize(0f);//设置焦点圆心的大小
        setComp1.setLineWidth(1f);
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(setComp1);
        ArrayList<String> xVals = new ArrayList<String>();
        if (entries == null) {
            return;
        }
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式
        for (int i = 0; i < entries.size(); i++) {
            xVals.add(df.format(markets.get(i).getDate()));
        }
        LineData data = new LineData(xVals, dataSets);
        data.setDrawValues(false);
        XAxis xAxis = simpleK.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        //simpleK.setTouchEnabled(false);
//        simpleK.setDragEnabled(false);
//        simpleK.setScaleEnabled(false);
//        simpleK.setScaleXEnabled(false);
//        simpleK.setScaleYEnabled(false);
        simpleK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MarketDetailedActivity.class);
                intent.putExtra("market", markets.get(0));
                getActivity().startActivity(intent);
            }
        });
        simpleK.setData(data);
        simpleK.setDescription(markets.get(0).getLeftCoin() + ":" + markets.get(0).getRightCoin());
        simpleK.setDescriptionTextSize(14);
        simpleK.setVisibleXRangeMaximum(entries.size());
        simpleK.invalidate(); // refresh
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
            if (getArguments().getSerializable("market") != null) {
                Market market = (Market) getArguments().getSerializable("market");
                leftCoin = market.getLeftCoin();
                rightCoin = market.getRightCoin();
            }
        }
        init(view);
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
