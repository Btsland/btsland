package info.btsland.app.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.Websocket_api;
import info.btsland.app.api.account_object;
import info.btsland.app.exception.CreateAccountException;
import info.btsland.app.exception.NetworkStatusException;

import static info.btsland.app.BtslandApplication.getMarketStat;


/**
 * author：lw1000
 * function：登录
 * 2017/11/3.
 */
public class LoginActivity extends AppCompatActivity {


    private static final String TAG ="LoginActivity" ;
    //登录界面
    private ConstraintLayout loginLayout;
    private EditText edLoginUser;//用户名
    private TextView tvLoginUserPoint;//用户名提示
    private TextView tvLoginUserClean;//清除用户名
    private EditText edLoginPwd;//密码
    private TextView tvLoginPwdPoint;//密码提示
    private TextView tvLoginPwdClean;//清除密码
    private Button btnLogin;//登录
    private Button btnToRegister;//去注册

    //注册界面
    private ConstraintLayout registerLayout;
        private EditText edRegisterUser;//用户名
    private TextView tvRegisterUserPoint;//用户名提示
    private TextView tvRegisterUserClean;//清除用户名
    private EditText edRegisterPwd;//密码
    private TextView tvRegisterPwdPoint;//密码提示
    private TextView tvRegisterPwdClean;//清除密码
    private EditText edRegisterRePwd;//确认密码
    private TextView tvRegisterRePwdPoint;//确认密码提示
    private TextView tvRegisterRePwdClean;//清除确认密码
    private CheckBox ckRegister;//确认备份
    private Button btnRegister;//注册
    private Button btnToLogin;//去登录

   // private Button tourist;
    private boolean isOpen = false;

