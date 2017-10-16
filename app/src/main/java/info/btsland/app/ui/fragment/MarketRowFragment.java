package info.btsland.app.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.R;
import info.btsland.app.model.Market;

public class MarketRowFragment extends Fragment {
    private View view;
    private int position;
    private Market market;

    private TextView tvCoin;
    private TextView tvFluctuation;
    private TextView tvNewPrice;
    private TextView tvBestAsk;
    private TextView tvBestBid;



    public MarketRowFragment() {
        // Required empty public constructor
    }

    public MarketRowFragment(int position, ArrayList<Market> markets) {
        this.position = position;
        this.market = markets.get(position);
    }

    public static MarketRowFragment newInstance(int position, List<Market> markets) {
        MarketRowFragment fragment = new MarketRowFragment();
        Bundle args = new Bundle();
        args.putInt("position",position);
        args.putSerializable("market",markets.get(position));
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

        view=inflater.inflate(R.layout.fragment_market_row, container, false);
        if (getArguments()!=null) {
            position = getArguments().getInt("position");
            market = (Market) getArguments().getSerializable("market");
        }
        init();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    private void init(){
        tvCoin=view.findViewById(R.id.tv_coin);
        tvFluctuation=view.findViewById(R.id.tv_fluctuation);
        tvNewPrice=view.findViewById(R.id.tv_newPrice);
        tvBestAsk=view.findViewById(R.id.tv_bestAsk);
        tvBestBid=view.findViewById(R.id.tv_bestBid);

        tvCoin.setText(market.getLeftCoin());
        tvFluctuation.setText(String.valueOf(market.getFluctuation()));
        tvNewPrice.setText(market.getNewPrice());
        tvBestAsk.setText(market.getBestAsk());
        tvBestBid.setText(market.getBestBid());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
