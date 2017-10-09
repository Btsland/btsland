package info.btsland.app.model;

/**
 * Created by zyf on 2017/10/8.
 * 新闻实体类,title表示新闻标题,content表示新闻内容
 */

public class News {
    private String title;
    private String content;
    private String date;    //新闻日期
    private String titleContent;    //标题内容

    public String getTitleContent() {
        return titleContent;
    }

    public void setTitleContent(String titleContent) {
        this.titleContent = titleContent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
