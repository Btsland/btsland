package info.btsland.app;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.model.News;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("info.btsland.app", appContext.getPackageName());
    }


    @Test
    public List<News> getNews(){

        List<News> nlist =new ArrayList<News>();
        nlist.add(new News("标题","内容","新闻日期","标题内容"));
        nlist.add(new News("标题","内容","新闻日期","标题内容"));
        nlist.add(new News("标题","内容","新闻日期","标题内容"));
        nlist.add(new News("标题","内容","新闻日期","标题内容"));
        nlist.add(new News("标题","内容","新闻日期","标题内容"));
        nlist.add(new News("标题","内容","新闻日期","标题内容"));
        nlist.add(new News("标题","内容","新闻日期","标题内容"));
        nlist.add(new News("标题","内容","新闻日期","标题内容"));
        nlist.add(new News("标题","内容","新闻日期","标题内容"));
        nlist.add(new News("标题","内容","新闻日期","标题内容"));


        return nlist;
    }



}


















