package info.btsland.app.ui.fragment;

/**
 * Created by zyf on 2017/10/8.
 * 与fragment_newscontent.xml相对应,通过 findViewById()方法分别获取到新闻的标题和内容控件
 */

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import info.btsland.app.R;
import info.btsland.app.model.News;

//重写父类Fragment的方法
public class NewsContentFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //为碎片加载布局news_content_frag.xml
        return inflater.inflate(R.layout.fragment_newscontent, container, false);
    }

    //点赞按钮
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button button = (Button) getActivity().findViewById(R.id.news_like);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getActivity(),"Clicked",Toast.LENGTH_LONG).show();
            }
        });
    }




    /**
     * 创建refresh()方法，
     * 这个方法用于将新闻的标题和内容显示在界面上
     *
     * @param news
     */
    public void refresh(News news) {
        View visibilityLayout = getActivity().findViewById(R.id.visibility_layout);
        visibilityLayout.setVisibility(View.VISIBLE);
        //接收新闻信息并刷新新闻内容
        TextView newsTitleText = (TextView) getActivity().findViewById(R.id.news_title);        //得到新闻标题控件的实例
        TextView newsContentText = (TextView) getActivity().findViewById(R.id.news_content);    //得到新闻内容控件的实例
        TextView newsDateText = (TextView) getActivity().findViewById(R.id.news_date);          //得到新闻时间控件的示例
        TextView newsAuthorText = (TextView) getActivity().findViewById(R.id.news_author);      //得到新闻作者控件的示例
        newsTitleText.setText(news.getTitle());         //刷新新闻的标题
        newsContentText.setText(news.getContent());     //刷新新闻的内容
        newsDateText.setText(news.getDate());           //刷新新闻的时间
        newsAuthorText.setText(news.getAuthor());       //刷新新闻的作者

    }
}
