package info.btsland.app.ui.view;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.model.Market;
import info.btsland.app.model.MarketTicker;

public class AppListDialog {
    private List<MarketTicker> list;
    private LayoutInflater inflater;
    private Activity mActivity;
    private AlertDialog.Builder mDialogBuilder;
    private AlertDialog mDialog;
    private OnDialogInterationListener mListener;
    private View view;
    private TextView txtCancel;
    private TextView tvTitle;

    private ListView listView;

    private List<String> strings;
    private int index;

    private int type;


    private String title="提示";

    public AppListDialog(Activity mActivity,List<MarketTicker> list) {
        this.list=list;
        this.mActivity = mActivity;
        this.inflater = LayoutInflater.from(mActivity);
        this.type=1;
        fillIn();
    }
    public AppListDialog(Activity mActivity,List<String> list,int index) {
        this.strings=list;
        this.index=index;
        this.mActivity = mActivity;
        this.inflater = LayoutInflater.from(mActivity);
        this.type=2;
        fillIn();
    }
    public void fillIn(){
        mDialogBuilder = new AlertDialog.Builder(mActivity);

        view = mActivity.getLayoutInflater().inflate(R.layout.dialog_app_list, null);
        tvTitle=view.findViewById(R.id.tv_app_list_title);

        listView=view.findViewById(R.id.lv_app_list_select);
        if(type==1){
            listView.setAdapter(new ListAdapter(list));
            txtCancel =view.findViewById(R.id.tv_app_list_cancel);
            txtCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mDialog.dismiss();
                        mListener.onReject();
                    }
                }
            });
        }else if (type==2){
            listView.setAdapter(new ListAdapter(strings,index));
            txtCancel =view.findViewById(R.id.tv_app_list_cancel);
            txtCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mDialog.dismiss();
                        mListener.onReject();
                    }
                }
            });
        }

        mDialogBuilder.setView(view);
    }
    class ListAdapter extends BaseAdapter{
        private List<MarketTicker> list;

        private List<String> strings;
        private int index;

        private int type;

        public ListAdapter(List<MarketTicker> list) {
            this.list=list;
            this.type=1;
        }
        public ListAdapter(List<String> strings,int index) {
            this.strings=strings;
            this.index=index;
            this.type=2;
        }
        @Override
        public int getCount() {
            if(type==1){
                return list.size();
            }else {
                return strings.size();
            }

        }

        @Override
        public Object getItem(int i) {
            if(type==1){
                return list.get(i);
            }else {
                return strings.get(i);
            }
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if(type==1){
                if (view == null) {
                    view = inflater.inflate(R.layout.select_item, null);
                }
                if(list.get(i)==null){
                    return view;
                }
                TextView tvQuote=view.findViewById(R.id.tv_selectItem_quote);
                TextView tvBase=view.findViewById(R.id.tv_selectItem_base);
                final MarketTicker market=list.get(i);
                tvBase.setText(market.base);
                tvQuote.setText(market.quote);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialog.dismiss();
                        mListener.onConfirm(market);
                    }
                });
            }else if(type==2){
                if (view == null) {
                    view = inflater.inflate(R.layout.select_server_item, null);
                }
                if(strings.get(i)==null){
                    return view;
                }
                TextView tvText=view.findViewById(R.id.tv_selectItem_text);
                TextView tvStat=view.findViewById(R.id.tv_selectItem_stat);
                final String str = strings.get(i);
                tvText.setText(str);
                if(i==index){
                    tvStat.setVisibility(View.VISIBLE);
                }else {
                    tvStat.setVisibility(View.INVISIBLE);
                }

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialog.dismiss();
                        mListener.onConfirm(str);
                    }
                });

            }
            return view;
        }
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
        void onConfirm(MarketTicker market);
        void onConfirm(String server);
        void onReject();
    }
}
