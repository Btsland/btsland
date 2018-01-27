package info.btsland.app.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.model.Order;

/**
 * Created by Administrator on 2017/11/14.
 */

public class TransactionSellBuyRecyclerViewAdapter extends RecyclerView.Adapter<TransactionSellBuyRecyclerViewAdapter.ViewHolder>  {
    private EditText edPrice;
    private List<Order> list;
    private int blue;
    private int bid;
    private int ask;
    private int type;

    private double maxNum=0.0;

    public TransactionSellBuyRecyclerViewAdapter(EditText edPrice,int type) {
        this.edPrice=edPrice;
        this.type=type;
        this.blue=BtslandApplication.getInstance().getResources().getColor(R.color.color_font_blue);
        this.bid=BtslandApplication.getInstance().getResources().getColor(R.color.color_order_bid);
        this.ask=BtslandApplication.getInstance().getResources().getColor(R.color.color_order_ask);
    }

    public void setList(List<Order> list){
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(type==1){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.order_item_bid, parent, false);
            return new ViewHolder(view);
        }else if(type==2){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.order_item_ask, parent, false);
            return new ViewHolder(view);
        }
        return null;

    }

    public TransactionSellBuyRecyclerViewAdapter setMaxNum(Double maxNum) {
        this.maxNum = maxNum;
        return this;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.update(list.get(position));
    }

    @Override
    public int getItemCount() {
        if(list==null){
            return 0;
        }else {

            return list.size();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public Order item;
        public TextView tvPrice;
        public TextView tvVol;
        public ProgressBar progressBar;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            tvPrice = view.findViewById(R.id.tv_item_price);
            tvVol = view.findViewById(R.id.tv_item_vol);
            progressBar=view.findViewById(R.id.progressBar);
        }

        public void update(final Order order){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    edPrice.getEditableText().clear();
                    edPrice.getEditableText().append(String.valueOf(order.price));
                }
            });
            tvPrice.setText(String.valueOf(order.price));
            tvVol.setText(String.valueOf(order.quote));
            if(type==1){
                tvPrice.setTextColor(bid);
            }else if(type==2){
                tvPrice.setTextColor(ask);
            }
            progressBar.setMax((int)maxNum);
            progressBar.setProgress((int) order.quote);
            if(BtslandApplication.accountObject!=null){
                if(order.seller.equals(BtslandApplication.accountObject.id)){
                    tvPrice.setTextColor(blue);
                    tvVol.setTextColor(blue);
                }
            }

        }
    }
}
