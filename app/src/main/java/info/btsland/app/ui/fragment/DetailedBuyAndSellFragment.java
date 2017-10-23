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

public class DetailedBuyAndSellFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;


    public DetailedBuyAndSellFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailedBuyAndSellFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailedBuyAndSellFragment newInstance(String param1, String param2) {
        DetailedBuyAndSellFragment fragment = new DetailedBuyAndSellFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
