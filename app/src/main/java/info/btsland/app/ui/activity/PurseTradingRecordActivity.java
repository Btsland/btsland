package info.btsland.app.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import info.btsland.app.Adapter.OperationRecyclerViewAdapter;
import info.btsland.app.Adapter.TransactionSellBuyRecyclerViewAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.object_id;
import info.btsland.app.api.operation_history_object;
import info.btsland.app.exception.NetworkStatusException;
import info.btsland.app.ui.fragment.HeadFragment;

/**
 * Created by Administrator on 2017/10/30 0030.
 */

public class PurseTradingRecordActivity extends AppCompatActivity {
    private HeadFragment headFragment;
    private RecyclerView rlvOperation;
    private OperationRecyclerViewAdapter rlvOperationAdapter;

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
        fillIn();

    }

    private void fillIn() {
        List<operation_history_object> listHistoryObject = null;
        try {
            listHistoryObject = BtslandApplication.getWalletApi().get_account_history(
                    BtslandApplication.accountObject.id,
                    new object_id<operation_history_object>(0, operation_history_object.class),
                    100
            );
            for (int i=0;i<listHistoryObject.size();i++){
                Log.i("purse", "fillIn: "+listHistoryObject.get(i).toString());
            }
        } catch (NetworkStatusException e) {
            e.printStackTrace();
        }
        rlvOperation.setLayoutManager(new LinearLayoutManager(this));
        rlvOperationAdapter = new OperationRecyclerViewAdapter();
        rlvOperationAdapter.setList(listHistoryObject);
        rlvOperation.setAdapter(rlvOperationAdapter);
        rlvOperation.setItemAnimator(null);
    }

    /**
     * 装载顶部导航
     */
    private void fillInHead() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment == null) {
            headFragment = new HeadFragment();
            headFragment.setType(HeadFragment.HeadType.BACK_NULL);
            transaction.add(R.id.fra_trading_head, headFragment);
        }
        transaction.commit();
    }

}
