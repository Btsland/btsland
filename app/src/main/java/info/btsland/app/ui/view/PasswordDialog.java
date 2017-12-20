package info.btsland.app.ui.view;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;

public class PasswordDialog {
    private Activity mActivity;
    private AlertDialog.Builder mDialogBuilder;
    private AlertDialog mDialog;
    private OnDialogInterationListener mListener;
    private View view;
    private EditText editTextPwd;
    private TextView tvTitle;
    private TextView tvMeg;
    private TextView tvConfirm;
    private TextView txtCancel;
    private TextView tvHoint;

    private String msg="请输入您的密码";

    public void setMsg(String msg) {
        this.msg = msg;
        tvMeg.setText(msg);

    }

    public PasswordDialog(Activity mActivity) {
        this.mActivity = mActivity;

        mDialogBuilder = new AlertDialog.Builder(mActivity);

        view = mActivity.getLayoutInflater().inflate(R.layout.dialog_password, null);
        tvMeg=view.findViewById(R.id.tv_dialog_msg);
        editTextPwd =view.findViewById(R.id.editTextPassword);
        editTextPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvHoint.setVisibility(View.INVISIBLE);
            }
        });
        tvConfirm =view.findViewById(R.id.tv_dialog_confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if(mListener != null){
                    mListener.onConfirm(mDialog, editTextPwd.getText().toString());
                }
            }
        });

        txtCancel =view.findViewById(R.id.tv_dialog_cancel);
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if(mListener != null){
                    mListener.onReject(mDialog);
                }
            }
        });
        tvHoint=view.findViewById(R.id.tv_dialog_point);
        mDialogBuilder.setView(view);
    }

    public void setTvPoint(Boolean hoint) {
        tvHoint.setVisibility(View.VISIBLE);
        if(hoint){
            tvHoint.setText("密码正确！");
            tvHoint.setTextColor(BtslandApplication.getInstance().getResources().getColor(R.color.color_green));
        }else {
            tvHoint.setText("密码错误！");
            tvHoint.setTextColor(BtslandApplication.getInstance().getResources().getColor(R.color.color_font_red));
        }

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
