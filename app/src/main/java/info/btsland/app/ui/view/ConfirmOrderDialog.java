package info.btsland.app.ui.view;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import info.btsland.app.R;

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
    private String want;

    public static class ConfirmOrderData {
        public static final String BUY="buy";
        public static final String SELL="sell";
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

        public String getWant() {
            return want;
        }

        public ConfirmOrderData setWant(String want) {
            this.want = want;
            return this;
        }

        public String getPriceNum() {
            return priceNum;
        }

        public ConfirmOrderData setPriceNum(String priceNum) {
            this.priceNum = priceNum;
            return this;
        }

        public String getPriceCoin() {
            return priceCoin;
        }

        public ConfirmOrderData setPriceCoin(String priceCoin) {
            this.priceCoin = priceCoin;
            return this;
        }

        public String getTotalNum() {
            return totalNum;
        }

        public ConfirmOrderData setTotalNum(String totalNum) {
            this.totalNum = totalNum;
            return this;
        }

        public String getVolNum() {
            return volNum;
        }

        public ConfirmOrderData setVolNum(String volNum) {
            this.volNum = volNum;
            return this;
        }

        public String getChatgeNum() {
            return chargeNum;
        }

        public ConfirmOrderData setChatgeNum(String chargeNum) {
            this.chargeNum = chargeNum;
            return this;
        }

        public String getQuoteCoin() {
            return quoteCoin;
        }

        public ConfirmOrderData setQuoteCoin(String quoteCoin) {
            this.quoteCoin = quoteCoin;
            return this;
        }

        public String getTotalCoin() {
            return totalCoin;
        }

        public ConfirmOrderData setTotalCoin(String totalCoin) {
            this.totalCoin = totalCoin;
            return this;
        }

        public String getChargeCoin() {
            return chargeCoin;
        }

        public ConfirmOrderData setChargeCoin(String chargeCoin) {
            this.chargeCoin = chargeCoin;
            return this;
        }
    }

    public ConfirmOrderDialog(Activity mActivity, ConfirmOrderData confirmOrderData, OnDialogInterationListener listener) {
        this.mActivity = mActivity;
        this.confirmOrderData = confirmOrderData;
        this.mListener = listener;
        init();
        fillin();
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
    }
    public void fillin(){
        tvTitle.setText("请确认订单");
        if(confirmOrderData.getWant().equals(ConfirmOrderData.BUY)){
            tvWant.setText("买入");
            want=ConfirmOrderData.BUY;
        }else if(confirmOrderData.getWant().equals(ConfirmOrderData.SELL)){
            want=ConfirmOrderData.SELL;
            tvWant.setText("卖出");
        }
        tvPriceNum.setText(confirmOrderData.getPriceNum());
        tvPriceCoin.setText(confirmOrderData.getPriceCoin());
        tvVolNum.setText(confirmOrderData.getVolNum());
        tvVolCoin.setText(confirmOrderData.getQuoteCoin());
        tvTotalNum.setText(confirmOrderData.getTotalNum());
        tvTotalCoin.setText(confirmOrderData.getTotalCoin());
        tvChargeNum.setText(confirmOrderData.getChatgeNum());
        tvChargeCoin.setText(confirmOrderData.getChargeCoin());

        txtConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if(mListener != null){
                    mListener.onConfirm(want);
                }
            }
        });
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
