package info.btsland.exchange.entity;

public class RealAsset {
    private Integer id;

    private String dealerId;

    private String realAssetNo;

    private String realAssetType;

    private Integer isAvailable;

    private String depict;

    private String name;

    private Integer type;

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

    public String getRealAssetNo() {
        return realAssetNo;
    }

    public void setRealAssetNo(String realAssetNo) {
        this.realAssetNo = realAssetNo == null ? null : realAssetNo.trim();
    }

    public String getRealAssetType() {
        return realAssetType;
    }

    public void setRealAssetType(String realAssetType) {
        this.realAssetType = realAssetType == null ? null : realAssetType.trim();
    }

    public Integer getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Integer isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getDepict() {
        return depict;
    }

    public void setDepict(String depict) {
        this.depict = depict == null ? null : depict.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "RealAsset{" +
                "id=" + id +
                ", dealerId='" + dealerId + '\'' +
                ", realAssetNo='" + realAssetNo + '\'' +
                ", realAssetType='" + realAssetType + '\'' +
                ", isAvailable=" + isAvailable +
                ", depict='" + depict + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}