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

import java.text.DecimalFormat;
import java.util.List;

import info.btsland.app.R;
import info.btsland.app.model.MarketTicker;
import info.btsland.app.ui.activity.MarketDetailedActivity;
import info.btsland.app.ui.fragment.MarketSimpleKFragment;

/**
 * Created by Administrator on 2017/10/16.
 */

public class MarketRowAdapter extends BaseAdapter {

    long mLastTime=0;
    long mCurTime=0;
    private MarketSimpleKFragment simpleKFragment;
    private List<MarketTicker> markets;
    private LayoutInflater inflater;
    private Context context;

    public MarketRowAdapter(MarketSimpleKFragment simpleKFragment, Context context, List<MarketTicker> markets) {
        this.simpleKFragment = simpleKFragment;
        this.markets = markets;
        Log.e("marketAdaper", "MarketRowAdapter:  "+markets.size());
        for(int i=0;i<markets.size();i++){
            Log.e("marketAdaper", "MarketRowAdapter:  "+markets.get(i).toString());
        }
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
        final MarketTicker market = markets.get(i);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.market_row, null);

        }
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

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message=Message.obtain();
                Log.i("sadasdadada", "onClick: i:"+i);
                mLastTime=mCurTime;
                mCurTime= System.currentTimeMillis();
                if(mCurTime-mLastTime<300){//双击事件
                    mCurTime =0;
                    mLastTime = 0;
                    Bundle bundle=new Bundle();
                    bundle.putInt("result",2);
                    bundle.putSerializable("MarketTicker",markets.get(i));
                    message.setData(bundle);
                    handler.sendMessage(message);
                }else{//单击事件
                    Bundle bundle=new Bundle();
                    bundle.putInt("result",1);
                    bundle.putSerializable("MarketTicker",markets.get(i));
                    message.setData(bundle);
                    handler.sendMessage(message);
                }

            }
        });
        return convertView;

    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle=msg.getData();
            MarketTicker market = (MarketTicker) bundle.getSerializable("MarketTicker");
            switch (bundle.getInt("result")) {
                case 1:
                    Toast.makeText(context,"双击可以进入详细页面哦！",Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(context,"双击事件！",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, MarketDetailedActivity.class);
                    intent.putExtra("MarketTicker", market);
                    context.startActivity(intent);
                    break;
            }
        }
    };
}
