package info.btsland.app.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.Adapter.AssetRowAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.asset;
import info.btsland.app.api.asset_object;
import info.btsland.app.api.object_id;
import info.btsland.app.exception.NetworkStatusException;
import info.btsland.app.model.IAsset;
import info.btsland.app.model.MarketTicker;
import info.btsland.app.ui.activity.LookActivity;
import info.btsland.app.ui.view.AppDialog;
import info.btsland.app.util.NumericUtil;


public class LookAccountAssetFragment extends Fragment implements LookActivity.ReFurbishInfo {

    private String TAG="LookAccountAsset";
    private ListView listView;
    private LookActivity lookActivity;
    private AssetRowAdapter rowAdapter;
    private TextView tvCharge;

    public LookAccountAssetFragment() {
        // Required empty public constructor
    }


    public static LookAccountAssetFragment newInstance() {
        LookAccountAssetFragment fragment = new LookAccountAssetFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_look_account_asset, container, false);
        init(view);
        return view;
    }


    private void init(View view){
        listView=view.findViewById(R.id.lv_look_asset_list);
        tvCharge=view.findViewById(R.id.tv_look_asset_charge);
        tvCharge.setText("折合"+BtslandApplication.chargeUnit);
    }

    List<IAsset> iAssets;
    List<asset>  assets;
    @Override
    public void refurbish() {
        iAssets=new ArrayList<>();
        lookActivity= (LookActivity) getActivity();
        assets=lookActivity.accountMap.get(lookActivity.name).assetlist;
        for (int i=0;i<assets.size();i++){
            iAssets.add(new IAsset(assets.get(i)));
        }
        rowAdapter=new AssetRowAdapter(getActivity());
        listView.setAdapter(rowAdapter);
        rowAdapter.setAsset(iAssets);

    }
}
