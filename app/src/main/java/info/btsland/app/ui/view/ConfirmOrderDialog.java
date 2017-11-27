package info.btsland.app.ui.view;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.util.NumericUtil;

public class ConfirmOrderDialog {
    private Activity mActivity;
    private AlertDialog.Builder mDialogBuilder;
    private AlertDialog mDialog;
    private OnDialogInterationListener mListener;
    private TextView tvWant;
    private TextView tvPriceNum;
    private TextView tvPriceCoin;
    private TextView tvVolNum;
    private TextView tvTotalNum;
    private TextView tvChargeNum;
    private TextView txtConfirm;
    private TextView txtCancel;
    private View view;
    private ConfirmOrderData confirmOrderData;
    private TextView tvTitle;
    private TextView tvVolCoin;
    private TextView tvChargeCoin;
    private TextView tvTotalCoin;
    private TextView tvPoint;
    private String want;
    private boolean isShow=false;
    private boolean isOK=false;

    public static class ConfirmOrderData {
        public static final String BUY="buy";
        public static final String SELL="sell";
        private String point;
        private String want;
        private String priceNum;
        private String priceCoin;
        private String totalNum;
        private String volNum;
        private String chargeNum;
        private String quoteCoin;
        private String totalCoin;
        private String chargeCoin;
        public ConfirmOrderData() {

        }

        public ConfirmOrderData(String want,
                                String priceNum,
                                String priceCoin,
                                String totalNum,
                                String volNum,
                                String chargeNum,
                                String quoteCoin,
                                String totalCoin,
                                String chargeCoin) {
            this.want = want;
            this.priceNum = priceNum;
            this.priceCoin = priceCoin;
            this.totalNum = totalNum;
            this.volNum = volNum;
            this.chargeNum = chargeNum;
            this.quoteCoin = quoteCoin;
            this.totalCoin = totalCoin;
            this.chargeCoin = chargeCoin;
        }

    }

    public ConfirmOrderDialog(Activity mActivity, ConfirmOrderData confirmOrderData, OnDialogInterationListener listener) {
        this.mActivity = mActivity;
        this.confirmOrderData = confirmOrderData;
        this.mListener = listener;
        init();
        fillin();
    }
    public void setPoint(String text,boolean b){
        tvPoint.setText(text);
        if(b){
            tvPoint.setTextColor(mActivity.getResources().getColor(R.color.color_green));
        }else {
            tvPoint.setTextColor(mActivity.getResources().getColor(R.color.color_font_red));
        }
    }
    public boolean isShow(){
        return isShow;
    }
    public void init(){
        mDialogBuilder = new AlertDialog.Builder(mActivity);
//        mDialogBuilder.setTitle(R.string.label_please_confirm);
        view = mActivity.getLayoutInflater().inflate(R.layout.dialog_confirm_order, null);
        tvTitle=view.findViewById(R.id.tv_dialog_title);
        tvWant=view.findViewById(R.id.tv_dialog_want);
        tvPriceNum=view.findViewById(R.id.tv_dialog_priceNum);
        tvPriceCoin=view.findViewById(R.id.tv_dialog_priceCoin);
        tvVolNum=view.findViewById(R.id.tv_dialog_volNum);
        tvVolCoin=view.findViewById(R.id.tv_dialog_volCoin);
        tvTotalNum=view.findViewById(R.id.tv_dialog_totalNum);
        tvTotalCoin=view.findViewById(R.id.tv_dialog_totalCoin);
        tvChargeNum=view.findViewById(R.id.tv_dialog_chargeNum);
        tvChargeCoin=view.findViewById(R.id.tv_dialog_chargeCoin);
        txtConfirm =view.findViewById(R.id.tv_dialog_confirm);
        txtCancel =view.findViewById(R.id.tv_dialog_cancel);
        tvPoint=view.findViewById(R.id.tv_dialog_point);
    }
    public void fillin(){
        tvTitle.setText("请确认订单");
        if(confirmOrderData.want.equals(ConfirmOrderData.BUY)){
            tvWant.setText("买入");
            want=ConfirmOrderData.BUY;
            if(NumericUtil.parseDouble(confirmOrderData.totalNum)<BtslandApplication.getAssetTotalByName(confirmOrderData.totalCoin)){
                setPoint("余额充足！",true);
                isOK=true;
            }else {
                setPoint("余额不足！",false);
                isOK=false;
            }
        }else if(confirmOrderData.want.equals(ConfirmOrderData.SELL)){
            want=ConfirmOrderData.SELL;
            tvWant.setText("卖出");
            if(NumericUtil.parseDouble(confirmOrderData.volNum)<BtslandApplication.getAssetTotalByName(confirmOrderData.quoteCoin)){
                setPoint("余额充足！",true);
                isOK=true;
            }else {
                setPoint("余额不足！",false);
                isOK=false;
            }
        }
        tvPriceNum.setText(confirmOrderData.priceNum);
        tvPriceCoin.setText(confirmOrderData.priceCoin);
        tvVolNum.setText(confirmOrderData.volNum);
        tvVolCoin.setText(confirmOrderData.quoteCoin);
        tvTotalNum.setText(confirmOrderData.totalNum);
        tvTotalCoin.setText(confirmOrderData.totalCoin);
        tvChargeNum.setText(confirmOrderData.chargeNum);
        tvChargeCoin.setText(confirmOrderData.chargeCoin);


        txtConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isShow=false;
                mDialog.dismiss();
                if(!isOK){
                    Toast.makeText(mActivity,"余额不足",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mListener != null){
                    mListener.onConfirm(want);
                }
            }
        });
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShow=false;
                mDialog.dismiss();
                if(mListener != null){
                    mListener.onReject();
                }
            }
        });
        mDialogBuilder.setView(view);
    }

    public void show(){
        mDialog = mDialogBuilder.create();
        isShow=true;
        mDialog.show();
    }

    public void setListener(OnDialogInterationListener listener){
        mListener = listener;
    }

    public interface OnDialogInterationListener {
        void onConfirm(String want);
        void onReject();
    }
}
