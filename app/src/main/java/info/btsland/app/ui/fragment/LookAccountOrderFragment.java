package info.btsland.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.btsland.app.Adapter.OperationRecyclerViewAdapter;
import info.btsland.app.R;
import info.btsland.app.api.account_object;
import info.btsland.app.ui.activity.LookActivity;

public class LookAccountOrderFragment extends Fragment implements LookActivity.ReFurbishInfo {
    private RecyclerView listView;
    private LookActivity lookActivity;


    public LookAccountOrderFragment() {
        // Required empty public constructor
    }

    public static LookAccountOrderFragment newInstance() {
        LookAccountOrderFragment fragment = new LookAccountOrderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_look_account_order, container, false);
        init(view);
        return view;
    }

    private void init(View view){
        listView=view.findViewById(R.id.rlv_look_order_list);
    }

    @Override
    public void refurbish() {
        if (isAdded()){
            listView.setLayoutManager(new LinearLayoutManager(getActivity()));
            OperationRecyclerViewAdapter rlvOperationAdapter = new OperationRecyclerViewAdapter();
            listView.setAdapter(rlvOperationAdapter);
            listView.setItemAnimator(null);
            lookActivity= (LookActivity) getActivity();
            account_object accountObject = lookActivity.accountMap.get(lookActivity.name);
            rlvOperationAdapter.setList(accountObject.listHistoryObject);
            rlvOperationAdapter.notifyDataSetChanged();
        }
    }
}
