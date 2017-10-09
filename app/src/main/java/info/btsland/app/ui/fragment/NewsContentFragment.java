package info.btsland.app.ui.fragment;

/**
 * Created by zyf on 2017/10/8.
 * 与news_content_frag.xml相对应,通过 findViewById()方法分别获取到新闻的标题和内容控件
 */

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import info.btsland.app.R;

//重写父类Fragment的方法
public class NewsContentFragment extends Fragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //为碎片加载布局news_content_frag.xml
        view = inflater.inflate(R.layout.fragment_newscontent, container, false);
        return view;

    }
    //创建refresh()方法，这个方法用于将新闻的标题和内容显示在界面上
    public void refresh(String newsTitle, String newsContent) {
        View visibilityLayout = view.findViewById(R.id.visibility_layout);
        visibilityLayout.setVisibility(View.VISIBLE);
        //得到新闻标题控件的实例
        TextView newsTitleText = (TextView) view.findViewById(R.id.news_title);
        //得到新闻内容控件的实例
        TextView newsContentText = (TextView) view.findViewById(R.id.news_content);
        //刷新新闻的标题
        newsTitleText.setText(newsTitle);
        //刷新新闻的内容
        newsContentText.setText(newsContent);
    }
}
