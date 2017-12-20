package info.btsland.app.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;
import info.btsland.app.ui.view.AppDialog;
import info.btsland.app.util.NumericUtil;
import info.btsland.exchange.entity.Note;
import info.btsland.exchange.entity.RealAsset;
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
    private User user=new User();

    private TextView tvDealerName;
    private TextView tvDealerId;
    private ListView lvTypes;
    private TextView tvNoteNo;
    private TextView tvQuote;
    private TextView tvBrokerage;
    private Spinner spPays;
    private EditText edtotalNum;
    private TextView tvRemarkCode;
    private EditText edRemark;
    private TextView tvRemafkCodeCopy;

    private TextView tvConfirm;
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
        AppDialog appDialog=new AppDialog(C2CExchangeActivity.this);
        appDialog.setMsg("C2C模块系统测试已经完成，APP客户端入口即将开发完成，当前展示的内容为测试数据，感谢您的使用和支持。");
        appDialog.show();
    }

    private void init() {
        tvDealerName=findViewById(R.id.tv_exchange_dealerName_text);
        tvDealerId=findViewById(R.id.tv_exchange_dealerId_text);
        tvBrokerage=findViewById(R.id.tv_exchange_brokerage_text);
        tvNoteNo=findViewById(R.id.tv_exchange_noteNo_text);
        tvQuote=findViewById(R.id.tv_exchange_quote_text);
        lvTypes=findViewById(R.id.lv_exchange_type_text);
        spPays=findViewById(R.id.tv_exchange_pay_text);
        edtotalNum=findViewById(R.id.ed_exchange_aspect_text);
        edRemark=findViewById(R.id.ed_exchange_remark_text);
        tvRemarkCode=findViewById(R.id.tv_exchange_remarkCode_text);
        tvRemafkCodeCopy=findViewById(R.id.tv_exchange_remarkCode_text_copy);
        tvConfirm=findViewById(R.id.tv_c2c_confirm);
    }

    private void fillIn() {
        final int[] a = {0};
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "run: account:"+account );
                UserHttp.queryAccount(account, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String str = response.body().string();
                        Log.e(TAG, "onResponse:account: "+str);
                        fillInAccount(str);
                        a[0]++;
                        if(a[0]==3) {
                            handler.sendEmptyMessage(1);
                        }
                    }
                });
            }
        }).start();

        if(type==IN){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    UserHttp.queryDealer(dealerId, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String str = response.body().string();
                            Log.e(TAG, "onResponse:dealer: "+str);
                            fillInUser(str);
                            a[0]++;
                            if(a[0]==3) {
                                handler.sendEmptyMessage(1);
                            }
                        }
                    });
                    TradeHttp.createNote(account, "CNY", dealerId, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String str = response.body().string();
                            fillInNote(str);
                            a[0]++;
                            if(a[0]==3){
                                handler.sendEmptyMessage(1);
                                //查询保证金
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        max = BtslandApplication.getMarketStat().mWebsocketApi.getAssetTotalByAccountAndCoin(dealer.getAccount(),"CNY");
                                    }
                                }).start();
                            }
                        }
                    });
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {

                }
            }).start();

        }else {

        }

    }

    private void fillInAccount(String json) {
        Gson gson=new Gson();
        user = gson.fromJson(json,new TypeToken<User>() {}.getType());
    }

    private void fillInUser(String json) {
        Gson gson=new Gson();
        dealer = gson.fromJson(json,new TypeToken<User>() {}.getType());
    }

    private void fillInNote(String json){
        Gson gson=new Gson();
        note = gson.fromJson(json,new TypeToken<Note>() {}.getType());
    }

    private void fillInBody() {
        Log.e(TAG, "fillInBody: " );
        tvDealerName.setText(dealer.getDealerName());
        tvDealerId.setText(dealer.getDealerId());
        if(type==IN) {
            tvQuote.setText("CNY");
        }else if(type==OUT) {
            tvQuote.setText("RMB");
        }
        tvNoteNo.setText(note.getNoteNo());
        tvBrokerage.setText(""+note.getBrokerage()*100+"%");
        tvRemarkCode.setText(note.getRemarkCode());
        Log.e(TAG, "fillInBody: dealer.realAssets:"+dealer.realAssets.size() );
        if(dealer.realAssets!=null&&dealer.realAssets.size()>0) {
            String[] strings = new String[dealer.realAssets.size()];
            for (int i = 0; i < dealer.realAssets.size(); i++) {
                strings[i] = dealer.realAssets.get(i).getRealAssetNo()+"("+dealer.realAssets.get(i).getDepict()+")";
            }
            Log.e(TAG, "fillInBody:dealer:"+strings.length );
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.coin_item, R.id.tv_transfer_coinName, strings);
            lvTypes.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            setListViewHeightBasedOnChildren(lvTypes);
        }
        if(user.realAssets!=null&&user.realAssets.size()>0) {
            String[] strings = new String[user.realAssets.size()];
            for (int i = 0; i < user.realAssets.size(); i++) {
                strings[i] = user.realAssets.get(i).getRealAssetNo()+"("+user.realAssets.get(i).getDepict()+")";
            }
            Log.e(TAG, "fillInBody: user:"+strings.toString() );
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.coin_item, R.id.tv_transfer_coinName, strings);
            spPays.setAdapter(adapter);
            spPays.setSelection(0);
            adapter.notifyDataSetChanged();
        }
        spPays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spPaysIndex=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                String num= edtotalNum.getText().toString();
                if(num==null||num.equals("")){
                    AppDialog appDialog=new AppDialog(C2CExchangeActivity.this);
                    appDialog.setMsg("请输入充值额度");
                    appDialog.show();
                    return;
                }
                if(user.realAssets==null||user.realAssets.size()==0){
                    AppDialog appDialog=new AppDialog(C2CExchangeActivity.this);
                    appDialog.setMsg("您没有付款帐号");
                    appDialog.show();
                    return;
                }
                RealAsset realAsset = user.realAssets.get(spPaysIndex);
                String depict = edRemark.getText().toString();
                note.setRealNo(realAsset.getRealAssetNo());
                note.setRealType(realAsset.getRealAssetType());
                note.setRealDepict(realAsset.getDepict());
                note.setAssetNum(NumericUtil.parseDouble(num));
                note.setDepict(depict);
                SaveNote saveNote=new SaveNote(user.getAccount(), note, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        int a = Integer.parseInt(response.body().string());
                        Log.i(TAG, "onResponse: "+a);
                        Bundle bundle=new Bundle();
                        bundle.putInt("a",a);
                        Message message=Message.obtain();
                        message.setData(bundle);
                        saveHandler.sendMessage(message);
                    }
                });
                saveNote.start();
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

    private Handler saveHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int a= msg.getData().getInt("a");
            AppDialog appDialog=new AppDialog(C2CExchangeActivity.this);
            if(a>0){
                appDialog.setMsg("订单发起成功，线下完成转账后请确认转账。");
                appDialog.show();
            }else if(a==0){
                appDialog.setMsg("订单失效，请重试或请重新生成订单。");
                appDialog.show();
            }else if(a==-1) {
                appDialog.setMsg("不能发起重复的订单。");
                appDialog.show();
            }
        }
    };

}
