package info.btsland.app.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.account_object;
import info.btsland.app.api.asset_object;
import info.btsland.app.api.object_id;
import info.btsland.app.api.operation_history_object;
import info.btsland.app.api.operations;
import info.btsland.app.api.utils;
import info.btsland.app.exception.NetworkStatusException;
import info.btsland.app.model.OpenOrder;
import info.btsland.app.model.Order;
import info.btsland.app.ui.view.PasswordDialog;

/**
 * Created by Administrator on 2017/11/14.
 */

public class HaveInHandRecyclerViewAdapter extends RecyclerView.Adapter<HaveInHandRecyclerViewAdapter.ViewHolder>  {

    private Handler handler;
    private Activity activity;
    private List<OpenOrder>  list;

    private KProgressHUD hud;

    public HaveInHandRecyclerViewAdapter(Activity activity, Handler handler) {
        this.handler=handler;
        this.activity=activity;
    }

    public void setList(List<OpenOrder> openOrders){
//        if(getItemCount()>0){
//            notifyItemRangeRemoved(0,getItemCount());
//        }
        this.list = openOrders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.haveinhand_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.update(list.get(list.size()-1-position));
    }

    @Override
    public int getItemCount() {
        return list==null ? 0: list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public Order item;
        private TextView tvBase;
        private TextView tvQuote;
        private TextView tvBaseCoin;
        private TextView tvQuoteCoin;
        private TextView tvPriceNum;
        private TextView tvPriceCoin;
        private TextView tvTime;
        private TextView tvCancel;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            tvBase=view.findViewById(R.id.tv_hIH_expenseNum);
            tvQuote=view.findViewById(R.id.tv_hIH_gainNum);
            tvBaseCoin=view.findViewById(R.id.tv_hIH_expenseCoin);
            tvQuoteCoin=view.findViewById(R.id.tv_hIH_gainCoin);
            tvPriceNum=view.findViewById(R.id.tv_hIH_priceNum);
            tvPriceCoin=view.findViewById(R.id.tv_hIH_priceNumCoin);
            tvTime=view.findViewById(R.id.tv_hIH_expirationTime);
            tvCancel=view.findViewById(R.id.tv_hIH_cancel);
        }
        public void update(final OpenOrder order){
            tvBase.setText(String.valueOf(order.baseNum));
            tvQuote.setText(String.valueOf(order.quoteNum));
            tvBaseCoin.setText(order.baseName);
            tvQuoteCoin.setText(order.quoteName);
            tvPriceNum.setText(String.valueOf(order.price));
            tvPriceCoin.setText(order.baseName+"/"+order.quoteName);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            tvTime.setText(df.format(order.limitOrder.expiration));
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hud=KProgressHUD.create(activity);
                    hud.setLabel(activity.getResources().getString(R.string.please_wait));
                    if(BtslandApplication.getWalletApi().is_locked()){
                        hud.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if(BtslandApplication.getWalletApi().cancel_order(order.limitOrder.id)!=null){
                                        handler.sendEmptyMessage(1);
                                    }else {
                                        handler.sendEmptyMessage(-1);
                                    }
                                    hudHandler.sendEmptyMessage(1);
                                } catch (NetworkStatusException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }else {
                        final PasswordDialog pwdDialog=new PasswordDialog(activity);
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
                                                        handler.sendEmptyMessage(1);
                                                    } else {
                                                        handler.sendEmptyMessage(-1);
                                                    }
                                                    hudHandler.sendEmptyMessage(1);
                                                } catch (NetworkStatusException e) {
                                                    e.printStackTrace();
                                                }
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

                    }
                }
            });
        }
    }
    private Handler hudHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(hud.isShowing()){
                hud.dismiss();
            }
        }
    };
}
