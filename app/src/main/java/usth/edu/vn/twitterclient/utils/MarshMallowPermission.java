package usth.edu.vn.twitterclient.utils;

import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class MarshMallowPermission {
    public static int checkPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission);
    }

    public static boolean checkMashMallowPermissions(AppCompatActivity appCompatActivity, String[] permissionsToGrant, int PERMISSION_REQUEST_CODE) {
        ArrayList<String> permissions = new ArrayList<>();
        for (String permission : permissionsToGrant) {
            if (checkPermission(appCompatActivity, permission) != 0) {
                permissions.add(permission);
            }
        }
        if (permissions.size() == 0) {
            return true;
        }
        ActivityCompat.requestPermissions(appCompatActivity,  permissions.toArray(new String[permissions.size()]), PERMISSION_REQUEST_CODE);
        return false;
    }
}
