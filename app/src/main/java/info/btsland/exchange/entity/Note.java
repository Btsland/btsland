package info.btsland.exchange.entity;

import java.util.Date;

public class Note {
    private Integer id;

    private String dealerId;

    private String noteNo;

    private Double assetNum;

    private String assetCoin;

    private Double brokerage;

    private String account;

    private String realNo;

    private String realType;

    private String realDepict;

    private Date startTime;

    private Date accountReTime;

    private Date dealerReTime;

    private Date endTime;

    private String remarkCode;

    private Integer statNo;

    private String depict;

    private String flowPath;

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

    public String getNoteNo() {
        return noteNo;
    }

    public void setNoteNo(String noteNo) {
        this.noteNo = noteNo == null ? null : noteNo.trim();
    }

    public Double getAssetNum() {
        return assetNum;
    }

    public void setAssetNum(Double assetNum) {
        this.assetNum = assetNum;
    }

    public String getAssetCoin() {
        return assetCoin;
    }

    public void setAssetCoin(String assetCoin) {
        this.assetCoin = assetCoin == null ? null : assetCoin.trim();
    }

    public Double getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(Double brokerage) {
        this.brokerage = brokerage;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getRealNo() {
        return realNo;
    }

    public void setRealNo(String realNo) {
        this.realNo = realNo == null ? null : realNo.trim();
    }

    public String getRealType() {
        return realType;
    }

    public void setRealType(String realType) {
        this.realType = realType == null ? null : realType.trim();
    }

    public String getRealDepict() {
        return realDepict;
    }

    public void setRealDepict(String realDepict) {
        this.realDepict = realDepict == null ? null : realDepict.trim();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getAccountReTime() {
        return accountReTime;
    }

    public void setAccountReTime(Date accountReTime) {
        this.accountReTime = accountReTime;
    }

    public Date getDealerReTime() {
        return dealerReTime;
    }

    public void setDealerReTime(Date dealerReTime) {
        this.dealerReTime = dealerReTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getRemarkCode() {
        return remarkCode;
    }

    public void setRemarkCode(String remarkCode) {
        this.remarkCode = remarkCode == null ? null : remarkCode.trim();
    }

    public Integer getStatNo() {
        return statNo;
    }

    public void setStatNo(Integer statNo) {
        this.statNo = statNo;
    }

    public String getDepict() {
        return depict;
    }

    public void setDepict(String depict) {
        this.depict = depict == null ? null : depict.trim();
    }

    public String getFlowPath() {
        return flowPath;
    }

    public void setFlowPath(String flowPath) {
        this.flowPath = flowPath == null ? null : flowPath.trim();
    }
}