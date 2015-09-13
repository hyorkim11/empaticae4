package empaticae4.hrker.com.empaticae4.wrapper;

import java.util.Calendar;

public class ReportDataWrapper  {

    private Calendar dateTime;

    private String reportType;
    private String response_1; // How are you feeling
    private String response_2; // Intensity
    private String response_3; // What is going on right now
    private String response_4; //
    private String response_5;
    private String response_6;


    public ReportDataWrapper()  {}

    public Calendar getDateTime() {
        return dateTime;
    }

    public void setDateTime(Calendar dateTime) {
        this.dateTime = dateTime;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getResponse_1() {
        return response_1;
    }

    public void setResponse_1(String response_1) {
        this.response_1 = response_1;
    }

    public String getResponse_2() {
        return response_2;
    }

    public void setResponse_2(String response_2) {
        this.response_2 = response_2;
    }

    public String getResponse_3() {
        return response_3;
    }

    public void setResponse_3(String response_3) {
        this.response_3 = response_3;
    }

    public String getResponse_4() {
        return response_4;
    }

    public void setResponse_4(String response_4) {
        this.response_4 = response_4;
    }

    public String getResponse_5() {
        return response_5;
    }

    public void setResponse_5(String response_5) {
        this.response_5 = response_5;
    }

    public String getResponse_6() {
        return response_6;
    }

    public void setResponse_6(String response_6) {
        this.response_6 = response_6;
    }
}