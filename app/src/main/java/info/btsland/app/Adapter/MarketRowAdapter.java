package info.btsland.app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.CombinedChart;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.model.Market;
import info.btsland.app.model.MarketTicker;
import info.btsland.app.ui.activity.MarketDetailedActivity;
import info.btsland.app.ui.fragment.MarketSimpleKFragment;

/**
 * Created by Administrator on 2017/10/16.
 */

public class MarketRowAdapter extends BaseAdapter {

    private static final String TAG = "MarketRowAdapter";

    private MarketSimpleKFragment simpleKFragment;
    private Map<String,MarketTicker> markets;
    private LayoutInflater inflater;
    private Context context;
    private String[] quotes;

    public MarketRowAdapter(MarketSimpleKFragment simpleKFragment, Context context,String[] quotes, Map<String,MarketTicker> markets) {
        this.simpleKFragment = simpleKFragment;
        this.quotes=quotes;
        this.markets = markets;
        Log.e("marketAdaper", "MarketRowAdapter:  "+markets.size());
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return markets.size();
    }

    @Override
    public Object getItem(int i) {
        return markets.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        Log.i(TAG, "getView: i:"+i+",marketIsNull:"+String.valueOf(markets.get(quotes[i])==null)+",markets.size():"+markets.size()+",markets.keySet():"+markets.keySet().toArray()[i]);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.market_row, null);
        }
        if(markets.get(quotes[i])==null){
            return convertView;
        }
        MarketTicker market = markets.get(quotes[i]);
        TextView tvCoin = convertView.findViewById(R.id.tv_coin);
        TextView tvFluctuation = convertView.findViewById(R.id.tv_fluctuation);
        TextView tvNewPrice = convertView.findViewById(R.id.tv_newPrice);
        TextView tvBestAskNum = convertView.findViewById(R.id.tv_bestAskNum);
        TextView tvBestBidNum = convertView.findViewById(R.id.tv_bestBidNum);
        tvCoin.setText(market.quote);
        DecimalFormat dfFluc = new DecimalFormat();
        dfFluc.applyPattern("0.00");
        String fluctuation= String.valueOf(dfFluc.format(market.percent_change))+"%";
        Log.e("getView", "fluctuation: "+ fluctuation);
        tvFluctuation.setText(fluctuation);
        tvNewPrice.setText(market.latest.substring(0,8));
        tvBestAskNum.setText(market.lowest_ask.substring(0,8));
        tvBestBidNum.setText(market.lowest_ask.substring(0,8));
        if (market.percent_change > 0) {

            tvFluctuation.setTextColor(context.getResources().getColor(R.color.color_green));
            tvNewPrice.setTextColor(context.getResources().getColor(R.color.color_green));
        } else if(market.percent_change < 0) {
            tvFluctuation.setTextColor(context.getResources().getColor(R.color.color_font_red));
            tvNewPrice.setTextColor(context.getResources().getColor(R.color.color_font_red));
        }else {
            tvFluctuation.setTextColor(context.getResources().getColor(R.color.color_font_blue));
            tvNewPrice.setTextColor(context.getResources().getColor(R.color.color_font_blue));
        }
        rowOnClickListener clickListener=new rowOnClickListener(market);
        convertView.setOnClickListener(clickListener);
        return convertView;

    }
    private class rowOnClickListener implements View.OnClickListener{
        private MarketTicker market;
        long mLastTime=0;
        long mCurTime=0;
        public rowOnClickListener(MarketTicker market) {
            this.market=market;
        }

        @Override
        public void onClick(View v) {
            Message message=Message.obtain();
            mLastTime=mCurTime;
            mCurTime= System.currentTimeMillis();
            if(mCurTime-mLastTime<300){//双击事件
                mCurTime =0;
                mLastTime = 0;
                Bundle bundle=new Bundle();
                bundle.putInt("result",2);
                bundle.putSerializable("MarketTicker",market);
                Log.i(TAG, "onClick: market:"+market);
                message.setData(bundle);
                handler.sendMessage(message);
            }else{//单击事件
                Bundle bundle=new Bundle();
                bundle.putInt("result",1);
                bundle.putSerializable("MarketTicker",market);
                Log.i(TAG, "onClick: market:"+market);
                message.setData(bundle);
                handler.sendMessage(message);
            }

        }
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle=msg.getData();
            MarketTicker market = (MarketTicker) bundle.getSerializable("MarketTicker");
            switch (bundle.getInt("result")) {
                case 1:
                    Toast.makeText(context,"双击可以进入详细页面哦！",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "handleMessage: market:"+market);
                    String key=market.quote+"/"+market.base;
                    simpleKFragment.key = key;
                    simpleKFragment.drawK(market);
                    break;
                case 2:
                    Log.i(TAG, "handleMessage: market:"+market);
                    Toast.makeText(context,"双击事件！",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MarketDetailedActivity.class);
                    intent.putExtra("MarketTicker", market);
                    context.startActivity(intent);
                    break;
            }
        }
    };
}
