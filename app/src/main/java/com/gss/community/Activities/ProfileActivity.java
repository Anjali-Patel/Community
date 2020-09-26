package com.gss.community.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
public class ProfileActivity extends AppCompatActivity {
    ImageView back_button,pPhoto;
    String url1,url2,userid;
    Button edit_profile;
    Toolbar toolbar;
    String imageurl="",encodedImageData;
    Button update_profile;
    SharedPreferences sharedPreferences;
    EditText gender,caddress,paddress,zip,city,state,country,caste,subcaste,password,name;
    String str_name,str_gender,Str_curent_address,Str_permanent_address,Str_zip,str_city,str_state,str_country,str_caste,str_subcaste;
    TextView email,mobile_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
//        back_button=findViewById(R.id.back_button);
//        back_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        back_button=findViewById(R.id.back_button);
        pPhoto=findViewById(R.id.pPhoto);
        name=findViewById(R.id.im_photo);
        url2= Constant.UpdateProfile;
        url1=Constant.Profile;
        update_profile=findViewById(R.id.update_profile);
        email=findViewById(R.id.email);
        edit_profile=findViewById(R.id.edit_profile);
        mobile_number=findViewById(R.id.mobile);
        caddress=findViewById(R.id.caddress);
        paddress=findViewById(R.id.paddress);
        zip=findViewById(R.id.zip_code);
        city=findViewById(R.id.city);
        state=findViewById(R.id.state);
        country=findViewById(R.id.country);
        caste=findViewById(R.id.caste);
        gender=findViewById(R.id.gender);
        password=findViewById(R.id.password);
        subcaste=findViewById(R.id.subcaste);
        userid = SharedPreferenceUtils.getInstance(this).getStringValue(CommanUtils.UserId, "");;
        if (CheckNetwork.isInternetAvailable(ProfileActivity.this)) {
            new GetProfileTask(ProfileActivity.this).execute();
        }else{
            Toast.makeText(ProfileActivity.this, "No Internet Connection.Please Check Your Intent Connection", Toast.LENGTH_LONG).show();
        }
//        back_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_name=name.getText().toString();
                str_gender=gender.getText().toString();
                Str_curent_address=caddress.getText().toString();
                Str_permanent_address=paddress.getText().toString();
                Str_zip=zip.getText().toString();
                str_city=city.getText().toString();
                str_state=state.getText().toString();
                str_country=country.getText().toString();
                str_caste=caste.getText().toString();
                str_subcaste=subcaste.getText().toString();
                pPhoto.buildDrawingCache();
                Bitmap bmap = pPhoto.getDrawingCache();
                encodedImageData =getEncoded64ImageStringFromBitmap(bmap);
                if(CheckNetwork.isInternetAvailable(ProfileActivity.this)) {
                    new UpdateProfile(ProfileActivity.this).execute();
                }else{
                    Toast.makeText(ProfileActivity.this,"No Internet Connection.Please Check your Internet Connection",Toast.LENGTH_SHORT).show();
                }
            }
        });
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this,"Now you can edit your profile successfully",Toast.LENGTH_SHORT).show();
                name.setFocusableInTouchMode(true);
                gender.setFocusableInTouchMode(true);
                caddress.setFocusableInTouchMode(true);

                paddress.setFocusableInTouchMode(true);

                zip.setFocusableInTouchMode(true);

                city.setFocusableInTouchMode(true);
                state.setFocusableInTouchMode(true);
                country.setFocusableInTouchMode(true);

                caste.setFocusableInTouchMode(true);
                subcaste.setFocusableInTouchMode(true);
//                gender.setFocusableInTouchMode(true);
//                gender.setFocusableInTouchMode(true);
                pPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ProfileActivity.this);
