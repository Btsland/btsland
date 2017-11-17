package info.btsland.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.object_id;
import info.btsland.app.api.operation_history_object;
import info.btsland.app.exception.NetworkStatusException;
import info.btsland.app.model.MarketTicker;


public class DetailedHaveInHandFragment extends Fragment {
    private static String MARKET="market";

    public DetailedHaveInHandFragment() {
        // Required empty public constructor
    }

    public static DetailedHaveInHandFragment newInstance(MarketTicker market) {
        DetailedHaveInHandFragment fragment = new DetailedHaveInHandFragment();
        Bundle args = new Bundle();
        args.putSerializable(MARKET, market);
        fragment.setArguments(args);
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

        return inflater.inflate(R.layout.fragment_detailed_have_in_hand, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
