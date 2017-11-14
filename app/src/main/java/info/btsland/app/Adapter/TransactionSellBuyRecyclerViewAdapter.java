package info.btsland.app.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import info.btsland.app.R;
import info.btsland.app.model.Order;

/**
 * Created by Administrator on 2017/11/14.
 */

public class TransactionSellBuyRecyclerViewAdapter extends RecyclerView.Adapter<TransactionSellBuyRecyclerViewAdapter.ViewHolder>  {
    private List<Order> list;

    public TransactionSellBuyRecyclerViewAdapter() {

    }

    public void setList(List<Order> list){
        if(getItemCount()>0){
            notifyItemRangeRemoved(0,getItemCount());
        }
        this.list = list;
        notifyItemRangeInserted(0,getItemCount());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.update(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list==null ? 0: list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public Order item;
        public TextView tvPrice;
        public TextView tvVol;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            tvPrice = (TextView) view.findViewById(R.id.tv_item_vol);
            tvVol = (TextView) view.findViewById(R.id.tv_item_vol);
        }

        public void update(Order order){
            tvPrice.setText(String .format("%.4f",order.price));
            tvVol.setText(String .format("%.4f",order.quote));
        }
    }
}
