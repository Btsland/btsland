package info.btsland.app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import info.btsland.app.R;
import info.btsland.app.model.IAsset;
import info.btsland.app.ui.activity.MarketDetailedActivity;
import info.btsland.app.ui.activity.TransferActivity;

/**
 * author：lw1000
 * function：获取资产信息适配器
 * 2017/10/21.
 */

public class AssetSimpleCursorAdapter extends BaseAdapter {
    private static final String TAG="AssetSimpleCursorAdapter";
    private  Context context;
    private  List<IAsset> assets;
    private LayoutInflater inflater;
    public AssetSimpleCursorAdapter(Context context, List<IAsset> assets) {
        this.context=context;
        this.assets=assets;
        this.inflater=LayoutInflater.from(context);
    }

    public void setAssets(List<IAsset> assets) {
        this.assets = assets;
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
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.asset_item, null);
        }
        if(assets.size()>0&&position<assets.size()&&assets.get(position)==null){
            return convertView;
        }
        TextView tvAssetCoin=convertView.findViewById(R.id.tv_asset_coin);
        TextView tvAssetNum=convertView.findViewById(R.id.tv_asset_num);
        TextView tvTransfer=convertView.findViewById(R.id.tv_transfer);
        TextView tvTransaction=convertView.findViewById(R.id.tv_transaction);

        tvTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transactionIntent=new Intent(context,TransferActivity.class);
                context.startActivity(transactionIntent);

            }
        });

        tvTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transactionIntent=new Intent(context,MarketDetailedActivity.class);
                context.startActivity(transactionIntent);
            }
        });


        if(assets.size()>0&&assets.get(position).coinName!=null&&!assets.get(position).coinName.equals("")){
            tvAssetCoin.setText(String.valueOf(assets.get(position).coinName));
        }else{
            tvAssetCoin.setText("");
        }
        if(assets.size()>0&&assets.get(position).total!=null){
            tvAssetNum.setText(String.valueOf(assets.get(position).total));
        }else{
            tvAssetNum.setText("0.0");
        }

        return convertView;
    }
}

















