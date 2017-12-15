package info.btsland.exchange.entity;

public class UserRecord {
    private Integer id;

    private String dealerId;

    private Integer clinchCount;

    private Integer havingCount;

    private Double clinchTotal;

    private Double havingTotal;

    private Double time;

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

    public Integer getClinchCount() {
        return clinchCount;
    }

    public void setClinchCount(Integer clinchCount) {
        this.clinchCount = clinchCount;
    }

    public Integer getHavingCount() {
        return havingCount;
    }

    public void setHavingCount(Integer havingCount) {
        this.havingCount = havingCount;
    }

    public Double getClinchTotal() {
        return clinchTotal;
    }

    public void setClinchTotal(Double clinchTotal) {
        this.clinchTotal = clinchTotal;
    }

    public Double getHavingTotal() {
        return havingTotal;
    }

    public void setHavingTotal(Double havingTotal) {
        this.havingTotal = havingTotal;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public UserRecord(String dealerId, Integer clinchCount, Integer havingCount, Double clinchTotal, Double havingTotal, Double time) {
        this.dealerId = dealerId;
        this.clinchCount = clinchCount;
        this.havingCount = havingCount;
        this.clinchTotal = clinchTotal;
        this.havingTotal = havingTotal;
        this.time = time;
    }

    public UserRecord() {
    }
}