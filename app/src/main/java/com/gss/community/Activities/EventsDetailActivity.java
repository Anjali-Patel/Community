package com.gss.community.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gss.community.CheckNetwork;
import com.gss.community.Constant;
import com.gss.community.R;
import com.gss.community.RestJsonClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventsDetailActivity extends AppCompatActivity {
    ImageView back_button;
    Toolbar toolbar;
    TextView tvgetname, tvgetmobno;
    ImageView imageView;
    Button submit;
    WebView webView;
    String userid, url;
    String imageurl = null;
    String title1, description1;
    TextView upload_photo, pic_video;
    TextView date, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_detail);
//        back_button = findViewById(R.id.back_button);
        webView = findViewById(R.id.video);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvgetname = findViewById(R.id.tvgetname);
        tvgetmobno = findViewById(R.id.description);
        imageView = findViewById(R.id.id_post_image);
        Calendar c = Calendar.getInstance();
        time = findViewById(R.id.time);
        url = Constant.Event_details;
        date = findViewById(R.id.date);



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userid = extras.getString("id");
        }
        if (CheckNetwork.isInternetAvailable(EventsDetailActivity.this)) {
            new GetProfileTask(EventsDetailActivity.this).execute();
        } else {
            Toast.makeText(EventsDetailActivity.this, "No Internet Connection.Please Check Your Intent Connection", Toast.LENGTH_LONG).show();

        }

    }

    class GetProfileTask extends AsyncTask<String, String, JSONObject> {
        JSONObject json = new JSONObject();
        private final Context context;
        private ProgressDialog progress;

        public GetProfileTask(EventsDetailActivity context) {
            this.context = context;

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
                    JSONArray jsonArray = jsonObject.getJSONArray("message");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jb = jsonArray.getJSONObject(i);
                        tvgetname.setText(jb.getString("title"));
                        tvgetmobno.setText(jb.getString("discription"));
                        date.setText(jb.getString("created_date"));
                        time.setText(jb.getString("time"));
                        if (jb.getString("ext").toUpperCase().equalsIgnoreCase("JPEG") ||
                                jb.getString("ext").toUpperCase().equalsIgnoreCase("PNG") ||
                                jb.getString("ext").toUpperCase().equalsIgnoreCase("RAW") ||
                                jb.getString("ext").toUpperCase().equalsIgnoreCase("INDD") ||
                                jb.getString("ext").toUpperCase().equalsIgnoreCase("AI") ||
                                jb.getString("ext").toUpperCase().equalsIgnoreCase("EPS") ||
                                jb.getString("ext").toUpperCase().equalsIgnoreCase("PDF") ||
                                jb.getString("ext").toUpperCase().equalsIgnoreCase("PSD") ||
                                jb.getString("ext").toUpperCase().equalsIgnoreCase("TIFF") ||
                                jb.getString("ext").toUpperCase().equalsIgnoreCase("GIF")) {
                            imageView.setVisibility(View.VISIBLE);
                            Glide.with(EventsDetailActivity.this).load("http://demo1.geniesoftsystem.com/newweb/community/uploads/events/" + jb.getString("photos")).into(imageView);
                        } else {
                            webView.setVisibility(View.VISIBLE);
                            webView.loadUrl("http://demo1.geniesoftsystem.com/newweb/community/uploads/events/" + jb.getString("photos"));
                        }
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}