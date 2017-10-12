package info.btsland.app.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Random;

import info.btsland.app.R;
import info.btsland.app.util.DensityUtil;


public class MarketSimpleKFragment extends Fragment {

    private TextView tvTitle;
    private LineChart simpleK;

    public MarketSimpleKFragment() {
        // Required empty public constructor
    }

    private void init(){
        tvTitle=getActivity().findViewById(R.id.tv_market_simple_title);
        simpleK=getActivity().findViewById(R.id.lc_market_simple_K);
        simpleK.setDescription("BTC:CNY");
        simpleK.setDescriptionTextSize(DensityUtil.dip2px(getActivity(),20f));
        ArrayList<Entry> valsComp1 = new ArrayList<Entry>();
        for(int i=0;i<15;i++){
            int max=100;
            int min=0;
            Random random = new Random();
            int s= random.nextInt(max)%(max-min+1) + min;
            Entry c1e = new Entry(s, i*4);
            valsComp1.add(c1e);
        }

        LineDataSet setComp1 = new LineDataSet(valsComp1, "最新成交价");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp1.setValueTextColor(R.color.color_dullRed1);
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(setComp1);

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i=0;i<60;i++){
            xVals.add(i+"");
        }
        LineData data = new LineData(xVals, dataSets);
        data.setDrawValues(false);
        simpleK.setData(data);
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
