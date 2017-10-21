package info.btsland.app.ui.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import java.util.Locale;

import info.btsland.app.util.PreferenceUtil;

/**
 * Created by Administrator on 2017/10/13 0013.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * <切换语言>
     *
     * @param language
     */

    protected void switchLanguage(String language) {
        // 设置应用语言类型
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();

        if (language.equals("en")) {
            config.locale = Locale.ENGLISH;
        } else {
            config.locale = Locale.SIMPLIFIED_CHINESE;
        }
        resources.updateConfiguration(config, dm);

        // 保存设置语言的类型
        PreferenceUtil.commitString("language", language);

    }
}
