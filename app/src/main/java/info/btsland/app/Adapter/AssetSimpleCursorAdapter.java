package info.btsland.app.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import info.btsland.app.R;
import info.btsland.app.api.asset;

/**
 * author：lw1000
 * function：获取资产信息适配器
 * 2017/10/21.
 */

public class AssetSimpleCursorAdapter extends BaseAdapter {
    private static final String TAG="AssetSimpleCursorAdapter";
    private  Context context;
    private  List<asset> assets;
    private LayoutInflater inflater;
    public AssetSimpleCursorAdapter(Context context, List<asset> assets) {
        this.context=context;
        this.assets=assets;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return assets.size();
    }

    @Override
    public Object getItem(int position) {
        return assets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e("AssetSimple", "getView: "+position+"//"+assets.size());
        Log.e("AssetSimple", "getView: "+assets.get(position) );
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.asset_item, null);
        }
        if(assets.get(position)==null){
            return convertView;
        }
        TextView tvAssetCoin=convertView.findViewById(R.id.tv_asset_coin);
        TextView tvAssetNum=convertView.findViewById(R.id.tv_asset_num);
        tvAssetCoin.setText(String.valueOf(assets.get(position).asset_id));
        tvAssetNum.setText(String.valueOf(assets.get(position).amount));
        return convertView;
    }
}

















