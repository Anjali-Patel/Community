package com.gss.community.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.gss.community.Api;
import com.gss.community.CheckNetwork;
import com.gss.community.Constant;
import com.gss.community.Model.PostOffice;
import com.gss.community.Model.Zipcode;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Button register;
    String url, imageurl = null, fcm_token;
    SharedPreferenceUtils preferances;
    TextView profile;
    Toolbar toolbar;
    Zipcode userListResponseData;
    Location location;
    Double latitude, longitude;
    String fetch_city, fetch_state, fetch_zip, fetch_country, encodedImageData, imei;

    String MobilePattern = "[0-9]{10}";
    String ZPattern = "[0-9]{6}";

    RadioGroup radioGroup;
    ImageView back_button;
    Spinner caste, subcaste;
    RadioButton male, female;
    String str_pass, str_name, str_email, str_gender, str_mobile, Str_curent_address, Str_permanent_address, Str_zip, str_city, str_state, str_country, str_caste, str_subcaste;
    EditText name, email, mobile_number, current_address, permanent_address, zip_code, city, state, country, password;
    ImageView upload_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email_id);
        profile = findViewById(R.id.profile);
        radioGroup = findViewById(R.id.radio);
        male = findViewById(R.id.male);
        upload_pic = findViewById(R.id.upload_pic);
        female = findViewById(R.id.female);
        password = findViewById(R.id.password);
        mobile_number = findViewById(R.id.mobile_number);
        current_address = findViewById(R.id.current_address);
        permanent_address = findViewById(R.id.permanent_address);
        zip_code = findViewById(R.id.zip_code);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        toolbar = findViewById(R.id.toolbar);
//        back_button=findViewById(R.id.back_button);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        country = findViewById(R.id.country);
        caste = findViewById(R.id.caste1);
        subcaste = findViewById(R.id.sub_caste1);
//        preferances = SharedPreferenceUtils.getInstance(MainActivity.this);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
            }
        });
        upload_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();

            }
        });
        caste.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_caste = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        subcaste.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_subcaste = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        url = Constant.Register;
