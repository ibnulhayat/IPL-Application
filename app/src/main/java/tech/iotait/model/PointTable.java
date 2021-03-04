package tech.iotait.model;

public class PointTable {

    private String teamName,totalmatch,winn,lost,nrr,point,pos;

    public PointTable(String teamName, String totalmatch, String winn, String lost, String nrr, String point,String pos) {
        this.teamName = teamName;
        this.totalmatch = totalmatch;
        this.winn = winn;
        this.lost = lost;
        this.nrr = nrr;
        this.point = point;
        this.pos = pos;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTotalmatch() {
        return totalmatch;
    }

    public void setTotalmatch(String totalmatch) {
        this.totalmatch = totalmatch;
    }

    public String getWinn() {
        return winn;
    }

    public void setWinn(String winn) {
        this.winn = winn;
    }

    public String getLost() {
        return lost;
    }

    public void setLost(String lost) {
        this.lost = lost;
    }

    public String getNrr() {
        return nrr;
    }

    public void setNrr(String nrr) {
        this.nrr = nrr;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }
}
