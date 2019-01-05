package usth.edu.vn.twitterclient;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import usth.edu.vn.twitterclient.R;
import usth.edu.vn.twitterclient.utils.CameraUtils;
import usth.edu.vn.twitterclient.utils.FileNameCreation;
import usth.edu.vn.twitterclient.utils.MarshMallowPermission;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;

public class TweetActivity extends AppCompatActivity {
    private ImageView pickedImageView;

    private static final int GALLERY_REQUEST_CODE = 332;
    private static final int CAMERA_REQUEST_CODE = 333;
    private static final int SHARE_PERMISSION_CODE = 223;

    private Uri cameraFileURI;
    private TwitterAuthClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_composetweet);
        pickedImageView = findViewById(R.id.picked_image_view);
    }

    public void triggerPickImageTask(View view) {
        checkStorageAndCameraPermission();
    }

    private void checkStorageAndCameraPermission() {
        if (MarshMallowPermission.checkMashMallowPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, SHARE_PERMISSION_CODE)) {
            onPermissionGranted();
        }
    }

    public void shareUsingTwitterComposer(View view) {
        if (cameraFileURI != null) {
            TweetComposer.Builder builder = new TweetComposer.Builder(this)
                    .text("This is a testing tweet!!")
                    .image(cameraFileURI);
            builder.show();
        } else {
            Toast.makeText(this, "Please select image first to share.", Toast.LENGTH_SHORT).show();
            checkStorageAndCameraPermission();
        }
    }

    public void shareUsingTwitterNativeComposer(View view) {
        if (cameraFileURI != null) {
            TwitterSession session = TwitterCore.getInstance().getSessionManager()
                    .getActiveSession();
            if (session != null) {
                shareUsingNativeComposer(session);
            } else {
                authenticateUser();
            }
        } else {
            Toast.makeText(this, "Please select image first to share.", Toast.LENGTH_SHORT).show();
            checkStorageAndCameraPermission();
        }
    }

    private void shareUsingNativeComposer(TwitterSession session) {
        Intent intent = new ComposerActivity.Builder(this)
                .session(session)
                .image(cameraFileURI)
                .text("This is Native Kit Composer Tweet!!")
                .hashtags("#android")
                .createIntent();
        startActivity(intent);
    }

    private void authenticateUser() {
        client = new TwitterAuthClient();
        client.authorize(this, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> twitterSessionResult) {
                Toast.makeText(TweetActivity.this, "Login successful.", Toast.LENGTH_SHORT).show();
                shareUsingNativeComposer(twitterSessionResult.data);
            }

            @Override
            public void failure(TwitterException e) {
                Toast.makeText(TweetActivity.this, "Failed to authenticate by Twitter. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onPermissionGranted() {
        new AlertDialog.Builder(this)
                .setTitle("Select Option")
                .setItems(new String[]{"Gallery", "Camera"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                selectImageFromGallery();
                                break;
                            case 1:
                                captureImageFormCamera();
                                break;
                        }
                    }
                })
                .setCancelable(true)
                .create()
                .show();
    }

    private void selectImageFromGallery() {
        Intent in = new Intent(Intent.ACTION_PICK);
        in.setType("image/*");
        startActivityForResult(in, GALLERY_REQUEST_CODE);
    }

    private void captureImageFormCamera() {
        if (!CameraUtils.isDeviceSupportCamera(this)) {
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraFileURI = FileProvider.getUriForFile(this, "usth.edu.vn.twitterclient.file_provider", FileNameCreation.createImageFile(this));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraFileURI);
        for (ResolveInfo resolveInfo : getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)) {
            grantUriPermission(resolveInfo.activityInfo.packageName, cameraFileURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
        else {
            Toast.makeText(this, "No apps to capture image.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case SHARE_PERMISSION_CODE:
                if (permissions.length > 0 && grantResults.length > 0) {
                    int counter = 0;
                    for (int result : grantResults) {
                        if (result != 0) {
                            onPermissionDenied();
                            return;
                        }
                        counter++;

                    }
                    if (counter == permissions.length) {
                        onPermissionGranted();
                    }
                }
                break;
        }
    }

    private void onPermissionDenied() {
        new AlertDialog.Builder(this)
                .setMessage("Both permission are required to pick/capture image. Do you want to try again.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkStorageAndCameraPermission();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(TweetActivity.this, "You cannot share the images without giving these permissions.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GALLERY_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri imageUri = data.getData();
                    this.cameraFileURI = imageUri;
                    displayImage(imageUri);
                } else {
                    Toast.makeText(this, "Failed to pick up image from gallery.", Toast.LENGTH_SHORT).show();
                }
                break;
            case CAMERA_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    if (cameraFileURI != null) {
                        displayImage(cameraFileURI);
                    }
                    else {
                        Toast.makeText(this, "Failed to capture image.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(this, "Failed to capture image.", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                if (client != null)
                    client.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void displayImage(Uri imageUri) {
//        Picasso.get().load(imageUri).into(pickedImageView);
        Glide.with(this).load(imageUri).into(pickedImageView);
    }

}

