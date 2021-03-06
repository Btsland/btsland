package info.btsland.app.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.List;

import info.btsland.app.Adapter.OperationRecyclerViewAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.object_id;
import info.btsland.app.api.operation_history_object;
import info.btsland.app.exception.NetworkStatusException;
import info.btsland.app.ui.fragment.HeadFragment;

/**
 * 交易记录
 * Created by Administrator on 2017/10/30 0030.
 */

public class PurseTradingRecordActivity extends AppCompatActivity {
    private HeadFragment headFragment;
    private RecyclerView rlvOperation;
    private OperationRecyclerViewAdapter rlvOperationAdapter;
    private List<operation_history_object> listHistoryObject;

    private KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purse_trading);
        fillInHead();
        init();
    }
    /**
     * 初始化
     */
    private void init() {
        rlvOperation=findViewById(R.id.rlv_operation);
        rlvOperation.setLayoutManager(new LinearLayoutManager(this));
        rlvOperationAdapter = new OperationRecyclerViewAdapter();
        rlvOperation.setAdapter(rlvOperationAdapter);
        rlvOperation.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        fillIn();

    }

    private void fillIn() {
        hud=KProgressHUD.create(PurseTradingRecordActivity.this);
        hud.setLabel(getResources().getString(R.string.please_wait));
        hud.show();
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    listHistoryObject = BtslandApplication.getWalletApi().get_account_history(
                            BtslandApplication.accountObject.id,
                            new object_id<operation_history_object>(0, operation_history_object.class),
                            40
                    );
                    if(listHistoryObject!=null){
                        handler.sendEmptyMessage(1);
                    }else {
                        handler.sendEmptyMessage(0);
                    }
                } catch (NetworkStatusException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(-1);
                }
            }
        });
        thread.start();
    }

    /**
     * 装载顶部导航
     */
    private void fillInHead() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment == null) {
            headFragment = HeadFragment.newInstance(HeadFragment.HeadType.BACK_NULL,getString(R.string.tradingrecord));
            transaction.add(R.id.fra_trading_head, headFragment);
        }
        transaction.commit();
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(hud.isShowing()){
                hud.dismiss();
            }
            if(msg.what==1){
                rlvOperationAdapter.setList(listHistoryObject);
                rlvOperationAdapter.notifyDataSetChanged();
            }else if(msg.what==0) {
                Toast.makeText(PurseTradingRecordActivity.this,"您的帐号目前没有记录",Toast.LENGTH_SHORT).show();
            }else if(msg.what==-1){
                Toast.makeText(PurseTradingRecordActivity.this,"数据获取失败",Toast.LENGTH_SHORT).show();
            }
        }
    };

}
