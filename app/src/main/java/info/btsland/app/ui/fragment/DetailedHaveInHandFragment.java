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
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.List;

import info.btsland.app.Adapter.HaveInHandRecyclerViewAdapter;
import info.btsland.app.Adapter.TransactionSellBuyRecyclerViewAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.MarketStat;
import info.btsland.app.api.account_object;
import info.btsland.app.api.object_id;
import info.btsland.app.api.operation_history_object;
import info.btsland.app.exception.NetworkStatusException;
import info.btsland.app.model.MarketTicker;
import info.btsland.app.model.OpenOrder;
import info.btsland.app.ui.view.AppDialog;
import info.btsland.app.ui.view.PasswordDialog;


public class DetailedHaveInHandFragment extends Fragment {
    private static String MARKET="market";
    private KProgressHUD hud;

    private RecyclerView rlv;

    private HaveInHandRecyclerViewAdapter adapter;
    private OpenOrderReceiver openOrderReceiver;
    public DetailedHaveInHandFragment() {
        // Required empty public constructor
    }

    public static DetailedHaveInHandFragment newInstance(MarketTicker market) {
        DetailedHaveInHandFragment fragment = new DetailedHaveInHandFragment();
        Bundle args = new Bundle();
        args.putSerializable(MARKET, market);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openOrderReceiver=new OpenOrderReceiver();
        IntentFilter intentFilter=new IntentFilter(OpenOrderReceiver.EVENT);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(openOrderReceiver,intentFilter);
        if (getArguments() != null) {
        }
        hud=KProgressHUD.create(this.getActivity());
        hud.setLabel(getResources().getString(R.string.please_wait));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_detailed_have_in_hand, container, false);
        init(view);
        fillIn();
        return view;
    }

    private void fillIn() {
        rlv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HaveInHandRecyclerViewAdapter(new HaveInHandRecyclerViewAdapter.CancelOnClickListener() {
            @Override
            public void onClick(final OpenOrder order) {
                hud=KProgressHUD.create(getActivity());
                hud.setLabel(getActivity().getResources().getString(R.string.please_wait));
                if(BtslandApplication.getWalletApi().is_locked()){
                    final PasswordDialog pwdDialog=new PasswordDialog(getActivity());
                    pwdDialog.setListener(new PasswordDialog.OnDialogInterationListener() {
                        @Override
                        public void onConfirm(AlertDialog dialog, final String passwordString) {
                            if(passwordString!=null&&passwordString.length()>0){
                                account_object accountObject = null;
                                try {
                                    accountObject = BtslandApplication.getWalletApi().import_account_password(BtslandApplication.accountObject.name, passwordString);
                                } catch (NetworkStatusException e) {
                                    e.printStackTrace();
                                }
                                if(accountObject==null){
                                    pwdDialog.setTvPoint(false);
                                    return;
                                }
                                dialog.dismiss();
                                hud.show();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        if (BtslandApplication.getWalletApi().unlock(passwordString) == 0) {
                                            try {
                                                if (BtslandApplication.getWalletApi().cancel_order(order.limitOrder.id) != null) {
                                                    handler2.sendEmptyMessage(1);
                                                } else {
                                                    handler2.sendEmptyMessage(-1);
                                                }
                                            } catch (NetworkStatusException e) {
                                                e.printStackTrace();
                                            }
                                        }else {
                                            handler2.sendEmptyMessage(-2);
                                        }
                                    }
                                }).start();
                            }else {
                                pwdDialog.setTvPoint(false);
                            }
                        }
                        @Override
                        public void onReject(AlertDialog dialog) {
                            dialog.dismiss();
                        }
                    });

                    pwdDialog.show();
                }else {
                    hud.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(BtslandApplication.getWalletApi().cancel_order(order.limitOrder.id)!=null){
                                    handler2.sendEmptyMessage(1);
                                }else {
                                    handler2.sendEmptyMessage(-1);
                                }
                            } catch (NetworkStatusException e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();

                }
            }

        });
        rlv.setAdapter(adapter);
        adapter.setList(BtslandApplication.openOrders);
        rlv.setItemAnimator(null);
    }

    private void init(View view) {
        rlv=view.findViewById(R.id.rlv_detailed_haveInHand);
    }

    private Handler handler2=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(hud.isShowing()){
                hud.dismiss();
            }
            if (msg.what==1){
                AppDialog appDialog=new AppDialog(getActivity(),"提示","取消成功！");
                appDialog.show();
                BtslandApplication.queryOrders();
            }else if(msg.what==-1){
                AppDialog appDialog=new AppDialog(getActivity(),"提示","取消失败！");
                appDialog.show();
            }
        }
    };

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(openOrderReceiver);
    }

    public static void sendBroadcast(Context context){
        Intent intent=new Intent("OpenOrderReceiver");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    class OpenOrderReceiver extends BroadcastReceiver{
        public static final String EVENT="OpenOrderReceiver";
        @Override
        public void onReceive(Context context, Intent intent) {
            handler.sendEmptyMessage(1);
        }
    }
}
