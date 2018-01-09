package info.btsland.app.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.IOException;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.account_object;
import info.btsland.app.exception.NetworkStatusException;
import info.btsland.app.ui.fragment.HeadFragment;
import info.btsland.app.ui.view.AppDialog;
import info.btsland.app.ui.view.PasswordDialog;
import info.btsland.app.util.NumericUtil;
import info.btsland.exchange.entity.Note;
import info.btsland.exchange.entity.User;
import info.btsland.exchange.http.TradeHttp;
import info.btsland.exchange.http.UserHttp;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class C2CExchangeActivity extends AppCompatActivity {
    private HeadFragment headFragment;

    private int type=1;
    public static final int IN=1;//进（充值）
    public static final int OUT=2;//出（提现）

    private User dealer;
    private Double max;
    private String account;
    private String dealerId;
    private Note note=new Note();

    private TextView tvDealerName;
    private TextView tvDealerId;
    private ListView lvTypes;
    private TextView tvNoteNo;
    private TextView tvQuote;
    private TextView tvBrokerage;
//    private Spinner spPays;
    private EditText edtotalNum;
    private TextView tvRemarkCode;
    private EditText edRemark;
    private TextView tvRemafkCodeCopy;
    private TextView tvAspect;
//    private TextView tvPays;
    private LinearLayout llTypes;

    private TextView tvConfirm;
    private TextView tvCancel;
    private String TAG ="C2CExchangeActivity";
    private int spPaysIndex;

    public C2CExchangeActivity() {
    }

    public static void startAction(Context context,String account, int type, String dealerId) {
        Intent intent=new Intent(context,C2CExchangeActivity.class);
        intent.putExtra("type",type);
        intent.putExtra("account",account);
        intent.putExtra("dealerId",dealerId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c2c_exchange);
        type = getIntent().getIntExtra("type",type);
        account = getIntent().getStringExtra("account");
        dealerId = getIntent().getStringExtra("dealerId");
        fillInHead();
        init();
        fillIn();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void init() {
        tvDealerName=findViewById(R.id.tv_exchange_dealerName_text);
        tvDealerId=findViewById(R.id.tv_exchange_dealerId_text);
        tvBrokerage=findViewById(R.id.tv_exchange_brokerage_text);
        tvNoteNo=findViewById(R.id.tv_exchange_noteNo_text);
        tvQuote=findViewById(R.id.tv_exchange_quote_text);
        lvTypes=findViewById(R.id.lv_exchange_type_text);
//        spPays=findViewById(R.id.sp_exchange_pay_text);
        edtotalNum=findViewById(R.id.ed_exchange_aspect_text);
        edRemark=findViewById(R.id.ed_exchange_remark_text);
        tvRemarkCode=findViewById(R.id.tv_exchange_remarkCode_text);
        tvRemafkCodeCopy=findViewById(R.id.tv_exchange_remarkCode_text_copy);
        tvConfirm=findViewById(R.id.tv_c2c_confirm);
        tvAspect=findViewById(R.id.tv_exchange_aspect);
//        tvPays=findViewById(R.id.tv_exchange_pay_text);
        llTypes=findViewById(R.id.ll_exchange_type);
        tvCancel=findViewById(R.id.tv_c2c_cancel);
        if(type==IN){
            tvConfirm.setText("确认充值");
        }else if(type==OUT){
            tvConfirm.setText("确认提现");
        }
    }

    private void fillIn() {
        Log.e(TAG, "fillIn: " );
        final int[] a = {0};
        if(type==IN){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    UserHttp.queryDealer(dealerId, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {}
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String json = response.body().string();
                            if(json.indexOf("error")!=-1){
                                BtslandApplication.sendBroadcastDialog(C2CExchangeActivity.this,json);
                            }else {
                                fillInUser(json);
                                a[0]++;
                                if (a[0] == 2) {
                                    handler.sendEmptyMessage(1);
                                }
                            }
                        }
                    });
                    TradeHttp.createNote(account, "CNY", dealerId, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String json = response.body().string();
                            if(json.indexOf("error")!=-1){
                                //BtslandApplication.sendBroadcastDialog(C2CExchangeActivity.this,json);
                            }else {
                                fillInNote(json);
                                a[0]++;
                                if (a[0] == 2) {
                                    handler.sendEmptyMessage(1);
                                    //查询保证金
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            max = BtslandApplication.getMarketStat().mWebsocketApi.getAssetTotalByAccountAndCoin(dealer.getDealerId(), "CNY");
                                        }
                                    }).start();
                                }
                            }
                        }
                    });
                }
            }).start();

        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    UserHttp.queryDealer(dealerId, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {}
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String json = response.body().string();
                            if(json.indexOf("error")!=-1){
                                //BtslandApplication.sendBroadcastDialog(C2CExchangeActivity.this,json);
                            }else {
                                fillInUser(json);
                                a[0]++;
                                if (a[0] == 2) {
                                    handler.sendEmptyMessage(1);
                                }
                            }
                        }
                    });
                    TradeHttp.createNote(account, "RMB", dealerId, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String json = response.body().string();
                            if(json.indexOf("error")!=-1){
                                BtslandApplication.sendBroadcastDialog(C2CExchangeActivity.this,json);
                            }else {
                                fillInNote(json);
                                a[0]++;
                                if (a[0] == 2) {
                                    handler.sendEmptyMessage(1);
                                }
                            }
                        }
                    });
                }
            }).start();

        }

    }


    private void fillInUser(String json) {
        Gson gson=new Gson();
        dealer = gson.fromJson(json,new TypeToken<User>() {}.getType());
    }

    private void fillInNote(String json){
        Gson gson=new Gson();
        note = gson.fromJson(json,new TypeToken<Note>() {}.getType());
    }
    private KProgressHUD hud;
    private void fillInBody() {
        Log.e(TAG, "fillInBody: " );
        tvDealerName.setText(dealer.getDealerName());
        tvDealerId.setText(dealer.getDealerId());
        if(type==IN) {
            tvAspect.setText("充值额度：");
            tvQuote.setText("CNY");
//            tvPays.setVisibility(View.GONE);
//            spPays.setVisibility(View.VISIBLE);

        }else if(type==OUT) {
            llTypes.setVisibility(View.GONE);
            tvAspect.setText("提现额度：");
            tvQuote.setText("RMB");
//            tvPays.setVisibility(View.VISIBLE);
//            tvPays.setText(BtslandApplication.accountObject.name);
//            spPays.setVisibility(View.GONE);
        }
        if(note.getNoteNo()!=null){
            tvNoteNo.setText(note.getNoteNo());
        }else {
            tvNoteNo.setText("");
        }
        if(note.getBrokerage()!=null){
            tvBrokerage.setText(""+note.getBrokerage()*100+"%");
        }else {
            tvBrokerage.setText("0.0%");
        }
        if(note.getRemarkCode()!=null){
            tvRemarkCode.setText(note.getRemarkCode());
        }else {
            tvRemarkCode.setText("");
        }
        Log.e(TAG, "fillInBody: dealer.realAssets:"+dealer.realAssets.size() );
        if(dealer.realAssets!=null&&dealer.realAssets.size()>0) {
            String[] strings = new String[dealer.realAssets.size()];
            for (int i = 0; i < dealer.realAssets.size(); i++) {
                if(dealer.realAssets.get(i).getDepict()!=null&&!dealer.realAssets.get(i).getDepict().equals("")){
                    int a = dealer.realAssets.get(i).getDepict().indexOf("(");
                    if(a==-1){
                        strings[i] = dealer.realAssets.get(i).getRealAssetNo()+"("+dealer.realAssets.get(i).getDepict()+")";
                    }else {
                        strings[i] = dealer.realAssets.get(i).getRealAssetNo()+"("+dealer.realAssets.get(i).getDepict().substring(0,a)+")";
                    }
                }
            }
            Log.e(TAG, "fillInBody:dealer:"+strings.length );
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.coin_item, R.id.tv_transfer_coinName, strings);
            lvTypes.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            setListViewHeightBasedOnChildren(lvTypes);
        }
        if(BtslandApplication.dealer.realAssets!=null&&BtslandApplication.dealer.realAssets.size()>0) {
            String[] strings = new String[BtslandApplication.dealer.realAssets.size()];
            for (int i = 0; i < BtslandApplication.dealer.realAssets.size(); i++) {
                if(BtslandApplication.dealer.realAssets.get(i).getDepict()!=null&&!BtslandApplication.dealer.realAssets.get(i).getDepict().equals("")) {
                    int a = BtslandApplication.dealer.realAssets.get(i).getDepict().indexOf("(");
                    if (a == -1) {
                        strings[i] = BtslandApplication.dealer.realAssets.get(i).getRealAssetNo() + "(" + BtslandApplication.dealer.realAssets.get(i).getDepict() + ")";
                    } else {
                        strings[i] = BtslandApplication.dealer.realAssets.get(i).getRealAssetNo() + "(" + BtslandApplication.dealer.realAssets.get(i).getDepict().substring(0, a) + ")";
                    }
                }
            }
            Log.e(TAG, "fillInBody: user:"+strings.toString() );
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.coin_item, R.id.tv_transfer_coinName, strings);
//            spPays.setAdapter(adapter);
//            spPays.setSelection(0);
            adapter.notifyDataSetChanged();
        }
