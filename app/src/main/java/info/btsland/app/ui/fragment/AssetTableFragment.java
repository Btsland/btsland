package info.btsland.app.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import info.btsland.app.R;

/**
 * author：lw1000
 * function：资产表的碎片
 * 2017/11/7.
 */

public class AssetTableFragment  extends Fragment implements AdapterView.OnItemClickListener{

    private TextView tvAssetTable;
    private TextView tvScaledValueTable;
    private TextView tvSupplySharetable;
    private TextView tvTransferAccountsTable;
    private  TextView tvMarketOperationTable;

    public AssetTableFragment() {


    }


    protected void  init(){


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //加载fragment_table.xml 布局
        View view=inflater.inflate(R.layout.fragment_table,container,false);
        //得到ListView实例
        view.findViewById(R.id.lv_userset);

        init();


        return super.onCreateView(inflater, container, savedInstanceState);

    }



    @Override
    public void onStart() {
        super.onStart();


    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }



    @Override
    public void onDetach() {
        super.onDetach();
    }


    //ListView子项目的点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}











