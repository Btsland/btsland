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
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import info.btsland.app.R;
import info.btsland.app.model.News;

public class NewsAdapter extends ArrayAdapter<News> {
    private int resourceId;
    private LayoutInflater inflater;

    public NewsAdapter(Context context, int textViewResourceId, List<News> objects) {
        super(context, textViewResourceId, objects);
        // 重写父类ArrayAdapter的方法 ,textViewResourceId即为news_item.xml布局的id(ListView子项布局的id)
        resourceId = textViewResourceId;
    }

    @Override
    //重写父类ArrayAdapterd的getView方法
    public View getView(int position, View convertView, ViewGroup parent) {
        // 得到当前ListView中子项的实例
        News news = (News) getItem(position);
        View view;
        if (convertView == null) {
            //为实例加载布局（即为activity_newsitem.xml的布局，ListView子项的布局）
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        } else {
            //convertView参数：这个参数用于将之前加载好的布局进行缓存，提高ListView的运行效率
            view = convertView;
        }

        //实例化新闻标题
        TextView newsTitleText = (TextView) view.findViewById(R.id.news_title);
        //实例化新闻内容
        TextView newsTitleContentText = (TextView) view.findViewById(R.id.news_titlecontent);
        //实例化时间
        TextView newsDateText = (TextView) view.findViewById(R.id.news_date);
        //实例化新闻作者
        TextView newsAuthorText = (TextView) view.findViewById(R.id.news_author);
        //实例化新闻图片
//        TextView newsImageText = (TextView) view.findViewById(R.id.news_images);

        //让新闻的标题在列表中进行显示
        newsTitleText.setText(news.getTitle());
        //让新闻的内容在列表中进行显示
        newsTitleContentText.setText(news.getTitleContent());
        //让日期在列表中进行显示
        newsDateText.setText(news.getDate());
        //让新闻的作者在列表中进行显示
        newsAuthorText.setText(news.getAuthor());
        //让新闻的图片在列表中进行显示
//        newsImageText.setText(news.getImage());

        return view;
    }
}

