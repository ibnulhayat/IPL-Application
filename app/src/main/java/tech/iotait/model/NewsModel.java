package tech.iotait.model;

public class NewsModel {
    String newsTitle;
    String newsDescre;
    String NewsImage;
    String matchID;

    public NewsModel(String newsTitle, String newsDes, String newsImage, String matchID) {
        this.newsTitle = newsTitle;
        this.newsDescre = newsDes;
        NewsImage = newsImage;
        this.matchID = matchID;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsDescre() {
        return newsDescre;
    }

    public void setNewsDescre(String newsDescre) {
        this.newsDescre = newsDescre;
    }

    public String getNewsImage() {
        return NewsImage;
    }

    public void setNewsImage(String newsImage) {
        NewsImage = newsImage;
    }

    public String getMatchID() {
        return matchID;
    }

    public void setMatchID(String matchID) {
        this.matchID = matchID;
    }
}
