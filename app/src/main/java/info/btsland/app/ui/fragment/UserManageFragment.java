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
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.IOException;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.sha256_object;
import info.btsland.app.ui.activity.AccountC2CTypesActivity;
import info.btsland.app.ui.activity.BorrowActivity;
import info.btsland.app.ui.activity.LoginActivity;
import info.btsland.app.ui.activity.LookActivity;
import info.btsland.app.ui.activity.MarketDetailedActivity;
import info.btsland.app.ui.activity.PurseAccessRecordActivity;
import info.btsland.app.ui.activity.PurseAssetActivity;
import info.btsland.app.ui.activity.PurseTradingRecordActivity;
import info.btsland.app.ui.activity.TransferActivity;
import info.btsland.app.ui.view.AppDialog;
import info.btsland.app.ui.view.MyConstraintLayout;
import info.btsland.app.ui.view.PasswordDialog;
import info.btsland.exchange.http.UserHttp;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PurseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PurseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

/**
 * 用户界面
 */
public class UserManageFragment extends Fragment {
    //全部资产
    private TextView tvPurseAllAsset;
    //充提记录
    private TextView tvPurseRW;
    //c2c支付帐号
    private TextView tvPueseTypes;

    //交易记录
    private TextView tvPurseDeal;
    //全部挂单
    private TextView tvPurseAllRemain;
    //观察
    private TextView tvPurseBackup;

    private TextView tvPurseConvert;
    //头像
    private WebView portrait;
    //用户名
    private TextView tvUserName;
    //别名
    private TextView tvUserAnotherName;

    //转账
    private TextView tvPurseTransferAccounts;
    //抵押
    private TextView tvBorrow;

    private TextView tvUserLogoff;

    private FrameLayout flPurse;
    private FrameLayout flPurseLoginPrompt;

    private TextView tvGoLogin;

    private TextView tvGoRegister;

    private ScrollView scrollView;
    private MyConstraintLayout clPurse;
    private UserManageReceiver userManageReceiver ;

