package info.btsland.app.Adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.model.IAsset;
import info.btsland.exchange.entity.RealAsset;

/**
 * Created by Administrator on 2017/10/16.
 */

public class AccountTypesAdapter extends BaseAdapter {

    private static final String TAG = "AccuntTypesAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<RealAsset> realAssets;

    private OnCancelListener onCancelListener ;
    private OnItemClickListener onItemClickListener;

    public void setAsset(List<RealAsset> realAssets) {
        this.realAssets = realAssets;
    }

    public AccountTypesAdapter setOnCancelListener(OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
        return this;
    }

    public AccountTypesAdapter setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    public AccountTypesAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(realAssets!=null){
            return realAssets.size();
        }else {
            return 0;
        }

    }

    @Override
    public Object getItem(int i) {
        return realAssets.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.account_types_item, null);
        }
        if(realAssets==null){
            return null;
        }
        final RealAsset realAsset=realAssets.get(i);
        ConstraintLayout layout=convertView.findViewById(R.id.cl_account_types_item_back);
        TextView tvName=convertView.findViewById(R.id.tv_account_types_item_name);
        TextView tvNo=convertView.findViewById(R.id.tv_account_types_item_no);
        TextView tvType=convertView.findViewById(R.id.tv_account_types_item_type);
        TextView tvCancel=convertView.findViewById(R.id.tv_account_types_item_cancel);
        tvName.setText(realAsset.getName());
        tvNo.setText(realAsset.getRealAssetNo());
        switch (realAsset.getRealAssetType()){
            case "1":
                tvType.setBackground(BtslandApplication.getInstance().getDrawable(R.drawable.zfb));
                break;
            case "2":
                tvType.setBackground(BtslandApplication.getInstance().getDrawable(R.drawable.wx));
                break;
            case "3":
                tvType.setBackground(BtslandApplication.getInstance().getDrawable(R.drawable.yh));
                break;
        }
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelListener.onCancel(realAssets,i);
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(realAssets,i);
            }
        });
        return convertView;
    }
    public interface OnCancelListener{
        void onCancel(List<RealAsset> realAssets,int i);
    }
    public interface OnItemClickListener{
        void onItemClick(List<RealAsset> realAssets,int i);
    }
}
