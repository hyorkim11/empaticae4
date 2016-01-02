package empaticae4.hrker.com.empaticae4.wrapper;

import java.util.Calendar;

public class ReportDataWrapper  {

    private String userID;
    private String reportType;
    private float EDA, EDAT;
    private String callContact;

    private Calendar startTime;
    private Calendar endTime;
    private long duration;
    private long duration_1;
    private long duration_2;
    private long duration_3;
    private long duration_4;
    private long duration_5;
    private long duration_6;

    private int answer1;
    private int intensity;
    private int answer2;
    private int answer3;
    private int answer4;
    private int answer5;
    private Boolean drinking;
    
    
    private String icnm;
    private String cnm;

    private String ice;
    private String ce;

    private String icgm;
    private String cgm;

    private String icct;
    private String cct;

    private String icd;
    private String cd;


    public ReportDataWrapper()  {

        // Initialize default values
        userID = "No_ID";

        reportType = "N/A";
        EDA = 0.0f;
        EDAT = 0.0f;

        callContact = "Unsaved";

        duration = 0;
        duration_1 = 0;
        duration_2 = 0;
        duration_3 = 0;
        duration_4 = 0;
        duration_5 = 0;
        duration_6 = 0;

        answer1 = -1;
        intensity = -1;
        answer2 = -1;
        answer3 = -1;
        answer4 = -1;
        answer5 = -1;
        drinking = false;

        icnm = "Other";
        cnm = "Other";
        ice = "Other";
        ce = "Other";
        icgm = "Other";
        cgm = "Other";
        icct = "Other";
        cct = "Other";
        icd = "Other";
        cd = "Other";

    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCallContact() {
        return callContact;
    }

    public void setCallContact(String contact) {
        this.callContact = contact;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration_1() {
        return duration_1;
    }

    public void setDuration_1(long duration_1) {
        this.duration_1 = duration_1;
    }

    public long getDuration_2() {
        return duration_2;
    }

    public void setDuration_2(long duration_2) {
        this.duration_2 = duration_2;
    }

    public long getDuration_3() {
        return duration_3;
    }

    public void setDuration_3(long duration_3) {
        this.duration_3 = duration_3;
    }

    public long getDuration_4() {
        return duration_4;
    }

    public void setDuration_4(long duration_4) {
        this.duration_4 = duration_4;
    }

    public long getDuration_5() {
        return duration_5;
    }

    public void setDuration_5(long duration_5) {
        this.duration_5 = duration_5;
    }

    public long getDuration_6() {
        return duration_6;
    }

    public void setDuration_6(long duration_6) {
        this.duration_6 = duration_6;
    }

    public int getAnswer1() {
        return answer1;
    }

    public void setAnswer1(int answer1) {
        this.answer1 = answer1;
    }

    public int getAnswer2() {
        return answer2;
    }

    public void setAnswer2(int answer2) {
        this.answer2 = answer2;
    }

    public int getAnswer3() {
        return answer3;
    }

    public void setAnswer3(int answer3) {
        this.answer3 = answer3;
    }

    public int getAnswer4() {
        return answer4;
    }

    public void setAnswer4(int answer4) {
        this.answer4 = answer4;
    }

    public int getAnswer5() {
        return answer5;
    }

    public void setAnswer5(int answer5) {
        this.answer5 = answer5;
    }

    public String getCct() {
        return cct;
    }

    public void setCct(String cct) {
        this.cct = cct;
    }

    public String getCd() {
        return cd;
    }

    public void setCd(String cd) {
        this.cd = cd;
    }

    public String getCe() {
        return ce;
    }

    public void setCe(String ce) {
        this.ce = ce;
    }

    public String getCgm() {
        return cgm;
    }

    public void setCgm(String cgm) {
        this.cgm = cgm;
    }

    public String getCnm() {
        return cnm;
    }

    public void setCnm(String cnm) {
        this.cnm = cnm;
    }

    public Boolean getDrinking() {
        return drinking;
    }

    public void setDrinking(Boolean drinking) {
        this.drinking = drinking;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public String getIcct() {
        return icct;
    }

    public void setIcct(String icct) {
        this.icct = icct;
    }

    public String getIcd() {
        return icd;
    }

    public void setIcd(String icd) {
        this.icd = icd;
    }

    public String getIce() {
        return ice;
    }

    public void setIce(String ice) {
        this.ice = ice;
    }

    public String getIcgm() {
        return icgm;
    }

    public void setIcgm(String icgm) {
        this.icgm = icgm;
    }

    public String getIcnm() {
        return icnm;
    }

    public void setIcnm(String icnm) {
        this.icnm = icnm;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public float getEDA() {
        return EDA;
    }

    public void setEDA(float i) {
        this.EDA = i;
    }

    public float getEDAT() {
        return EDAT;
    }

    public void setEDAT(float i) {
        this.EDAT = i;
    }


    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }
}

