package info.btsland.app.ui.view;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.exchange.entity.RealAsset;

/**
 * Created by Administrator on 2017/12/29 0029.
 */

public class AccountTypeListDialog {
    private static String TAG="AccountTypeListDialog";
    private int index;
    private List<RealAsset> list;
    private LayoutInflater inflater;
    private Activity mActivity;
    private AlertDialog.Builder mDialogBuilder;
    private AlertDialog mDialog;
    private AccountTypeListDialog.OnDialogInterationListener mListener;
    private View view;
    private TextView tvCancel;
    private TextView tvTitle;

    private ListView listView;



    private String title="提示";

    public AccountTypeListDialog(Activity mActivity,List<RealAsset> list) {
        this.list=list;
        this.mActivity = mActivity;
        this.inflater = LayoutInflater.from(mActivity);
        Log.e(TAG, "AccountTypeListDialog: "+ list.size());
        fillIn();
    }
    public AccountTypeListDialog(Activity mActivity,List<RealAsset> list,int index) {
        this.list=list;
        this.mActivity = mActivity;
        this.inflater = LayoutInflater.from(mActivity);
        this.index=index;
        fillIn();
    }
    private void fillIn(){
        mDialogBuilder = new AlertDialog.Builder(mActivity);
        view = mActivity.getLayoutInflater().inflate(R.layout.dialog_account_type_list, null);
        tvTitle=view.findViewById(R.id.tv_account_type_list_dialog_title);
        listView=view.findViewById(R.id.tv_account_type_list_dialog_list);
        ListAdapter listAdapter=new ListAdapter(list,index);
        listView.setAdapter(listAdapter);
        tvCancel=view.findViewById(R.id.tv_account_type_list_dialog_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                if(mListener != null){
                    mListener.onReject();
                }
            }
        });
        mDialogBuilder.setView(view);
    }
     class ListAdapter extends BaseAdapter {
        private List<RealAsset> list;
        private int index;
        public ListAdapter(List<RealAsset> list) {
            this.list = list;
        }

        public ListAdapter(List<RealAsset> list, int index) {
            this.list = list;
            this.index = index;
        }

        @Override
        public int getCount() {
            if (list != null) {
                return list.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int i) {
            if (list != null) {
                return list.get(i);
            }
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = inflater.inflate(R.layout.account_type_list_item, null);
            }
            if (list.get(i) == null) {
                return null;
            }
            final RealAsset realAsset = list.get(i);
            TextView tvText = view.findViewById(R.id.tv_account_type_list_item_text);
            TextView tvImg = view.findViewById(R.id.tv_account_type_list_item_img);
            String depict="未知";
            if(realAsset.getDepict()!=null&&!realAsset.getDepict().equals("")) {
                int a = realAsset.getDepict().indexOf("(");

                if (a != -1) {
                    depict = "(" + realAsset.getDepict().substring(0, a) + ")";
                } else {
                    depict = "(" + realAsset.getDepict() + ")";
                }
            }
            final String text = realAsset.getRealAssetNo() + depict;
            tvText.setText(text);
            Drawable img = null;
            switch (realAsset.getRealAssetType()) {
                case "1":
                    img = BtslandApplication.getInstance().getDrawable(R.drawable.zfb);
                    break;
                case "2":
                    img = BtslandApplication.getInstance().getDrawable(R.drawable.wx);
                    break;
                case "3":
                    img = BtslandApplication.getInstance().getDrawable(R.drawable.yh);
                    break;
            }
            if (img != null) {
                tvImg.setBackground(img);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onConfirm(realAsset,text);
                    mDialog.dismiss();
                }
            });
            return view;
        }

    }
    public void show(){
        tvTitle.setText(title);
        mDialog = mDialogBuilder.show();
    }

    public void setListener(AccountTypeListDialog.OnDialogInterationListener listener){
        mListener = listener;
    }

    public void setTitle(String title){
        this.title=title;
    }

    public interface OnDialogInterationListener {
        void onConfirm(RealAsset realAsset,String type);
        void onReject();
    }
}
