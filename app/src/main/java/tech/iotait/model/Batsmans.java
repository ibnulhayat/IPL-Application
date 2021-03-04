package tech.iotait.model;

public class Batsmans {
    String bid;
    String rans;
    String balls;
    String fours;
    String sixs;
    String stike_rate;
    String bOutDescriptions;

    public Batsmans() {
    }

    public Batsmans(String bid, String rans, String balls, String fours, String sixs, String stike_rate, String bOutDescriptions) {
        this.bid = bid;
        this.rans = rans;
        this.balls = balls;
        this.fours = fours;
        this.sixs = sixs;
        this.stike_rate = stike_rate;
        this.bOutDescriptions = bOutDescriptions;
    }

    public String getId() {
        return bid;
    }

    public String getRans() {
        return rans;
    }

    public String getBalls() {
        return balls;
    }

    public String getFours() {
        return fours;
    }

    public String getSixs() {
        return sixs;
    }

    public String getStike_rate() {
        return stike_rate;
    }

    public String getbOutDescriptions() {
        return bOutDescriptions;
    }
}
