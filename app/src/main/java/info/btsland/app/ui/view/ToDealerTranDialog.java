package info.btsland.app.ui.view;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import info.btsland.app.R;

public class ToDealerTranDialog {
    private LayoutInflater inflater;
    private Context mActivity;
    private AlertDialog.Builder mDialogBuilder;
    private AlertDialog mDialog;
    private OnDialogInterationListener mListener;
    private View view;
    private TextView tvTitle;
    private TextView tvFrom;
    private TextView tvTo;
    private TextView tvCoin;
    private TextView tvNum;
    private TextView tvMemo;
    private TextView tvConfirm;
    private TextView txtCancel;

    private String title="提示";
    private String from;
    private String to;
    private String vol;
    private String symbol;
    private String memo;

    public ToDealerTranDialog(Context context, String title) {
        this.mActivity = context;
        this.inflater = LayoutInflater.from(context);
        this.title=title;
        fillIn();
    }

    public ToDealerTranDialog(Context context, String from, String to, String vol, String symbol, String memo) {
        this.mActivity = context;
        this.inflater =  LayoutInflater.from(context);
        this.from = from;
        this.to = to;
        this.vol = vol;
        this.symbol = symbol;
        this.memo = memo;
        fillIn();
    }

    public ToDealerTranDialog(Context context) {
        this.mActivity = context;
        this.inflater = LayoutInflater.from(context);
        fillIn();
    }
    public void fillIn(){
        mDialogBuilder = new AlertDialog.Builder(mActivity);

        view = inflater.inflate(R.layout.dialog_to_dealer_tran, null);
        tvTitle=view.findViewById(R.id.tv_app_dialog_title);
        tvFrom=view.findViewById(R.id.tv_dialog_from);
        tvTo=view.findViewById(R.id.tv_dialog_to);
        tvCoin=view.findViewById(R.id.tv_dialog_coin);
        tvNum=view.findViewById(R.id.tv_dialog_num);
        tvMemo=view.findViewById(R.id.tv_dialog_memo);
        tvConfirm =view.findViewById(R.id.tv_app_dialog_confirm);
        tvFrom.setText(from);
        tvTo.setText(to);
        tvMemo.setText(memo);
        tvCoin.setText(symbol);
        tvNum.setText(vol);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if(mListener != null){
                    mListener.onConfirm();
                }
            }
        });

        txtCancel =view.findViewById(R.id.tv_app_dialog_cancel);
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
        tvTitle.setText(title);
        mDialog = mDialogBuilder.show();
    }

    public void setListener(OnDialogInterationListener listener){
        mListener = listener;
    }

    public void setTitle(String title){
        this.title=title;

    }

    public interface OnDialogInterationListener {
        void onConfirm();
        void onReject();
    }
}
