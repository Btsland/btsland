package info.btsland.app.ui.view;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.R;
import info.btsland.exchange.entity.RealAsset;

/**
 * Created by Administrator on 2017/12/27.
 */

public class AccountTpyeDialog {
    private LayoutInflater inflater;
    private Context mActivity;
    private AlertDialog.Builder mDialogBuilder;
    private AlertDialog mDialog;
    private AccountTpyeDialog.OnDialogInterationListener mListener;
    private View view;
    private TextView tvTitle;
    private EditText edName;
    private EditText edNo;
    private EditText edDepict;
    private Spinner spType;
    private TextView tvConfirm;
    private TextView txtCancel;
    private LinearLayout llBottom;
    private EditText edTypeText;
    private RealAsset realAsset;
    private TextView tvNameHint;
    private TextView tvNoHint;

    private String title="支付帐号";


    public AccountTpyeDialog(Context context,String title,RealAsset realAsset) {
        this.mActivity = context;
        this.inflater = LayoutInflater.from(context);
        this.title=title;
        this.realAsset=realAsset;
        fillIn();
    }
    public AccountTpyeDialog(Context context,RealAsset realAsset) {
        this.mActivity = context;
        this.inflater = LayoutInflater.from(context);
        this.realAsset=realAsset;
        fillIn();
    }
    public AccountTpyeDialog(Context context) {
        this.mActivity = context;
        this.inflater = LayoutInflater.from(context);
        fillIn();
    }
    private List<String> list;
    public void fillIn(){
        mDialogBuilder = new AlertDialog.Builder(mActivity);

        view = inflater.inflate(R.layout.dialog_account_type, null);
        edName=view.findViewById(R.id.ed_account_types_dialog_name);
        edNo=view.findViewById(R.id.ed_account_types_dialog_no);
        edDepict=view.findViewById(R.id.ed_account_types_dialog_depict);
        spType=view.findViewById(R.id.sp_account_types_dialog_type);
        tvTitle=view.findViewById(R.id.tv_account_types_dialog_title);
        llBottom=view.findViewById(R.id.ll_account_types_dialog_bottom);
        edTypeText=view.findViewById(R.id.ed_account_types_dialog_typeText);
        tvNameHint=view.findViewById(R.id.tv_account_types_dialog_nameHint);
        tvNoHint=view.findViewById(R.id.tv_account_types_dialog_noHint);
        tvNoHint.setVisibility(View.GONE);
        tvNameHint.setVisibility(View.GONE);
        list =new ArrayList<>();
        list.add("支付宝");
        list.add("微信");
        list.add("银行卡");

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(mActivity,R.layout.coin_item,R.id.tv_transfer_coinName,list);
        spType.setAdapter(adapter);

        if(realAsset!=null){
            if(realAsset.getType()!=null){
                spType.setSelection(realAsset.getType()+1);
            }else {
                spType.setSelection(0);
            }
            edName.getEditableText().insert(0,realAsset.getName());
            edNo.getEditableText().insert(0,realAsset.getRealAssetNo());
            if(realAsset.getDepict()!=null){
                int a =realAsset.getDepict().indexOf("(");
                if(a!=-1){
                    edTypeText.getEditableText().insert(0,realAsset.getDepict().substring(0,a));
                    edDepict.getEditableText().insert(0,realAsset.getDepict().substring(a));
                }

            }


        }
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==2){
                    llBottom.setVisibility(View.VISIBLE);
                }else {
                    llBottom.setVisibility(View.GONE);
                    edTypeText.getEditableText().clear();
                    edDepict.getEditableText().clear();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        edName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().equals("")){
                    tvNameHint.setVisibility(View.VISIBLE);
                }else {
                    tvNameHint.setVisibility(View.GONE);
                }
            }
        });
        edNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().equals("")){
                    tvNoHint.setVisibility(View.VISIBLE);
                }else {
                    tvNoHint.setVisibility(View.GONE);
                }
            }
        });
        tvConfirm =view.findViewById(R.id.tv_account_types_dialog_confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mListener != null){
                    String name=edName.getEditableText().toString();
                    String no=edNo.getEditableText().toString();
                    int type=spType.getSelectedItemPosition();
                    String depict="";
                    if(type==2){
                        depict=edTypeText.getEditableText().toString()+"("+edDepict.getEditableText().toString()+")";
                    }else {
                        depict=list.get(type);
                    }
                    if(realAsset==null){
                        realAsset=new RealAsset();
                        realAsset.setType(0);
                    }
                    if(name.equals("")||no.equals("")){

                    }else {
                        realAsset.setName(name);
                        realAsset.setRealAssetNo(no);
                        realAsset.setDepict(depict);
                        realAsset.setRealAssetType("" + (type + 1));
                        mDialog.dismiss();
                        mListener.onConfirm(realAsset);
                    }
                }
            }
        });

        txtCancel =view.findViewById(R.id.tv_account_types_dialog_cancel);
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

    public void setListener(AccountTpyeDialog.OnDialogInterationListener listener){
        mListener = listener;
    }
    public void setTitle(String title){
        this.title=title;

    }

    public interface OnDialogInterationListener {
        void onConfirm(RealAsset realAsset);
        void onReject();
    }
}
