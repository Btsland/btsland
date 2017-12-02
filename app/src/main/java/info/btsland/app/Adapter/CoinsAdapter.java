package info.btsland.app.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
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
import info.btsland.app.ui.activity.MarketDetailedActivity;
import info.btsland.app.ui.fragment.MarketSimpleKFragment;

/**
 * Created by Administrator on 2017/10/16.
 */

public class CoinsAdapter extends BaseAdapter {

    private static final String TAG = "CoinsAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<String> coins=new ArrayList<>();
    private int selectorItem=-1;

    public void setCoins(List<String> coins) {
        this.coins = coins;
        this.notifyDataSetChanged();
    }

    public CoinsAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return coins.size()+1;
    }

    @Override
    public Object getItem(int i) {
        if(i<coins.size()) {
            return coins.get(i);
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
            convertView = inflater.inflate(R.layout.coin_base_item, null);
        }else {
            convertView.setBackground(context.getDrawable(R.drawable.tv_market_left_coin));
        }
        if(coins==null){
            return null;
        }
        //Log.i(TAG, "getView: "+market+"i:"+i);
        TextView tvCoin = convertView.findViewById(R.id.tv_market_coin);
        TextView tvBack = convertView.findViewById(R.id.tv_coin_back);
        if(i>=coins.size()){
            tvBack.setVisibility(View.VISIBLE);
            tvCoin.setVisibility(View.GONE);
            tvBack.setBackground(context.getDrawable(R.drawable.image_add));
        }else {
            if (i==selectorItem){
                convertView.setBackground(context.getDrawable(R.drawable.tv_market_left_coin_touch));
            }
            tvBack.setVisibility(View.GONE);
            tvCoin.setVisibility(View.VISIBLE);
            String coin = coins.get(i);
            tvCoin.setText(coin);
        }
        return convertView;
    }

    public void setSelectorItem(int i) {
        this.selectorItem=i;
        this.notifyDataSetChanged();
    }
}
