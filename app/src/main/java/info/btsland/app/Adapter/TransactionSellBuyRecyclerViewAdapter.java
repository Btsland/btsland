package info.btsland.app.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import info.btsland.app.R;
import info.btsland.app.model.Order;
import info.btsland.app.ui.fragment.DetailedBuyAndSellFragment;

/**
 * Created by Administrator on 2017/11/14.
 */

public class TransactionSellBuyRecyclerViewAdapter extends RecyclerView.Adapter<TransactionSellBuyRecyclerViewAdapter.ViewHolder>  {
    private EditText edPrice;
    private List<Order> list;

    public TransactionSellBuyRecyclerViewAdapter(EditText edPrice) {
        this.edPrice=edPrice;
    }

    public void setList(List<Order> list){
        if(getItemCount()>0){
           notifyItemRangeRemoved(0,getItemCount());
        }
        this.list = list;
//        notifyItemRangeInserted(0,getItemCount());
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
            tvPrice = view.findViewById(R.id.tv_item_price);
            tvVol = view.findViewById(R.id.tv_item_vol);

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
        }
    }
}
