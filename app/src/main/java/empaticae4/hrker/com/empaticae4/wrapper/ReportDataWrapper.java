package empaticae4.hrker.com.empaticae4.wrapper;

import java.util.Calendar;

public class ReportDataWrapper  {

    private String reportType;

    private Calendar startTime;
    private Calendar endTime;
    private long duration;

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
        reportType = "N/A";
        answer1 = -1;
        intensity = -1;
        answer2 = -1;
        answer3 = -1;
        answer4 = -1;
        answer5 = -1;
        drinking = false;

        cnm = "Other";
        ce = "Other";
        cgm = "Other";
        cct = "Other";
        cd = "Other";

    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
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

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }
}

