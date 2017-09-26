package info.btsland.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
    private ImageView ivNavUser ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivNavUser=(ImageView)findViewById(R.id.iv_nav_user);
        ivNavUser.setOnClickListener(new ivNavUserOnClick());

    }
    class ivNavUserOnClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(MainActivity.this,UserActivity.class);
            MainActivity.this.startActivity(intent);
        }
    }
    protected void show(){
        System.out.print("测试");
    }

}
