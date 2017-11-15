package info.btsland.app.ui.view;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import info.btsland.app.R;

public class PasswordDialog {
    private Activity mActivity;
    private AlertDialog.Builder mDialogBuilder;
    private AlertDialog mDialog;
    private OnDialogInterationListener mListener;
    private View view;
    private EditText editTextPwd;
    private TextView tvTitle;
    private TextView tvPoint;
    private TextView tvConfirm;
    private TextView txtCancel;


    public PasswordDialog(Activity mActivity) {
        this.mActivity = mActivity;

        mDialogBuilder = new AlertDialog.Builder(mActivity);

        view = mActivity.getLayoutInflater().inflate(R.layout.dialog_password, null);

        editTextPwd =view.findViewById(R.id.editTextPassword);

        tvConfirm =view.findViewById(R.id.tv_dialog_confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onConfirm(mDialog, editTextPwd.getText().toString());
                }
            }
        });

        txtCancel =view.findViewById(R.id.tv_dialog_cancel);
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onReject(mDialog);
                }
            }
        });
        mDialogBuilder.setView(view);
    }

    public void show(){
        mDialog = mDialogBuilder.show();
    }

    public void setListener(OnDialogInterationListener listener){
        mListener = listener;
    }

    public interface OnDialogInterationListener {
        void onConfirm(AlertDialog dialog, String passwordString);
        void onReject(AlertDialog dialog);
    }
}
