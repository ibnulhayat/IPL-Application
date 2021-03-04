package tech.iotait.model;

public class VideoModel {

    private String title,mUrl,imageUrl,youTubeID;

    public VideoModel(String teamName, String url, String img_Url, String youTubeid) {
        this.title = teamName;
        this.mUrl = url;
        this.imageUrl = img_Url;
        this.youTubeID = youTubeid;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String teamName) {
        this.title = teamName;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getYouTubeID() {
        return youTubeID;
    }

    public void setYouTubeID(String youTubeID) {
        this.youTubeID = youTubeID;
    }
}
