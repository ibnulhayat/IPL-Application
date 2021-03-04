package tech.iotait.model;

public class Bowlers {
    String bowlerId;
    String bOver;
    String bRans;
    String bMaiden;
    String bWickets;
    String economyRate;

    public Bowlers() {
    }

    public Bowlers(String bowlerId, String bOver, String bMaiden, String bRans, String bWickets, String economyRate) {
        this.bowlerId = bowlerId;
        this.bOver = bOver;
        this.bRans = bRans;
        this.bMaiden = bMaiden;
        this.bWickets = bWickets;
        this.economyRate = economyRate;
    }

    public String getBowlerId() {
        return bowlerId;
    }

    public String getbOver() {
        return bOver;
    }

    public String getbRans() {
        return bRans;
    }

    public String getbMaiden() {
        return bMaiden;
    }

    public String getbWickets() {
        return bWickets;
    }

    public String getEconomyRate() {
        return economyRate;
    }
}
