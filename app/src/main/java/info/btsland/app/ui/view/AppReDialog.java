package info.btsland.app.ui.view;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;

public class AppReDialog {
    private LayoutInflater inflater;
    private Context mActivity;
    private AlertDialog.Builder mDialogBuilder;
    private AlertDialog mDialog;
    private OnDialogInterationListener mListener;
    private View view;
    private EditText editTextPwd;
    private TextView tvTitle;
    private TextView tvMsg;
    private TextView tvConfirm;
    private TextView txtCancel;
    private EditText edText;
    private TextView tvPoint;

    private String msg;
    private String title="提示";


    public AppReDialog(Context context, String title, String msg) {
        this.mActivity = context;
        this.inflater = LayoutInflater.from(context);
        this.title=title;
        this.msg=msg;
        fillIn();
    }
    public AppReDialog(Context context) {
        this.mActivity = context;
        this.inflater = LayoutInflater.from(context);
        fillIn();
    }
    public void fillIn(){
        mDialogBuilder = new AlertDialog.Builder(mActivity);
        view = inflater.inflate(R.layout.dialog_app_re, null);
        tvTitle=view.findViewById(R.id.tv_app_dialog_title);
        tvMsg=view.findViewById(R.id.tv_app_dialog_msg);
        edText=view.findViewById(R.id.ed_app_dialog_text);
        tvConfirm =view.findViewById(R.id.tv_app_dialog_confirm);
        tvPoint=view.findViewById(R.id.tv_app_dialog_point);
        edText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text=editable.toString();
                if(text.equals("确认已收款")){
                    tvPoint.setText("输入正确");
                    tvPoint.setTextColor(BtslandApplication.getInstance().getResources().getColor(R.color.green));
                }
                tvPoint.setText("");
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=edText.getText().toString();
                if(text.equals("确认已收款")){
                    mDialog.dismiss();
                    if(mListener != null){
                        mListener.onConfirm();
                    }
                }else {
                    tvPoint.setText("输入错误");
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
        if(msg!=null&&!msg.equals("")) {
            tvMsg.setText(msg);
        }
        mDialog = mDialogBuilder.show();
    }

    public void setListener(OnDialogInterationListener listener){
        mListener = listener;
    }

    public void setMsg(String msg){
        this.msg=msg;

    }
    public void setTitle(String title){
        this.title=title;

    }

    public interface OnDialogInterationListener {
        void onConfirm();
        void onReject();
    }
}
