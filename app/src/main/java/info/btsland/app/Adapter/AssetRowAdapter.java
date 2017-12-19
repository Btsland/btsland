package info.btsland.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import info.btsland.app.R;
import info.btsland.app.api.asset;
import info.btsland.app.model.IAsset;

/**
 * Created by Administrator on 2017/10/16.
 */

public class AssetRowAdapter extends BaseAdapter {

    private static final String TAG = "AssetRowAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<IAsset> assets=new ArrayList<>();

    public void setAsset(List<IAsset> assets) {
        this.assets = assets;
        this.notifyDataSetChanged();
    }

    public AssetRowAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return assets.size();
    }

    @Override
    public Object getItem(int i) {
        return assets.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.asset_row_item, null);
        }
        if(assets==null){
            return null;
        }
        IAsset asset=assets.get(i);
        TextView tvName=convertView.findViewById(R.id.tv_coin_name);
        tvName.setText(asset.coinName);
        TextView tvNum=convertView.findViewById(R.id.tv_coin_num);
        tvNum.setText(String.valueOf(asset.total));
        TextView tvtotal=convertView.findViewById(R.id.tv_coin_total);
        BigDecimal b =new BigDecimal(asset.totalCNY);
        double f1 = b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        tvtotal.setText("≈"+String.valueOf(f1));
        return convertView;
    }

}
