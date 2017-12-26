package info.btsland.app.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.account_object;
import info.btsland.app.exception.NetworkStatusException;
import info.btsland.app.ui.fragment.HeadFragment;
import info.btsland.app.ui.view.AppDialog;
import info.btsland.app.ui.view.PasswordDialog;

public class BorrowActivity extends AppCompatActivity {


    private String TAG="BorrowActivity";
    private HeadFragment headFragment;
    String num1;
    String num2;
    PasswordDialog passwordDialog;
    String password;

    private EditText edNum1;
    private EditText edNum2;
    private TextView tvBtn;
    private TextView tvFeedPrice;
    private TextView tvFeedPriceCoin;
    private TextView tvTriggerPrice;
    private TextView tvTriggerPriceCoin;
    private TextView tvScale;
    private TextView tvBalance;
    private TextView tvBalancCoin;
    private TextView tvBTSBalance;
    private SeekBar sbBorrowNum;
    private SeekBar sbBTSNum;
    private TextView tvCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow);
        fillInHead();
        init();
        fillIn();
    }
    private void init() {
        edNum1=findViewById(R.id.tv_borrow_num1);
        edNum2=findViewById(R.id.tv_borrow_num2);
        tvBtn=findViewById(R.id.tv_borrow_btn);
        tvFeedPrice=findViewById(R.id.tv_borrow_feedPrice);
        tvFeedPriceCoin=findViewById(R.id.tv_borrow_feedPrice_coin);
        tvTriggerPrice=findViewById(R.id.tv_borrow_triggerPrice);
        tvTriggerPriceCoin=findViewById(R.id.tv_borrow_triggerPrice_coin);
        tvScale=findViewById(R.id.tv_borrow_scale);
        tvBalance=findViewById(R.id.tv_borrow_scale);
        tvBalancCoin=findViewById(R.id.tv_borrow_balance_coin);
        tvBTSBalance=findViewById(R.id.tv_borrow_BTSBalance);
        sbBorrowNum=findViewById(R.id.sb_Borrow_BorrowNum);
        sbBTSNum=findViewById(R.id.sb_Borrow_BTSNum);
        tvCancel=findViewById(R.id.tv_borrow_cancel);
    }
    private void fillInHead(){
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment==null){
            headFragment= HeadFragment.newInstance(HeadFragment.HeadType.BACK_NULL,"抵押");
            transaction.add(R.id.fra_borrow_head,headFragment);
        }
        transaction.commit();
    }

    private void fillIn() {
        tvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num1=edNum1.getEditableText().toString();
                num2=edNum2.getEditableText().toString();
                passwordDialog=new PasswordDialog(BorrowActivity.this);
                passwordDialog.setListener(new PasswordDialog.OnDialogInterationListener() {
                    @Override
                    public void onConfirm(final AlertDialog dialog, final String passwordString) {
                        dialog.dismiss();
                        account_object accountObject= null;
                        try {
                            accountObject = BtslandApplication.getWalletApi().import_account_password(BtslandApplication.accountObject.name,passwordString);
                        } catch (NetworkStatusException e) {
                            e.printStackTrace();
                        }
                        if(accountObject!=null) {
                            password=passwordString;
                            borrow();
                        }else {
                            handler.sendEmptyMessage(-2);
                        }
                    }
                    @Override
                    public void onReject(AlertDialog dialog) {

                    }
                });
                passwordDialog.show();

            }
        });
    }
    private void borrow(){
        Log.e(TAG, "borrow: ");
        if (BtslandApplication.getWalletApi().unlock(password) == 0) {
            try {
                if (BtslandApplication.getWalletApi().borrow_asset(num1, "CNY", num2) != null) {
                    handler.sendEmptyMessage(1);

                } else {
                    handler.sendEmptyMessage(-1);
                }
            } catch (NetworkStatusException e) {
                e.printStackTrace();
            }
        }else {
            handler.sendEmptyMessage(-1);
        }
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG, "handleMessage: "+msg.what );
            if(msg.what==1){
                AppDialog appDialog=new AppDialog(BorrowActivity.this,"提示","成功！");
                appDialog.show();
            }else if(msg.what==-1) {
                AppDialog appDialog=new AppDialog(BorrowActivity.this,"提示","失败！");
                appDialog.show();
            }else if(msg.what==-2) {
                AppDialog appDialog=new AppDialog(BorrowActivity.this,"提示","密码错误！");
                appDialog.show();
            }
        }
    };


}
