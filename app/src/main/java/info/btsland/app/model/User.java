package info.btsland.app.model;

import java.util.Objects;

/**
 * Created by Administrator on 2017/9/27.
 */

public class User {
    private String name;
    private String pwd;


    public User() {
    }

    public User(String name, String pwd) {
        this.name = name;
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

  
}
