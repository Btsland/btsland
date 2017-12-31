package info.btsland.exchange.entity;

import java.util.ArrayList;
import java.util.List;

public class User {
    private Integer id=0;

    private String dealerId;

    private String dealerName;

    private String password;

    private String account;

    private Double brokerageIn;

    private Double brokerageOut;

    private String depict;

    private Integer stat;

    private Integer type;

    private Double lowerLimitIn;

    private Double upperLimitOut;

    private Double lowerLimitOut;

    public UserInfo userInfo;

    public List<RealAsset> realAssets;

    public UserRecord userRecord;

    public List<Note> havingNotes;

    public List<Chat> helpOldChatList=new ArrayList<>();

    public List<Chat> helpNewChatList=new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId == null ? null : dealerId.trim();
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName == null ? null : dealerName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public Double getBrokerageIn() {
        return brokerageIn;
    }

    public void setBrokerageIn(Double brokerageIn) {
        this.brokerageIn = brokerageIn;
    }

    public Double getBrokerageOut() {
        return brokerageOut;
    }

    public void setBrokerageOut(Double brokerageOut) {
        this.brokerageOut = brokerageOut;
    }

    public String getDepict() {
        return depict;
    }

    public void setDepict(String depict) {
        this.depict = depict == null ? null : depict.trim();
    }

    public Integer getStat() {
        return stat;
    }

    public void setStat(Integer stat) {
        this.stat = stat;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getLowerLimitIn() {
        return lowerLimitIn;
    }

    public void setLowerLimitIn(Double lowerLimitIn) {
        this.lowerLimitIn = lowerLimitIn;
    }

    public Double getUpperLimitOut() {
        return upperLimitOut;
    }

    public void setUpperLimitOut(Double upperLimitOut) {
        this.upperLimitOut = upperLimitOut;
    }

    public Double getLowerLimitOut() {
        return lowerLimitOut;
    }

    public void setLowerLimitOut(Double lowerLimitOut) {
        this.lowerLimitOut = lowerLimitOut;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", dealerId='" + dealerId + '\'' +
                ", dealerName='" + dealerName + '\'' +
                ", password='" + password + '\'' +
                ", account='" + account + '\'' +
                ", brokerageIn=" + brokerageIn +
                ", brokerageOut=" + brokerageOut +
                ", depict='" + depict + '\'' +
                ", stat=" + stat +
                ", type=" + type +
                ", lowerLimitIn=" + lowerLimitIn +
                ", upperLimitOut=" + upperLimitOut +
                ", lowerLimitOut=" + lowerLimitOut +
                ", userInfo=" + userInfo +
                ", realAssets=" + realAssets +
                ", userRecord=" + userRecord +
                '}';
    }
}