package usth.edu.vn.twitterclient.login;

import android.content.Context;
import android.content.SharedPreferences;



public class MyPreferenceManager {


    private static final String PREF_KEY = "login_prefs";
    private static final String USER_ID = "user_id";
    private static final String SCREEN_NAME = "screen_name";

    //preference manager instance
    private SharedPreferences sharedPreferences;

    public MyPreferenceManager(Context context) {
        //initialize preference
        sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
    }


    public void saveUserId(Long userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(USER_ID, userId);
        editor.apply();
    }


    public Long getUserId() {
        return sharedPreferences.getLong(USER_ID, 0);
    }


    public void saveScreenName(String screenName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SCREEN_NAME, screenName);
        editor.apply();
    }


    public String getScreenName() {
        return sharedPreferences.getString(SCREEN_NAME, "");
    }
}
