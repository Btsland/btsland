package info.btsland.app.model;

/**
 * Created by zyf on 2017/10/8.
 * 新闻实体类,title表示新闻标题,content表示新闻内容
 */

public class News {
    private String title;
    private String content;

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
