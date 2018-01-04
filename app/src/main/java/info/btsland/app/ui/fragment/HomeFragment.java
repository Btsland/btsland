package info.btsland.app.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.Adapter.DetailedFragmentAdapter;
import info.btsland.app.R;

public class HomeFragment extends Fragment {
    private String TAG="HomeFragment";
    public HomeFragment() {

    }
    public HomeFragment newInstance() {
        HomeFragment homeFragment=new HomeFragment();
        return homeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");

    }

    /**
     * 初始化
     */
    private void init(View view){
        Log.e(TAG, "init: ");
        ViewPager viewPager1= view.findViewById(R.id.vp_detailed);
        String[] titles={"最新资讯","热点资讯","BTSLAND","IFAST"};
        List<Fragment> fragments=new ArrayList<Fragment>();
        //最新资讯
        LatestNewsFragment latestNewsFragment=new LatestNewsFragment();
        //热点资讯
        HotNewsFragment hotNewsFragment=new HotNewsFragment();
        //Btsland
        BtslandNewsFragment btslandNewsFragment=new BtslandNewsFragment();
        //IFAST
        IFASTFragment foreignInformationFragment=new IFASTFragment();

        fragments.add(latestNewsFragment);
        fragments.add(hotNewsFragment);
        fragments.add(btslandNewsFragment);
        fragments.add(foreignInformationFragment);

        DetailedFragmentAdapter adapter=new DetailedFragmentAdapter(getChildFragmentManager(),fragments,titles);
        PagerSlidingTabStrip tabStrip = view.findViewById(R.id.psts_detailed_title1);
        viewPager1.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        viewPager1.setCurrentItem(0);
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
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
