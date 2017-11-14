package info.btsland.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import info.btsland.app.R;
import info.btsland.app.model.MarketTicker;

public class DetailedBuyAndSellFragment extends Fragment {
    private static final String MARKET = "market";

    private RecyclerView rlvBuy;
    private RecyclerView rlvSell;
    private TextView tvNewPrice;



    public DetailedBuyAndSellFragment() {
        // Required empty public constructor
    }

    public static DetailedBuyAndSellFragment newInstance(MarketTicker market) {
        DetailedBuyAndSellFragment fragment = new DetailedBuyAndSellFragment();
        Bundle args = new Bundle();
        args.putSerializable(MARKET, market);
        fragment.setArguments(args);
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
        View view=inflater.inflate(R.layout.fragment_detailed_buy_and_sell, container, false);
        init(view);
        return view;
    }
    private void init(View view){

    }
}
