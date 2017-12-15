package info.btsland.app.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.exchange.entity.RealAsset;
import info.btsland.exchange.entity.User;
import info.btsland.exchange.utils.UserStatUtil;


/**
 * Created by Administrator on 2017/12/13.
 */

public class DealerListAdapter extends BaseAdapter {

    private List<DealerData> dataList;
    private Context context;
    private LayoutInflater inflater;

    private List<View> views=new ArrayList<>();
    private String TAG="DealerListAdapter";

    public DealerListAdapter(Context context) {
        this.context=context;
        this.inflater=LayoutInflater.from(context);
    }

    public void setDataList(List<DealerData> dataList) {
        Log.e(TAG, "setDataList: " );
        this.dataList = dataList;
        for(int i=0;i<dataList.size();i++){
            fillInViews(i);
            queryTotal(i);
        }
    }

    private void fillInViews(int i){
        View view=inflater.inflate(R.layout.c2c_item, null);
        if(dataList.get(i)==null){
            return ;
        }
        DealerData dealerData=dataList.get(i);
        TextView tvName=view.findViewById(R.id.tv_c2c_item_name);//昵称
        TextView tvDepict=view.findViewById(R.id.tv_c2c_item_depict);//描述
        TextView tvBrokerage=view.findViewById(R.id.tv_c2c_item_brokerage_num);//手续费
        TextView tvAccountNo=view.findViewById(R.id.tv_c2c_item_account_no);//状态
        TextView tvContact=view.findViewById(R.id.tv_c2c_item_contact);//联系
        TextView tvMax=view.findViewById(R.id.tv_c2c_item_max_num);//最大额度
        TextView tvUsable=view.findViewById(R.id.tv_c2c_item_usable_num);//可用
        TextView tvHaving=view.findViewById(R.id.tv_c2c_item_having_num);//进行中
        TextView tvLevel=view.findViewById(R.id.tv_c2c_item_level);//等级
        TextView tvStat=view.findViewById(R.id.tv_c2c_item_stat);//状态
        TextView tvLower=view.findViewById(R.id.tv_c2c_item_lower_num);//下限
        TextView tvTime=view.findViewById(R.id.tv_c2c_item_time_num);//时间
        TextView tvCount=view.findViewById(R.id.tv_c2c_item_count_num);//交易总数
        TextView tvTotal=view.findViewById(R.id.tv_c2c_item_total_num);//交易总额
        TextView tvZFB=view.findViewById(R.id.tv_c2c_item_zfb);//支付宝图标
        TextView tvWX=view.findViewById(R.id.tv_c2c_item_wx);//微信图标
        TextView tvYH=view.findViewById(R.id.tv_c2c_item_yh);//银行图标

        tvName.setText(dealerData.user.getDealerName());
        tvDepict.setText(dealerData.user.getDepict());
        tvBrokerage.setText(dealerData.user.getBrokerage()*100+"%");
        tvAccountNo.setText(dealerData.user.getAccount());
        tvContact.setOnClickListener(dealerData.constactOnClickListener);
        tvMax.setText(""+dealerData.max);
        tvUsable.setText(""+dealerData.usable);
        tvStat.setText(""+ UserStatUtil.getUserStat(dealerData.user.getStat()));
        tvLower.setText(""+dealerData.user.getLowerLimit());
        if(dealerData.user.userInfo!=null){
            tvLevel.setText(""+dealerData.user.userInfo.getLevel());
        }else {
            tvLevel.setText(""+0.0);
        }
        if(dealerData.user.userRecord!=null){
            tvHaving.setText(""+dealerData.user.userRecord.getHavingTotal());
            if(dealerData.user.userRecord.getTime()==-1){
                tvTime.setText("-");
            } else {
                tvTime.setText(""+dealerData.user.userRecord.getTime());
            }
            tvCount.setText(""+dealerData.user.userRecord.getClinchCount());
            tvTotal.setText(""+dealerData.user.userRecord.getClinchTotal());
        }else {
            tvHaving.setText(""+0.0);
            tvTime.setText(""+0.0);
            tvCount.setText(""+0);
            tvTotal.setText(""+0.0);
        }
        tvZFB.setVisibility(View.GONE);
        tvWX.setVisibility(View.GONE);
        tvYH.setVisibility(View.GONE);
        if(dealerData.user.realAssets!=null){
            for(RealAsset realAsset : dealerData.user.realAssets){
                switch (realAsset.getRealAssetType()){
                    case "1":
                        tvZFB.setVisibility(View.VISIBLE);
                        break;
                    case "2":
                        tvWX.setVisibility(View.VISIBLE);
                        break;
                    case "3":
                        tvYH.setVisibility(View.VISIBLE);
                        break;
                }
            }

        }
        if(views.size()>0&&views.size()>i&&views.get(i)!=null){
            views.remove(i);
            views.add(i,view);
        }else{
            views.add(i,view);
        }


    }
    public void queryTotal(int i){
        final String account = dataList.get(i).user.getAccount();
        final int a=i;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Double total = BtslandApplication.getMarketStat().mWebsocketApi.getAssetTotalByAccountAndCoin(account,"CNY");
                Message msg=Message.obtain();
                Bundle bundle=new Bundle();
                bundle.putDouble("total",total);
                msg.setData(bundle);
                msg.what=a;
                handler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public Object getItem(int i) {
        return views.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return views.get(i);
    }
    public static class DealerData{
        public DealerData(User user, View.OnClickListener constactOnClickListener, Double max, Double usable, List<Integer> types) {
            this.user = user;
            this.constactOnClickListener = constactOnClickListener;
            this.max = max;
            this.usable = usable;
        }

        public DealerData() {
        }

        public User user;
        public View.OnClickListener constactOnClickListener;
        public Double max=0.0;
        public Double usable=0.0;

        public void replce(DealerData dealerData) {
            this.user=dealerData.user;
        }
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Double total=msg.getData().getDouble("total");
            dataList.get(msg.what).max=total;
            Double having = dataList.get(msg.what).user.userRecord.getHavingTotal();
            dataList.get(msg.what).usable=total-having;
            fillInViews(msg.what);
            notifyDataSetChanged();
        }
    };

}
