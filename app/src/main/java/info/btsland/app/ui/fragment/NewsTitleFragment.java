package info.btsland.app.ui.fragment;

/**
 * Created by zyf on 2017/10/8.
 * 与fragment_newstitle.xml相对应
 * 根据碎片的生命周期,onAttach()方法会首先执行,因此在这里做了一些数据初始化的操作,比如调用 getNews()方法获取几
 条新闻数据,以及完成 NewsAdapter 的创建。然后在 onCreateView()方法中加载了news_title_frag 布局,
 并给新闻列表的 ListView 注册了点击事件。
 然后在 ListView 的点击事件里我们就可以判断,如果当前是单页模式,就启动一个新的活动去显示新闻内容
 */

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.Adapter.NewsAdapter;
import info.btsland.app.R;
import info.btsland.app.model.News;
import info.btsland.app.ui.NewsContentActivity;


public class NewsTitleFragment extends Fragment implements OnItemClickListener {
    private ListView newsTitleListView;
    private List<News> newsList;
    private NewsAdapter adapter;
    private boolean isTwoPane;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //初始化新闻数据
        newsList = getNews();
        adapter = new NewsAdapter(activity, R.layout.activity_newsitem, newsList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //加载fragment_newstitle.xml 布局
        View view = inflater.inflate(R.layout.fragment_newstitle, container, false);
        //得到ListView的实例
        newsTitleListView = (ListView) view.findViewById(R.id.news_title_list_view);
        //启动ListView的适配器，这样ListView就能与适配器的数据相关联了
        newsTitleListView.setAdapter(adapter);
        //为ListView中的子项设置监听器
        newsTitleListView.setOnItemClickListener(this);
        return view;
    }

    @Override
    //ListView子项目的点击事件
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        News news = newsList.get(position);
            //如果是单页模式（手机），就启动一个新的活动去显示新闻内容。
        Intent intent=new Intent(getActivity(),NewsContentActivity.class);
        intent.putExtra("news",news);
        getActivity().startActivity(intent);
    }


    private List<News> getNews() {
        //初始化新闻标题及内容
        List<News> newsList = new ArrayList<News>();
        News news1 = new News();
        news1.setTitle("Succeed in College as a Learning Disabled Student");
        news1.setDate("2017-10-09");
        news1.setTitleContent("College freshmen will soon learn to live with a roommate,adjust to a new social scene and survive less-than-stellae dining ball food.Students with learning disabilities will face these transitions while also grappling with a few more hurdles.");
        news1.setContent("College freshmen will soon learn to live with a roommate,adjust to a new social scene and survive less-than-stellae dining ball food.Students with learning disabilities will face these transitions while also grappling with a few more hurdles.");
        newsList.add(news1);
        News new2 = new News();
        new2.setTitle("Google Android exec poached by China's Xiaomi");
        new2.setDate("2017-10-09");
        new2.setTitleContent("China's Xiaomi has poached a key google executive involed in the tech giant's Android phones,in a move seen as a coup for the rapidly growing Chinese smartphone market.");
        new2.setContent("China's Xiaomi has poached a key google executive involed in the tech giant's Android phones,in a move seen as a coup for the rapidly growing Chinese smartphone market.");
        newsList.add(new2);
        return newsList;
    }
}
