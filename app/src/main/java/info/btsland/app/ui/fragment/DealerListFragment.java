package info.btsland.app.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.Adapter.DealerListAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.ui.activity.C2CExchangeActivity;
import info.btsland.app.ui.view.AppDialog;
import info.btsland.exchange.entity.User;

public class DealerListFragment extends Fragment {
    private static String TAG="DealerListFragment";

    private int type=1;

    public static final int IN=1;//进（充值）
    public static final int OUT=2;//出（提现）
    private ListView listView;
    private DealerListAdapter dealerListAdapter;
    private List<DealerListAdapter.DealerData> dataList=new ArrayList<>();
    private AllDealersReceiver allDealersReceiver;

    public DealerListFragment() {
    }
    public static DealerListFragment newInstance(int type) {
        DealerListFragment fragment = new DealerListFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("type",type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.type=getArguments().getInt("type");
        IntentFilter intentFilter=new IntentFilter(AllDealersReceiver.EVENT);
        allDealersReceiver=new AllDealersReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(allDealersReceiver,intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dealer_list, container, false);
        init(view);
        fillIn();
        fillInUser();
        return view;
    }

    private void init(View view){
        listView=view.findViewById(R.id.lv_c2c_list);
    }

    private void fillIn() {
        dealerListAdapter=new DealerListAdapter(getActivity());
        dealerListAdapter.setType(type);
        listView.setAdapter(dealerListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DealerListAdapter.DealerData dealerData = dataList.get(i);
                toExchange(dealerData.user);
            }
        });

    }
    private void toExchange(User user){
        if(BtslandApplication.accountObject==null){
            AppDialog appDialog=new AppDialog(getActivity());
            appDialog.setMsg("您未登录，请先登录！");
            appDialog.show();
        }else {
            C2CExchangeActivity.startAction(getActivity(), BtslandApplication.accountObject.name, type, user.getDealerId());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(allDealersReceiver);
    }

    private synchronized void fillInUser(){
        if(BtslandApplication.dealers!=null) {
            synchronized (BtslandApplication.dealers) {
                dataList.clear();
                for (int i = 0; i < BtslandApplication.dealers.size(); i++) {
                    DealerListAdapter.DealerData dealerData = new DealerListAdapter.DealerData();
                    User user = BtslandApplication.dealers.get(i);
                    if (user.getType() == 3) {
                        dealerData.user = user;
                        dataList.add(dealerData);
                    }
                }
                handler.sendEmptyMessage(1);
            }
        }
    }
    public static void sendBroadcast(Context context){
        Intent intent=new Intent(AllDealersReceiver.EVENT);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    class AllDealersReceiver extends BroadcastReceiver{
        public static final String EVENT="AllDealersReceiver";
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive: " );
            fillInUser();
        }
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            dealerListAdapter.setDataList(dataList);
            dealerListAdapter.notifyDataSetChanged();
        }
    };
}
