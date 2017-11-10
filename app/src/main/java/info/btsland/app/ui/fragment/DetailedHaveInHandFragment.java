package info.btsland.app.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.btsland.app.R;
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detailed_have_in_hand, container, false);
    }

}
