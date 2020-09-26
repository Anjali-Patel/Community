package com.gss.community.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.gss.community.Constant;
import com.gss.community.R;
import com.gss.community.RestJsonClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PersonDetailActivity extends AppCompatActivity {
ImageView back_button,pPhoto;
String url,userid;
    Toolbar toolbar;
    SharedPreferences sharedPreferences;
    String str_name,str_email,str_mobile,Str_curent_address,Str_permanent_address,Str_zip,str_city,str_state,str_country,str_caste,str_subcaste;
    TextView gender,name,email,mobile_number,caddress,paddress,zip,city,state,country,caste,subcaste,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);
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
        name=findViewById(R.id.im_photo);
        url= Constant.Person_Details;
        email=findViewById(R.id.email);
        pPhoto=findViewById(R.id.pPhoto);
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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userid = extras.getString("id");
        }

        new GetProfileTask(PersonDetailActivity.this).execute();

//        back_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }
    class GetProfileTask extends AsyncTask<String, String, JSONObject> {
        JSONObject json= new JSONObject();
        private final Context context;
        private ProgressDialog progress;

        public GetProfileTask(PersonDetailActivity context) {
            this.context=context;

        }


        @Override
        protected JSONObject doInBackground(String... params) {
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("id", userid));

                Log.d("datap", nameValuePairs.toString());
                // String url = "http://139.59.15.90/absolute_protector/index.php/api/api/user_profile";
                json = RestJsonClient.post(url, nameValuePairs);


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
                    name.setText(":"+" "+jb.getString("uname"));
                    email.setText(":"+" "+jb.getString("email"));
                    mobile_number.setText(":"+" "+jb.getString("mobileno"));

                    subcaste.setText(":"+" "+jb.getString("subcaste"));
                    gender.setText(":"+" "+jb.getString("gender"));
                    zip.setText(":"+" "+jb.getString("zipcode"));
                    city.setText(":"+" "+jb.getString("city"));
                    state.setText(":"+" "+jb.getString("state"));
                    country.setText(":"+" "+jb.getString("country"));

                        caste.setText(":"+" "+jb.getString("caste"));
                    caddress.setText(":"+" "+jb.getString("c_address"));
                    paddress.setText(":"+" "+jb.getString("p_address"));
                        Glide.with(PersonDetailActivity.this).load("http://demo1.geniesoftsystem.com/newweb/community/uploads/images/"+jb.getString("photo")).error(R.drawable.profile).into(pPhoto);
                    }
                }else{
                    Toast.makeText(PersonDetailActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
