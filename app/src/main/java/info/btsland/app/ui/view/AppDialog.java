package info.btsland.app.ui.view;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import info.btsland.app.R;

public class AppDialog {
    private Activity mActivity;
    private AlertDialog.Builder mDialogBuilder;
    private AlertDialog mDialog;
    private OnDialogInterationListener mListener;
    private View view;
    private EditText editTextPwd;
    private TextView tvTitle;
    private TextView tvMsg;
    private TextView tvConfirm;
    private TextView txtCancel;

    private String msg;
    private String title="提示";


    public AppDialog(Activity mActivity,String title,String msg) {
        this.mActivity = mActivity;
        this.title=title;
        this.msg=msg;
        fillIn();
    }
    public AppDialog(Activity mActivity) {
        this.mActivity = mActivity;
        fillIn();
    }
    public void fillIn(){
        mDialogBuilder = new AlertDialog.Builder(mActivity);

        view = mActivity.getLayoutInflater().inflate(R.layout.dialog_app, null);
        tvTitle=view.findViewById(R.id.tv_app_dialog_title);
        tvMsg=view.findViewById(R.id.tv_app_dialog_msg);

        tvConfirm =view.findViewById(R.id.tv_app_dialog_confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mDialog.dismiss();
                    mListener.onConfirm();
                }
            }
        });

        txtCancel =view.findViewById(R.id.tv_app_dialog_cancel);
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mDialog.dismiss();
                    mListener.onReject();
                }
            }
        });
        mDialogBuilder.setView(view);
    }

    public void show(){
        tvTitle.setText(title);
        tvMsg.setText(msg);
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
