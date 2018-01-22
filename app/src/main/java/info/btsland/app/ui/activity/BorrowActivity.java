package info.btsland.app.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.account_object;
import info.btsland.app.exception.NetworkStatusException;
import info.btsland.app.model.IAsset;
import info.btsland.app.ui.fragment.HeadFragment;
import info.btsland.app.ui.view.AppDialog;
import info.btsland.app.ui.view.PasswordDialog;

public class BorrowActivity extends AppCompatActivity {


    private String TAG="BorrowActivity";
    private HeadFragment headFragment;
    private Double num1=0.0;
    private Double num2=0.0;
    private Double scale=0.0;
    PasswordDialog passwordDialog;
    String password;
    private Double feedPrice=3.4444;
    private Double triggerPrice=0.0;

    private EditText edNum1;
    private EditText edNum2;
    private TextView tvBtn;
    private TextView tvFeedPrice;
    private TextView tvFeedPriceCoin;
    private TextView tvTriggerPrice;
    private TextView tvTriggerPriceCoin;
    private TextView tvScale;
    private TextView tvBalancNum;
    private TextView tvBalancCoin;
    private TextView tvBTSBalance;
    private SeekBar sbScale;
    private Spinner spType;
    private TextView tvDepict;
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
        tvBalancNum=findViewById(R.id.tv_borrow_balance);
        tvBalancCoin=findViewById(R.id.tv_borrow_balance_coin);
        tvBTSBalance=findViewById(R.id.tv_borrow_BTSBalance);
        sbScale=findViewById(R.id.sb_Borrow_BTSNum);
        spType=findViewById(R.id.sp_borrow_type);
        tvDepict=findViewById(R.id.tv_borrow_depict);
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
    private void fillInFeedPrice(){
        tvFeedPrice.setText(""+feedPrice);

    }
    private void fillIn() {
        tvFeedPrice.setText(""+feedPrice);
        List<String> list =new ArrayList<>();
        list.add("CNY");
        list.add("BTC");
        list.add("USD");
        list.add("EUR");
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(BorrowActivity.this,R.layout.coin_item,R.id.tv_transfer_coinName,list);
        spType.setAdapter(adapter);
        spType.setSelection(0);
        String coin = (String) spType.getItemAtPosition(0);
        tvBalancCoin.setText(coin);
        synchronized (BtslandApplication.iAssets) {
            if(BtslandApplication.iAssets!=null){
                int a=0;
                for (int i = 0; i < BtslandApplication.iAssets.size(); i++) {
                    IAsset iAsset = BtslandApplication.iAssets.get(i);
                    if (iAsset.coinName.equals(coin)) {
                        tvBalancNum.setText(String.valueOf(iAsset.total));
                        a++;
                        if(a==2){
                            break;
                        }
                    }
                    if (iAsset.coinName.equals("BTS")) {
                        a++;
                        tvBTSBalance.setText(String.valueOf(iAsset.total));
                        if(a==2){
                            break;
                        }
                    }
                }
            }
        }
        edNum1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str=s.toString();
                if(str.indexOf(".")==0){
                    StringBuilder sb=new StringBuilder(str);
                    sb.insert(0,"0");
                }
                if(str.equals("")){
                    num1=0.0;
                    return;
                }
                num1= Double.valueOf(str);
            }
        });

        sbScale.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Double num= Double.valueOf(progress);
                DecimalFormat df = new DecimalFormat("#.####");
                scale= num/100;
                tvScale.setText(""+scale);
                num2=num1/feedPrice*scale;
                edNum2.getText().clear();
                edNum2.getText().insert(0,df.format(num2));
                triggerPrice=num1/(num2/1.75);
                tvTriggerPrice.setText(df.format(triggerPrice));
                if(scale>1.75&&scale<2.5){
                    tvTriggerPrice.setTextColor(getResources().getColor(R.color.color_yellow_red));
                    tvScale.setTextColor(getResources().getColor(R.color.color_yellow_red));
                    tvDepict.setText("你的抵押率已接近最低抵押率 1.75 ，这意味着如果价格继续下跌你的头寸有可能被强制平仓");
                    tvDepict.setTextColor(getResources().getColor(R.color.color_yellow_red));
                }else if(scale>2.5){
                    tvTriggerPrice.setTextColor(getResources().getColor(R.color.black));
                    tvScale.setTextColor(getResources().getColor(R.color.black));
                    tvDepict.setText("");
                }else {
                    tvTriggerPrice.setTextColor(getResources().getColor(R.color.color_font_red));
                    tvScale.setTextColor(getResources().getColor(R.color.color_font_red));
                    tvDepict.setTextColor(getResources().getColor(R.color.color_font_red));
                    tvDepict.setText("保证金抵押率低于维持保证金要求");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        tvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                if (BtslandApplication.getWalletApi().borrow_asset(num1.toString(), "CNY", num2.toString()) != null) {
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
