package info.btsland.app.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import info.btsland.app.R;
import info.btsland.app.model.MarketTicker;
import info.btsland.app.ui.activity.MarketDetailedActivity;
import info.btsland.app.ui.fragment.MarketSimpleKFragment;

/**
 * Created by Administrator on 2017/10/16.
 */

public class RowAdapter extends BaseAdapter {
    private List<RowItemData> rowItemDataList;
    private Context context;
    private LayoutInflater inflater;

    public RowAdapter(List<RowItemData>  rowItemDataList, Context context) {
        this.rowItemDataList = rowItemDataList;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return rowItemDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return rowItemDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.row_item, null);
        }
        if(rowItemDataList.get(i)==null){
            return view;
        }
        //初始化
        TextView tvTitle=view.findViewById(R.id.tv_row_item_title);
        TextView tvLabel=view.findViewById(R.id.tv_row_item_label);
        TextView tvDescribe=view.findViewById(R.id.tv_row_item_describe);
        Switch aSwitch=view.findViewById(R.id.sw_row_item_switch);
        TextView tvRight=view.findViewById(R.id.tv_row_item_right);
        ConstraintLayout layout=view.findViewById(R.id.cl_row_item);
        //填充内容
        RowItemData data=rowItemDataList.get(i);
        tvTitle.setText(data.title);
        tvLabel.setText(data.label);
        tvDescribe.setText(data.describe);
        if(data.showSwitch){
            aSwitch.setVisibility(View.VISIBLE);
        }else {
            aSwitch.setVisibility(View.GONE);
        }
        if(data.showRight){
            tvRight.setVisibility(View.VISIBLE);
        }else {
            tvRight.setVisibility(View.GONE);
        }
        if(data.showTitle){
            tvTitle.setVisibility(View.VISIBLE);
        }else {
            tvTitle.setVisibility(View.GONE);
        }
        if(data.showDescribe){
            tvDescribe.setVisibility(View.VISIBLE);
        }else {
            tvDescribe.setVisibility(View.GONE);
        }
        if(data.showDrawable){
            tvLabel.setCompoundDrawables(data.labelDrawable,null,null,null);
        }
        layout.setOnClickListener(data.clListener);
        aSwitch.setOnCheckedChangeListener(data.switchListener);
        return view;
    }

    /**
     * row数据类
     */
    public static class RowItemData{
        public String title;
        public String label;
        public String describe;
        public boolean showTitle;
        public boolean showSwitch;
        public boolean showRight;
        public boolean showDescribe;
        public boolean showDrawable;
        public CompoundButton.OnCheckedChangeListener switchListener;
        public View.OnClickListener clListener;
        public Drawable labelDrawable;

        /**
         *
         * @param title 标题
         * @param label 内容
         * @param describe 描述
         * @param showTitle 是否显示标题
         * @param showSwitch 是否显示开关
         * @param showRight 是否显示右侧
         * @param showDescribe 是否显示描述
         * @param showDrawable 是否显示图标
         * @param switchListener 开关选择事件监听器
         * @param clListener 行单击事件监听器
         * @param labelDrawable 内容左侧图标
         */
        public RowItemData(String title, String label, String describe,boolean showTitle, boolean showSwitch, boolean showRight,boolean showDescribe,boolean showDrawable, CompoundButton.OnCheckedChangeListener switchListener, View.OnClickListener clListener,Drawable labelDrawable) {
            this.title = title;
            this.label = label;
            this.describe = describe;
            this.showTitle = showTitle;
            this.showSwitch = showSwitch;
            this.showRight = showRight;
            this.showDescribe = showDescribe;
            this.showDrawable = showDrawable;
            this.switchListener = switchListener;
            this.clListener = clListener;
            this.labelDrawable=labelDrawable;
        }
    }
}
