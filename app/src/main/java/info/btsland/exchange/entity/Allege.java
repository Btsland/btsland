package info.btsland.exchange.entity;

public class Allege {
    private Integer id;

    private String account="";

    private String dealerId="";

    private String depict="";

    private String noteNo="";

    private Integer statNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId == null ? null : dealerId.trim();
    }

    public String getDepict() {
        return depict;
    }

    public void setDepict(String depict) {
        this.depict = depict == null ? null : depict.trim();
    }

    public String getNoteNo() {
        return noteNo;
    }

    public void setNoteNo(String noteNo) {
        this.noteNo = noteNo == null ? null : noteNo.trim();
    }

    public Integer getStatNo() {
        return statNo;
    }

    public void setStatNo(Integer statNo) {
        this.statNo = statNo;
    }
}