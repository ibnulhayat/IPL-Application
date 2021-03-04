package tech.iotait.model;

public class BowlingLeadersModel {

    String name;
    String imageUrl;
    String playerScore;
    String playerLeading;

    public BowlingLeadersModel(String name, String imageUrl, String playerScore, String playerLeading) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.playerScore = playerScore;
        this.playerLeading = playerLeading;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(String playerScore) {
        this.playerScore = playerScore;
    }

    public String getPlayerLeading() {
        return playerLeading;
    }

    public void setPlayerLeading(String playerLeading) {
        this.playerLeading = playerLeading;
    }
}
