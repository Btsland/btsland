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
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        Log.e(TAG, "init: ");
        ViewPager viewPager1= (ViewPager) getActivity().findViewById(R.id.vp_detailed);
        String[] titles={"最新资讯","热点资讯","国内资讯","国外资讯"};
        List<Fragment> fragments=new ArrayList<Fragment>();
        LatestNewsFragment latestNewsFragment=new LatestNewsFragment();
        HotNewsFragment hotNewsFragment=new HotNewsFragment();
        DomesticInformationFragment domesticInformationFragment=new DomesticInformationFragment();
        ForeignInformationFragment foreignInformationFragment=new ForeignInformationFragment();
        fragments.add(latestNewsFragment);
        fragments.add(hotNewsFragment);
        fragments.add(domesticInformationFragment);
        fragments.add(foreignInformationFragment);
        DetailedFragmentAdapter adapter=new DetailedFragmentAdapter(getChildFragmentManager(),fragments,titles);
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) getActivity().findViewById(R.id.psts_detailed_title1);
        viewPager1.setAdapter(adapter);
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
