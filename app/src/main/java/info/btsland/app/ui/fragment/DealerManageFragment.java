package info.btsland.app.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.model.MarketTicker;
import info.btsland.app.ui.activity.AccountC2CTypesActivity;
import info.btsland.app.ui.activity.DealerInfoActivity;
import info.btsland.app.ui.activity.DealerNoteListActivity;
import info.btsland.app.ui.view.AppDialog;
import info.btsland.app.ui.view.AppListDialog;
import info.btsland.exchange.entity.User;
import info.btsland.exchange.http.UserHttp;
import info.btsland.exchange.utils.UserStatCode;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DealerManageFragment extends Fragment {
    private TextView tvInfo;
    private TextView tvStat;
    private TextView tvReal;
    private TextView tvHaving;
    private TextView tvClinch;
    private TextView tvHelp;
    private TextView tvUser;

    private TextView tvStatText;
    private TextView tvName;
    private TextView tvDealerId;
    private TextView tvInLowNum;
    private TextView tvOutLowNum;
    private TextView tvInBro;
    private TextView tvOutBro;
    private TextView tvTime;
    private TextView tvDepict;
    private TextView tvCountNum;
    private TextView tvInNum;
    private TextView tvOutNum;
    private TextView tvWX;
    private TextView tvZFB;
    private TextView tvYH;
    private ImageView ivPho;
    private TextView tvPoint;

    private NumberProgressBar progressBar;

    private DealerManageReceiver dealerManageReceiver;
    private DealerManageReceiverPoint dealerManageReceiverPoint;

    public DealerManageFragment() {

    }

    public static DealerManageFragment newInstance() {
        DealerManageFragment fragment = new DealerManageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter =new IntentFilter(DealerManageReceiver.EVENT) ;
        dealerManageReceiver=new DealerManageReceiver() ;
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(dealerManageReceiver,intentFilter);
        IntentFilter intentFilter2 =new IntentFilter(DealerManageReceiverPoint.EVENT) ;
        dealerManageReceiverPoint=new DealerManageReceiverPoint() ;
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(dealerManageReceiverPoint,intentFilter2);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dealer_manage, container, false);
        init(view);
        fillIn();
        return view;
    }


    private void init(View view) {
        ivPho=view.findViewById(R.id.iv_dealer_manage_pho);
        progressBar=view.findViewById(R.id.nb_dealer_manage_level);
        tvStatText=view.findViewById(R.id.tv_dealer_manage_stat);
        tvName=view.findViewById(R.id.tv_dealer_manage_dealerName);
        tvDealerId=view.findViewById(R.id.tv_dealer_manage_dealerId);
        tvInLowNum=view.findViewById(R.id.tv_dealer_manage_inLowNum);
        tvOutLowNum=view.findViewById(R.id.tv_dealer_manage_outLowNum);
        tvInBro=view.findViewById(R.id.tv_dealer_manage_inBroNum);
        tvOutBro=view.findViewById(R.id.tv_dealer_manage_outBroNum);
        tvTime=view.findViewById(R.id.tv_dealer_manage_timeNum);
        tvDepict=view.findViewById(R.id.tv_dealer_manage_depict);
        tvCountNum=view.findViewById(R.id.tv_dealer_manage_countNum);
        tvInNum=view.findViewById(R.id.tv_dealer_manage_inNum);
        tvOutNum=view.findViewById(R.id.tv_dealer_manage_outNum);
        tvWX=view.findViewById(R.id.tv_dealer_manage_wx);
        tvZFB=view.findViewById(R.id.tv_dealer_manage_zfb);
        tvYH=view.findViewById(R.id.tv_dealer_manage_yh);

        tvStat=view.findViewById(R.id.tv_dealer_stat);
        tvInfo=view.findViewById(R.id.tv_dealer_info);
        tvReal=view.findViewById(R.id.tv_dealer_real);
        tvHaving=view.findViewById(R.id.tv_dealer_having);
        tvClinch=view.findViewById(R.id.tv_dealer_clinch);
        tvHelp=view.findViewById(R.id.tv_dealer_help);
        tvUser=view.findViewById(R.id.tv_dealer_user);

        tvPoint=view.findViewById(R.id.tv_dealer_manage_point);
    }
    public void setPoint(int point) {
        if(point==0){
            tvPoint.setText(""+point);
            tvPoint.setVisibility(View.GONE);
        }else {
            tvPoint.setText(""+point);
            tvPoint.setVisibility(View.VISIBLE);
        }
    }


    private void fillInTop(){
        switch (BtslandApplication.dealer.getStat()){
            case UserStatCode.ONLINE:
                tvStatText.setText("在线");
            break;
            case UserStatCode.OFFLINE:
                tvStatText.setText("离开");
                break;
            case UserStatCode.BEOUT:
                tvStatText.setText("离线");
                break;
        }
        if(BtslandApplication.dealer.getDealerName()!=null){
            tvName.setText(BtslandApplication.dealer.getDealerName());
        }

        if(BtslandApplication.dealer.getDealerId()!=null){
            tvDealerId.setText(BtslandApplication.dealer.getDealerId());
        }

        if(BtslandApplication.dealer.getLowerLimitIn()!=null){
            tvInLowNum.setText(""+BtslandApplication.dealer.getLowerLimitIn());
        }

        if(BtslandApplication.dealer.getLowerLimitOut()!=null){
            tvOutLowNum.setText(""+BtslandApplication.dealer.getLowerLimitOut());
        }

        if(BtslandApplication.dealer.getBrokerageIn()!=null){
            tvInBro.setText(""+BtslandApplication.dealer.getBrokerageIn()*100+"%");
        }

        if(BtslandApplication.dealer.getBrokerageOut()!=null){
            tvOutBro.setText(""+BtslandApplication.dealer.getBrokerageOut()*100+"%");
        }

        if(BtslandApplication.dealer.getDepict()!=null){
            tvDepict.setText(BtslandApplication.dealer.getDepict());
        }

        progressBar.setMax(2000);
        if(BtslandApplication.dealer.userInfo!=null){
            Double level = BtslandApplication.dealer.userInfo.getLevel();
            int a = (int) (level/20);
            progressBar.setProgress((int) ((level-(a*20))*100));
            switch (a){
                case 0:
                    ivPho.setImageDrawable(getContext().getDrawable(R.mipmap.level1));
                    break;
                case 1:
                    ivPho.setImageDrawable(getContext().getDrawable(R.mipmap.level2));
                    break;
                case 2:
                    ivPho.setImageDrawable(getContext().getDrawable(R.mipmap.level3));
                    break;
                case 3:
                    ivPho.setImageDrawable(getContext().getDrawable(R.mipmap.level4));
                    break;
                case 4:
                    ivPho.setImageDrawable(getContext().getDrawable(R.mipmap.level5));
                    break;
                case 5:
                    ivPho.setImageDrawable(getContext().getDrawable(R.mipmap.level5));
                    progressBar.setProgress(2000);
                    break;
            }
        }
        if(BtslandApplication.dealer.userRecord!=null) {
            tvTime.setText(""+BtslandApplication.dealer.userRecord.getTime());
            tvInNum.setText(""+(BtslandApplication.dealer.userRecord.getInClinchTotal()+BtslandApplication.dealer.userRecord.getOutClinchTotal()));
            tvOutNum.setText(""+(BtslandApplication.dealer.userRecord.getOutClinchTotal()+BtslandApplication.dealer.userRecord.getOutClinchTotal()));
            tvCountNum.setText(""+(BtslandApplication.dealer.userRecord.getInClinchCount() + BtslandApplication.dealer.userRecord.getOutClinchCount()));
        }else {
            tvTime.setText("-");
            tvInNum.setText("-");
            tvOutNum.setText("-");
            tvCountNum.setText("-");
        }
        tvWX.setVisibility(View.GONE);
        tvZFB.setVisibility(View.GONE);
        tvYH.setVisibility(View.GONE);
        for(int i=0;i<BtslandApplication.dealer.realAssets.size();i++) {
            switch (BtslandApplication.dealer.realAssets.get(i).getRealAssetType()) {
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
    private void fillIn() {
        tvStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStatDialog();
            }
        });
        tvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), DealerInfoActivity.class);
                getActivity().startActivity(intent);
            }
        });
        tvReal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), AccountC2CTypesActivity.class);
                intent.putExtra("type",1);
                getActivity().startActivity(intent);
            }
        });
        tvHaving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), DealerNoteListActivity.class);
                intent.putExtra("type",1);
                intent.putExtra("dealerId","");
                getActivity().startActivity(intent);
            }
        });
        tvClinch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), DealerNoteListActivity.class);
                intent.putExtra("type",2);
                getActivity().startActivity(intent);
            }
        });
    }

    private void showStatDialog() {
        final List<Integer> string1= Arrays.asList(UserStatCode.ONLINE,UserStatCode.OFFLINE,UserStatCode.BEOUT);
        final List<String> string2= Arrays.asList("在线","离开","离线");
        int checkNum=2;
        for(int i=0;i<string1.size();i++){
            if(BtslandApplication.dealer!=null){
                if(string1.get(i).equals(BtslandApplication.dealer.getStat())){
                    checkNum=i;
                    break;
                }
            }
        }
        AppListDialog dialog=new AppListDialog(getActivity(),string2,checkNum);
        dialog.setTitle("设置状态");
        dialog.setListener(new AppListDialog.OnDialogInterationListener() {
            @Override
            public void onConfirm(MarketTicker market) {

            }

            @Override
            public void onConfirm(String server) {
                for(int i=0;i<string2.size();i++){
                    if(string2.get(i).equals(server)){
                        Log.e("1111111111111111", "onConfirm: "+BtslandApplication.dealer );
                        if(BtslandApplication.dealer!=null){
                            UserHttp.updateStat(BtslandApplication.dealer.getDealerId(), string1.get(i), new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String json=response.body().string();
                                    if(json.indexOf("error")!=-1){
                                        BtslandApplication.sendBroadcastDialog(getActivity(),json);
                                        handler.sendEmptyMessage(-1);
                                    }else {
                                        handler.sendEmptyMessage(1);
                                        fillInUser(json);
                                    }
                                }
                            });
                        }else {
                            AppDialog appDialog = new AppDialog(getActivity());
                            appDialog.setMsg("您未登录，请登录。");
                            appDialog.show();
                        }
                        break;
                    }
                }
                BtslandApplication.saveLanguage();
            }

            @Override
            public void onReject() {

            }
        });
        dialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(dealerManageReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(dealerManageReceiverPoint);
    }
    public static void sendBroadcastPoint(Context context, int num){
        Intent intent=new Intent(DealerManageReceiverPoint.EVENT);
        intent.putExtra("num",num);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    private class DealerManageReceiverPoint extends BroadcastReceiver {
        public static final String EVENT = "DealerManageReceiverPoint";
        @Override
        public void onReceive(Context context, Intent intent) {
            int num=intent.getIntExtra("num",0);
            setPoint(num);
        }
    }
    public static void sendBroadcast(Context context){
        Intent intent=new Intent(DealerManageReceiver.EVENT);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    class DealerManageReceiver extends BroadcastReceiver{
        public static final String EVENT="DealerManageReceiver";
        @Override
        public void onReceive(Context context, Intent intent) {
            fillInTop();
        }
    }

    private void fillInUser(String json) {
        Gson gson=new Gson();
        BtslandApplication.dealer=gson.fromJson(json,User.class);
    }

    public interface ShowPoint{
        void show(int i);
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            AppDialog appDialog=new AppDialog(getActivity());
            if(msg.what==1){
                appDialog.setMsg("状态更新成功");
                fillInTop();
            }else {
                appDialog.setMsg("状态更新失败，请重试");
            }
            appDialog.show();
        }
    };

}
