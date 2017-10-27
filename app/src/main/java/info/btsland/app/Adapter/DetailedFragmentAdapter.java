package info.btsland.app.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.lang.reflect.InvocationHandler;
import java.util.List;

/**
 * Created by Administrator on 2017/10/20.
 */

public class DetailedFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private String[] titles;

    public DetailedFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList,String[] titles) {
        super(fm);
        this.fragmentList=fragmentList;
        this.titles=titles;
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
        return titles[position];
    }
    public int getPageIconResId(int position){
        return 1;
    }
}
