package info.btsland.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.R;

/**
 * Created by Administrator on 2017/10/16.
 */

public class CoinsRowAdapter extends BaseAdapter {

    private static final String TAG = "CoinsRowAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<String> coins=new ArrayList<>();
    private int selectorItem=-1;

    public void setCoins(List<String> coins) {
        this.coins = coins;
        this.notifyDataSetChanged();
    }

    public CoinsRowAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return coins.size();
    }

    @Override
    public Object getItem(int i) {
        return coins.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.coin_row_item, null);
        }
        if(coins==null){
            return null;
        }
        TextView textView=convertView.findViewById(R.id.tv_coin_row);
        textView.setText(coins.get(i));
        return convertView;
    }

}
