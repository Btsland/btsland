package info.btsland.exchange.entity;

public class Help {
    private Integer id;

    private String helpid;

    private String dealerid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHelpid() {
        return helpid;
    }

    public void setHelpid(String helpid) {
        this.helpid = helpid == null ? null : helpid.trim();
    }

    public String getDealerid() {
        return dealerid;
    }

    public void setDealerid(String dealerid) {
        this.dealerid = dealerid == null ? null : dealerid.trim();
    }
}