package info.btsland.app.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.text.Html;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.model.MarketTicker;
import info.btsland.app.ui.fragment.MarketSimpleKFragment;
import info.btsland.app.util.NumericUtil;

/**
 * Created by Administrator on 2017/10/16.
 */

public class MarketRowAdapter extends BaseAdapter {

    private static final String TAG = "MarketRowAdapter";

    private MarketSimpleKFragment simpleKFragment;
    private List<MarketTicker> markets=new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;
    private MarketItemClickListener itemClickListener;
    private int selectorItem=-1;

    public void setMarkets(List<MarketTicker> markets) {
        this.markets = markets;
        this.notifyDataSetChanged();
    }
    public void setMarketItemClickListener(MarketItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }
    public void setSelectorItem(int selectorItem){
        this.selectorItem=selectorItem;
        this.notifyDataSetChanged();
    }
    public MarketRowAdapter(MarketSimpleKFragment simpleKFragment, Context context) {
        this.simpleKFragment = simpleKFragment;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return markets.size();
    }

    @Override
    public Object getItem(int i) {
        if(i<markets.size()) {
            return markets.get(i);
        }else {
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.market_item, null);
        }

        if(markets==null){
            return null;
        }
        //Log.i(TAG, "getView: "+market+"i:"+i);
        TextView tvCoin = convertView.findViewById(R.id.tv_coin);
        TextView tvFluctuation = convertView.findViewById(R.id.tv_fluctuation);
        TextView tvNewPrice = convertView.findViewById(R.id.tv_newPrice);
        TextView tvBestAskNum = convertView.findViewById(R.id.tv_bestAskNum);
        TextView tvBestBidNum = convertView.findViewById(R.id.tv_bestBidNum);
        ConstraintLayout clBack=convertView.findViewById(R.id.cl_back);
        TextView tvAdd=convertView.findViewById(R.id.tv_add);
        TextView tvVol=convertView.findViewById(R.id.tv_volNum);

        if(i>=markets.size()){
            tvAdd.setVisibility(View.VISIBLE);
            clBack.setVisibility(View.GONE);
            convertView.setMinimumHeight(40);
        }else {
            MarketTicker market = markets.get(i);
            if (market.quote == null) {
                tvCoin.setText("");
            }
            tvCoin.setText(market.quote);
            DecimalFormat dfFluc = new DecimalFormat();
            dfFluc.applyPattern("0.00");
            String fluctuation = String.valueOf(dfFluc.format(market.percent_change)) + "%";
            //Log.e("getView", "fluctuation: "+ fluctuation);
            tvFluctuation.setText(fluctuation);
            tvVol.setText(Html.fromHtml(NumericUtil.doubleToString(market.base_volume)));
            if (market.latest == null) {
                tvNewPrice.setText("");
            } else if (market.latest.length() < 9) {
                tvNewPrice.setText(market.latest);
            } else {
                tvNewPrice.setText(market.latest.substring(0, 8));
            }
            if (market.lowest_ask == null) {
                tvBestAskNum.setText("");
            } else if (market.lowest_ask.length() < 9) {
                tvBestAskNum.setText(market.lowest_ask);
            } else {
                tvBestAskNum.setText(market.lowest_ask.substring(0, 8));
            }
            if (market.highest_bid == null) {
                tvBestBidNum.setText("");
            } else if (market.highest_bid.length() < 9) {
                tvBestBidNum.setText(market.highest_bid);
            } else {
                tvBestBidNum.setText(market.highest_bid.substring(0, 8));
            }

            if (market.percent_change > 0) {

                tvFluctuation.setTextColor(BtslandApplication.goUp);
                tvNewPrice.setTextColor(BtslandApplication.goUp);
            } else if (market.percent_change < 0) {
                tvFluctuation.setTextColor(BtslandApplication.goDown);
                tvNewPrice.setTextColor(BtslandApplication.goDown);
            } else {
                tvFluctuation.setTextColor(BtslandApplication.suspend);
                tvNewPrice.setTextColor(BtslandApplication.suspend);
            }
            if (selectorItem == i) {
                //设置选定样式
            }
            tvAdd.setVisibility(View.GONE);
            clBack.setVisibility(View.VISIBLE);
        }
        rowOnClickListener clickListener=new rowOnClickListener(i);
        convertView.setOnClickListener(clickListener);
        return convertView;

    }
    long mLastTime=0;
    long mCurTime=0;
    private class rowOnClickListener implements View.OnClickListener{
        private int index;

        public rowOnClickListener(int index) {
            this.index=index;
        }

        @Override
        public void onClick(View v) {
            Message message=Message.obtain();
            if(index<markets.size()){
                mLastTime=mCurTime;
                mCurTime= System.currentTimeMillis();
                if(mCurTime-mLastTime<300){//双击事件
                    mCurTime =0;
                    mLastTime = 0;
                    message.what=2;
                    message.obj=index;
                    handler.sendMessage(message);
                }else{//单击事件
                    Toast.makeText(context,"双击可进入交易详情界面",Toast.LENGTH_SHORT).show();
                    message.what=1;
                    message.obj=index;
                    handler.sendMessage(message);
                }
            }
        }
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int i= (int) msg.obj;
            MarketTicker market = null;
            if(i<markets.size()){
                market = markets.get(i);
            }
            switch (msg.what) {
                case 1:
                    itemClickListener.onClick(i,market);
                    break;
                case 2:
                    itemClickListener.dblClick(i,market);
                    break;
            }
        }
    };

    public interface MarketItemClickListener{
        void dblClick(int i,MarketTicker market);
        void onClick(int i,MarketTicker market);
    }
}
