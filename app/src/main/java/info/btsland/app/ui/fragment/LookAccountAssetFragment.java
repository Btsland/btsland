package info.btsland.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.Adapter.AssetRowAdapter;
import info.btsland.app.R;
import info.btsland.app.api.asset;
import info.btsland.app.model.IAsset;
import info.btsland.app.ui.activity.LookActivity;
import info.btsland.app.ui.view.AppDialog;


public class LookAccountAssetFragment extends Fragment implements LookActivity.ReFurbishInfo {
    private ListView listView;
    private LookActivity lookActivity;
    private AssetRowAdapter rowAdapter;

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
    }


    @Override
    public void refurbish() {
        List<IAsset> iAssets=new ArrayList<>();
        lookActivity= (LookActivity) getActivity();
        List<asset>  assets=lookActivity.accountMap.get(lookActivity.name).assetlist;
        for (int i=0;i<assets.size();i++){
            iAssets.add(new IAsset(assets.get(i)));
        }
        if (isAdded()){
            rowAdapter=new AssetRowAdapter(getActivity());
            listView.setAdapter(rowAdapter);
            if(iAssets.size()==0){
                AppDialog appDialog=new AppDialog(getActivity());
                appDialog.setMsg("该用户无资产");
                appDialog.show();
            }
            rowAdapter.setAsset(iAssets);
        }
    }
}