    public UserManageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userManageReceiver=new UserManageReceiver();
        IntentFilter intentFilter =new IntentFilter(UserManageReceiver.EVENT);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(userManageReceiver,intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_user_manage, container, false);
        init(view);
        fillIn();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        yesOrNoLogin();
    }



    private void yesOrNoLogin() {
        if (BtslandApplication.accountObject==null) {
            // flPurseLoginPrompt.setVisibility(View.VISIBLE);
            flPurseLoginPrompt.setVisibility(View.VISIBLE);
            flPurseLoginPrompt.setOnTouchListener(new View.OnTouchListener(){
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }

            });
            // clPurse.setEnabled(false);
            // clPurse.setClickable(false);
            // scrollView.setClickable(false);
            flPurse.setVisibility(View.VISIBLE);
            clPurse.isForbidden=true;
        } else {
            flPurse.setVisibility(View.VISIBLE);
            flPurseLoginPrompt.setVisibility(View.GONE);
            clPurse.isForbidden=false;
        }


    }

    private void fillIn() {
        if (BtslandApplication.accountObject != null) {
            createPortrait();//设置头像
            tvUserName.setText(BtslandApplication.accountObject.name);
            setTotal(0.0,BtslandApplication.chargeUnit);
            tvUserAnotherName.setText("#" + BtslandApplication.accountObject.id.get_instance());
        } else {
            portrait.stopLoading();
            portrait.clearHistory();
            tvUserName.setText("");
            tvPurseConvert.setText("");
            tvUserAnotherName.setText("");
        }
    }

    private void setTotal(Double total,String coin){
        if(BtslandApplication.chargeUnit.equals(coin)){
            if(String.valueOf(total)==null){
                tvPurseConvert.setText("0.0"+coin);
            }else if(String.valueOf(total).length()>8){
                tvPurseConvert.setText(String.valueOf(total).substring(0,8) + coin);
            }else {
                tvPurseConvert.setText(String.valueOf(total) + coin);
            }
        }
    }


    /**
     * 设置头像
     */
    public void createPortrait() {
        sha256_object.encoder encoder=new sha256_object.encoder();
        encoder.write(BtslandApplication.accountObject.name.getBytes());
        String htmlShareAccountName="<html><head><style>body,html { margin:0; padding:0; text-align:center;}</style><meta name=viewport content=width=" + 100 + ",user-scalable=no/></head><body><canvas width=" + 100 + " height=" + 100 + " data-jdenticon-hash=" + encoder.result().toString() + "></canvas><script src=https://cdn.jsdelivr.net/jdenticon/1.3.2/jdenticon.min.js async></script></body></html>";
        WebSettings webSettings=portrait.getSettings();
        webSettings.setJavaScriptEnabled(true);
        portrait.loadData(htmlShareAccountName, "text/html", "UTF-8");
    }


    private void init(View view) {
        clPurse=view.findViewById(R.id.cl_purse);
        //全部资产
        tvPurseAllAsset=view.findViewById(R.id.tv_purse_allAsset);
        //充提记录
        tvPurseRW=view.findViewById(R.id.tv_purse_rw);
        tvPueseTypes=view.findViewById(R.id.tv_purse_types);
        //交易记录
        tvPurseDeal=view.findViewById(R.id.tv_purse_deal);
        //全部挂单
        tvPurseAllRemain=view.findViewById(R.id.tv_purse_allRemain);
        //钱包备份
        tvPurseBackup=view.findViewById(R.id.tv_purse_backup);
        //折合总金额
        tvPurseConvert=view.findViewById(R.id.tv_purse_convert);
        //转账操作
        tvPurseTransferAccounts=view.findViewById(R.id.tv_purse_transfer_accounts);

        //去登陆按钮
        tvGoLogin=view.findViewById(R.id.tv_go_login);
        //去注册
        tvGoRegister=view.findViewById(R.id.tv_go_register);
        //已登陆钱包
        flPurse=view.findViewById(R.id.fl_purse);
        //未登录
        flPurseLoginPrompt=view.findViewById(R.id.fl_purse_login_prompt);

        tvBorrow=view.findViewById(R.id.tv_purse_borrow);

        portrait=view.findViewById(R.id.iv_user_pho);
        tvUserName=view.findViewById(R.id.tv_user_name);
        tvUserAnotherName=view.findViewById(R.id.tv_user_anotherName);
        tvUserLogoff=view.findViewById(R.id.tv_user_logoff);
        scrollView=view.findViewById(R.id.sv_purse);

        TextViewOnCLickListener onCLickListener=new TextViewOnCLickListener();
        tvPurseAllAsset.setOnClickListener(onCLickListener);
        tvPurseDeal.setOnClickListener(onCLickListener);
        tvPurseRW.setOnClickListener(onCLickListener);
        tvPurseAllRemain.setOnClickListener(onCLickListener);
        tvPurseBackup.setOnClickListener(onCLickListener);
        tvUserLogoff.setOnClickListener(onCLickListener);
        tvGoLogin.setOnClickListener(onCLickListener);
        tvPurseTransferAccounts.setOnClickListener(onCLickListener);
        tvGoRegister.setOnClickListener(onCLickListener);
        tvBorrow.setOnClickListener(onCLickListener);
        tvPueseTypes.setOnClickListener(onCLickListener);

    }
    public static void sendBroadcast(Context context,Double total,String coin){
        Intent intent=new Intent(UserManageReceiver.EVENT);
        intent.putExtra("total",total);
        intent.putExtra("coin",coin);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    public static void sendBroadcast(Context context,int want){
        Intent intent=new Intent(UserManageReceiver.EVENT);
        intent.putExtra("want",want);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    public class UserManageReceiver extends BroadcastReceiver {
        public static final String EVENT = "UserManageReceiver";
        @Override
        public void onReceive(Context context, Intent intent) {
            int want=intent.getIntExtra("want", 1);
            if(want==1) {
                Double total = intent.getDoubleExtra("total", 0.0);
                String coin = intent.getStringExtra("coin");
                setTotal(total, coin);
            }else if(want==2) {
                fillIn();
            }
        }
    }
    /**
     * 单击特效
     *
     * @param textView    被单击的tv
     * @param motionEvent 当前状态
     */
    protected void touchColor(TextView textView, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            textView.setBackground(getResources().getDrawable(R.drawable.tv_row_touch, null));
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            textView.setBackground(getResources().getDrawable(R.drawable.tv_row, null));
        }
    }

    class TextViewOnCLickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.tv_purse_allAsset:
                    //全部资产
                    Intent intent=new Intent(getActivity(), PurseAssetActivity.class);
                    getActivity().startActivity(intent);
                    break;
                case R.id.tv_purse_rw:
                    //充值记录
                    Intent rw=new Intent(getActivity(), PurseAccessRecordActivity.class);
                    getActivity().startActivity(rw);
                    break;
                case R.id.tv_purse_types:
                    //c2c支付帐号
                    if(BtslandApplication.dealer!=null){
                        Intent types=new Intent(getActivity(), AccountC2CTypesActivity.class);
                        types.putExtra("type",0);
                        getActivity().startActivity(types);
                    }else {
                        Bundle bundle=new Bundle();
                        bundle.putString("want","hud");
                        Message msg=Message.obtain();
                        msg.what=1;
                        msg.setData(bundle);
                        registerHandler.sendMessage(msg);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                UserHttp.registerAccount(BtslandApplication.accountObject.name, "", new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        int a= Integer.parseInt(response.body().string());
                                        Bundle bundle=new Bundle();
                                        bundle.putString("want","register");
                                        Message msg=Message.obtain();
                                        msg.setData(bundle);
                                        if(a>0){
                                            msg.what=1;
                                            registerHandler.sendMessage(msg);
                                        }else {
                                            msg.what=2;
                                            registerHandler.sendMessage(msg);
                                        }
                                    }
                                });
                            }
                        }).start();

                    }
                    break;
                case R.id.tv_purse_deal:
                    //交易记录
                    Intent deal=new Intent(getActivity(), PurseTradingRecordActivity.class);
                    getActivity().startActivity(deal);
                    break;
                case R.id.tv_purse_allRemain:
                    //全部挂单
                    MarketDetailedActivity.startAction(getActivity(), null, 2);
                    break;
                case R.id.tv_purse_backup:
                    //观察
                    Intent backup=new Intent(getActivity(), LookActivity.class);
                    getActivity().startActivity(backup);
                    break;
                case R.id.tv_user_logoff:
                    AppDialog appDialog=new AppDialog(getActivity());

                    appDialog.setListener(new AppDialog.OnDialogInterationListener() {
                        @Override
                        public void onConfirm() {

                            BtslandApplication.isLogin=false;
                            BtslandApplication.accountObject=null;
                            BtslandApplication.clearUser();
                            fillIn();
                            yesOrNoLogin();
                        }

                        @Override
                        public void onReject() {

                        }
                    });
                    //appDialog.setTitle("");
                    appDialog.setMsg("你确定要退出吗？");
                    appDialog.show();
                    break;

                case R.id.tv_purse_transfer_accounts:
                    Intent intentTransfer=new Intent(getActivity(), TransferActivity.class);
                    getActivity().startActivity(intentTransfer);

                    break;
                case R.id.tv_go_login:
                    // purseHander handler=new purseHander();
                    Intent iGoLogin=new Intent(getActivity(), LoginActivity.class);
