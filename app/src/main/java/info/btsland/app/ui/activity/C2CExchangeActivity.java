package info.btsland.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;
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

    private User user;
    private Double max;
    private String remarkCode;
    private String account;
    private String dealerId;
    private Note note;

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
        fillIn();
    }

    private void fillIn() {
        if(type==IN){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final int[] a = {0};
                    UserHttp.queryUser(dealerId, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String str = response.body().string();
                            fillInUser(str);
                            a[0]++;
                            if(a[0]==2){
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
                            if(a[0]==2){
                                handler.sendEmptyMessage(1);
                                //查询保证金
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        max = BtslandApplication.getMarketStat().mWebsocketApi.getAssetTotalByAccountAndCoin(user.getAccount(),"CNY");
                                    }
                                }).start();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                }).start();
                                //
                            }
                        }
                    });
                }
            }).start();

        }else {

        }

    }

    private void fillInUser(String json) {
        Gson gson=new Gson();
        user = gson.fromJson(json,new TypeToken<User>() {}.getType());
    }

    private void fillInNote(String json){
        Gson gson=new Gson();
        note = gson.fromJson(json,new TypeToken<Note>() {}.getType());
    }

    private void fillInBody() {

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

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            fillInBody();
        }
    };

}
