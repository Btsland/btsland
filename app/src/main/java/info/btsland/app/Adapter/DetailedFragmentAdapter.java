package info.btsland.app.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/20.
 */

public class DetailedFragmentAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "DetailedFragmentAdapter";
    private List<Fragment> fragmentList=new ArrayList<>();
    private String[] titles;
    private FragmentManager fm;

    public DetailedFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList,String[] titles) {
        super(fm);
        this.fm=fm;
        this.fragmentList=fragmentList;
        this.titles=titles;
    }
    public DetailedFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList=fragmentList;
    }
    public DetailedFragmentAdapter(FragmentManager fm) {
        super(fm);
    }
    public void setFragmentList(List<Fragment> fragmentList) {
        if(this.fragmentList != null){
            if(fm!=null) {
                FragmentTransaction ft = fm.beginTransaction();
                for (Fragment f : this.fragmentList) {
                    ft.remove(f);
                }
                ft.commit();
                ft = null;
                fm.executePendingTransactions();
            }
        }
        this.fragmentList = fragmentList;
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE; //这个是必须的
    }
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        if(fragmentList==null){
            return 0;
        }
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
}
