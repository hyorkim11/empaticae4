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

    private static final String REPORT_TYPE = "report_type";
    private static final String INTENSITY = "intensity";

    private static final String INIT_CUSTOM_NEGATIVE_MOOD = "init_custom_negative_mood";
    private static final String CUSTOM_NEGATIVE_MOOD = "custom_negative_mood";

    private static final String INIT_CUSTOM_EVENT = "init_custom_event";
    private static final String CUSTOM_EVENT = "custom_event";

    private static final String INIT_CUSTOM_GOODMOVE = "init_custom_goodmove";
    private static final String CUSTOM_GOODMOVE = "custom_goodmove";

    private static final String INIT_CUSTOM_COOLTHOUGHT = "init_custom_coolthought";
    private static final String CUSTOM_COOLTHOUGHT = "custom_coolthought";

    private static final String INIT_CUSTOM_DRINKING = "init_custom_drinking";
    private static final String CUSTOM_DRINKING = "custom_drinking";


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

        Set<String> tmpSet = mPrefs.getStringSet(REPORT_TYPE, new HashSet<String>());

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        tmpSet.add(gson.toJson(r));

        setProperty(REPORT_TYPE, tmpSet);
    }

    public ArrayList<ReportDataWrapper> getReportData()  {

        // Create a new empty array for storing the ReportDataWrapper objects
        ArrayList<ReportDataWrapper> array = new ArrayList<>();

        // Get the Report Data Set
        Set<String> tmpSet = mPrefs.getStringSet(REPORT_TYPE, new HashSet<String>());

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        for (String s : tmpSet)  {

            ReportDataWrapper obj = gson.fromJson(s, ReportDataWrapper.class);
            array.add(obj);
        }

        return array;
    }

    // INIT CUSTOM NEGATIVE MOOD
    public void setInitCustomNegativeMood(String s)  {
        setProperty(INIT_CUSTOM_NEGATIVE_MOOD, s);
    }
    public String getInitCustomNegativeMood()  {
        return mPrefs.getString(INIT_CUSTOM_NEGATIVE_MOOD, "");
    }

    // CUSTOM NEGATIVE MOOD
    public void setCustomNegativeMood(String s) {
        setProperty(CUSTOM_NEGATIVE_MOOD, s);
    }
    public String getCustomNegativeMood() {
        return mPrefs.getString(CUSTOM_NEGATIVE_MOOD, "");
    }

    // INIT CUSTOM EVENT
    public void setInitCustomEvent(String s) {
        setProperty(INIT_CUSTOM_EVENT, s);
    }
    public String getInitCustomEvent() {
        return mPrefs.getString(INIT_CUSTOM_EVENT, "");
    }

    // CUSTOM EVENT
    public void setCustomEvent(String s) {
        setProperty(CUSTOM_EVENT, s);
    }
    public String getCustomEvent() {
        return mPrefs.getString(CUSTOM_EVENT, "");
    }

    // INIT CUSTOM GOODMOVES
    public void setInitCustomGoodmove(String s) {
        setProperty(INIT_CUSTOM_GOODMOVE, s);
    }
    public String getInitCustomGoodmove() {
        return mPrefs.getString(INIT_CUSTOM_GOODMOVE, "");
    }

    // CUSTOM GOODMOVES
    public void setCustomGoodmove(String s) {
        setProperty(CUSTOM_GOODMOVE, s);
    }
    public String getCustomGoodmove() {
        return mPrefs.getString(CUSTOM_GOODMOVE, "");
    }

    // INIT CUSTOM COOLTHOUGHT
    public void setInitCustomCoolthought(String s) {
        setProperty(INIT_CUSTOM_COOLTHOUGHT, s);
    }
    public String getInitCustomCoolthought() {
        return mPrefs.getString(INIT_CUSTOM_COOLTHOUGHT, "");
    }

    // CUSTOM COOLTHOUGHT
    public void setCustomCoolthought(String s) {
        setProperty(CUSTOM_COOLTHOUGHT, s);
    }
    public String getCustomCoolthought() {
        return mPrefs.getString(CUSTOM_COOLTHOUGHT, "");
    }

    // INIT CUSTOM DRINKING
    public void setInitCustomDrinking(String s) {
        setProperty(INIT_CUSTOM_DRINKING, s);
    }
    public String getInitCustomDrinking() {
        return mPrefs.getString(INIT_CUSTOM_DRINKING, "");
    }

    // CUSTOM DRINKING
    public void setCustomDrinking(String s) {
        setProperty(CUSTOM_DRINKING, s);
    }
    public String getCustomDrinking() {
        return mPrefs.getString(CUSTOM_DRINKING, "");
    }

}