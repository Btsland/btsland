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

    public DealerTypesAdaper(Context context) {
        this.inflater=LayoutInflater.from(context);
    }

    public DealerTypesAdaper(Context context,List<RealAsset> realAssets) {
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
            TextView tvNo=view.findViewById(R.id.tv_dealer_type_item_realNo);
            TextView tvType=view.findViewById(R.id.tv_dealer_type_item_realType);
            TextView tvName=view.findViewById(R.id.tv_dealer_type_item_name);
            tvNo.setText(realAsset.getRealAssetNo());
            int a=realAsset.getDepict().indexOf("(");
            String depict="";
            if(a!=-1){
                depict=realAsset.getDepict().substring(0,a);
            }else {
                depict=realAsset.getDepict();
            }
            tvType.setText(depict);
            tvName.setText(realAsset.getName());
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
}