//        spPays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                spPaysIndex=i;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
        tvRemafkCodeCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label", tvRemarkCode.getText().toString());
                cm.setPrimaryClip(mClipData);
                Toast.makeText(C2CExchangeActivity.this,"已复制到剪贴板",Toast.LENGTH_SHORT).show();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String num= edtotalNum.getText().toString();
                if(num==null||num.equals("")){
                    AppDialog appDialog=new AppDialog(C2CExchangeActivity.this);
                    appDialog.setMsg("请输入充值额度");
                    appDialog.show();
                    return;
                }

                if(BtslandApplication.dealer.realAssets==null||BtslandApplication.dealer.realAssets.size()==0||BtslandApplication.dealer.realAssets.get(0).getRealAssetNo()==null||BtslandApplication.dealer.realAssets.get(0).getRealAssetNo().equals("")){
                    AppDialog appDialog=new AppDialog(C2CExchangeActivity.this);
                    appDialog.setMsg("您没有付款帐号,请设置");
                    appDialog.setListener(new AppDialog.OnDialogInterationListener() {
                        @Override
                        public void onConfirm() {
                            Intent intent=new Intent(C2CExchangeActivity.this,AccountC2CTypesActivity.class);
                            intent.putExtra("type",0);
                            startActivity(intent);
                        }

                        @Override
                        public void onReject() {

                        }
                    });
                    appDialog.show();
                    return;
                }Double iNum= Double.valueOf(num);
                if(type==IN){
                    if(iNum<dealer.getLowerLimitIn()){
                        AppDialog appDialog=new AppDialog(C2CExchangeActivity.this);
                        appDialog.setMsg("充值额度不能低于额度"+dealer.getLowerLimitIn());
                        appDialog.show();
                        return;
                    }
                }else if(type==OUT){
                    if(iNum<dealer.getLowerLimitOut()){
                        AppDialog appDialog=new AppDialog(C2CExchangeActivity.this);
                        appDialog.setMsg("充值额度不能低于额度"+dealer.getLowerLimitOut());
                        appDialog.show();
                        return;
                    }else if(iNum>dealer.getUpperLimitOut()) {
                        AppDialog appDialog=new AppDialog(C2CExchangeActivity.this);
                        appDialog.setMsg("充值额度不能大于额度"+dealer.getLowerLimitOut());
                        appDialog.show();
                        return;
                    }
                }
                String depict = edRemark.getText().toString();
                note.setAssetNum(NumericUtil.parseDouble(num));
                note.setDepict(depict);
                PasswordDialog passwordDialog=new PasswordDialog(C2CExchangeActivity.this);
                passwordDialog.setMsg("请输入密码");
                passwordDialog.setListener(new PasswordDialog.OnDialogInterationListener() {
                    @Override
                    public void onConfirm(AlertDialog dialog, final String passwordString) {
                        hudHandler.sendEmptyMessage(1);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    account_object accountObject = BtslandApplication.getWalletApi().import_account_password(BtslandApplication.accountObject.name,passwordString);
                                    if(accountObject!=null){
                                        Log.e(TAG, "run: "+note);
                                        SaveNote saveNote=new SaveNote(BtslandApplication.dealer.getAccount(), note, new Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {

                                            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                String json = response.body().string();
                                                Bundle bundle = new Bundle();
                                                String noteNo="";
                                                int a=0;
                                                if(json.indexOf("error")!=-1){
                                                    BtslandApplication.sendBroadcastDialog(C2CExchangeActivity.this,json);
                                                    a=-3;
                                                }else {
                                                    noteNo=json;
                                                    if(noteNo.equals("")){
                                                        a=-3;
                                                    }else {
                                                        a=1;
                                                    }
                                                }
                                                bundle.putInt("a", a);
                                                bundle.putString("noteNo",noteNo);
                                                Message message = Message.obtain();
                                                message.setData(bundle);
                                                saveHandler.sendMessage(message);

                                            }
                                        });
                                        saveNote.start();
                                    }else {
                                        Log.e(TAG, "run: 密码错误" );
                                        Bundle bundle=new Bundle();
                                        bundle.putInt("a",-2);
                                        bundle.putString("noteNo","");
                                        Message message=Message.obtain();
                                        message.setData(bundle);
                                        saveHandler.sendMessage(message);
                                    }
                                } catch (NetworkStatusException e) {
                                    e.printStackTrace();
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
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dealer!=null&&dealer.getAccount()!=null) {
                    Intent intent = new Intent(C2CExchangeActivity.this, ChatActivity.class);
                    intent.putExtra("account", dealer.getAccount());
                    startActivity(intent);
                }
            }
        });
    }

    class SaveNote extends Thread{
        private Callback callback;
        private Note note;
        private String account;
        public SaveNote(String account,Note note,Callback callback) {
            this.account=account;
            this.note=note;
            this.callback=callback;
        }

        @Override
        public void run() {
            Gson gson=new Gson();
            TradeHttp.saveNote(account,gson.toJson(note),callback);
        }
    }


    /**
     * 装载顶部导航
     */
    private void fillInHead(){
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment==null){
            String title="";
            if(type==IN){
                title="充值";
            }else {
                title="提现";
            }
            headFragment= HeadFragment.newInstance(HeadFragment.HeadType.BACK_NULL,title);
            transaction.add(R.id.fra_C2C_head,headFragment);
        }
        transaction.commit();
    }

    /**
     * 设置listView高度
     * @param listView
     */
    private void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) {
            return;
        }
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            fillInBody();
        }
    };
    private Handler hudHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(hud==null){
                hud=KProgressHUD.create(C2CExchangeActivity.this);
                hud.setLabel("请稍等。。。");
            }
            hud.show();
        }
    };
    private Handler saveHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int a= msg.getData().getInt("a");
            final String noteNo=msg.getData().getString("noteNo");
            if(hud!=null&&hud.isShowing()){
                hud.dismiss();
                hud=null;
            }
            AppDialog appDialog=new AppDialog(C2CExchangeActivity.this);
            if(a>0){
                if(type==IN) {
                    appDialog.setMsg("订单发起成功，线下完成转账后请确认转账");
                    appDialog.setListener(new AppDialog.OnDialogInterationListener() {
                        @Override
                        public void onConfirm() {
                            Intent intent = new Intent(C2CExchangeActivity.this, ExchangeDetailedActivity.class);
                            intent.putExtra("noteNo", noteNo);
                            intent.putExtra("type",type);
                            startActivity(intent);
                        }

                        @Override
                        public void onReject() {

                        }
                    });
                    appDialog.show();
                }else if(type==OUT){
                    appDialog.setMsg("订单发起成功，完善订单并且向承兑商资金账户进行转账后，承兑商将会给您提供的收款账户进行转账");
                    appDialog.setListener(new AppDialog.OnDialogInterationListener() {
                        @Override
                        public void onConfirm() {
                            Intent intent = new Intent(C2CExchangeActivity.this, ExchangeDetailedActivity.class);
                            intent.putExtra("noteNo", noteNo);
                            intent.putExtra("type",type);
                            startActivity(intent);
                        }

                        @Override
                        public void onReject() {

                        }
                    });
                    appDialog.show();
                }
            }else if(a==0){
                appDialog.setMsg("订单失效，请重试或请重新生成订单");
                appDialog.show();
            }else if(a==-1) {
                appDialog.setMsg("不能发起重复的订单");
                appDialog.show();
            }else if(a==-2){
                appDialog.setMsg("密码错误");
                appDialog.show();
            }else if(a==-3){
                appDialog.setMsg("订单发起失败，请重试");
                appDialog.show();
            }
        }
    };

}