    private boolean pwdIsPual=false;
    private boolean userIsPual=false;
    private String registerUser;
    private String registerPwd;
    private KProgressHUD hud;
    private Handler purseHander;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        purseHander =(Handler) getIntent().getSerializableExtra("hander");
        initView();
        //声明函数文件名与操作模式

    }
    private void initView() {
        loginLayout= (ConstraintLayout) findViewById(R.id.cl_login);
        edLoginUser= (EditText) findViewById(R.id.ed_login_userName);//用户名
        tvLoginUserPoint= (TextView) findViewById(R.id.tv_login_user_point);//用户名提示
        tvLoginUserClean= (TextView) findViewById(R.id.tv_login_user_cleanUp);//清除用户名
        edLoginPwd= (EditText) findViewById(R.id.ed_login_password);//密码
        tvLoginPwdPoint= (TextView) findViewById(R.id.tv_login_Pwd_point);//密码提示
        tvLoginPwdClean= (TextView) findViewById(R.id.tv_login_pwd_cleanUp);//清除密码
        btnLogin= (Button) findViewById(R.id.btn_login);//登录
        btnToRegister= (Button) findViewById(R.id.btn_toRegister);//去注册

        registerLayout= (ConstraintLayout) findViewById(R.id.cl_register);
        edRegisterUser= (EditText) findViewById(R.id.ed_register_userName);//用户名
        tvRegisterUserPoint= (TextView) findViewById(R.id.tv_register_user_point);//用户名提示
        tvRegisterUserClean= (TextView) findViewById(R.id.tv_register_user_cleanUp);//清除用户名
        edRegisterPwd= (EditText) findViewById(R.id.ed_register_password);//密码
        tvRegisterPwdPoint= (TextView) findViewById(R.id.tv_register_Pwd_point);//密码提示
        tvRegisterPwdClean= (TextView) findViewById(R.id.tv_register_Pwd_cleanUp);//清除密码
        edRegisterRePwd= (EditText) findViewById(R.id.ed_register_rePassword);//确认密码
        tvRegisterRePwdPoint= (TextView) findViewById(R.id.tv_register_rePwd_point);//确认密码提示
        tvRegisterRePwdClean = (TextView) findViewById(R.id.tv_register_rePwd_cleanUp);;//清除确认密码
        ckRegister= (CheckBox) findViewById(R.id.ck_register);//确认备份
        btnRegister= (Button) findViewById(R.id.btn_register);//注册
        btnToLogin= (Button) findViewById(R.id.btn_toLogin);//去登录

        IOnClickListener iOnClick=new IOnClickListener();
        btnLogin.setOnClickListener(iOnClick);
        btnToRegister.setOnClickListener(iOnClick);
        btnRegister.setOnClickListener(iOnClick);
        btnToLogin.setOnClickListener(iOnClick);

        //用户名输入信息校验
        edRegisterUser.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                userIsPual=false;
                tvRegisterUserPoint.setTextColor(getResources().getColor(R.color.color_font_red));
                String strAccountName = editable.toString();
                if (strAccountName.isEmpty() == true) {
                    tvRegisterUserPoint.setText("");
                    return;
                }
                if (Character.isLetter(strAccountName.charAt(0)) == false) {
                    tvRegisterUserPoint.setText(R.string.The_user_name_must_begin_with_a_letter);
                } else if (strAccountName.length() <= 4) {  // 用户名太短
                    tvRegisterUserPoint.setText(R.string.Username_too_short);
                } else if (strAccountName.endsWith("-")) {
                    tvRegisterUserPoint.setText(R.string.User_name_cannot_end_with_一);
                } else {
                    boolean b = false;
                    for (char c : strAccountName.toCharArray()) {
                        if (Character.isLetter(c) == false) {
                            b = true;
                        }
                    }
                    if (b == false) {
                        tvRegisterUserPoint.setText(R.string.User_name_must_not_contain_only_letters);
                    }else {
                        tvRegisterUserPoint.setText(R.string.User_name_is_valid_Query_is_already_available);
                        //判断该用户名是否可用
                        processCheckAccount(strAccountName);
                    }
                }
            }
        });
        //密码校验
        edRegisterPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                pwdIsPual=false;
                String str = s.toString();
                if(str.isEmpty()){
                    tvRegisterPwdPoint.setText("");
                    return;
                }
                if (str.length() < 12) {
                    tvRegisterPwdPoint.setText(R.string.The_password_needs_at_least_12_characters_and_contains_at_least_1_capitals_and_1_digits);
                } else {
                    boolean bDigit = str.matches(".*\\d+.*");
                    boolean bUpper = str.matches(".*[A-Z]+.*");
                    boolean bLower = str.matches(".*[a-z]+.*");
                    if ((bDigit && bUpper && bLower) == false) {
                        tvRegisterPwdPoint.setText(R.string.The_password_needs_at_least_12_characters_and_contains_at_least_1_capitals_and_1_digits);
                    } else {
                        tvRegisterPwdPoint.setText("");
                    }
                }
            }
        });
        //确认密码校验
        edRegisterRePwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                tvRegisterRePwdPoint.setText("");
                pwdIsPual=false;
                String strPwd = edRegisterPwd.getText().toString();
                String strRePsd = s.toString();

                if (strPwd.compareTo(strRePsd) == 0) {
                    if(strPwd.length()>0&&strRePsd.length()>0){
                        tvRegisterRePwdPoint.setText("");
                        pwdIsPual=true;//设置密码合格
                    }else {
                        tvRegisterRePwdPoint.setText("");
                        pwdIsPual = false;//设置密码不合格
                    }
                } else {
                    tvRegisterRePwdPoint.setText(R.string.The_password_for_the_two_input_is_inconsistent);
                    pwdIsPual=false;
                }
            }
        });

    }

    /**
     * 判断该用户名是否可用
     * @param strAccount
     */
    private void processCheckAccount(final String strAccount) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int nRet = BtslandApplication.nRet;
                if (nRet == Websocket_api.WEBSOCKET_CONNECT_SUCCESS) {
                    account_object accountObject=null;
                    try {
                        accountObject = getMarketStat().mWebsocketApi.get_account_by_name(strAccount);
                    } catch (NetworkStatusException e) {
                        e.printStackTrace();
                    }
                    if (accountObject == null) {
                        checkHander.sendEmptyMessage(0);
                    } else {
                        checkHander.sendEmptyMessage(1);
                    }
                }
            }
        }).start();
    }

    class IOnClickListener implements View.OnClickListener{
        private AccountThread thread;
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_login:
                    String loginUserName=edLoginUser.getText().toString();
                    String loginPassword=edLoginPwd.getText().toString();
                    if(!loginUserName.isEmpty()&&!loginPassword.isEmpty()){
                        hud=KProgressHUD.create(LoginActivity.this);
                        hud.setLabel(getResources().getString(R.string.please_wait));
                        hud.show();
                        thread=new AccountThread(loginUserName,loginPassword,AccountThread.LOGIN_BY_PASSWORD,mHander,purseHander);
                        thread.start();
                    }
                    break;
                case R.id.btn_toLogin:
                    loginLayout.setVisibility(View.VISIBLE);
                    registerLayout.setVisibility(View.GONE);
                    break;
                case R.id.btn_register:
                    if(ckRegister.isChecked()){
                        String registerUserName=edRegisterUser.getText().toString();
                        String registerPassword=edRegisterPwd.getText().toString();
                        String registerRePassword=edRegisterRePwd.getText().toString();
                        if(registerPassword.equals(registerRePassword)){
                            //判断用户名密码是不合格
                            if(pwdIsPual&&userIsPual){
                                registerUser=registerUserName;
                                registerPwd=registerPassword;
                                hud=KProgressHUD.create(LoginActivity.this);
                                hud.setLabel(getResources().getString(R.string.please_wait));
                                hud.show();
                                thread=new AccountThread(registerUser,registerPwd,AccountThread.REGISTER_BY_PASSWORD,mHander,purseHander);
                                thread.start();
                            }
                        }
                    }
                    break;
                case R.id.btn_toRegister:
                    loginLayout.setVisibility(View.GONE);
                    registerLayout.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

   public static class AccountThread extends Thread{
        public static final int LOGIN_BY_PASSWORD=1;
        public static final int LOGIN_BY_BIN=2;
        public static final int REGISTER_BY_PASSWORD=3;
       private  Handler handler;
       private Handler purseHander;

       private int want;
        private String name;
        private String pwd;

       public AccountThread(String name, String pwd,int want,Handler handler,Handler purseHander) {
            this.name=name;
            this.pwd=pwd;
            this.want=want;
            this.handler=handler;
            this.purseHander=purseHander;
        }


       @Override
        public void run() {
            switch (want){
                //账号登录
                case LOGIN_BY_PASSWORD:
                    account_object accountObject=null;
                    String loginRet="success";
                    try {
                        accountObject= BtslandApplication.getWalletApi().import_account_password(name,pwd);

                    } catch (NetworkStatusException e) {
                        e.printStackTrace();
                    }
                    Bundle loginBundle=new Bundle();
                    if(accountObject==null){
                        loginRet="failure";
                    }else {
                        BtslandApplication.accountObject=accountObject;
                        BtslandApplication.queryAsset();
                        BtslandApplication.isLogin=true;
                    }
                    loginBundle.putString("login",loginRet);

                    Message loginMsg=Message.obtain();
                    loginMsg.setData(loginBundle);
                    if(purseHander!=null){

                        purseHander.sendEmptyMessage(1);
                    }
                    handler.sendMessage(loginMsg);

                    break;
                //钱包登录
                case LOGIN_BY_BIN:

                    break;
                //帐号注册
                case REGISTER_BY_PASSWORD:
                    int registerNRet=-1;

                    try {
                        Log.i(TAG, "onClick: 注册");
                        registerNRet = BtslandApplication.getWalletApi().create_account_with_password(name,pwd);
                    } catch (NetworkStatusException e) {
                        e.printStackTrace();
                    } catch (CreateAccountException e) {
                        e.printStackTrace();
                    }
                    Bundle registerBundle=new Bundle();
                    Message registerMsg=Message.obtain();
                    if(registerNRet==0){
                        registerBundle.putString("register","success");

                    }else {
                        registerBundle.putString("register","fail");
                    }
                    registerMsg.setData(registerBundle);
                    if(purseHander!=null){

                        purseHander.sendEmptyMessage(1);
                    }
                    handler.sendMessage(registerMsg);

                    break;
            }
        }
    }

    /**
     * 接收是否登录
     */
    public Handler mHander=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle=msg.getData();
            if(bundle.getString("register")!=null&&bundle.getString("register").equals("success")){
                Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                AccountThread accountThread=new AccountThread(registerUser,registerPwd,AccountThread.LOGIN_BY_PASSWORD,this,purseHander);
                accountThread.start();
            }else if(bundle.getString("register")!=null&&bundle.getString("register").equals("fail")){
                Toast.makeText(LoginActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
            }
            if(bundle.getString("login")!=null&&bundle.getString("login").equals("success")){
                Log.i(TAG, "handleMessage: 登录成功");
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                hud.dismiss();
                saveUser();
//                Intent iLogin=new Intent(LoginActivity.this,MainActivity.class);
//                startActivity(iLogin);
                finish();



            }else if(bundle.getString("login")!=null&&bundle.getString("login").equals("failure")){
                Log.i(TAG, "handleMessage: 登录失败");
                hud.dismiss();
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 接收用户名校验信息
     */
    public Handler checkHander=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0){
                tvRegisterUserPoint.setText(R.string.User_name_available);
                tvRegisterUserPoint.setTextColor(getResources().getColor(R.color.color_green));
                userIsPual=true;//设置用户名可用
            }else {
                tvRegisterUserPoint.setText(R.string.User_name_already_exists);
                tvRegisterUserPoint.setTextColor(getResources().getColor(R.color.color_font_red));
            }
        }
    };


    /**
     * 保存用户
     */
    public static void saveUser(){
        //借助Editor实现共享参数储存
        SharedPreferences  sps=BtslandApplication.getInstance().getSharedPreferences("Login",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sps.edit();
        editor.putString("username",BtslandApplication.accountObject.name);
        editor.commit();
    }











}























