package info.btsland.app.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.bitcoinj.core.Utils;

import java.util.ArrayList;
import java.util.List;

import de.bitsharesmunich.graphenej.Util;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.asset_object;
import info.btsland.app.api.object_id;
import info.btsland.app.api.operation_history_object;
import info.btsland.app.api.operations;
import info.btsland.app.api.utils;
import info.btsland.app.exception.NetworkStatusException;
import info.btsland.app.model.Order;

/**
 * Created by Administrator on 2017/11/14.
 */

public class OperationRecyclerViewAdapter extends RecyclerView.Adapter<OperationRecyclerViewAdapter.ViewHolder>  {

    private List<operation_history_object> list;

    public OperationRecyclerViewAdapter() {
    }

    public void setList(List<operation_history_object> listHistoryObject){
        if(getItemCount()>0){
            notifyItemRangeRemoved(0,getItemCount());
        }
        this.list = listHistoryObject;
        notifyItemRangeInserted(0,getItemCount());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.operation_item, parent, false);
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
        public TextView tvType;
        public TextView tvContent;
        public TextView tvPrice;
        public TextView tvTime;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            tvType = view.findViewById(R.id.tv_operation_type);
            tvContent = view.findViewById(R.id.tv_operation_content);
            tvPrice = view.findViewById(R.id.tv_operation_price);
            tvTime = view.findViewById(R.id.tv_operation_time);

        }
        public void update(final operation_history_object oper){
            int op = oper.op.nOperationType;

            String strResult = "";
            switch (op) {
                case operations.ID_TRANSER_OPERATION:
                    tvType.setText("");
                    operations.transfer_operation operTranser = (operations.transfer_operation) oper.op.operationContent;
                    asset_object asset=null;
                    if(BtslandApplication.assetObjectMap.get(operTranser.amount.asset_id)!=null){
                        asset= BtslandApplication.assetObjectMap.get(operTranser.amount.asset_id);
                    }else {
                        List<object_id<asset_object>> object_ids=new ArrayList <>();
                        object_ids.add(operTranser.amount.asset_id);
                        try {
                            List<asset_object> objects=BtslandApplication.getMarketStat().mWebsocketApi.get_assets(object_ids);
                            asset=objects.get(0);
                        } catch (NetworkStatusException e) {
                            e.printStackTrace();
                        }
                    }
                    tvPrice.setText("数量："+utils.get_asset_amount(operTranser.amount.amount,asset)+asset.symbol);
                    tvTime.setText("");
                    break;
                case operations.ID_CREATE_LIMIT_ORDER_OPERATION:
                    tvType.setText("");
                    operations.limit_order_create_operation operCreateLimit = (operations.limit_order_create_operation) oper.op.operationContent;
                    asset_object payCreateLimit=null;
                    if(BtslandApplication.assetObjectMap.get(operCreateLimit.amount_to_sell.asset_id)!=null){
                        payCreateLimit= BtslandApplication.assetObjectMap.get(operCreateLimit.amount_to_sell.asset_id);
                    }else {
                        List<object_id<asset_object>> object_ids=new ArrayList <>();
                        object_ids.add(operCreateLimit.amount_to_sell.asset_id);
                        try {
                            List<asset_object> objects=BtslandApplication.getMarketStat().mWebsocketApi.get_assets(object_ids);
                            payCreateLimit=objects.get(0);
                        } catch (NetworkStatusException e) {
                            e.printStackTrace();
                        }
                    }
                    asset_object receivesCreateLimit=null;
                    if(BtslandApplication.assetObjectMap.get(operCreateLimit.min_to_receive.asset_id)!=null){
                        receivesCreateLimit= BtslandApplication.assetObjectMap.get(operCreateLimit.min_to_receive.asset_id);
                    }else {
                        List<object_id<asset_object>> object_ids=new ArrayList <>();
                        object_ids.add(operCreateLimit.min_to_receive.asset_id);
                        try {
                            List<asset_object> objects=BtslandApplication.getMarketStat().mWebsocketApi.get_assets(object_ids);
                            receivesCreateLimit=objects.get(0);
                        } catch (NetworkStatusException e) {
                            e.printStackTrace();
                        }
                    }
                    tvContent.setText("发布用"+payCreateLimit.symbol+"去买"+receivesCreateLimit.symbol+"的广播");
                    tvPrice.setText("价格："+utils.get_asset_price(operCreateLimit.min_to_receive.amount,receivesCreateLimit,operCreateLimit.amount_to_sell.amount,payCreateLimit)+payCreateLimit.symbol+"/"+receivesCreateLimit.symbol);
                    tvTime.setText("");
                    break;
                case operations.ID_CANCEL_LMMIT_ORDER_OPERATION:
                    tvType.setText("");
                    //strResult = process_limit_order_cancel_operation(holder, object.operationHistoryObject.op);
                    break;
                case operations.ID_UPDATE_LMMIT_ORDER_OPERATION:
                    tvType.setText("");
                    //strResult = process_call_order_update_operation(holder, object.operationHistoryObject.op);
                    break;
                case operations.ID_FILL_LMMIT_ORDER_OPERATION:
                    tvType.setText("");
                    operations.fill_order_operation operFill = (operations.fill_order_operation)oper.op.operationContent;
                    asset_object payFill=null;
                    if(BtslandApplication.assetObjectMap.get(operFill.pays.asset_id)!=null){
                        payFill= BtslandApplication.assetObjectMap.get(operFill.pays.asset_id);
                    }else {
                        List<object_id<asset_object>> object_ids=new ArrayList <>();
                        object_ids.add(operFill.pays.asset_id);
                        try {
                            List<asset_object> objects=BtslandApplication.getMarketStat().mWebsocketApi.get_assets(object_ids);
                            payFill=objects.get(0);
                        } catch (NetworkStatusException e) {
                            e.printStackTrace();
                        }
                    }
                    asset_object receivesFill=null;
                    if(BtslandApplication.assetObjectMap.get(operFill.pays.asset_id)!=null){
                        receivesFill= BtslandApplication.assetObjectMap.get(operFill.receives.asset_id);
                    }else {
                        List<object_id<asset_object>> object_ids=new ArrayList <>();
                        object_ids.add(operFill.receives.asset_id);
                        try {
                            List<asset_object> objects=BtslandApplication.getMarketStat().mWebsocketApi.get_assets(object_ids);
                            receivesFill=objects.get(0);
                        } catch (NetworkStatusException e) {
                            e.printStackTrace();
                        }
                    }


                    tvContent.setText("用"+utils.get_asset_amount(operFill.pays.amount,payFill) +"个"+payFill.symbol+"购买了"+utils.get_asset_amount(operFill.receives.amount,receivesFill)+"个"+receivesFill.symbol);
                    tvPrice.setText("价格："+utils.get_asset_price(operFill.pays.amount,payFill,operFill.receives.amount,receivesFill)+payFill.symbol+"/"+receivesFill.symbol);
                    tvTime.setText("");
                    break;
                case operations.ID_CREATE_ACCOUNT_OPERATION:
                    tvType.setText("");
                    operations.account_create_operation operCreateAccount = (operations.account_create_operation) oper.op.operationContent;

                    //strResult = process_account_create_operation(holder, object.operationHistoryObject.op);
                    tvTime.setText("");
                    break;
            }

        }
    }
}
