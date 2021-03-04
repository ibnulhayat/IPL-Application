package tech.iotait.model;

public class LiveTv  {
    private String name,url,alt_url,image,alt_image,apk;


    public LiveTv(String name, String url, String alt_url, String image, String alt_image, String apk) {
        this.name = name;
        this.url = url;
        this.alt_url = alt_url;
        this.image = image;
        this.alt_image = alt_image;
        this.apk = apk;
    }

//    public LiveTv(String name, String url) {
//        this.name = name;
//        this.url = url;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlt_url() {
        return alt_url;
    }

    public void setAlt_url(String alt_url) {
        this.alt_url = alt_url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAlt_image() {
        return alt_image;
    }

    public void setAlt_image(String alt_image) {
        this.alt_image = alt_image;
    }

    public String getApk() {
        return apk;
    }

    public void setApk(String apk) {
        this.apk = apk;
    }
}
