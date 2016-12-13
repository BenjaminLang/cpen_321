package androidapp.smartshopper.smartshopper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by JohnS on 2016-12-12.
 */

public class SharedPrefSingle {
    private static final String SETTINGS_NAME = "default_settings";
    private static SharedPrefSingle sharedPref;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private boolean bulkUpdate = false;

    public enum prefKey {
        LOGIN_STAT,
        CURR_EMAIL,
        CURR_NAME,
        CURR_LIST,
        CURR_MAX,
        CURR_MIN,
        STORE_OPT,
        NUM_ITEM,
        NUM_SPIN_POS,
        SORT_OPT,
        SORT_SPIN_POS,
        WALMART_STAT,
        COSTCO_STAT,
        SUPERSTORE_STAT
    }

    private SharedPrefSingle(Context context) {
        pref = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPrefSingle getInstance(Context context) {
        if (sharedPref == null) {
            sharedPref = new SharedPrefSingle(context.getApplicationContext());
        }
        return sharedPref;
    }

    /*
    Methods to put data into shared preferences
     */
    public void put(prefKey key, String val) {
        doEdit();
        editor.putString(key.name(), val);
        doCommit();
    }

    public void put(String key, String val) {
        doEdit();
        editor.putString(key, val);
        doCommit();
    }

    public void put(prefKey key, int val) {
        doEdit();
        editor.putInt(key.name(), val);
        doCommit();
    }

    public void put(prefKey key, boolean val) {
        doEdit();
        editor.putBoolean(key.name(), val);
        doCommit();
    }

    public void put(prefKey key, float val) {
        doEdit();
        editor.putFloat(key.name(), val);
        doCommit();
    }

    /*
    Methods to get data from shared preferences
     */
    public String getString(prefKey key, String defaultValue) {
        return pref.getString(key.name(), defaultValue);
    }

    public String getString(String key, String defaultValue) {
        return pref.getString(key, defaultValue);
    }

    public boolean getBoolean(prefKey key, boolean defaultValue) {
        return pref.getBoolean(key.name(), defaultValue);
    }

    public int getInt(prefKey key, int defaultValue) {
        return pref.getInt(key.name(), defaultValue);
    }

    public float getFloat(prefKey key, float defaultValue) {
        return pref.getFloat(key.name(), defaultValue);
    }

    public boolean contains(prefKey key) {
        return pref.contains(key.name());
    }

    public boolean contains(String key) {
        return pref.contains(key);
    }

    /*
    Support methods to enable the use of editing
     */
    public void edit() {
        bulkUpdate = true;
        editor = pref.edit();
    }

    public void commit() {
        bulkUpdate = false;
        editor.commit();
        editor = null;
    }

    private void doEdit() {
        if (!bulkUpdate && editor == null) {
            editor = pref.edit();
        }
    }

    private void doCommit() {
        if (!bulkUpdate && editor != null) {
            editor.commit();
            editor = null;
        }
    }
}
