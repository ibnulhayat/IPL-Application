package tech.iotait.model;

public class Fixture {

    private String teamname1,teamname2, date,vanue,marchNum;
    private String team1img,team2img,stadium_name;

    public Fixture(String teamname1, String teamname2, String date, String marchNum, String team1img, String team2img,String stadium) {
        this.teamname1 = teamname1;
        this.teamname2 = teamname2;
        this.date = date;
        this.marchNum = marchNum;
        this.team1img = team1img;
        this.team2img = team2img;
        this.stadium_name = stadium;
    }

    public String getStadium_name() {
        return stadium_name;
    }

    public void setStadium_name(String stadium_name) {
        this.stadium_name = stadium_name;
    }

    public String getTeamname1() {
        return teamname1;
    }

    public void setTeamname1(String teamname1) {
        this.teamname1 = teamname1;
    }

    public String getTeamname2() {
        return teamname2;
    }

    public void setTeamname2(String teamname2) {
        this.teamname2 = teamname2;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTeam1img() {
        return team1img;
    }

    public String getTeam2img() {
        return team2img;
    }

    public String getMarchNum() {
        return marchNum;
    }

    public void setMarchNum(String marchNum) {
        this.marchNum = marchNum;
    }
}
