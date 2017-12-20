package info.btsland.app.Adapter;

/**
 * Created by zyf on 2017/10/8.
 * 创建新闻列表的适配器,因为数据不能直接在ListView中显示，需要使用到适配器传给LiseView,
 * 在 getView()方法中,获取到了相应位置上的 News 类,并让新闻的标题在列表中进行显示
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.R;
import info.btsland.app.model.News;

public class NewsAdapter extends BaseAdapter {
    private int id;
    private Context context;
    private List<News> news;
    private LayoutInflater inflater;
    private List<View> views1=new ArrayList<>();


    List<View> views=new ArrayList<>();

    public NewsAdapter(Context context, int textViewResourceId, List<News> objects) {
        this.context=context;
        this.news=objects;
        this.inflater=LayoutInflater.from(this.context);
        this.id=textViewResourceId;
        fillIn();
    }
    public void fillIn(){
        for (int i=0;i<news.size();i++){
            View view = inflater.inflate(id, null);
            //实例化新闻标题
            TextView newsTitleText = (TextView) view.findViewById(R.id.news_title);
            //实例化新闻内容
            TextView newsTitleContentText = (TextView) view.findViewById(R.id.news_titlecontent);
            //实例化时间
            TextView newsDateText = (TextView) view.findViewById(R.id.news_date);
            //实例化新闻作者
            TextView newsAuthorText = (TextView) view.findViewById(R.id.news_author);
            //实例化新闻图片
           // ImageView newsImageText = (ImageView) view.findViewById(R.id.news_images);

            //让新闻的标题在列表中进行显示
            newsTitleText.setText(news.get(i).getTitle());
            //让新闻的内容在列表中进行显示
            newsTitleContentText.setText(news.get(i).getTitleContent());
            //让日期在列表中进行显示
            newsDateText.setText(news.get(i).getDate());
            //让新闻的作者在列表中进行显示
            newsAuthorText.setText(news.get(i).getAuthor());
            //让新闻的图片在列表中进行显示
           // newsImageText.setImageDrawable(context.getDrawable(news.get(i).getImage()));
            views1.add(view);
        }
    }

    @Override
    public int getCount() {
        return news.size();
    }

    @Override
    public Object getItem(int i) {
        return news.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    //重写父类ArrayAdapterd的getView方法
    public View getView(int position, View view, ViewGroup parent) {
        return views1.get(position);
    }
}

