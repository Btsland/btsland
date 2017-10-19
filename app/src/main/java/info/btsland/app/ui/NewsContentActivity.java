package info.btsland.app.ui;

/**
 * Created by zyf on 2017/10/8.
 * 显示新闻内容的活动，与activity_newscontent.xml相对应
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import info.btsland.app.R;
import info.btsland.app.model.News;
import info.btsland.app.ui.fragment.HeadFragment;
import info.btsland.app.ui.fragment.NewsContentFragment;

public class NewsContentActivity extends AppCompatActivity {
    private HeadFragment headFragment;
    private TextView textView;
    //actionStart()方法，将启动NewsActivity所需要的参数导入进来
    public static void actionStart(Context context,News news) {
        Intent intent = new Intent(context, NewsContentActivity.class);
        //将参数新闻标题、新闻内容（参数）保存到intent中
        intent.putExtra("news", news);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newscontent);
        fillInHead();//装载顶部导航栏
        init();//初始化
        showNews();//展示新闻
    }
    /**
     * 初始化
     */
    private void init(){
    }
    /**
     * 装载顶部导航
     */
    private void fillInHead(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment==null){
            headFragment=new HeadFragment();
            headFragment.setType(HeadFragment.HeadType.BACK_NULL);
            transaction.add(R.id.fra_news_head,headFragment);
        }
        transaction.commit();
    }

    /**
     * 展示新闻详细信息
     */
    private void showNews(){
        //取出Intent中保存的参数(即为新闻的标题、新闻的内容)
        News news = (News) getIntent().getSerializableExtra("news");
        NewsContentFragment newsContentFragment = (NewsContentFragment) getFragmentManager().findFragmentById(R.id.news_content_fragment);
        // 通过findFragment()方法，在活动中得到碎片NewsContentFragment的实例，从而调用其refresh()
        newsContentFragment.refresh(news);
    }
}

