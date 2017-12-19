package info.btsland.app.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.astuetz.PagerSlidingTabStrip;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.spongycastle.crypto.agreement.srp.SRP6Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import info.btsland.app.Adapter.DetailedFragmentAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.asset;
import info.btsland.app.api.asset_object;
import info.btsland.app.ui.activity.C2CExchangeActivity;
import info.btsland.app.ui.view.AppDialog;
import info.btsland.exchange.C2C;
import info.btsland.app.Adapter.DealerListAdapter;
import info.btsland.exchange.entity.Note;
import info.btsland.exchange.entity.User;
import info.btsland.exchange.http.UserHttp;
import info.btsland.exchange.utils.GsonDateAdapter;
import info.btsland.exchange.utils.NoteUtil;
import info.btsland.app.exception.NetworkStatusException;
import info.btsland.app.util.MyUitls;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class C2CFragment extends Fragment {
    private static final String TAG="C2CFragment";
    private int index =0;


    public C2CFragment() {
        // Required empty public constructor
    }

    public static C2CFragment newInstance() {
        C2CFragment fragment = new C2CFragment();
        Bundle args = new Bundle();
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
        View view=inflater.inflate(R.layout.fragment_c2c, container, false);
        if(savedInstanceState!=null){
            this.index = savedInstanceState.getInt("index", 1);
        }
        init(view);
        return view;
    }


    private void init(View view) {
        ViewPager viewPager1= view.findViewById(R.id.vp_c2c);
        String[] titles={"充值(RMB-CNY)","提现(CNY-RMB)"};
        List<Fragment> fragments=new ArrayList<Fragment>();
        DealerListFragment inFragment = DealerListFragment.newInstance(1);
        DealerListFragment outFragment = DealerListFragment.newInstance(2);
        fragments.add(inFragment);
        fragments.add(outFragment);
        DetailedFragmentAdapter adapter=new DetailedFragmentAdapter(getChildFragmentManager(),fragments,titles);
        PagerSlidingTabStrip tabStrip = view.findViewById(R.id.psts_c2c_title1);
        viewPager1.setAdapter(adapter);
        viewPager1.setCurrentItem(index);
        tabStrip.setViewPager(viewPager1);
        tabStrip.setOnPageChangeListener(new OnPage());
    }
    class OnPage implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("index",index);
        super.onSaveInstanceState(outState);
    }
}
