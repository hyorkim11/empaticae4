package empaticae4.hrker.com.empaticae4.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import empaticae4.hrker.com.empaticae4.wrapper.ReportDataWrapper;

public class AppSharedPrefs {

    private SharedPreferences mPrefs;
    private Context mContext;

    private static final String REPORT_DATA_SET = "report_data_set";
    private static final String INIT_CUSTOM_NEGATIVE_MOOD = "init_custom_negative_mood";
    private static final String CUSTOM_NEGATIVE_MOOD = "custom_negative_mood";


    public AppSharedPrefs(Context context)  {

        this.mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.mContext = context;
    }

    // Set Property Functions
    private void setProperty(String property, String value)  {

        mPrefs.edit().putString(property, value).apply();
    }

    private void setProperty(String property, Set<String> value)  {

        mPrefs.edit().putStringSet(property, value).apply();
    }

    private void setProperty(String property, int value)  {

        mPrefs.edit().putInt(property, value).apply();
    }

    private void setProperty(String property, long value)  {

        mPrefs.edit().putLong(property, value).apply();
    }

    private void setProperty(String property, boolean value)  {

        mPrefs.edit().putBoolean(property, value).apply();
    }

    public void appendReportData(ReportDataWrapper r)  {

        Set<String> tmpSet = mPrefs.getStringSet(REPORT_DATA_SET, new HashSet<String>());

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        tmpSet.add(gson.toJson(r));

        setProperty(REPORT_DATA_SET, tmpSet);
    }

    public ArrayList<ReportDataWrapper> getAllReportData()  {

        // Create a new empty array for storing the ReportDataWrapper objects
        ArrayList<ReportDataWrapper> array = new ArrayList<>();

        // Get the Report Data Set
        Set<String> tmpSet = mPrefs.getStringSet(REPORT_DATA_SET, new HashSet<String>());

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        for (String s : tmpSet)  {

            ReportDataWrapper obj = gson.fromJson(s, ReportDataWrapper.class);
            array.add(obj);
        }

        return array;
    }

    public void setInitCustomNegativeMood(String s)  {

        setProperty(INIT_CUSTOM_NEGATIVE_MOOD, s);
    }

    public String getInitCustomNegativeMood()  {

        return mPrefs.getString(INIT_CUSTOM_NEGATIVE_MOOD, "");
    }

}