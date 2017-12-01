package info.btsland.app.model;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.io.Serializable;

/**
 * Created by zyf on 2017/10/8.
 * 新闻实体类,title表示新闻标题,content表示新闻内容
 */
public class News implements Serializable {
    private String title;
    private String content;
    private String date;    //新闻日期
    private String titleContent;    //标题内容
    private String author;          //新闻作者
    private int  image;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public News() {
    }

    public News(String title, String content, String date, String author) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.author = author;
//        this.image = image;
    }

    public News(String title, String content, String date, String titleContent, String author) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.titleContent = titleContent;
        this.author = author;
//        this.image = image;
    }

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", author='" + author + '\'' +
//                ", image='" + image + '\'' +
                '}';
    }
}