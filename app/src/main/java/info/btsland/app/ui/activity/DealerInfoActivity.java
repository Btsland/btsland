package info.btsland.app.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.IOException;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.account_object;
import info.btsland.app.exception.NetworkStatusException;
import info.btsland.app.ui.fragment.HeadFragment;
import info.btsland.app.ui.view.AppDialog;
import info.btsland.app.ui.view.PasswordDialog;
import info.btsland.exchange.entity.User;
import info.btsland.exchange.http.UserHttp;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DealerInfoActivity extends AppCompatActivity {
    private HeadFragment headFragment;
    private EditText edDealerName;
    private EditText edDealer;
    private EditText edAccount;
    private EditText edDepict;
    private EditText edInBro;
    private EditText edOutBro;
    private EditText edLowIn;
    private EditText edLowOut;
    private EditText edUpOut;
    private TextView tvConfirm;
    private TextView tvCancel;
    private KProgressHUD hud;
    private String TAG="DealerInfoActivity";
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_info);
        fillInHead();
        init();
        fillIn();
    }

    private void fillIn() {
        edDealerName.getEditableText().clear();
        if(BtslandApplication.dealer.getDealerName()!=null) {
            edDealerName.getEditableText().insert(0, BtslandApplication.dealer.getDealerName());
        }
        edAccount.getEditableText().clear();
        if(BtslandApplication.dealer.getAccount()!=null){
            edAccount.getEditableText().insert(0,BtslandApplication.dealer.getAccount());
        }
        edAccount.setFocusable(false);
        edAccount.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        edAccount.setClickable(false);
        edDealer.getEditableText().clear();
        if(BtslandApplication.dealer.getDealerId()!=null) {
            edDealer.getEditableText().insert(0, BtslandApplication.dealer.getDealerId());
        }
        edDealer.setFocusable(false);
        edDealer.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        edDealer.setClickable(false);
        edDepict.getEditableText().clear();
        if(BtslandApplication.dealer.getDepict()!=null){
            edDepict.getEditableText().insert(0,BtslandApplication.dealer.getDepict());
        }
        edInBro.getEditableText().clear();
        if(BtslandApplication.dealer.getBrokerageIn()!=null) {
            edInBro.getEditableText().insert(0, "" + (BtslandApplication.dealer.getBrokerageIn() * 100));
        }
        edOutBro.getEditableText().clear();
        if(BtslandApplication.dealer.getBrokerageOut()!=null) {
            edOutBro.getEditableText().insert(0, "" + (BtslandApplication.dealer.getBrokerageOut() * 100));
        }
        edLowIn.getEditableText().clear();
        if(BtslandApplication.dealer.getLowerLimitIn()!=null){
            edLowIn.getEditableText().insert(0,""+BtslandApplication.dealer.getLowerLimitIn());
        }
        edLowOut.getEditableText().clear();
        if(BtslandApplication.dealer.getLowerLimitOut()!=null){
            edLowOut.getEditableText().insert(0,""+BtslandApplication.dealer.getLowerLimitOut());
        }
        edUpOut.getEditableText().clear();
        if(BtslandApplication.dealer.getUpperLimitOut()!=null) {
            edUpOut.getEditableText().insert(0, "" + BtslandApplication.dealer.getUpperLimitOut());
        }
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fillIn();
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user=new User();
                String inBro=edInBro.getEditableText().toString();
                String outBro=edOutBro.getEditableText().toString();
                String dealerName=edDealerName.getEditableText().toString();
                String depict=edDepict.getEditableText().toString();
                String lowIn=edLowIn.getEditableText().toString();
                String lowOut=edLowOut.getEditableText().toString();
                String upOut=edUpOut.getEditableText().toString();
                if(BtslandApplication.dealer==null) {
                    return;
                }
                if(BtslandApplication.dealer.getDealerId()!=null){
                    user.setDealerId(BtslandApplication.dealer.getDealerId());
                }else {
                    user.setDealerId("");
                }

                user.setDealerName(dealerName);
                if(inBro!=null&&!inBro.equals("")){
                    user.setBrokerageIn(Double.valueOf(inBro)/100);
                }else {
                    user.setBrokerageIn(0.0);
                }
                if(outBro!=null&&!outBro.equals("")){
                    user.setBrokerageOut(Double.valueOf(outBro)/100);
                }else {
                    user.setBrokerageOut(0.0);
                }

                user.setDepict(depict);
                if(lowIn!=null&&!lowIn.equals("")){
                    user.setLowerLimitIn(Double.valueOf(lowIn));
                }else {
                    user.setLowerLimitIn(0.0);
                }
                if(lowOut!=null&&!lowOut.equals("")){
                    user.setLowerLimitOut(Double.valueOf(lowOut));
                }else {
                    user.setLowerLimitOut(0.0);
                }
                if(upOut!=null&&!upOut.equals("")){
                    user.setUpperLimitOut(Double.valueOf(upOut));
                }else {
                    user.setUpperLimitOut(0.0);
                }

                Log.e(TAG, "onClick: "+BtslandApplication.dealer );
                PasswordDialog passwordDialog=new PasswordDialog(DealerInfoActivity.this);
                passwordDialog.setMsg("请输入密码");
                passwordDialog.setListener(new PasswordDialog.OnDialogInterationListener() {
                    @Override
                    public void onConfirm(AlertDialog dialog, final String passwordString) {
                        if(hud==null){
                            hud=KProgressHUD.create(DealerInfoActivity.this);
                        }
                        hud.setLabel("请稍等。。。");
                        hud.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    account_object accountObject=BtslandApplication.getWalletApi().import_account_password(BtslandApplication.accountObject.name,passwordString);
                                    if(accountObject!=null){
                                        UserHttp.updateUser(user.getDealerId(),user, new Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {

                                            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                String json= response.body().string();
                                                if(json.indexOf("error")!=-1){
                                                    BtslandApplication.sendBroadcastDialog(DealerInfoActivity.this,json);
                                                    handler.sendEmptyMessage(0);
                                                }else {
                                                    int a= Integer.parseInt(json);
                                                    handler.sendEmptyMessage(a);
                                                }
                                            }
                                        });
                                    }else {
                                        handler.sendEmptyMessage(-1);
                                    }
                                } catch (NetworkStatusException e) {
                                    e.printStackTrace();
                                    handler.sendEmptyMessage(0);
                                }

                            }
                        }).start();

                    }

                    @Override
                    public void onReject(AlertDialog dialog) {

                    }
                });
                passwordDialog.show();

            }
        });
    }
    class saveUserInfo extends Thread{



    }

    private void fillInBody() {

    }

    private void init() {
        edDealerName=findViewById(R.id.ed_dealer_info_dealerName);
        edDealer=findViewById(R.id.ed_dealer_info_dealer);
        edAccount=findViewById(R.id.ed_dealer_info_account);
        edDepict=findViewById(R.id.ed_dealer_info_depict);
        edInBro=findViewById(R.id.ed_dealer_info_inBro);
        edOutBro=findViewById(R.id.ed_dealer_info_outBro);
        tvCancel=findViewById(R.id.tv_dealer_info_cancel);
        tvConfirm=findViewById(R.id.tv_dealer_info_confirm);
        edLowIn=findViewById(R.id.ed_dealer_info_lowIn);
        edLowOut=findViewById(R.id.ed_dealer_info_lowOut);
        edUpOut=findViewById(R.id.ed_dealer_info_upOut);
    }

    private void fillInHead(){
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment==null){
            headFragment= HeadFragment.newInstance(HeadFragment.HeadType.BACK_NULL,"账户信息");
            transaction.add(R.id.fra_dealer_info_head,headFragment);
        }
        transaction.commit();
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(hud!=null&&hud.isShowing()){
                hud.dismiss();
                hud=null;
            }
            if(msg.what>0){
                AppDialog appDialog=new AppDialog(DealerInfoActivity.this);
                appDialog.setMsg("更新成功");
                appDialog.show();
            }else if(msg.what==-1){
                AppDialog appDialog=new AppDialog(DealerInfoActivity.this);
                appDialog.setMsg("密码错误");
                appDialog.show();
            }else if(msg.what==0) {
                AppDialog appDialog=new AppDialog(DealerInfoActivity.this);
                appDialog.setMsg("更新失败");
                appDialog.show();
            }
        }
    };
}
