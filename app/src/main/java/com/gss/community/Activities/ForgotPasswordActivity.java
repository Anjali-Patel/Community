package com.gss.community.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gss.community.CheckNetwork;
import com.gss.community.Constant;
import com.gss.community.R;
import com.gss.community.RestJsonClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ForgotPasswordActivity extends AppCompatActivity {
    ImageView back_button;
    EditText email;
    String url;
    String MobilePattern = "[0-9]{10}";
    String Email;
    Button forgot_psw, cancel;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
//        back_button=findViewById(R.id.back_button);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        back_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        email = findViewById(R.id.email);
        cancel = findViewById(R.id.cancel);
        url= Constant.ForgetPassword;
        forgot_psw = findViewById(R.id.forgot_code);
        forgot_psw.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Email = email.getText().toString().trim();
                                              if (Email.equalsIgnoreCase("")) {
                                                  Toast.makeText(ForgotPasswordActivity.this, "Please Enter Your Email Id", Toast.LENGTH_LONG).show();
                                              }else if(!isValidEmail(email.getText().toString().trim())){
                                                  Toast.makeText(ForgotPasswordActivity.this, "please enter valid email address", Toast.LENGTH_SHORT).show();
                                              } else if (CheckNetwork.isInternetAvailable(ForgotPasswordActivity.this)) {

                                                  new PostData(ForgotPasswordActivity.this).execute();

                                              } else {
                                                  Toast.makeText(ForgotPasswordActivity.this, "No Internet Connection.Please Check Your Intent Connection", Toast.LENGTH_LONG).show();


                                              }
                                          }
                                      }
        );
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.setText("");
            }
        });
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    class PostData extends AsyncTask<String, String, JSONObject> {
        JSONObject json = new JSONObject();
        private final Context context;
        private ProgressDialog progress;


        public PostData(ForgotPasswordActivity context) {
            this.context = context;
        }


        @Override
        protected JSONObject doInBackground(String... params) {
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("email_phone", Email));
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
            progress.setMessage("Please wait while Sending Password...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(false);
            progress.show();

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {


            try {
                progress.dismiss();
                if (jsonObject.getString("response").equalsIgnoreCase("success")) {



                    Toast.makeText(ForgotPasswordActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                    Intent iSettings = new Intent(ForgotPasswordActivity.this, LoginPageActivity.class);
//                    startActivity(iSettings);
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }



}
