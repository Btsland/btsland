package info.btsland.app.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.sha256_object;
import info.btsland.app.ui.activity.LoginActivity;
import info.btsland.app.ui.activity.MarketDetailedActivity;
import info.btsland.app.ui.activity.PurseAssetActivity;
import info.btsland.app.ui.activity.PurseTradingRecordActivity;
import info.btsland.app.ui.activity.PurseWalletBackupActivity;
import info.btsland.app.ui.activity.TransferActivity;
import info.btsland.app.ui.view.AppDialog;
import info.btsland.app.ui.view.MyConstraintLayout;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PurseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PurseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

/**
 * 行情
 */
public class PurseFragment extends Fragment {
    //全部资产
    private TextView tvPurseAllAsset;
    //充提记录
//    private TextView tvPurseRW;
    //交易记录
    private TextView tvPurseDeal;
    //全部挂单
    private TextView tvPurseAllRemain;
    //备份
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

    private TextView tvUserLogoff;

    private FrameLayout flPurse;
    private FrameLayout flPurseLoginPrompt;

    private TextView tvGoLogin;

    private TextView tvGoRegister;

    private ScrollView scrollView;
    private MyConstraintLayout clPurse;

    public PurseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_purse, container, false);
        init(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        fillIn();
        yesOrNoLogin();

    }



    private void yesOrNoLogin() {
        if (BtslandApplication.accountObject==null) {
          // flPurseLoginPrompt.setVisibility(View.VISIBLE);
           flPurseLoginPrompt.setVisibility(View.VISIBLE);
           flPurseLoginPrompt.setOnTouchListener(new View.OnTouchListener(){
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
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

            if(String.valueOf(BtslandApplication.accountObject.totalCNY)==null){
                tvPurseConvert.setText(" 正在计算中。。");
            }else if(String.valueOf(BtslandApplication.accountObject.totalCNY).length()>8){
                tvPurseConvert.setText(String.valueOf(BtslandApplication.accountObject.totalCNY).substring(0,8) + BtslandApplication.chargeUnit);
            }else {
                tvPurseConvert.setText(String.valueOf(BtslandApplication.accountObject.totalCNY) + BtslandApplication.chargeUnit);
            }
            tvUserAnotherName.setText("#" + BtslandApplication.accountObject.id.get_instance());
        } else {
            portrait.stopLoading();
            portrait.clearHistory();
            tvUserName.setText("");
            tvPurseConvert.setText("");
            tvUserAnotherName.setText("");
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
//        tvPurseRW=view.findViewById(R.id.tv_purse_rw);
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

        portrait=view.findViewById(R.id.iv_user_pho);
        tvUserName=view.findViewById(R.id.tv_user_name);
        tvUserAnotherName=view.findViewById(R.id.tv_user_anotherName);
        tvUserLogoff=view.findViewById(R.id.tv_user_logoff);
        scrollView=view.findViewById(R.id.sv_purse);



//        tvPurseConvert.setText();




        TextViewOnCLickListener onCLickListener=new TextViewOnCLickListener();
        tvPurseAllAsset.setOnClickListener(onCLickListener);
        tvPurseDeal.setOnClickListener(onCLickListener);
        tvPurseAllRemain.setOnClickListener(onCLickListener);
        tvPurseBackup.setOnClickListener(onCLickListener);
        tvUserLogoff.setOnClickListener(onCLickListener);
        tvGoLogin.setOnClickListener(onCLickListener);
        tvPurseTransferAccounts.setOnClickListener(onCLickListener);
        tvGoRegister.setOnClickListener(onCLickListener);

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
//                case R.id.tv_purse_rw:
//                    //充值记录
//                    Intent rw=new Intent(getActivity(), PurseAccessRecordActivity.class);
//                    getActivity().startActivity(rw);
//                    break;
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
                    //钱包备份
                    Intent backup=new Intent(getActivity(), PurseWalletBackupActivity.class);
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