package info.btsland.exchange.entity;

public class UserRecord {
    private Integer id;

    private String dealerId;

    private Integer inClinchCount;

    private Integer inHavingCount;

    private Double inClinchTotal;

    private Double inHavingTotal;

    private Integer outClinchCount;

    private Integer outHavingCount;

    private Double outClinchTotal;

    private Double outHavingTotal;

    private Double time;

    public UserRecord(String dealerId, Integer inClinchCount, Integer inHavingCount, Double inClinchTotal, Double inHavingTotal, Integer outClinchCount, Integer outHavingCount, Double outClinchTotal, Double outHavingTotal, Double time) {
        this.dealerId = dealerId;
        this.inClinchCount = inClinchCount;
        this.inHavingCount = inHavingCount;
        this.inClinchTotal = inClinchTotal;
        this.inHavingTotal = inHavingTotal;
        this.outClinchCount = outClinchCount;
        this.outHavingCount = outHavingCount;
        this.outClinchTotal = outClinchTotal;
        this.outHavingTotal = outHavingTotal;
        this.time = time;
    }

    public UserRecord() {
    }

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

    public Integer getInClinchCount() {
        return inClinchCount;
    }

    public void setInClinchCount(Integer inClinchCount) {
        this.inClinchCount = inClinchCount;
    }

    public Integer getInHavingCount() {
        return inHavingCount;
    }

    public void setInHavingCount(Integer inHavingCount) {
        this.inHavingCount = inHavingCount;
    }

    public Double getInClinchTotal() {
        return inClinchTotal;
    }

    public void setInClinchTotal(Double inClinchTotal) {
        this.inClinchTotal = inClinchTotal;
    }

    public Double getInHavingTotal() {
        return inHavingTotal;
    }

    public void setInHavingTotal(Double inHavingTotal) {
        this.inHavingTotal = inHavingTotal;
    }

    public Integer getOutClinchCount() {
        return outClinchCount;
    }

    public void setOutClinchCount(Integer outClinchCount) {
        this.outClinchCount = outClinchCount;
    }

    public Integer getOutHavingCount() {
        return outHavingCount;
    }

    public void setOutHavingCount(Integer outHavingCount) {
        this.outHavingCount = outHavingCount;
    }

    public Double getOutClinchTotal() {
        return outClinchTotal;
    }

    public void setOutClinchTotal(Double outClinchTotal) {
        this.outClinchTotal = outClinchTotal;
    }

    public Double getOutHavingTotal() {
        return outHavingTotal;
    }

    public void setOutHavingTotal(Double outHavingTotal) {
        this.outHavingTotal = outHavingTotal;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }
}