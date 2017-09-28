package info.btsland.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
    private ImageView ivNavUser ;
    private TextView tvNavSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //User
        ivNavUser=(ImageView)findViewById(R.id.iv_nav_user);
        ivNavUser.setOnClickListener(new ivNavUserOnClick());
        //Setting
        tvNavSet = (TextView)findViewById(R.id.tv_nav_set);
        tvNavSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);
            }
        });
    }


    class ivNavUserOnClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,UserActivity.class);
                startActivity(intent);
        }
    }



    protected void show(){
        System.out.print("测试");
        System.out.print("测试2");
        System.out.print("测试3");
        System.out.print("测试4");
    }
}
