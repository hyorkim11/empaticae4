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

    private static final String REPORT_TYPE = "report_type";

    private static final String REPORT_RESPONSE_DATA_SET = "report_response_data_set";
    private static final String REPORT_RESPONSE_CACHE = "report_response_cache";

    // REPORT ACTIVITY
    private static final String ANSWER1 = "answer1";
    private static final String INTENSITY = "intensity";
    private static final String INIT_CUSTOM_NEGATIVE_MOOD = "init_custom_negative_mood";
    private static final String CUSTOM_NEGATIVE_MOOD = "custom_negative_mood";

    // NEGATIVE ACTIVITY
    private static final String INIT_CUSTOM_EVENT = "init_custom_event";
    private static final String CUSTOM_EVENT = "custom_event";
    private static final String ANSWER2 = "answer2";

    // GOOD MOVES ACTIVITY
    private static final String INIT_CUSTOM_GOODMOVE = "init_custom_goodmove";
    private static final String CUSTOM_GOODMOVE = "custom_goodmove";
    private static final String ANSWER5 = "answer5";

    // NEGATIVE ACTIVITY 2
    private static final String INIT_CUSTOM_COOLTHOUGHT = "init_custom_coolthought";
    private static final String CUSTOM_COOLTHOUGHT = "custom_coolthought";
    private static final String ANSWER3 = "answer3";

    // Drinking or Planning on Drinking?
    private static final String DRINKING = "drinking";

    // DRINK ACTIVITY
    private static final String INIT_CUSTOM_DRINKING = "init_custom_drinking";
    private static final String CUSTOM_DRINKING = "custom_drinking";
    private static final String ANSWER4 = "answer4";

    private SharedPreferences mPrefs;
    private Context mContext;

    public AppSharedPrefs(Context context) {

        this.mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.mContext = context;
    }

    // Set Property Functions
    private void setProperty(String property, String value) {

        mPrefs.edit().putString(property, value).apply();
    }

    private void setProperty(String property, Set<String> value) {

        mPrefs.edit().putStringSet(property, value).apply();
    }

    private void setProperty(String property, int value) {

        mPrefs.edit().putInt(property, value).apply();
    }

    private void setProperty(String property, long value) {

        mPrefs.edit().putLong(property, value).apply();
    }

    private void setProperty(String property, boolean value) {

        mPrefs.edit().putBoolean(property, value).apply();
    }

    public void appendReportData(ReportDataWrapper r) {

        Set<String> tmpSet = new HashSet<>();
        tmpSet = mPrefs.getStringSet(REPORT_RESPONSE_DATA_SET, new HashSet<String>());

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        tmpSet.add(gson.toJson(r));

        setProperty(REPORT_RESPONSE_DATA_SET, tmpSet);

    }

    public ArrayList<ReportDataWrapper> getReportData() {

        // Create a new empty array for storing the ReportDataWrapper objects
        ArrayList<ReportDataWrapper> array = new ArrayList<>();

        // Get the Report Data Set
        Set<String> tmpSet = mPrefs.getStringSet(REPORT_RESPONSE_DATA_SET, new HashSet<String>());

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        for (String s : tmpSet) {

            ReportDataWrapper obj = gson.fromJson(s, ReportDataWrapper.class);
            array.add(obj);
        }

        return array;
    }

    public void setReportResponseCache(ReportDataWrapper r) {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        setProperty(REPORT_RESPONSE_CACHE, gson.toJson(r));
    }

    public ReportDataWrapper getReportResponseCache() {

        String tmp = mPrefs.getString(REPORT_RESPONSE_CACHE, "");

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        ReportDataWrapper obj = gson.fromJson(tmp, ReportDataWrapper.class);
        return obj;
    }


    public void setAnswer1(int i) {
        setProperty(ANSWER1, i);
    }

    public int getAnswer1() {
        return mPrefs.getInt(ANSWER1, -1);
    }

    public void setIntensity(int i) {
        setProperty(INTENSITY, i);
    }

    public int getIntensity() {
        return mPrefs.getInt(INTENSITY, -1);
    }

    // INIT CUSTOM NEGATIVE MOOD
    public void setInitCustomNegativeMood(String s) {
        setProperty(INIT_CUSTOM_NEGATIVE_MOOD, s);
    }

    public String getInitCustomNegativeMood() {
        return mPrefs.getString(INIT_CUSTOM_NEGATIVE_MOOD, "Other");
    }

    // CUSTOM NEGATIVE MOOD
    public void setCustomNegativeMood(String s) {
        setProperty(CUSTOM_NEGATIVE_MOOD, s);
    }

    public String getCustomNegativeMood() {
        return mPrefs.getString(CUSTOM_NEGATIVE_MOOD, "Other");
    }


    public void setAnswer2(int i) {
        setProperty(ANSWER2, i);
    }

    public int getAnswer2() {
        return mPrefs.getInt(ANSWER2, -1);
    }

    // INIT CUSTOM EVENT
    public void setInitCustomEvent(String s) {
        setProperty(INIT_CUSTOM_EVENT, s);
    }

    public String getInitCustomEvent() {
        return mPrefs.getString(INIT_CUSTOM_EVENT, "Other");
    }

    // CUSTOM EVENT
    public void setCustomEvent(String s) {
        setProperty(CUSTOM_EVENT, s);
    }

    public String getCustomEvent() {
        return mPrefs.getString(CUSTOM_EVENT, "Other");
    }


    // INIT CUSTOM GOODMOVES
    public void setAnswer5(int i) {
        setProperty(ANSWER5, i);
    }

    public int getAnswer5() {
        return mPrefs.getInt(ANSWER5, -1);
    }

    public void setInitCustomGoodmove(String s) {
        setProperty(INIT_CUSTOM_GOODMOVE, s);
    }

    public String getInitCustomGoodmove() {
        return mPrefs.getString(INIT_CUSTOM_GOODMOVE, "Other");
    }

    // CUSTOM GOODMOVES
    public void setCustomGoodmove(String s) {
        setProperty(CUSTOM_GOODMOVE, s);
    }

    public String getCustomGoodmove() {
        return mPrefs.getString(CUSTOM_GOODMOVE, "Other");
    }

    public void setAnswer3(int i) {
        setProperty(ANSWER3, i);
    }

    public int getAnswer3() {
        return mPrefs.getInt(ANSWER3, -1);
    }

    // INIT CUSTOM COOLTHOUGHT
    public void setInitCustomCoolthought(String s) {
        setProperty(INIT_CUSTOM_COOLTHOUGHT, s);
    }

    public String getInitCustomCoolthought() {
        return mPrefs.getString(INIT_CUSTOM_COOLTHOUGHT, "Other");
    }

    // CUSTOM COOLTHOUGHT
    public void setCustomCoolthought(String s) {
        setProperty(CUSTOM_COOLTHOUGHT, s);
    }

    public String getCustomCoolthought() {
        return mPrefs.getString(CUSTOM_COOLTHOUGHT, "Other");
    }

    public void setDrinking(Boolean i) {
        setProperty(DRINKING, i);
    }

    public Boolean getDrinking() {
        return mPrefs.getBoolean(DRINKING, false);
    }

    public void setAnswer4(int i) {
        setProperty(ANSWER4, i);
    }

    public int getAnswer4() {
        return mPrefs.getInt(ANSWER4, -1);
    }

    // INIT CUSTOM DRINKING
    public void setInitCustomDrinking(String s) {
        setProperty(INIT_CUSTOM_DRINKING, s);
    }

    public String getInitCustomDrinking() {
        return mPrefs.getString(INIT_CUSTOM_DRINKING, "Other");
    }

    // CUSTOM DRINKING
    public void setCustomDrinking(String s) {
        setProperty(CUSTOM_DRINKING, s);
    }

    public String getCustomDrinking() {
        return mPrefs.getString(CUSTOM_DRINKING, "Other");
    }


    // PRE-EXIT MEASURES
    public void wrapUp() {
        mPrefs.edit().clear().commit();
    }

}