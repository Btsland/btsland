package info.btsland.app.ui.fragment;


import android.content.Context;
import android.graphics.Color;
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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieRadarChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultXAxisValueFormatter;
import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import info.btsland.app.R;
import info.btsland.app.model.Market;
import info.btsland.app.util.DensityUtil;


public class MarketSimpleKFragment extends Fragment {

    private TextView deal;
    private TextView high;
    private TextView low;
    private TextView count;
    private LineChart simpleK;

    public String dealStr;
    private String highStr;
    private String lowStr;
    private String countStr;

    private Market freshMarket;

    public MarketSimpleKFragment() {
        // Required empty public constructor
    }

    private void init(){
        deal=getActivity().findViewById(R.id.tv_market_simple_deal);
        high=getActivity().findViewById(R.id.tv_market_simple_high);
        low=getActivity().findViewById(R.id.tv_market_simple_low);
        count=getActivity().findViewById(R.id.tv_market_simple_count);
        simpleK=getActivity().findViewById(R.id.lc_market_simple_K);
        deal.setText(dealStr);
        ReceiveMarkets receiveMarkets =new ReceiveMarkets();
        receiveMarkets.start();
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
    class MyXAxisValueFormatter implements XAxisValueFormatter{
        List<String> mValues;
        float start;
        float to;
        float length;
        float step;
        Map<String,String>tags;

        /**
         * X轴标签
         * @param start 起始
         * @param to 结束
         * @param values 需要设置的标签
         */
        public MyXAxisValueFormatter(float start,float to,List<String> values) {
            this.mValues = values;
            this.start=start;
            this.to=to;
            if (start<to){
                this.length=to-start+1;
            }
            Log.i("length", "MyXAxisValueFormatter+length: "+length);
            step=length/values.size();
            Log.i("step", "MyXAxisValueFormatter+step: "+step);
            tags=new HashMap<String, String>();
            for (int i=0;i<values.size();i++ ) {
                String value=values.get(i);
                int index= (int) (i*step);
                Log.i("index", "MyXAxisValueFormatter+index: "+index);
                tags.put(String.valueOf(index),value);
            }
        }
        @Override
        public String getXValue(String original, int index, ViewPortHandler viewPortHandler) {
            Log.i("index", "getXValue+index: "+index);
            String XValue="";
            if(index==30){
                XValue="中间";
            }
            if(tags.get("" + index)!=null) {
                XValue = tags.get("" + index);
            }
//            for (int i=0;i<tags.size();i++ ) {
//                Map<String,String> tag= tags.get(i);
//                XValue = tag.get(String.valueOf(index));
//            }
            return XValue;
        }
    }
    private class ReceiveMarkets extends Thread{
        @Override
        public void run() {
            List<Market> markets=new ArrayList<Market>();
            markets.add(new Market());
            Message message=Message.obtain();
            message.what=1;
            message.obj=markets;
            handler.sendMessage(message);
        }
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            List<Market> markets= (List<Market>) msg.obj;
            ArrayList<Entry> Entrys = toEntry(markets);
            draw(Entrys);
        }
    };
    private ArrayList<Entry> toEntry(List<Market> markets) {
        if(markets==null||markets.size()==0){
            return null;
        }
        ArrayList<Entry> Entrys = new ArrayList<Entry>();
        float high=0;
        float low=0;
        float count;
        for(int i=0;i<markets.size();i++){
            Market market=markets.get(i);
            if (i==0) {
            }
            //将market生成entry对象
            //Entry entry=new Entry(market.getNewPrice(),index);
            //Entrys.add(entry);
        }
        return Entrys;
    }
    private void draw(ArrayList<Entry> Entrys ){
        LineDataSet setComp1 = new LineDataSet(Entrys, "最新成交价");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp1.setColor(Color.RED);
        setComp1.setCircleSize(0f);//设置焦点圆心的大小
        setComp1.setLineWidth(1f);
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(setComp1);
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i=0;i<Entrys.size();i++){
            xVals.add(i+"");
        }
        LineData data = new LineData(xVals,dataSets);
        data.setDrawValues(false);
        simpleK.setData(data);
        simpleK.setVisibleXRangeMaximum(30f);
        simpleK.invalidate(); // refresh
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_market_simple_k, container, false);
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
