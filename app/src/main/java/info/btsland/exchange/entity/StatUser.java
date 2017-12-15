package info.btsland.exchange.entity;

public class StatUser {
    private Integer id;

    private Integer statNo;

    private String depict;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}