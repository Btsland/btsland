package info.btsland.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import java.util.ArrayList;
import java.util.List;

import info.btsland.app.Adapter.DetailedFragmentAdapter;
import info.btsland.app.R;


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
        viewPager1.setOffscreenPageLimit(2);
        String[] titles={"充值(RMB-CNY)","提现(CNY-RMB)"};
        List<Fragment> fragments=new ArrayList<Fragment>();
        DealerListFragment inFragment = DealerListFragment.newInstance(1);
        DealerListFragment outFragment = DealerListFragment.newInstance(2);
        fragments.add(inFragment);
        fragments.add(outFragment);
        DetailedFragmentAdapter adapter=new DetailedFragmentAdapter(getChildFragmentManager(),fragments,titles);
        PagerSlidingTabStrip tabStrip = view.findViewById(R.id.psts_c2c_title1);
        viewPager1.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
