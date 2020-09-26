package com.gss.community.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.gss.community.Adapter.FindPersonAdapter;
import com.gss.community.Adapter.HorizontalAdapter;
import com.gss.community.CheckNetwork;
import com.gss.community.Constant;
import com.gss.community.Model.HorizontalModel;
import com.gss.community.R;
import com.gss.community.RestJsonClient;
import com.gss.community.Service.MyFirebaseMessagingService;
import com.gss.community.Service.TrackMyNotificationsService;
import com.gss.community.Utility.CommanUtils;
import com.gss.community.Utility.SharedPreferenceUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NotificationDetailActivity extends AppCompatActivity {
    ImageView back_button;
    Toolbar toolbar;
    RecyclerView recycler_view;
    TextView tvgetname, tvgetmobno;
    ImageView imageView;
    Button submit;
    private RecyclerView.Adapter mAdapter;
    String type,Date,Time,File,Type;
WebView video;
ArrayList<HorizontalModel> horizontalModelArraylist;
    String notification_id,userid, url;
    String imageurl = null;
    String title1, description1;
    TextView upload_photo, pic_video;
    TextView date,time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);
        video=findViewById(R.id.video);

        horizontalModelArraylist= new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new HorizontalAdapter(NotificationDetailActivity.this, horizontalModelArraylist);
//        recyclerView.setAdapter(mAdapter);
        Bundle extra = getIntent().getExtras();
        if(extra != null && extra.getInt("notification_id") > -1 && MyFirebaseMessagingService.notification_count > 0){
            int count = MyFirebaseMessagingService.notification_count--;
            DashboardActivity.notification_count.setText(count +"");
        }
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
//        back_button = findViewById(R.id.back_button);
        tvgetname = findViewById(R.id.tvgetname);
        tvgetmobno = findViewById(R.id.description);
        imageView = findViewById(R.id.id_post_image);
        Calendar c = Calendar.getInstance();
        time = findViewById(R.id.time);
        url = Constant.NotificationDetails;
        userid= SharedPreferenceUtils.getInstance(this).getStringValue(CommanUtils.UserId, "");
        date = findViewById(R.id.date);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            notification_id = extras.getString("id");
            type=extras.getString("notification_type");

            if(type.equals("Event")) {
                //code
                title1 = extra.getString("notification_title");
                description1 = extra.getString("notification_body");
                Time = extra.getString("notification_time");
                Date = extra.getString("notification_date");
                File = extra.getString("notification_file");
                Type = extra.getString("notification_type");
                date.setText(Date);
                time.setText(Time);
                tvgetname.setText(title1);
                tvgetmobno.setText(description1);

                String[] exts = File.split("[.]");
                String ext = "";
                if (exts.length > 0) {
                    ext = exts[exts.length - 1];
                }

                if (ext.toUpperCase().equalsIgnoreCase("JPEG") ||
                        ext.toUpperCase().equalsIgnoreCase("PNG") ||
                        ext.toUpperCase().toUpperCase().equalsIgnoreCase("RAW") ||
                        ext.toUpperCase().toUpperCase().equalsIgnoreCase("INDD") ||
                        ext.toUpperCase().toUpperCase().equalsIgnoreCase("AI") ||
                        ext.toUpperCase().toUpperCase().equalsIgnoreCase("EPS") ||
                        ext.toUpperCase().toUpperCase().equalsIgnoreCase("PDF") ||
                        ext.toUpperCase().toUpperCase().equalsIgnoreCase("PSD") ||
                        ext.toUpperCase().toUpperCase().equalsIgnoreCase("TIFF") ||
                        ext.toUpperCase().toUpperCase().equalsIgnoreCase("GIF")) {
                    imageView.setVisibility(View.VISIBLE);
                    Glide.with(this).load("http://demo1.geniesoftsystem.com/newweb/community/uploads/events/" + File).into(imageView);
                } else {
                    video.setVisibility(View.VISIBLE);
                    video.loadUrl("http://demo1.geniesoftsystem.com/newweb/community/uploads/events/" + File);
                }
            }
        }

        if (CheckNetwork.isInternetAvailable(NotificationDetailActivity.this) && type.equals("events")) {
            new GetProfileTask(NotificationDetailActivity.this).execute();
        } else if(type.equals("events")){
            Toast.makeText(NotificationDetailActivity.this, "No Internet Connection.Please Check Your Intent Connection", Toast.LENGTH_LONG).show();

        }
//
//        back_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }

    class GetProfileTask extends AsyncTask<String, String, JSONObject> {
        JSONObject json = new JSONObject();
        private final Context context;
        private ProgressDialog progress;

        public GetProfileTask(NotificationDetailActivity context) {
            this.context = context;

        }
        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("user_id", userid));
                nameValuePairs.add(new BasicNameValuePair("notification_id", notification_id));
                nameValuePairs.add(new BasicNameValuePair("notification_type", type));
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
                        if (jb.getString("ftype").toUpperCase().equalsIgnoreCase("JPEG") ||
                                jb.getString("ftype").toUpperCase().equalsIgnoreCase("PNG") ||
                                jb.getString("ftype").toUpperCase().equalsIgnoreCase("RAW") ||
                                jb.getString("ftype").toUpperCase().equalsIgnoreCase("INDD") ||
                                jb.getString("ftype").toUpperCase().equalsIgnoreCase("AI") ||
                                jb.getString("ftype").toUpperCase().equalsIgnoreCase("EPS") ||
                                jb.getString("ftype").toUpperCase().equalsIgnoreCase("PDF") ||
                                jb.getString("ftype").toUpperCase().equalsIgnoreCase("PSD") ||
                                jb.getString("ftype").toUpperCase().equalsIgnoreCase("TIFF") ||
                                jb.getString("ftype").toUpperCase().equalsIgnoreCase("GIF")) {
                            imageView.setVisibility(View.VISIBLE);
                            Glide.with(NotificationDetailActivity.this).load("http://demo1.geniesoftsystem.com/newweb/community/uploads/events/" + jb.getString("photos")).into(imageView);
                        } else {
                            video.setVisibility(View.VISIBLE);
                            video.loadUrl("http://demo1.geniesoftsystem.com/newweb/community/uploads/events/" + jb.getString("photos"));
                        }
                        //Glide.with(NotificationDetailActivity.this).load("http://demo1.geniesoftsystem.com/newweb/community/uploads/events/" + jb.getString("photos")).into(imageView);
                    }
                } else {
                    Toast.makeText(NotificationDetailActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
