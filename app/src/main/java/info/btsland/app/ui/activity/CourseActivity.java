package info.btsland.app.ui.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;


public class CourseActivity extends AppCompatActivity {

    private HeadFragment headFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        fillInHead();
    }
    /**
     * 装载顶部导航
     */
    private void fillInHead() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment == null) {
            headFragment = HeadFragment.newInstance(HeadFragment.HeadType.BACK_NULL, "使用教程");
            transaction.add(R.id.fra_course_head, headFragment,"head");
        }
        transaction.commit();
    }

}
