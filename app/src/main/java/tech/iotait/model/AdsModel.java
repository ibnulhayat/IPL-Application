package tech.iotait.model;

public class AdsModel {
    private String key;
    private String advalue;
    private String type;

    public AdsModel(String key, String advalue, String type) {
        this.key = key;
        this.advalue = advalue;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAdvalue() {
        return advalue;
    }

    public void setAdvalue(String advalue) {
        this.advalue = advalue;
    }
}
