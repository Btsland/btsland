package info.btsland.app.ui.view;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.sha256_object;

public class CreateChatDialog {
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
    private WebView wbPho;

    private String msg="请输入对方的用户名";

    public void setMsg(String msg) {
        this.msg = msg;
        tvMeg.setText(msg);

    }

    public CreateChatDialog(Activity mActivity) {
        this.mActivity = mActivity;

        mDialogBuilder = new AlertDialog.Builder(mActivity);

        view = mActivity.getLayoutInflater().inflate(R.layout.dialog_create_chat, null);
        tvMeg=view.findViewById(R.id.tv_dialog_msg);
        editTextPwd =view.findViewById(R.id.editTextAccount);
        wbPho=view.findViewById(R.id.wb_account_pho);
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
                createPortrait(editable.toString());
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
    /**
     * 设置头像
     */
    private void createPortrait(String name) {
        sha256_object.encoder encoder=new sha256_object.encoder();
        encoder.write(name.getBytes());
        String htmlShareAccountName="<html><head><style>body,html { margin:0; padding:0; text-align:center;}</style><meta name=viewport content=width=" + 40 + ",user-scalable=no/></head><body><canvas width=" + 40 + " height=" + 40 + " data-jdenticon-hash=" + encoder.result().toString() + "></canvas><script src=https://cdn.jsdelivr.net/jdenticon/1.3.2/jdenticon.min.js async></script></body></html>";
        WebSettings webSettings=wbPho.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wbPho.loadData(htmlShareAccountName, "text/html", "UTF-8");
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