//                        builder.create();
//                        builder.setMessage("Do You Want to Edit Your Profile Picture?");
//                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//
//                            }
//                        });
//                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                        builder.show();
                        if (isReadStoragePermissionGranted() && isWriteStoragePermissionGranted()) {
                            selectImage();
                        } else if (!isReadStoragePermissionGranted() && !isWriteStoragePermissionGranted()) {
                            Toast.makeText(ProfileActivity.this, "Please give permission first", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
    }
    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString1 = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString1;
    }
    class UpdateProfile extends AsyncTask<String, String, JSONObject> {
        JSONObject json= new JSONObject();
        private final Context context;
        private ProgressDialog progress;

        public UpdateProfile(ProfileActivity context) {
            this.context=context;

        }
        @Override
        protected JSONObject doInBackground(String... params) {
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("userid", userid));

                nameValuePairs.add(new BasicNameValuePair("uname", str_name));
                nameValuePairs.add(new BasicNameValuePair("gender", str_gender));
                nameValuePairs.add(new BasicNameValuePair("c_address", Str_curent_address));
                nameValuePairs.add(new BasicNameValuePair("p_address", Str_permanent_address));
                nameValuePairs.add(new BasicNameValuePair("zipcode", Str_zip));
                nameValuePairs.add(new BasicNameValuePair("city", str_city));
                nameValuePairs.add(new BasicNameValuePair("state", str_state));
                nameValuePairs.add(new BasicNameValuePair("country", str_country));
                nameValuePairs.add(new BasicNameValuePair("subcaste", str_subcaste));
                nameValuePairs.add(new BasicNameValuePair("caste", str_caste));
                nameValuePairs.add(new BasicNameValuePair("photo", encodedImageData));
                //                nameValuePairs.add(new BasicNameValuePair("subcaste", userid));
                Log.d("datap", nameValuePairs.toString());
                // String url = "http://139.59.15.90/absolute_protector/index.php/api/api/user_profile";
                json = RestJsonClient.post(url2, nameValuePairs);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }
        @Override
        protected void onPreExecute() {
            progress=new ProgressDialog(context);
            progress.setMessage("Please wait while loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(false);
            progress.show();
        }
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                progress.dismiss();
                if (jsonObject.getString("response").equalsIgnoreCase("success")) {
                    Intent intent= new Intent(ProfileActivity.this, DashboardActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(ProfileActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onActivityResult(int requestcode, int resultcode, Intent data) {
        if (data != null) {
            if (requestcode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri resultUri = result.getUri();
//                        imageurl = getPath(resultUri);
                pPhoto.setImageURI(resultUri);
                imageurl = resultUri.getPath();

//                encodedImageData =getEncoded64ImageStringFromBitmap(bmap);
                Log.e("path", resultUri.getPath());

//                else if (requestcode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                    Exception error = result.getError();
            }
// else if (requestcode == 2) {
//                Uri selectedImageUri = data.getData();
//                imageurl = getPath(selectedImageUri);
//                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(imageurl, MediaStore.Video.Thumbnails.MINI_KIND);
//                video.setImageBitmap(thumb);
//            }
        }
    }

    private void selectImage() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }
    public boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("", "Permission is granted1");
                return true;
            } else {

                Log.v("", "Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("", "Permission is granted1");
            return true;
        }
    }

    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("", "Permission is granted2");
                return true;
            } else {

                Log.v("", "Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("", "Permission is granted2");
            return true;
        }
    }
    class GetProfileTask extends AsyncTask<String, String, JSONObject> {
        JSONObject json= new JSONObject();
        private final Context context;
        private ProgressDialog progress;

        public GetProfileTask(ProfileActivity context) {
            this.context=context;

        }
        @Override
        protected JSONObject doInBackground(String... params) {
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("id", userid));

                Log.d("datap", nameValuePairs.toString());
                // String url = "http://139.59.15.90/absolute_protector/index.php/api/api/user_profile";
                json = RestJsonClient.post(url1, nameValuePairs);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }
        @Override
        protected void onPreExecute() {
            progress=new ProgressDialog(context);
            progress.setMessage("Please wait while loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(false);
            progress.show();
        }

         @Override
        protected void onPostExecute(JSONObject jsonObject) {


            try {
                progress.dismiss();
                if (jsonObject.getString("response").equalsIgnoreCase("success"))
                {
                    JSONArray jsonArray = jsonObject.getJSONArray("message");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jb = jsonArray.getJSONObject(i);
                        name.setText(jb.getString("uname"));
                        email.setText(jb.getString("email"));
                        mobile_number.setText(jb.getString("mobileno"));
                        subcaste.setText(jb.getString("subcaste"));
                        gender.setText(jb.getString("gender"));
                        zip.setText(jb.getString("zipcode"));
                        city.setText(jb.getString("city"));
                        state.setText(jb.getString("state"));
                        country.setText(jb.getString("country"));
                        caste.setText(jb.getString("caste"));
                        caddress.setText(jb.getString("c_address"));
                        paddress.setText(jb.getString("p_address"));
                          Glide.with(ProfileActivity.this).load("http://demo1.geniesoftsystem.com/newweb/community/uploads/images/"+jb.getString("photo")).error(R.drawable.profile).into(pPhoto);
                    }
                }else{
                    Toast.makeText(ProfileActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
