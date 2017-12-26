package info.btsland.app.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
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
import info.btsland.app.ui.activity.DealerNoteListActivity;
import info.btsland.app.ui.view.AppDialog;
import info.btsland.app.ui.view.AppListDialog;
import info.btsland.app.ui.view.PasswordDialog;
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
    private NumberProgressBar progressBar;

    private DealerManageReceiver dealerManageReceiver;

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
        tvName.setText(BtslandApplication.dealer.getDealerName());
        tvDealerId.setText(BtslandApplication.dealer.getDealerId());
        tvInLowNum.setText(""+BtslandApplication.dealer.getLowerLimitIn());
        tvOutLowNum.setText(""+BtslandApplication.dealer.getLowerLimitOut());
        tvInBro.setText(""+BtslandApplication.dealer.getBrokerageIn()*100+"%");
        tvOutBro.setText(""+BtslandApplication.dealer.getBrokerageOut()*100+"%");
        tvDepict.setText(BtslandApplication.dealer.getDepict());
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
                    tvWX.setVisibility(View.VISIBLE);
                    break;
                case "2":
                    tvZFB.setVisibility(View.VISIBLE);
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
                            if(BtslandApplication.dealer.getPassword()==null){
                                PasswordDialog passwordDialog=new PasswordDialog(getActivity());
                                passwordDialog.setMsg("请输入您的承兑密码！");
                                final int finalI = i;
                                passwordDialog.setListener(new PasswordDialog.OnDialogInterationListener() {
                                    @Override
                                    public void onConfirm(AlertDialog dialog, String passwordString) {
                                        Log.e("ssssssssssssssssss", "onConfirm: "+passwordString );
                                        UserHttp.loginDealer(BtslandApplication.dealer.getDealerId(), passwordString, new Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {

                                            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                String json=response.body().string();
                                                if(json==null||json.equals("")){
                                                    AppDialog appDialog=new AppDialog(getActivity());
                                                    appDialog.setMsg("密码错误！");
                                                    appDialog.show();
                                                    return;
                                                }
                                                if(fillInUser(json)){
                                                    UserHttp.updateStat(BtslandApplication.dealer.getDealerId(), BtslandApplication.dealer.getPassword(), string1.get(finalI), new Callback() {
                                                        @Override
                                                        public void onFailure(Call call, IOException e) {

                                                        }

                                                        @Override
                                                        public void onResponse(Call call, Response response) throws IOException {
                                                            String json=response.body().string();
                                                            fillInUser(json);
                                                        }
                                                    });
                                                }

                                            }
                                        });
                                    }

                                    @Override
                                    public void onReject(AlertDialog dialog) {

                                    }
                                });
                                passwordDialog.show();
                            }else {
                                UserHttp.updateStat(BtslandApplication.dealer.getDealerId(), BtslandApplication.dealer.getPassword(), string1.get(i), new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String json=response.body().string();
                                        Log.e("wwwwwwwwwwwwwwwww", "onResponse: "+json );
                                        fillInUser(json);
                                    }
                                });
                            }
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

    private boolean fillInUser(String json) {
        Gson gson=new Gson();
        BtslandApplication.dealer=gson.fromJson(json,User.class);
        return true;
    }

    public interface ShowPoint{
        void show(int i);
    }

}
