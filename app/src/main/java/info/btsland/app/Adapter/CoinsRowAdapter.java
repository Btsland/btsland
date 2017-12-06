package info.btsland.app.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
    private OnItemClickListener listener;

    private int type=1;

    public void setCoins(List<String> coins) {
        this.coins = coins;
        this.notifyDataSetChanged();
    }
    public CoinsRowAdapter setListener(OnItemClickListener listener) {
        this.listener = listener;
        return this;
    }

    public CoinsRowAdapter(Context context,int type) {
        this.context = context;
        this.type=type;
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
        TextView tvCancel=convertView.findViewById(R.id.tv_coin_cancel);
        Drawable drawable=null;
        if(type==1){
            drawable=context.getDrawable(R.drawable.image_add_coin_row);
        }else if(type==-1) {
            drawable=context.getDrawable(R.drawable.image_remove_coin_row);
        }
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        tvCancel.setCompoundDrawables(null,null,drawable,null);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(coins.get(i), i);
            }
        });

        return convertView;
    }
    public interface OnItemClickListener{
        void onClick(String coin,int index);
    }

}
