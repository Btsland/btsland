package info.btsland.app.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import info.btsland.app.R;


/**
 * author：lw1000
 * function：用户设置窗口
 * 2017/10/21.
 */

public class UserSetActity extends Activity {

    private ListView lv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_userset);

        lv=(ListView) findViewById(R.id.lv);

        uptdaListView();


    }

    public void uptdaListView(){



    }



}











