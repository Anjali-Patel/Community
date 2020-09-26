package com.gss.community.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.gss.community.CheckNetwork;
import com.gss.community.Constant;
import com.gss.community.R;
import com.gss.community.RestJsonClient;
import com.gss.community.Utility.CommanUtils;
import com.gss.community.Utility.SharedPreferenceUtils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginPageActivity extends AppCompatActivity {
    Button login,register;
    TextView forgot_password;
    EditText email,password;
    String url,fcmtoken;
    String user_id;
    String[] allReqPermissions =
            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_PHONE_STATE,
                   };
    String Str_email,Str_password;
    public final static int PERMISSIONS_REQUEST_CODE_1 = 2;
    SharedPreferenceUtils preferances;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_login_page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        url = Constant.Login;
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        forgot_password = findViewById(R.id.forgot_password);
        register = findViewById(R.id.register);
        checkAllNecessaryPermissions();
//        if (isReadStoragePermissionGranted() && isWriteStoragePermissionGranted()) {
////            selectImage();
//        } else if (!isReadStoragePermissionGranted() && !isWriteStoragePermissionGranted()) {
//            Toast.makeText(LoginPageActivity.this, "Please give permission first", Toast.LENGTH_SHORT).show();
//        }


        FirebaseApp.initializeApp(this);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        SharedPreferenceUtils.getInstance(getApplicationContext()).setValue(CommanUtils.FCM, refreshedToken);
//        Log.i(TAG, "FCM Token  onCreate: " + refreshedToken);
//        preferances = SharedPreferenceUtils.getInstance(LoginPageActivity.this);
//        String  user_id=preferances.getStringValue(CommanUtils.UserId,"");
       user_id = SharedPreferenceUtils.getInstance(this).getStringValue(CommanUtils.UserId, "");
        if(!user_id.equalsIgnoreCase("")) {
            Intent intent = new Intent(LoginPageActivity.this, DashboardActivity.class);
            startActivity(intent);
        } else{
//             Intent intent =new Intent(MainActivity.this,MainActivity.class);
//             startActivity(intent);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Str_email = email.getText().toString().trim();
                Str_password = password.getText().toString().trim();
                fcmtoken=SharedPreferenceUtils.getInstance(LoginPageActivity.this).getStringValue(CommanUtils.FCM, "");

                if (Str_email.equalsIgnoreCase("")) {
                    Toast.makeText(LoginPageActivity.this, "Please Enter Your Email Id", Toast.LENGTH_LONG).show();

                } else if (Str_password.equalsIgnoreCase("")) {
                    Toast.makeText(LoginPageActivity.this, "Please Enter Password", Toast.LENGTH_LONG).show();
                } else if (!isValidEmail(email.getText().toString().trim())) {
                    Toast.makeText(LoginPageActivity.this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
                }else if (CheckNetwork.isInternetAvailable(LoginPageActivity.this)) {
                    new PostData(LoginPageActivity.this).execute();
                    //   status = json.getString("success");
                    //                    if (status.equalsIgnoreCase("1")) {
//                     //   Intent i = new Intent(UserComments.this, PostActivity.class);
//                       // startActivity(i);
//                    }else{
//
//                    }

                } else {
                    Toast.makeText(LoginPageActivity.this, "No Internet Connection.Please Check your Internet Connection", Toast.LENGTH_LONG).show();


                }
//                Intent intent= new Intent(LoginPageActivity.this,DashboardActivity.class);
//                startActivity(intent);
//                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPageActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
    private boolean checkPermission(String permission) {
        int permissionState = ActivityCompat.checkSelfPermission(this, permission);

        return permissionState == PackageManager.PERMISSION_GRANTED;
    }
    public boolean checkPermissionsGranted (){

        for (String permission : allReqPermissions){
            if (!checkPermission(permission)){
                return false;
            }

        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE_1:
                if (!checkPermissionsGranted())
                    Toast.makeText(getApplicationContext(), "You have not granted all necessary permissions for " +
                            "AntiTheft to function properly. Grant permissions from settings afterwards.", Toast.LENGTH_LONG).show();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    public void checkAllNecessaryPermissions () {

        ArrayList<String> requiredPermissionsList = new ArrayList<>();

        for (String permission : allReqPermissions){
            if (!checkPermission(permission)){
                requiredPermissionsList.add(permission);
            }
        }

        if(requiredPermissionsList.size() > 0){
            String[] requiredPermissions = requiredPermissionsList.toArray(new String[requiredPermissionsList.size()]);

            ActivityCompat.requestPermissions(LoginPageActivity.this, requiredPermissions, PERMISSIONS_REQUEST_CODE_1);
        }

    }


    private void selectImage() {
        /*
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(i, SELECT_IMAGE);*/
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }
    class PostData extends AsyncTask<String, String, JSONObject> {
        JSONObject json= new JSONObject();
        private final Context context;
        private ProgressDialog progress;


        public PostData(LoginPageActivity context) {
            this.context = context;
        }


        @Override
        protected JSONObject doInBackground(String... params) {

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("email", Str_email));
                nameValuePairs.add(new BasicNameValuePair("password", Str_password));
                nameValuePairs.add(new BasicNameValuePair("fcm_token", fcmtoken));
                Log.d("datap", nameValuePairs.toString());
                json = RestJsonClient.post(url, nameValuePairs);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }
        @Override
        protected void onPreExecute() {

            progress = new ProgressDialog(context);
            progress.setMessage("Please wait while Log In...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(false);
            progress.show();

        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                progress.dismiss();
                if (jsonObject.getString("response").equalsIgnoreCase("success")) {
                    SharedPreferenceUtils.getInstance(context).setValue(CommanUtils.UserId,jsonObject.getString("user_id"));
                    Toast.makeText(LoginPageActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    Intent iSettings = new Intent(LoginPageActivity.this, DashboardActivity.class);
                    startActivity(iSettings);
                } else {
                    Toast.makeText(LoginPageActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    @Override
    public void onBackPressed() {
      /*  if (back_pressed + 1000 > System.currentTimeMillis())
            finishAffinity();
        else
            Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();*/
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.create();
        builder.setMessage("Are You Sure You Want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