//
//        back_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        register = findViewById(R.id.register);
        zip_code.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if(s.length() == 6) {
                    getHeroes(s.toString());
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {


            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fcm_token = SharedPreferenceUtils.getInstance(MainActivity.this).getStringValue(CommanUtils.FCM, "");
                str_name = name.getText().toString().trim();
                str_name = str_name.toUpperCase();
                str_email = email.getText().toString().trim();
                str_mobile = mobile_number.getText().toString().trim();
                fetch_city = city.getText().toString().trim();
                fetch_country = country.getText().toString().trim();
                fetch_state = state.getText().toString().trim();
                doSomthing();
                upload_pic.buildDrawingCache();
                Bitmap bmap = upload_pic.getDrawingCache();
                encodedImageData = getEncoded64ImageStringFromBitmap(bmap);
                Str_curent_address = current_address.getText().toString().trim();
                Str_permanent_address = permanent_address.getText().toString().trim();
                Str_zip = zip_code.getText().toString().trim();
                str_pass = password.getText().toString().trim();

                if (male.isChecked() == true) {
                    str_gender = "male";
                } else {
                    str_gender = "female";
                }

                if (str_name.equalsIgnoreCase("")) {
                    Toast.makeText(MainActivity.this, "Please Enter Your Name", Toast.LENGTH_LONG).show();

                } else if (str_email.equalsIgnoreCase("")) {
                    Toast.makeText(MainActivity.this, "Please Enter Your Email Id", Toast.LENGTH_LONG).show();

                } else if (str_pass.equalsIgnoreCase("")) {
                    Toast.makeText(MainActivity.this, "Please Enter Your Password", Toast.LENGTH_LONG).show();
//               }else if(imei.equalsIgnoreCase("")) {
//                   Toast.makeText(MainActivity.this, "IMEI number not found", Toast.LENGTH_LONG).show();
                } else if (str_mobile.equalsIgnoreCase("")) {
                    Toast.makeText(MainActivity.this, "Please Enter Your Mobile Number", Toast.LENGTH_LONG).show();

                } else if (!str_name.matches("^[a-zA-Z\\s]*$")) {
                    Toast.makeText(MainActivity.this, "Please Enter Your Correct Name", Toast.LENGTH_LONG).show();

                } else if (!str_mobile.matches(MobilePattern)) {

                    Toast.makeText(MainActivity.this, "please enter valid phone number", Toast.LENGTH_SHORT).show();
                } else if (!Str_zip.matches(ZPattern)) {

                    Toast.makeText(MainActivity.this, "please enter valid Zip code", Toast.LENGTH_SHORT).show();
                } else if (Str_zip.equalsIgnoreCase("")) {
                    Toast.makeText(MainActivity.this, "Please Enter Your Zip Code", Toast.LENGTH_LONG).show();

                } else if (fetch_city.equalsIgnoreCase("")) {
                    Toast.makeText(MainActivity.this, "Please Enter Your City", Toast.LENGTH_LONG).show();

                } else if (fetch_state.equalsIgnoreCase("")) {
                    Toast.makeText(MainActivity.this, "Please Enter Your State", Toast.LENGTH_LONG).show();

                } else if (fetch_country.equalsIgnoreCase("")) {
                    Toast.makeText(MainActivity.this, "Please Enter Your Country", Toast.LENGTH_LONG).show();

                } else if (Str_permanent_address.equalsIgnoreCase("")) {
                    Toast.makeText(MainActivity.this, "Please Enter Your Permanent Address", Toast.LENGTH_LONG).show();
                } else if (Str_curent_address.equalsIgnoreCase("")) {
                    Toast.makeText(MainActivity.this, "Please Enter Your Current Address", Toast.LENGTH_LONG).show();
                } else if (!isValidEmail(email.getText().toString().trim())) {
                    Toast.makeText(MainActivity.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                } else if (CheckNetwork.isInternetAvailable(MainActivity.this)) {

                    new PostData(MainActivity.this).execute();

                } else {

                    Toast.makeText(MainActivity.this, "No Internet Connection.Please Check Your Intent Connection", Toast.LENGTH_LONG).show();

                }
            }
        });


        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        SharedPreferenceUtils.getInstance(getApplicationContext()).setValue(CommanUtils.FCM, refreshedToken);
        Log.i(TAG, "FCM Token  onCreate: " + refreshedToken);
    }
    private void getHeroes(String zipcode) {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog

        Call<Zipcode> mediaDetails = Api.getClient().getUsersList(zipcode);
        mediaDetails.enqueue(new Callback<Zipcode>() {
            @Override
            public void onResponse(Call<Zipcode> call, Response<Zipcode> response) {
                progressDialog.dismiss(); //dismiss progress dialog
                userListResponseData =response.body();
                if(userListResponseData.getPostOffice() != null && userListResponseData.getPostOffice().size() > 0) {
                    PostOffice postOffice = userListResponseData.getPostOffice().get(0);
                    city.setText(postOffice.getTaluk());
                    state.setText(postOffice.getState());
                    country.setText(postOffice.getCountry());
                }else{
                    Toast.makeText(MainActivity.this,"Invalid ZipCode",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Zipcode> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
            }
        });


    }

    private static final String TAG = "MainActivity";


    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString1 = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString1;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void doSomthing() {
        imei = getUniqueIMEIId(MainActivity.this);
    }

    @SuppressLint("HardwareIds")
    public String getUniqueIMEIId(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return "";
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                imei = telephonyManager.getImei();
            } else {
                imei = telephonyManager.getDeviceId();
            }
//            String imei = telephonyManager.getDeviceId();
            Log.e("imei", "=" + imei);
            if (imei != null && !imei.isEmpty()) {
                return imei;
            } else {
                return android.os.Build.SERIAL;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "not_found";
    }

    class PostData extends AsyncTask<String, String, JSONObject> {
        JSONObject json = new JSONObject();
        private Context context;
        private ProgressDialog progress;

        public PostData(MainActivity context) {
            this.context = context;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("uname", str_name));
                nameValuePairs.add(new BasicNameValuePair("mobileno", str_mobile));
                nameValuePairs.add(new BasicNameValuePair("email", str_email));

                nameValuePairs.add(new BasicNameValuePair("gender", str_gender));
                nameValuePairs.add(new BasicNameValuePair("c_address", Str_curent_address));
                nameValuePairs.add(new BasicNameValuePair("p_address", Str_permanent_address));
                nameValuePairs.add(new BasicNameValuePair("zipcode", Str_zip));

                nameValuePairs.add(new BasicNameValuePair("city", fetch_city));

                nameValuePairs.add(new BasicNameValuePair("state", fetch_state));

                nameValuePairs.add(new BasicNameValuePair("country", fetch_country));
                nameValuePairs.add(new BasicNameValuePair("caste", str_caste));
                nameValuePairs.add(new BasicNameValuePair("subcaste", str_subcaste));
                nameValuePairs.add(new BasicNameValuePair("photo", encodedImageData));
                nameValuePairs.add(new BasicNameValuePair("password", str_pass));
                nameValuePairs.add(new BasicNameValuePair("fcm_token", fcm_token));
                nameValuePairs.add(new BasicNameValuePair("imei", imei));
                nameValuePairs.add(new BasicNameValuePair("is_device_install", "1"));
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
            progress.setMessage("Please wait while loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(false);
            progress.show();

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {


            try {
                progress.dismiss();
                if (jsonObject.getString("response").equalsIgnoreCase("success")) {
                    String a = ((json.getString("user_id")));
                    SharedPreferenceUtils.getInstance(context).setValue(CommanUtils.UserId, a);
//                    preferances.setValue(CommanUtils.UserId, a);
                    Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    Intent iSettings = new Intent(MainActivity.this, LoginPageActivity.class);
                    startActivity(iSettings);
                } else {
                    Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    class GetCode extends AsyncTask<String, String, JSONObject> {
        JSONObject json = new JSONObject();
        private Context context;
        private ProgressDialog progress;

        public GetCode(MainActivity context) {
            this.context = context;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                json = RestJsonClient.post("http://www.postalpincode.in/api/pincode/"+Str_zip, nameValuePairs);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }


        @Override
        protected void onPreExecute() {

//            progress = new ProgressDialog(context);
//            progress.setMessage("Please wait while loading...");
//            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progress.setCancelable(false);
//            progress.show();

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {


            try {
//                progress.dismiss();
                if (jsonObject.getString("Status").equalsIgnoreCase("Success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("PostOffice");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jb = jsonArray.getJSONObject(i);
                        city.setText(jb.getString("Taluk"));
                        state.setText(jb.getString("State"));
                        country.setText(jb.getString("Country"));

                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
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
    @Override
    public void onActivityResult(int requestcode, int resultcode, Intent data) {
        if (data != null) {
            if (requestcode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri resultUri = result.getUri();
//                        imageurl = getPath(resultUri);
                upload_pic.setImageURI(resultUri);
                imageurl = resultUri.getPath();
                Log.e("path", resultUri.getPath());


            } else if (requestcode == 2) {
                Uri selectedImageUri = data.getData();
                imageurl = getPath(selectedImageUri);
                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(imageurl, MediaStore.Video.Thumbnails.MINI_KIND);
//                video.setImageBitmap(thumb);
            }
        }else {
        }
         if (requestcode == 1) {
                if (requestcode == RESULT_OK) {
                    doSomthing();
                } else {
                    Toast.makeText(MainActivity.this, "Please Give permission for Read Phone State", Toast.LENGTH_SHORT).show();
                }
            }else{

         }
        }



    public String getPath(Uri uri) {

        Cursor cursor = getContentResolver().query(uri, null, null,
                null, null);
        String path = "";

        if (cursor.getCount() > 0)
            if (cursor != null && cursor.moveToFirst()) {
                cursor.moveToFirst();
                String document_id = cursor.getString(0);
                document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
                cursor.close();

                cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
                cursor.moveToFirst();
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                cursor.close();
            }
        return path;
    }

}


