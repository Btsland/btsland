package info.btsland.app.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import info.btsland.app.model.Order;

/**
 * Created by Administrator on 2017/11/14.
 */

public class OperationRecyclerViewAdapter extends RecyclerView.Adapter<OperationRecyclerViewAdapter.ViewHolder>  {

    private Context context;
    private List<operation_history_object> list;

    public OperationRecyclerViewAdapter() {
    }

    public void setList(List<operation_history_object> listHistoryObject){
        this.list = listHistoryObject;
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
                    tvType.setText(BtslandApplication.getInstance().getString(R.string.str_transfer));
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
                    tvPrice.setText(BtslandApplication.getInstance().getString(R.string.str_num)+" "+utils.get_asset_amount(operTranser.amount.amount,asset)+asset.symbol);

                    List<object_id<account_object>> ids=new ArrayList<>();
                    ids.add(operTranser.from);
                    ids.add(operTranser.to);
                    try {
                        List<account_object> accounts = BtslandApplication.getMarketStat().mWebsocketApi.get_accounts(ids);
                        if(BtslandApplication.assetObjectMap.get(operTranser.amount.asset_id)==null){
                            List<object_id<asset_object>> assetObjects=new ArrayList<>();
                            assetObjects.add(operTranser.amount.asset_id);
                            BtslandApplication.getMarketStat().mWebsocketApi.get_assets(assetObjects);
                        }
                        asset_object assetobject = BtslandApplication.assetObjectMap.get(operTranser.amount.asset_id);

                        tvContent.setText(accounts.get(0).name+" " +BtslandApplication.getInstance().getString(R.string.str_pay)+" " +accounts.get(1).name+" " +BtslandApplication.getInstance().getString(R.string.str_transfers)+" "+ assetobject.symbol);

                    } catch (NetworkStatusException e) {
                        e.printStackTrace();
                    }
                    tvTime.setText("");
                    break;
                case operations.ID_CREATE_LIMIT_ORDER_OPERATION:
                    tvType.setText(BtslandApplication.getInstance().getString(R.string.str_publish));
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
                    tvContent.setText(payCreateLimit.symbol+"→"+receivesCreateLimit.symbol);
                    tvPrice.setText(BtslandApplication.getInstance().getString(R.string.str_price)+utils.get_asset_price(operCreateLimit.min_to_receive.amount,receivesCreateLimit,operCreateLimit.amount_to_sell.amount,payCreateLimit)+payCreateLimit.symbol+"/"+receivesCreateLimit.symbol);
                    tvTime.setText("");
                    break;
                case operations.ID_CANCEL_LMMIT_ORDER_OPERATION:
                    tvType.setText(BtslandApplication.getInstance().getString(R.string.str_cancel));
                    operations.limit_order_cancel_operation operCacaelLimit = (operations.limit_order_cancel_operation) oper.op.operationContent;
                    List<object_id<account_object>> idAccount=new ArrayList<>();
                    idAccount.add(operCacaelLimit.fee_paying_account);
                    try {
                        List<account_object> accounts = BtslandApplication.getMarketStat().mWebsocketApi.get_accounts(idAccount);


                        tvContent.setText(accounts.get(0).name+" " +BtslandApplication.getInstance().getString(R.string.str_cancel_the_order) );
                        tvPrice.setText(BtslandApplication.getInstance().getString(R.string.str_order_number)+operCacaelLimit.order.get_instance());
                    } catch (NetworkStatusException e) {
                        e.printStackTrace();
                    }
                    tvTime.setText("");
                    break;
                case operations.ID_UPDATE_LMMIT_ORDER_OPERATION:
                    tvType.setText(BtslandApplication.getInstance().getString(R.string.str_update));
                    //strResult = process_call_order_update_operation(holder, object.operationHistoryObject.op);
                    break;
                case operations.ID_FILL_LMMIT_ORDER_OPERATION:
                    tvType.setText(BtslandApplication.getInstance().getString(R.string.str_strike_a_bargain));
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


                    tvContent.setText(Html.fromHtml(BtslandApplication.getInstance().getString(R.string.str_successful_use)+utils.get_asset_amount(operFill.pays.amount,payFill) +BtslandApplication.getInstance().getString(R.string.str_ge)+payFill.symbol+BtslandApplication.getInstance().getString(R.string.str_byte_shop)+" "+utils.get_asset_amount(operFill.receives.amount,receivesFill)+BtslandApplication.getInstance().getString(R.string.str_ge)+receivesFill.symbol));
                    tvPrice.setText(BtslandApplication.getInstance().getString(R.string.str_price)+utils.get_asset_price(operFill.pays.amount,payFill,operFill.receives.amount,receivesFill)+payFill.symbol+"/"+receivesFill.symbol);
                    tvTime.setText("");
                    break;
                case operations.ID_CREATE_ACCOUNT_OPERATION:
                    tvType.setText(BtslandApplication.getInstance().getString(R.string.str_found));
                    operations.account_create_operation operCreateAccount = (operations.account_create_operation) oper.op.operationContent;
                    List<object_id<account_object>> idAccoun=new ArrayList<>();
                    idAccoun.add(operCreateAccount.registrar);
                    idAccoun.add(operCreateAccount.referrer);
                    try {
                        List<account_object> accounts = BtslandApplication.getMarketStat().mWebsocketApi.get_accounts(idAccoun);


                        tvContent.setText(accounts.get(0).name+" " +BtslandApplication.getInstance().getString(R.string.str_registered)+operCreateAccount.name );
                        tvPrice.setText(BtslandApplication.getInstance().getString(R.string.str_recommender)+accounts.get(1).name);
                    } catch (NetworkStatusException e) {
                        e.printStackTrace();
                    }

                    tvTime.setText("");
                    break;
            }

        }
    }
}
