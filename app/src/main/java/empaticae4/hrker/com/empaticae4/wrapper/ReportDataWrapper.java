package empaticae4.hrker.com.empaticae4.wrapper;

import java.util.Calendar;

public class ReportDataWrapper  {

    private Calendar dateTime;

    private String reportType;
    private int answer1;
    private int intensity;
    private int answer2;
    private int answer3;
    private int answer4;
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

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
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

/*
* // Sort the array based on DateTime (most recent to least recent)
        Collections.sort(array, new Comparator<ReportDataWrapper>() {
            @Override
            public int compare(ReportDataWrapper a, ReportDataWrapper b) {
                return (a.getDateTime().getTimeInMillis() > b.getDateTime().getTimeInMillis()) ? -1 :
                        (a.getDateTime().getTimeInMillis() > b.getDateTime().getTimeInMillis()) ? 1 : 0;
            }
        });

        for (ReportDataWrapper r : array)  {

            Log.i(TAG, String.valueOf(r.getResponse_1()));
            Log.i(TAG, r.getReportType());
        }

* */