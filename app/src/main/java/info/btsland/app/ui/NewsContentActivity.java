package info.btsland.app.ui;

/**
 * Created by zyf on 2017/10/8.
 * 显示新闻内容的活动，与news_content.xml相对应
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import info.btsland.app.R;
import info.btsland.app.ui.fragment.NewsContentFragment;

public class NewsContentActivity extends Activity {
    //actionStart()方法，将启动NewsActivity所需要的参数导入进来
    public static void actionStart(Context context, String newsTitle, String newsContent) {
        Intent intent = new Intent(context, NewsContentActivity.class);
        //将参数新闻标题、新闻内容（参数）保存到intent中
        intent.putExtra("news_title", newsTitle);
        intent.putExtra("news_content", newsContent);
        context.startActivity(intent);
    }

    @Override
    //重写父类Activity的onCreate方法
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏该活动自带的标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //加载news_content.xml的布局
        setContentView(R.layout.activity_newscontent);
        //取出Intent中保存的参数(即为新闻的标题、新闻的内容)
        String newsTitle = getIntent().getStringExtra("news_title");
        String newsContent = getIntent().getStringExtra("news_content");
        NewsContentFragment newsContentFragment = (NewsContentFragment) getFragmentManager().findFragmentById(R.id.news_content_fragment);
        // 通过findFragment()方法，在活动中得到碎片NewsContentFragment的实例，从而调用其refresh()
        newsContentFragment.refresh(newsTitle, newsContent);
    }
}

