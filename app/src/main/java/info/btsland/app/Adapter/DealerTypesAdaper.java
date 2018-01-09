package info.btsland.app.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.R;
import info.btsland.exchange.entity.RealAsset;

/**
 * Created by Administrator on 2017/12/30 0030.
 */

public class DealerTypesAdaper extends PagerAdapter {
    private List<RealAsset> realAssets;
    private LayoutInflater inflater;
    private List<View> views=new ArrayList<>();
    private Context context;
    private OnItemOnClickListener clickListener;

    public void setClickListener(OnItemOnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public DealerTypesAdaper(Context context) {
        this.inflater=LayoutInflater.from(context);
    }

    public DealerTypesAdaper(Context context,List<RealAsset> realAssets) {
        this.context=context;
        this.realAssets = realAssets;
        this.inflater=LayoutInflater.from(context);
        fillIn();
    }

    public void setRealAssets(List<RealAsset> realAssets) {
        this.realAssets = realAssets;
        fillIn();
    }
    private void fillIn(){
        if(views==null){
            views=new ArrayList<>();
        }
        views.clear();
        for(int i=0;i<realAssets.size();i++){
            View view=inflater.inflate(R.layout.dealer_type_item,null);
            RealAsset realAsset=realAssets.get(i);
            final TextView tvNo=view.findViewById(R.id.tv_dealer_type_item_realNo);
            TextView tvType=view.findViewById(R.id.tv_dealer_type_item_realType);
            TextView tvName=view.findViewById(R.id.tv_dealer_type_item_name);
            tvNo.setText(realAsset.getRealAssetNo());
            tvType.setText(realAsset.getDepict());
            tvName.setText(realAsset.getName());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemOnClick(tvNo.getText().toString());
                }
            });
            views.add(view);
        }

    }
    @Override
    public int getCount() {
        if(realAssets!=null){
            return  realAssets.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if(position>=views.size()){
            return null;
        }else {
            container.addView(views.get(position));
            return views.get(position);
        }

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    public interface OnItemOnClickListener{
        void onItemOnClick(String no);
    }
}