//                    iGoLogin.putExtra("hander", handler);
                    iGoLogin.putExtra("want",LoginActivity.GOLOGIN);
                    getActivity().startActivity(iGoLogin);

                    break;
                case R.id.tv_go_register:
                    Intent iGoRegister=new Intent(getActivity(), LoginActivity.class);
                    iGoRegister.putExtra("want",LoginActivity.GOREGISTER);
                    getActivity().startActivity(iGoRegister);
                    break;
                case R.id.tv_purse_borrow:
                    Intent iGoBorrow=new Intent(getActivity(), BorrowActivity.class);
                    getActivity().startActivity(iGoBorrow);
                    break;
            }
        }
    }


    class TextViewOnTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (view.getId()) {
                case R.id.tv_purse_allAsset:
                    touchColor(tvPurseAllAsset, motionEvent);
                    break;
//                case R.id.tv_purse_rw:
//                    touchColor(tvPurseRW, motionEvent);
//                    break;
                case R.id.tv_purse_deal:
                    touchColor(tvPurseDeal, motionEvent);
                    break;
                case R.id.tv_purse_allRemain:
                    touchColor(tvPurseAllRemain, motionEvent);
                    break;
                case R.id.tv_purse_backup:
                    touchColor(tvPurseBackup, motionEvent);
                    break;


            }
            return false;
        }
    }
    private KProgressHUD hud;
    private Handler registerHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String want = msg.getData().getString("want");
            if(want.equals("register")) {
                if (msg.what == 1) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(hud!=null&&hud.isShowing()){
                        hud.dismiss();
                    }
                    Toast.makeText(getActivity(),"第一次使用注册成功",Toast.LENGTH_SHORT).show();
                    Intent types=new Intent(getActivity(), AccountC2CTypesActivity.class);
                    types.putExtra("type",0);
                    getActivity().startActivity(types);
                }else {
                    if(hud!=null&&hud.isShowing()){
                        hud.dismiss();
                    }
                    Toast.makeText(getActivity(),"注册失败",Toast.LENGTH_SHORT).show();
                }
            }else if(want.equals("hud")){
                if(msg.what==1){
                    if(hud==null){
                        hud=KProgressHUD.create(getActivity());
                    }
                    hud.setLabel("请稍等。。。");
                    hud.show();
                }

            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

//    class purseHander extends Handler implements Serializable {
//        @Override
//        public void handleMessage(Message msg) {
//            try {
//
//                if (msg.what==1){
//                    flPurse.setVisibility(View.VISIBLE);
//                    flPurseLoginPrompt.setVisibility(View.GONE);
//                    fillIn();
//
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//
//        }
//    }
}