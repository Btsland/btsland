package info.btsland.app.ui.view;

/**
 * Created by Administrator on 2017/12/30 0030.
 */
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import info.btsland.app.R;
import info.btsland.exchange.entity.RealAsset;

;

public class HorizontalListViewAdapter extends BaseAdapter {

    private String TAG = "HorizontalListViewAdapter";
    private LayoutInflater inflater;
    private List<RealAsset> realAssets;


    public HorizontalListViewAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public HorizontalListViewAdapter(Context context,List<RealAsset> realAssets) {
        this.realAssets=realAssets;
        this.inflater = LayoutInflater.from(context);
    }

    public void setRealAssets(List<RealAsset> realAssets) {
        this.realAssets = realAssets;
        Log.e(TAG, "setRealAssets: "+realAssets.size() );
    }

    @Override
    public int getCount() {
        if(realAssets!=null){
            return realAssets.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if(realAssets!=null){
            return realAssets.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.dealer_type_item, null);
        }
        if(realAssets==null){
            return null;
        }
        RealAsset realAsset=realAssets.get(i);
        TextView tvNo=convertView.findViewById(R.id.tv_dealer_type_item_realNo);
        TextView tvType=convertView.findViewById(R.id.tv_dealer_type_item_realType);
        TextView tvName=convertView.findViewById(R.id.tv_dealer_type_item_name);
        tvNo.setText(realAsset.getRealAssetNo());
        int a=realAsset.getDepict().indexOf("(");
        String depict="未知";
        if(a!=-1){
            depict=realAsset.getDepict().substring(0,a);
        }else {
            depict=realAsset.getDepict();
        }
        tvType.setText(depict);
        tvName.setText(realAsset.getName());
        return convertView;
    }
}
