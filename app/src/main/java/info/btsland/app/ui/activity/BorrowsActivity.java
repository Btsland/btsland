package info.btsland.app.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import info.btsland.app.Adapter.BorrowAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;

public class BorrowsActivity extends AppCompatActivity {
    private HeadFragment headFragment;
    private ListView listView;
    private BorrowAdapter adapter;
    private BorrowsReceiver borrowsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrows);
        borrowsReceiver=new BorrowsReceiver();
        IntentFilter intentFilter=new IntentFilter(BorrowsReceiver.EVENT);
        LocalBroadcastManager.getInstance(BorrowsActivity.this).registerReceiver(borrowsReceiver,intentFilter);
        fillInHead();
        init();
        fillIn();
    }

    private void init() {
        listView=findViewById(R.id.listView);
        adapter=new BorrowAdapter(BorrowsActivity.this);
        adapter.setBorrows(BtslandApplication.borrows);
    }
    private void fillIn(){
        listView.setAdapter(adapter);
    }

    private void refurbish(){
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(BorrowsActivity.this).unregisterReceiver(borrowsReceiver);
    }

    /**
     * 装载顶部导航
     */
    private void fillInHead() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment == null) {
            headFragment = HeadFragment.newInstance(HeadFragment.HeadType.BACK_NULL,"借入排行榜");
            transaction.add(R.id.fra_borrows_head, headFragment);
        }
        transaction.commit();
    }
    public static void sendBroadcast(Context context){
        Intent intent=new Intent(BorrowsReceiver.EVENT);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    class BorrowsReceiver extends BroadcastReceiver{
        public static final String EVENT="BorrowsReceiver";
        @Override
        public void onReceive(Context context, Intent intent) {
            refurbish();
        }
    }
}
