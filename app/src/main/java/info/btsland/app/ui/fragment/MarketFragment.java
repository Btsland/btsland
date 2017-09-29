package info.btsland.app.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import info.btsland.app.R;
import info.btsland.app.model.Market;
import info.btsland.app.service.Impl.MarketServiceImpl;
import info.btsland.app.service.MarketService;
import info.btsland.app.ui.MainActivity;
import info.btsland.app.util.DensityUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MarketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MarketFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MarketService marketService;
    private OnFragmentInteractionListener mListener;

    private TextView tvMarketLeftCoin_1;
    private TextView tvMarketLeftCoin_2;
    private TextView tvMarketLeftCoin_3;
    private TextView tvMarketLeftCoin_4;
    private TextView tvMarketLeftCoin_5;
    private List marketsBTC;
    private List marketsCNY;
    private List marketsUSD;
    private List marketsBTS;
    private List marketsETH;
    private Map<String,List<Market>> market;
    public MarketFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MarketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MarketFragment newInstance(String param1, String param2) {
        MarketFragment fragment = new MarketFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_market, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init(){
        tvMarketLeftCoin_1=getActivity().findViewById(R.id.tv_market_left_coin1);
        tvMarketLeftCoin_2=getActivity().findViewById(R.id.tv_market_left_coin2);
        tvMarketLeftCoin_3=getActivity().findViewById(R.id.tv_market_left_coin3);
        tvMarketLeftCoin_4=getActivity().findViewById(R.id.tv_market_left_coin4);
        tvMarketLeftCoin_5=getActivity().findViewById(R.id.tv_market_left_coin5);
        LeftCoinOnClickListener onClickListener=new LeftCoinOnClickListener();
        tvMarketLeftCoin_1.setOnClickListener(onClickListener);
        tvMarketLeftCoin_2.setOnClickListener(onClickListener);
        tvMarketLeftCoin_3.setOnClickListener(onClickListener);
        tvMarketLeftCoin_4.setOnClickListener(onClickListener);
        tvMarketLeftCoin_5.setOnClickListener(onClickListener);
    }

    class LeftCoinOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            touchColor((TextView) view);//交互特效
            setMarket(view);
        }
        /**
         * 左侧导航栏交互特效
         * @param facingTextView 当前的控件
         */
        protected void touchColor(TextView facingTextView){
            facingTextView.setBackground(getView().getResources().getDrawable(R.drawable.tv_market_left_coin_touch,null));
            switch (facingTextView.getId()){
                case R.id.tv_market_left_coin1:
                    setBack(tvMarketLeftCoin_2);
                    setBack(tvMarketLeftCoin_3);
                    setBack(tvMarketLeftCoin_4);
                    setBack(tvMarketLeftCoin_5);
                    break;
                case R.id.tv_market_left_coin2:
                    setBack(tvMarketLeftCoin_1);
                    setBack(tvMarketLeftCoin_3);
                    setBack(tvMarketLeftCoin_4);
                    setBack(tvMarketLeftCoin_5);
                    break;
                case R.id.tv_market_left_coin3:
                    setBack(tvMarketLeftCoin_2);
                    setBack(tvMarketLeftCoin_1);
                    setBack(tvMarketLeftCoin_4);
                    setBack(tvMarketLeftCoin_5);
                    break;
                case R.id.tv_market_left_coin4:
                    setBack(tvMarketLeftCoin_2);
                    setBack(tvMarketLeftCoin_3);
                    setBack(tvMarketLeftCoin_1);
                    setBack(tvMarketLeftCoin_5);
                    break;
                case R.id.tv_market_left_coin5:
                    setBack(tvMarketLeftCoin_2);
                    setBack(tvMarketLeftCoin_3);
                    setBack(tvMarketLeftCoin_4);
                    setBack(tvMarketLeftCoin_1);
                    break;
            }
        }
        private void setBack(TextView TextView){
            TextView.setBackground(getView().getResources().getDrawable(R.drawable.tv_market_left_coin,null));
        }
        private void setMarket(View leftCoin){
            LinearLayout linearLayout=getActivity().findViewById(R.id.ll_market_info);
            linearLayout.removeAllViews();
            List<Market> markets=readMarket(leftCoin);
            createCol(linearLayout,markets);
        }
    }
        private List<Market> readMarket(View leftCoin){
            marketService=new MarketServiceImpl();
            market = marketService.getallinformation();
            List list=null;
            switch (leftCoin.getId()){
                case R.id.tv_market_left_coin1:
                    list = market.get("BTC");
                    break;
                case R.id.tv_market_left_coin2:
                    list = market.get("bitCNY");
                    break;
                case R.id.tv_market_left_coin3:
                    list = market.get("bitUSD");
                    break;
                case R.id.tv_market_left_coin4:
                    list = market.get("BTS");
                    break;
                case R.id.tv_market_left_coin5:
                    list =new ArrayList();
                    break;
            }
            return list;
        }
        private void createCol(LinearLayout Linear,List<Market> markets){
            Market market=null;
            for (int i = 0; i < markets.size(); i++) {
                market=markets.get(i);
                //生成一行
                LinearLayout row = new LinearLayout(getActivity());
                LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getActivity(),60f));
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setLayoutParams(rowParams);
                row.setBackground(getActivity().getDrawable(R.drawable.ll_market_info_row));
                //生成左侧linear
                LinearLayout left = new LinearLayout(getActivity());
                left.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams leftParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
                left.setLayoutParams(leftParams);
                //货币名称
                TextView coinName = new TextView(getActivity());
                LinearLayout.LayoutParams coinNameParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 2f);
                coinName.setLayoutParams(coinNameParams);
                coinName.setGravity(Gravity.CENTER);
                coinName.setTextSize(18);
                coinName.setLines(1);



                //涨跌幅
                TextView fluctuation = new TextView(getActivity());
                LinearLayout.LayoutParams fluctuationParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f);
                fluctuation.setLayoutParams(fluctuationParams);
                fluctuation.setGravity(Gravity.CENTER);
                fluctuation.setTextSize(12);
                fluctuation.setLines(1);


                //生成中间textView
                TextView price = new TextView(getActivity());
                LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 2.5f);
                price.setLayoutParams(priceParams);
                price.setGravity(Gravity.CENTER);
                price.setTextSize(30);
                price.setLines(1);
                price.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
                price.setSingleLine(true);

                //生成右侧linear
                LinearLayout right = new LinearLayout(getActivity());
                right.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams rightParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.8f);
                right.setLayoutParams(rightParams);
                //最低卖价
                TextView bestAsk = new TextView(getActivity());
                LinearLayout.LayoutParams bestBidParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f);
                bestBidParams.setMargins(DensityUtil.dip2px(getActivity(),6f),DensityUtil.dip2px(getActivity(),5f),0,0);
                bestAsk.setLayoutParams(bestBidParams);
                bestAsk.setGravity(Gravity.LEFT);
                bestAsk.setTextSize(12);
                bestAsk.setLines(1);
                bestAsk.setFilters(new InputFilter[]{new InputFilter.LengthFilter(14)});
                bestAsk.setSingleLine(true);


                //最高买价
                TextView bestBid = new TextView(getActivity());
                LinearLayout.LayoutParams bestAskParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f);
                bestAskParams.setMargins(DensityUtil.dip2px(getActivity(),6f),DensityUtil.dip2px(getActivity(),5f),0,0);
                bestBid.setLayoutParams(bestAskParams);
                bestBid.setGravity(Gravity.LEFT);
                bestBid.setTextSize(12);
                bestBid.setLines(1);;
                bestBid.setFilters(new InputFilter[]{new InputFilter.LengthFilter(14)});
                bestBid.setSingleLine(true);

                //设定值
                coinName.setText(market.getLeftCoin());
                fluctuation.setText( String.valueOf(market.getFluctuation()));
                price.setText(market.getNewPrice());
                String a="";
                bestAsk.setText(Html.fromHtml("<font color='"+getResources().getString(R.string.font_color_gray)+"'>卖出:&nbsp;&nbsp;</font>"+market.getBestAsk()));
                bestBid.setText(Html.fromHtml("<font color='"+getResources().getString(R.string.font_color_gray)+"'>买入:&nbsp;&nbsp;</font>"+market.getBestBid()));

                left.addView(coinName);
                left.addView(fluctuation);
                right.addView(bestAsk);
                right.addView(bestBid);

                row.addView(left);
                row.addView(price);
                row.addView(right);
                Linear.addView(row);
            }

        }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
