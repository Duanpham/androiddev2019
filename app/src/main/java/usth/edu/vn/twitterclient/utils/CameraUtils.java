package usth.edu.vn.twitterclient.utils;

        import android.content.Context;
        import android.content.pm.PackageManager;
        import android.widget.Toast;

public class CameraUtils {
    public static boolean isDeviceSupportCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        }
        Toast.makeText(context, "Device doesn't support camera.", Toast.LENGTH_SHORT).show();
        return false;
    }
}
