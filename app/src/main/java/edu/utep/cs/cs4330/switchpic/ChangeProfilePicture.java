package edu.utep.cs.cs4330.switchpic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

public class ChangeProfilePicture extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 123;
    public static final int CAMERA_PERMISSION_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    private ImageView profilePicture;
    private Button browseButton;
    private Button cameraButton;
    private Button setProfilePicture;
    private CallbackManager callbackManager;
    private ShareButton sbPhoto;
    private boolean imageChosen = false;
    private CheckBox facebookCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile_picture);
        facebookCheckBox = findViewById(R.id.facebookCheckBox);
        profilePicture = findViewById(R.id.profilePicture);
        cameraButton = findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(v -> askCameraPermission());
        browseButton = findViewById(R.id.browse_button);
        browseButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Choose Profile Picture"), GALLERY_REQUEST_CODE);
        });
        callbackManager = CallbackManager.Factory.create();
        sbPhoto = findViewById(R.id.fb_share_button);
        setProfilePicture = findViewById(R.id.setProfilePicture);
        setProfilePicture.setOnClickListener(v -> {
            if(facebookCheckBox.isChecked()) {
            if(profilePicture.getDrawable() != null && imageChosen){
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) profilePicture.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    SharePhoto sharePhoto = new SharePhoto.Builder().setBitmap(bitmap).build();
                    SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder().addPhoto(sharePhoto).build();
                    sbPhoto.setShareContent(sharePhotoContent);
                    sbPhoto.performClick();
            }else{
                Toast.makeText(this, "Please choose a picture.", Toast.LENGTH_LONG).show();
            }
            }else{
                Toast.makeText(this, "Please check a box.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri imageData = data.getData();
            profilePicture.setImageURI(imageData);
            imageChosen = true;
        }

        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            Uri imageData = data.getData();
            Bitmap image = (Bitmap) data.getExtras().get("data");
            profilePicture.setImageBitmap(image);
            profilePicture.setImageURI(imageData);
            imageChosen = true;
        }
    }

    private void askCameraPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }else{
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera();
            }else{
                Toast.makeText(this, "Camera Permission Is Required To Use Camera", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openCamera(){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
    }


}