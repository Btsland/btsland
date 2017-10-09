package info.btsland.app.ui;

/**
 * Created by zyf on 2017/10/8.
 * 把标题栏去除掉.与activity_main.xml相对应
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import info.btsland.app.R;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉自带的标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_home);
    }

}
