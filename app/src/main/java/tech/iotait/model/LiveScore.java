package tech.iotait.model;

public class LiveScore {
    private String showDes,batTeamId,batTeamName,batTeamImg,bowlTeamImg,bowlTeamName,batTeamScore,batTeamOvers,batTeamRR,batTeamCRR,target,bowlTeamScore,bowlTeamOvrs,status,datapath;

    private String mnum,matchId,type,mchState,NoOfIngs;


    public LiveScore(String showDes, String batTeamId, String batTeamName, String batTeamImg,
                     String bowlTeamImg, String bowlTeamName, String batTeamScore, String
                             batTeamOvers, String batTeamRR, String batTeamCRR, String target,
                     String bowlTeamScore, String bowlTeamOvrs, String status, String datapath,
                     String mnum, String matchId, String type, String mchState, String NoOfIngs) {
        this.showDes = showDes;
        this.batTeamId = batTeamId;
        this.batTeamName = batTeamName;
        this.batTeamImg = batTeamImg;
        this.bowlTeamImg = bowlTeamImg;
        this.bowlTeamName = bowlTeamName;
        this.batTeamScore = batTeamScore;
        this.batTeamOvers = batTeamOvers;
        this.batTeamRR = batTeamRR;
        this.batTeamCRR = batTeamCRR;
        this.target = target;
        this.bowlTeamScore = bowlTeamScore;
        this.bowlTeamOvrs = bowlTeamOvrs;
        this.status = status;
        this.datapath = datapath;
        this.mnum = mnum;
        this.matchId = matchId;
        this.type = type;
        this.mchState = mchState;
        this.NoOfIngs = NoOfIngs;
    }


    public String getNoOfIngs() {
        return NoOfIngs;
    }

    public void setNoOfIngs(String noOfIngs) {
        NoOfIngs = noOfIngs;
    }

    public String getMnum() {
        return mnum;
    }

    public void setMnum(String mnum) {
        this.mnum = mnum;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMchState() {
        return mchState;
    }

    public void setMchState(String mchState) {
        this.mchState = mchState;
    }

    public String getBatTeamName() {
        return batTeamName;
    }

    public void setBatTeamName(String batTeamName) {
        this.batTeamName = batTeamName;
    }

    public String getShowDes() {
        return showDes;
    }

    public void setShowDes(String showDes) {
        this.showDes = showDes;
    }

    public String getBatTeamId() {
        return batTeamId;
    }

    public void setBatTeamId(String batTeamId) {
        this.batTeamId = batTeamId;
    }

    public String getBatTeamImg() {
        return batTeamImg;
    }

    public void setBatTeamImg(String batTeamImg) {
        this.batTeamImg = batTeamImg;
    }


    public String getBowlTeamImg() {
        return bowlTeamImg;
    }

    public void setBowlTeamImg(String bowlTeamImg) {
        this.bowlTeamImg = bowlTeamImg;
    }

    public String getBowlTeamName() {
        return bowlTeamName;
    }

    public void setBowlTeamName(String bowlTeamName) {
        this.bowlTeamName = bowlTeamName;
    }

    public String getBatTeamScore() {
        return batTeamScore;
    }

    public void setBatTeamScore(String batTeamScore) {
        this.batTeamScore = batTeamScore;
    }

    public String getBatTeamOvers() {
        return batTeamOvers;
    }

    public void setBatTeamOvers(String batTeamOvers) {
        this.batTeamOvers = batTeamOvers;
    }

    public String getBatTeamRR() {
        return batTeamRR;
    }

    public void setBatTeamRR(String batTeamRR) {
        this.batTeamRR = batTeamRR;
    }

    public String getBatTeamCRR() {
        return batTeamCRR;
    }

    public void setBatTeamCRR(String batTeamCRR) {
        this.batTeamCRR = batTeamCRR;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getBowlTeamScore() {
        return bowlTeamScore;
    }

    public void setBowlTeamScore(String bowlTeamScore) {
        this.bowlTeamScore = bowlTeamScore;
    }

    public String getBowlTeamOvrs() {
        return bowlTeamOvrs;
    }

    public void setBowlTeamOvrs(String bowlTeamOvrs) {
        this.bowlTeamOvrs = bowlTeamOvrs;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDatapath() {
        return datapath;
    }

    public void setDatapath(String datapath) {
        this.datapath = datapath;
    }
}
