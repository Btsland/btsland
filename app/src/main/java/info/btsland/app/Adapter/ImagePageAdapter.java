package info.btsland.app.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.R;

/**
 * Created by Administrator on 2017/10/20.
 */

public class ImagePageAdapter extends PagerAdapter {
    private Context mCntext;
    private List<String> stringList;
    private List<TextView> textViews;
    public ImagePageAdapter(Context context,List<String> stringList ){
        this.mCntext=context;
        this.stringList=stringList;
        textViews=new ArrayList<TextView>();
        for(int i=0;i<stringList.size();i++){
            TextView textView=new TextView(mCntext);
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setText(stringList.get(i));
            textViews.add(textView);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return stringList.get(position);
    }

    @Override
    public int getCount() {
        return stringList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(textViews.get(position));
        return textViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(textViews.get(position));
    }
}
