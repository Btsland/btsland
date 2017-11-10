package info.btsland.app.ui.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import info.btsland.app.R;
import info.btsland.app.model.MarketTicker;

public class DetailedBuyAndSellFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MARKET = "market";
    private View view;


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
        view=inflater.inflate(R.layout.fragment_detailed_buy_and_sell, container, false);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ss() );

        return view;
    }
    class ss implements ViewTreeObserver.OnGlobalLayoutListener{

        @Override
        public void onGlobalLayout() {
            //移除布局变化监听
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            } else {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
            //getWindow().getDecorView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
            Rect r = new Rect();
            view.getWindowVisibleDisplayFrame(r);
            Log.i("lent", "lent = " + (r.height()));
            int height = r.height()+r.top;//手机屏幕可见区域高度
            Log.i("height","height:"+height);
        }
    }
}
