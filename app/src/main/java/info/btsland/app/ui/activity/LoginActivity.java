package info.btsland.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.time.Instant;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.MarketStat;
import info.btsland.app.api.Wallet_api;
import info.btsland.app.api.account_object;
import info.btsland.app.exception.CreateAccountException;
import info.btsland.app.exception.NetworkStatusException;


/**
 * author：lw1000
 * function：登录
 * 2017/11/3.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener ,MarketStat.OnMarketStatUpdateListener{


    private static final String TAG ="LoginActivity" ;
    private EditText username, password;
    private Button bt_username_clear;
    private Button bt_pwd_clear;
    private Button tourist;
    private Button bt_pwd_eye;
    private Button login;
    private Button register;
   // private Button tourist;
    private boolean isOpen = false;

    private SharedPreferences  sps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        //声明函数文件名与操作模式
        sps=getSharedPreferences("Login",Context.MODE_PRIVATE);

    }




    private void initView() {

        username = (EditText) findViewById(R.id.username);
        // 监听文本框内容变化
        username.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // 获得文本框中的用户
                String user = username.getText().toString().trim();
                if ("".equals(user)) {
                    // 用户名为空,设置按钮不可见
                    bt_username_clear.setVisibility(View.INVISIBLE);
                    login.setVisibility(View.INVISIBLE);
                } else {
                    // 用户名不为空，设置按钮可见
                    bt_username_clear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password = (EditText) findViewById(R.id.password);
        // 监听文本框内容变化
        password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // 获得文本框中的用户
                String pwd = password.getText().toString().trim();
                if ("".equals(pwd)) {
                    // 密码为空,设置按钮不可见
                    bt_pwd_clear.setVisibility(View.INVISIBLE);

                } else {
                    // 密码不为空，设置按钮可见
                    bt_pwd_clear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bt_username_clear = (Button) findViewById(R.id.bt_username_clear);
        bt_username_clear.setOnClickListener(this);

        bt_pwd_clear = (Button) findViewById(R.id.bt_pwd_clear);
        bt_pwd_clear.setOnClickListener(this);

        bt_pwd_eye = (Button) findViewById(R.id.bt_pwd_eye);
        bt_pwd_eye.setOnClickListener(this);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);

        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);

        tourist = (Button) findViewById(R.id.tourist);
        tourist.setOnClickListener(this);


    }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_username_clear:
                // 清除登录名
                username.setText("");
                break;
            case R.id.bt_pwd_clear:
                // 清除密码
                password.setText("");
                break;
            case R.id.bt_pwd_eye:
                // 密码可见与不可见的切换
                if (isOpen) {
                    isOpen = false;
                } else {
                    isOpen = true;
                }

                // 默认isOpen是false,密码不可见
                changePwdOpenOrClose(isOpen);

                break;
            case R.id.login:
                if (username.getText().toString()==""){
                    Toast.makeText(LoginActivity.this,"请输入用户名",0).show();

                }else if (password.getText().toString()==""){
                    Toast.makeText(LoginActivity.this,"请输入密码",0).show();

                }else if(username.getText().toString()!=""&&password.getText().toString()!=""){

                    AccountThread loginThread=new AccountThread(username.getText().toString(),password.getText().toString(),AccountThread.LOGIN_BY_PASSWORD);
                    loginThread.start();
                }

//                AccountThread loginThread=new AccountThread("xjh1010","X123456789zz",AccountThread.LOGIN_BY_PASSWORD);
//                loginThread.start();
                break;
            case R.id.register:
                // 注册按钮
                AccountThread registerThread=new AccountThread("what123","X123456789xx",AccountThread.REGISTER_BY_PASSWORD);
                registerThread.start();
                break;
            case R.id.tourist:

               // Toast.makeText(LoginActivity.this, "忘记密码", 0).show();
                LoginActivity.this.finish();
                break;

            default:
                break;
        }
    }

    /**
     * 密码可见与不可见的切换
     *
     * @param flag
     */
    private void changePwdOpenOrClose(boolean flag) {
        // 第一次过来是false，密码不可见
        if (flag) {
            // 密码可见
            bt_pwd_eye.setBackgroundResource(R.drawable.password_open);
            // 设置EditText的密码可见
            password.setTransformationMethod(HideReturnsTransformationMethod
                    .getInstance());
        } else {
            // 密码不接见
            bt_pwd_eye.setBackgroundResource(R.drawable.password_close);
            // 设置EditText的密码隐藏
            password.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());
        }


    }


    @Override
    public void onMarketStatUpdate(MarketStat.Stat stat) {
        Log.i(TAG, "onMarketStatUpdate: stat:"+stat.account_object.toString());

    }

   public class AccountThread extends Thread{
        public static final int LOGIN_BY_PASSWORD=1;
        public static final int LOGIN_BY_BIN=2;
        public static final int REGISTER_BY_PASSWORD=3;
        private int want;
        private String name;
        private String pwd;

        public AccountThread(String name, String pwd,int want) {
            this.name=name;
            this.pwd=pwd;
            this.want=want;
        }

        @Override
        public void run() {
            Wallet_api wallet_api=new Wallet_api();
            switch (want){
                //账号登录
                case LOGIN_BY_PASSWORD:
                    account_object accountObject=null;
                    String loginRet="success";
                    try {
                        accountObject= wallet_api.import_account_password(name,pwd);
                    } catch (NetworkStatusException e) {
                        e.printStackTrace();
                    }
                    Bundle loginbundle=new Bundle();
                    if(accountObject==null){
                        loginRet="failure";
                    }
                    BtslandApplication.accountObject=accountObject;
                    loginbundle.putString("login",loginRet);
                    Message loginmsg=Message.obtain();
                    loginmsg.setData(loginbundle);
                    mHander.sendMessage(loginmsg);
                    break;
                //钱包登录
                case LOGIN_BY_BIN:

                    break;
                //帐号注册
                case REGISTER_BY_PASSWORD:
                    int registernRet=0;

                    try {
                        Log.i(TAG, "onClick: 注册");
                        registernRet = wallet_api.create_account_with_password(name,pwd);
                    } catch (NetworkStatusException e) {
                        e.printStackTrace();
                    } catch (CreateAccountException e) {
                        e.printStackTrace();
                    }
                    Bundle registerbundle=new Bundle();
                    registerbundle.putString("register","success");
                    Message registermsg=Message.obtain();
                    registermsg.setData(registerbundle);
                    mHander.sendMessage(registermsg);
                    break;
            }
        }
    }
    public Handler mHander=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle=msg.getData();
            if(bundle.getString("register")!=null&&bundle.getString("register").equals("success")){
                Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
            }
            if(bundle.getString("login")!=null&&bundle.getString("login").equals("success")){
                Log.i(TAG, "handleMessage: 登录成功");
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                rememberPassword();
                Intent iLogin=new Intent(LoginActivity.this,UserActivity.class);
                startActivity(iLogin);
                finish();

            }else if(bundle.getString("login")!=null&&bundle.getString("login").equals("failure")){
                Log.i(TAG, "handleMessage: 登录失败");
            }
        }
    };




    /*
    *
    * 初次登陆时记住密码
    * */
    private  void rememberPassword(){

        //借助Editor实现共享参数储存
        SharedPreferences.Editor editor=sps.edit();
        editor.putString("username",BtslandApplication.accountObject.name);
//        editor.putString("password",password.getText().toString());
        editor.commit();




    }











}























