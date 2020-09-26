package com.gss.community.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gss.community.CheckNetwork;
import com.gss.community.Constant;
import com.gss.community.Adapter.EventListAdapter;
import com.gss.community.Fragment.EventNotification;
import com.gss.community.Model.EventListModal;
import com.gss.community.Model.NotificationModal;
import com.gss.community.R;
import com.gss.community.RestJsonClient;
import com.gss.community.Utility.SharedPreferenceUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends AppCompatActivity {
ImageView back_button;
    SharedPreferenceUtils preferances;
    String url;
    JSONObject json;
Toolbar toolbar;
Button load_more;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<EventListModal> person;
    ProgressBar pb;
     private int i=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        load_more=findViewById(R.id.load_more);
        pb=findViewById(R.id.pb);
//        back_button=findViewById(R.id.back_button);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        load_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadMOre(EventsActivity.this).execute();
            }
        });

//        back_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        url = Constant.Event_list;
//        preferances = SharedPreferenceUtils.getInstance(EventsActivity.this);
//        try {
//            preferances.setValue(CommanUtils.USERID,json.getString("id"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        recyclerView.setHasFixedSize(true);
        person = new ArrayList<EventListModal>();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        back_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        mAdapter = new EventListAdapter(person, EventsActivity.this);
        recyclerView.setAdapter(mAdapter);
        if (CheckNetwork.isInternetAvailable(EventsActivity.this)) {
            new GetProfileTask(EventsActivity.this).execute();
        }else{
            Toast.makeText(EventsActivity.this, "No Internet Connection.Please Check Your Intent Connection", Toast.LENGTH_LONG).show();
        }
    }
    class GetProfileTask extends AsyncTask<String, String, JSONObject> {
        JSONObject json;
        private Context context;
        private ProgressDialog progress;

        public GetProfileTask(EventsActivity findPersonActivity) {
            context = findPersonActivity;
        }


        @Override
        protected JSONObject doInBackground(String... params) {
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("pageno", String.valueOf(i)));

                // String url = "http://139.59.15.90/absolute_protector/index.php/api/api/user_profile";
                json = RestJsonClient.post(url, nameValuePairs);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }


        @Override
        protected void onPreExecute() {
pb.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {



            try {
    pb.setVisibility(View.GONE);
//                person.clear();
                if (jsonObject.getString("response").equalsIgnoreCase("success"))
                {
                    JSONArray jsonArray = jsonObject.getJSONArray("message");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jObj = jsonArray.getJSONObject(i);
                        EventListModal eventList=new EventListModal();
//                    locationLogModel.setId(jObj.getString("id"));
                        eventList.setId(jObj.getString("id"));
                        eventList.setEvent(jObj.getString("title"));
                        eventList.setDate(jObj.getString("created_date")+" "+jObj.getString("time"));

                        person.add(eventList);

                    }

                    mAdapter.notifyDataSetChanged();

                }else{
                    Toast.makeText(EventsActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
    class LoadMOre extends AsyncTask<String, String, JSONObject> {
        JSONObject json;
        private Context context;
        private ProgressDialog progress;

        public LoadMOre(EventsActivity findPersonActivity) {
            context = findPersonActivity;
        }


        @Override
        protected JSONObject doInBackground(String... params) {
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

//                nameValuePairs.add(new BasicNameValuePair("notification_type", type));
                nameValuePairs.add(new BasicNameValuePair("pageno", String.valueOf(++i)));


                json = RestJsonClient.post(url, nameValuePairs);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }


        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {


            try {
                pb.setVisibility(View.GONE);
//                person.clear();
                if (jsonObject.getString("response").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("message");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jObj = jsonArray.getJSONObject(i);
                        EventListModal eventList = new EventListModal();
//                    locationLogModel.setId(jObj.getString("id"));
                        eventList.setId(jObj.getString("id"));
                        eventList.setEvent(jObj.getString("title"));
                        eventList.setDate(jObj.getString("created_date")+" "+jObj.getString("time"));

                        person.add(eventList);

                    }

                    mAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(EventsActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }
}




