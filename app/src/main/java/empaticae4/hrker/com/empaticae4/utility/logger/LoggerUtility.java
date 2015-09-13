package empaticae4.hrker.com.empaticae4.utility.logger;

import android.content.Context;

import empaticae4.hrker.com.empaticae4.sharedprefs.AppSharedPrefs;
import empaticae4.hrker.com.empaticae4.wrapper.ReportDataWrapper;

public class LoggerUtility  {

    private Context mContext;
    private AppSharedPrefs mPrefs;

    public LoggerUtility(Context context)  {

        mContext = context;
        mPrefs = new AppSharedPrefs(context);
    }

    public void appendReportData(ReportDataWrapper r)  {

        mPrefs.appendReportData(r);
    }
}