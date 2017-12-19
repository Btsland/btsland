package info.btsland.app.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.util.List;

/**
 * Created by Administrator on 2017/10/20.
 */

public class DetailedFragmentAdapter extends FragmentPagerAdapter {
    private static final String TAG = "DetailedFragmentAdapter";
    private List<Fragment> fragmentList;
    private String[] titles;

    public DetailedFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList,String[] titles) {
        super(fm);
        this.fragmentList=fragmentList;
        this.titles=titles;
    }
    public DetailedFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList=fragmentList;
    }




    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(titles!=null&&titles.length>0){
            return titles[position];
        }else {
            return "";
        }

    }
    public int getPageIconResId(int position){
        return 1;
    }
}
