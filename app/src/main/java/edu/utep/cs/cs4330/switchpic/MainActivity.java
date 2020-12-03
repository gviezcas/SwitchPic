package edu.utep.cs.cs4330.switchpic;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    private LoginButton facebookloginButton;
    private Button changeProfilePictureButton;
    private Profile facebookProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeProfilePictureButton = findViewById(R.id.change_profile_picture_button);
        changeProfilePictureButton.setOnClickListener(this::changeProfilePictureButtonClicked);
        facebookloginButton = findViewById(R.id.facebook_login_button);
        callbackManager = CallbackManager.Factory.create();
        AccessToken facebookAccessToken = AccessToken.getCurrentAccessToken();
        boolean facebookisLoggedIn = facebookAccessToken != null && !facebookAccessToken.isExpired();


        if(facebookisLoggedIn){
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        }else {

            // Callback registration
            facebookloginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    facebookProfile = Profile.getCurrentProfile();

                    Toast.makeText(MainActivity.this, "Log In Successful", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(MainActivity.this, "Log In Canceled", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(FacebookException exception) {
                    Toast.makeText(MainActivity.this, "Log In Error", Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        facebookProfile = Profile.getCurrentProfile();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void changeProfilePictureButtonClicked(View view){
        if(facebookProfile != null) {
            Intent intent = new Intent(MainActivity.this, ChangeProfilePicture.class);
            intent.putExtra("facebookProfile", facebookProfile);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}