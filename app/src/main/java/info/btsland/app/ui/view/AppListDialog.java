package info.btsland.app.ui.view;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

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

    private String title="提示";

    public AppListDialog(Activity mActivity,List<MarketTicker> list) {
        this.list=list;
        this.mActivity = mActivity;
        this.inflater = LayoutInflater.from(mActivity);
        fillIn();
    }
    public void fillIn(){
        mDialogBuilder = new AlertDialog.Builder(mActivity);

        view = mActivity.getLayoutInflater().inflate(R.layout.dialog_app_list, null);
        tvTitle=view.findViewById(R.id.tv_app_list_title);

        listView=view.findViewById(R.id.lv_app_list_select);
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
        mDialogBuilder.setView(view);
    }
    class ListAdapter extends BaseAdapter{
        private List<MarketTicker> list;

        public ListAdapter(List<MarketTicker> list) {
            this.list=list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
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
        void onReject();
    }
}
