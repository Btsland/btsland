package info.btsland.app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import info.btsland.app.BtslandApplication;
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
    public AssetSimpleCursorAdapter(Context context) {
        this.context=context;
        this.inflater=LayoutInflater.from(context);
    }

    public void setAssets(List<IAsset> assets) {
        this.assets = assets;
    }

    @Override
    public int getCount() {
        if(assets==null){
            return 0;
        }
        return assets.size();
    }

    @Override
    public Object getItem(int position) {
        if(assets==null){
            return null;
        }
        return assets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        public TextView tvAssetCoin;
        public TextView tvAssetNum;
        public TextView tvTransfer;
        public TextView tvTransaction;
        public TextView tvConvertNum;
        public TextView tvConvertCoin;
        public TextView tvUsableNum;
        public TextView tvOrdersNum;
        public TextView tvBorrowNum;
        public TextView tvBorrow;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(assets.size()>0&&position<assets.size()&&assets.get(position)==null){
            return convertView;
        }
        ViewHolder viewHolder=null;
        if(convertView==null) {
            convertView = inflater.inflate(R.layout.asset_item, null);
            viewHolder = new ViewHolder();

            viewHolder.tvAssetCoin = convertView.findViewById(R.id.tv_asset_coin);
            viewHolder.tvAssetNum = convertView.findViewById(R.id.tv_asset_num);
            viewHolder.tvTransfer = convertView.findViewById(R.id.tv_transfer);
            viewHolder.tvTransaction = convertView.findViewById(R.id.tv_transaction);
            viewHolder.tvConvertNum = convertView.findViewById(R.id.tv_convert_num);
            viewHolder.tvConvertCoin = convertView.findViewById(R.id.tv_convert_coin);
            viewHolder.tvUsableNum = convertView.findViewById(R.id.tv_usable_num);
            viewHolder.tvOrdersNum = convertView.findViewById(R.id.tv_orders_num);
            viewHolder.tvBorrowNum = convertView.findViewById(R.id.tv_borrow_num);
            viewHolder.tvBorrow = convertView.findViewById(R.id.tv_borrow);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        IAsset asset=assets.get(position);
        if(asset==null){
            return null;
        }
        viewHolder.tvTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transactionIntent=new Intent(context,TransferActivity.class);
                context.startActivity(transactionIntent);
            }
        });
        viewHolder.tvTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transactionIntent=new Intent(context,MarketDetailedActivity.class);
                context.startActivity(transactionIntent);
            }
        });
        if(!asset.coinName.equals("")){
            viewHolder.tvAssetCoin.setText(String.valueOf(asset.coinName));
        }else{
            viewHolder.tvAssetCoin.setText("");
        }
        if(asset.total!=null){
            viewHolder.tvAssetNum.setText(String.valueOf(asset.total));
        }else{
            viewHolder.tvAssetNum.setText("0.0");
        }
        if(!asset.coinName.equals("CNY")) {
            if (BtslandApplication.chargeUnit.equals("CNY")) {
                viewHolder.tvConvertNum.setText("≈"+String.valueOf(asset.totalCNY));
                viewHolder.tvConvertCoin.setText("CNY");
            } else if (BtslandApplication.chargeUnit.equals("BTS")) {
                viewHolder.tvConvertNum.setText("≈"+String.valueOf(asset.totalBTS));
                viewHolder.tvConvertCoin.setText("BTS");
            }
        }else {
            viewHolder.tvConvertNum.setText("");
            viewHolder.tvConvertCoin.setText("");
        }
        if(asset.coinName.equals("BTS")){
            viewHolder.tvBorrow.setText("抵押：");
            viewHolder.tvBorrowNum.setText(String.valueOf(asset.borrow));
        }else if(asset.borrow>0.0) {
            viewHolder.tvBorrow.setText("负债：");
            viewHolder.tvBorrowNum.setText(String.valueOf(asset.borrow));
        }else{
            viewHolder.tvBorrow.setText("");
            viewHolder.tvBorrowNum.setText("");
        }
        viewHolder.tvUsableNum.setText(String.valueOf(asset.usable));
        viewHolder.tvOrdersNum.setText(String.valueOf(asset.orders));
        return convertView;
    }
}

















