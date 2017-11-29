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

    private CancelOnClickListener cancelOnClickListener;
    private List<OpenOrder>  list;

    private KProgressHUD hud;

    public HaveInHandRecyclerViewAdapter(CancelOnClickListener cancelOnClickListener) {
        this.cancelOnClickListener=cancelOnClickListener;
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
            tvPriceCoin.setText(order.quoteName+"/"+order.baseName);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            tvTime.setText(df.format(order.limitOrder.expiration));
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancelOnClickListener.onClick(order);
                }
            });
        }
    }
   public interface CancelOnClickListener {
        void onClick(OpenOrder order);
   }
}
