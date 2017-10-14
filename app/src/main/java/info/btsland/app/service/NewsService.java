package info.btsland.app.service;

import java.util.List;


import info.btsland.app.model.News;


/**
 * author：lw1000
 * function：新闻资讯接口
 * 2017/10/9.
 *
 */


public interface NewsService {


    /**
     * 获取新闻标题及内容
     */
    List<News>  getNews();



}



