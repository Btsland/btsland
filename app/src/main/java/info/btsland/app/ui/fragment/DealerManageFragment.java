package info.btsland.app.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.model.MarketTicker;
import info.btsland.app.ui.activity.DealerHavingListActivity;
import info.btsland.app.ui.activity.SettingActivity;
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

    private ShowPoint point;


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
        tvStat=view.findViewById(R.id.tv_dealer_stat);
        tvInfo=view.findViewById(R.id.tv_dealer_info);
        tvReal=view.findViewById(R.id.tv_dealer_real);
        tvHaving=view.findViewById(R.id.tv_dealer_having);
        tvClinch=view.findViewById(R.id.tv_dealer_clinch);
        tvHelp=view.findViewById(R.id.tv_dealer_help);
        tvUser=view.findViewById(R.id.tv_dealer_user);
    }

    private void fillIn() {
        tvStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStatDialog();
            }
        });
        tvHaving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), DealerHavingListActivity.class);
                intent.putExtra("type",1);
                getActivity().startActivity(intent);
            }
        });
        tvClinch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), DealerHavingListActivity.class);
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


    private boolean fillInUser(String json) {
        Gson gson=new Gson();
        BtslandApplication.dealer=gson.fromJson(json,User.class);
        return true;
    }

    public interface ShowPoint{
        void show(int i);
    }

}
