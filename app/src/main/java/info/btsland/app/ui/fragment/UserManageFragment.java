package info.btsland.app.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.sha256_object;
import info.btsland.app.ui.activity.AccountC2CTypesActivity;
import info.btsland.app.ui.activity.BorrowActivity;
import info.btsland.app.ui.activity.ChatAccountListActivity;
import info.btsland.app.ui.activity.LoginActivity;
import info.btsland.app.ui.activity.LookActivity;
import info.btsland.app.ui.activity.MarketDetailedActivity;
import info.btsland.app.ui.activity.PurseAccessRecordActivity;
import info.btsland.app.ui.activity.PurseAssetActivity;
import info.btsland.app.ui.activity.PurseTradingRecordActivity;
import info.btsland.app.ui.activity.TransferActivity;
import info.btsland.app.ui.view.AppDialog;
import info.btsland.app.ui.view.MyConstraintLayout;
import info.btsland.app.util.BaseThread;

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

    private TextView tvChat;
    //抵押
    private TextView tvBorrow;

    private TextView tvUserLogoff;

    private FrameLayout flPurse;
    private FrameLayout flPurseLoginPrompt;

    private TextView tvGoLogin;

    private TextView tvGoRegister;

    private TextView tvPoint;
    private TextView tvPoint2;

    private ScrollView scrollView;
    private MyConstraintLayout clPurse;
    private UserManageReceiver userManageReceiver ;
    private UserManageReceiverPoint userManageReceiverPoint ;
    private UserManageReceiverChatPoint userManageReceiverChatPoint;

    public UserManageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userManageReceiver=new UserManageReceiver();
        IntentFilter intentFilter =new IntentFilter(UserManageReceiver.EVENT);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(userManageReceiver,intentFilter);
        userManageReceiverPoint=new UserManageReceiverPoint();
        IntentFilter intentFilter2 =new IntentFilter(UserManageReceiverPoint.EVENT);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(userManageReceiverPoint,intentFilter2);
        userManageReceiverChatPoint=new UserManageReceiverChatPoint();
        IntentFilter intentFilter3 =new IntentFilter(UserManageReceiverChatPoint.EVENT);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(userManageReceiverChatPoint,intentFilter3);
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
        //聊天
        tvChat=view.findViewById(R.id.tv_purse_chat);
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
        tvPoint=view.findViewById(R.id.tv_user_manage_point);
        tvPoint2=view.findViewById(R.id.tv_user_manage_point2);
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
        tvChat.setOnClickListener(onCLickListener);

    }
    public static void sendBroadcast(Context context,Double total,String coin){
        Intent intent=new Intent(UserManageReceiver.EVENT);
        intent.putExtra("total",total);
        intent.putExtra("coin",coin);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    public static void sendBroadcast(Context context,int want){
        Log.e("UserManageReceiver", "sendBroadcast: " );
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
    public void setPoint(int point) {
        if(point==0){
            tvPoint.setText(""+point);
            tvPoint.setVisibility(View.GONE);
        }else {
            tvPoint.setText(""+point);
            tvPoint.setVisibility(View.VISIBLE);
        }
    }
    public void setPoint2(int point) {
        if(point==0){
            tvPoint2.setText(""+point);
            tvPoint2.setVisibility(View.GONE);
        }else {
            tvPoint2.setText(""+point);
            tvPoint2.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(userManageReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(userManageReceiverPoint);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(userManageReceiverChatPoint);
    }

    public static void sendBroadcastPoint(Context context, int num){
        Intent intent=new Intent(UserManageReceiverPoint.EVENT);
        intent.putExtra("num",num);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    private class UserManageReceiverPoint extends BroadcastReceiver {
        public static final String EVENT = "UserManageReceiverPoint";
        @Override
        public void onReceive(Context context, Intent intent) {
            int num=intent.getIntExtra("num",0);
            setPoint(num);
        }
    }
    public static void sendBroadcastChatPoint(Context context, int num){
        Intent intent=new Intent(UserManageReceiverChatPoint.EVENT);
        intent.putExtra("num",num);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    private class UserManageReceiverChatPoint extends BroadcastReceiver {
        public static final String EVENT = "UserManageReceiverChatPoint";
        @Override
        public void onReceive(Context context, Intent intent) {
            int num=intent.getIntExtra("num",0);
            setPoint2(num);
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
                    Intent types=new Intent(getActivity(), AccountC2CTypesActivity.class);
                    types.putExtra("type",0);
                    getActivity().startActivity(types);

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
                case R.id.tv_purse_chat:
                    //聊天
                    Intent chat=new Intent(getActivity(), ChatAccountListActivity.class);
                    getActivity().startActivity(chat);
                    break;
                case R.id.tv_user_logoff:
                    AppDialog appDialog=new AppDialog(getActivity());

                    appDialog.setListener(new AppDialog.OnDialogInterationListener() {
                        @Override
                        public void onConfirm() {

                            BtslandApplication.isLogin=false;
                            BtslandApplication.accountObject=null;
                            BtslandApplication.clearUser();
                            BtslandApplication.dealer=null;
                            BtslandApplication.userHavingInNotes.clear();
                            BtslandApplication.userHavingOutNotes.clear();
                            BtslandApplication.dealerHavingNotes.clear();
                            BtslandApplication.dealerClinchNotes.clear();
                            BtslandApplication.helpUserMap.clear();
                            for (String name : BtslandApplication.helpQueryThreadMap.keySet()){
                                 BaseThread baseThread = BtslandApplication.helpQueryThreadMap.get(name);
                                 baseThread.kill();
                            }
                            BtslandApplication.helpQueryThreadMap.clear();
                            fillIn();
                            yesOrNoLogin();
                            PurseFragment.sendBroadcast(getActivity());
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