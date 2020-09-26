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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gss.community.CheckNetwork;
import com.gss.community.Constant;
import com.gss.community.Adapter.FindPersonAdapter;
import com.gss.community.Model.PersonModal;
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

public class FindPersonActivity extends AppCompatActivity {
    ImageView back_button;
    String strText;
    Button load_more ;
    ProgressBar pb;
    int i=1;
    AutoCompleteTextView simpleSearchView;
    SharedPreferenceUtils preferances;
    RelativeLayout event1, event2, event3, event4, event5, event6;
    //    String date1;
    String url;
    Toolbar toolbar;
    ImageView ic_search;
    JSONObject json;
 private    ArrayList<PersonModal> MemberDetailsArrayListTmp;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<PersonModal> person;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_person);
        ic_search = findViewById(R.id.btn_search);
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
        load_more=findViewById(R.id.load_more);
        pb=findViewById(R.id.pb);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        url = Constant.Find_Person;
        simpleSearchView = findViewById(R.id.searchflags);
        MemberDetailsArrayListTmp = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        person = new ArrayList<PersonModal>();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        simpleSearchView.setFocusable(true);
        simpleSearchView.setCursorVisible(true);
//        simpleSearchView.setAdapter(mAdapter);

        ic_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(simpleSearchView.getText().toString().length() == 0)) {
                    MemberDetailsArrayListTmp.clear();
                    for (int i = 0; i < person.size(); i++) {
                        if ((person.get(i).getName().toLowerCase().startsWith(simpleSearchView.getText().toString().toLowerCase()))) {

                            MemberDetailsArrayListTmp.add(person.get(i));
                        }
                    }
                    if (MemberDetailsArrayListTmp.isEmpty()) {
                        Toast.makeText(FindPersonActivity.this, "No Result Found", Toast.LENGTH_LONG).show();
                    }
                    mAdapter = new FindPersonAdapter(FindPersonActivity.this, MemberDetailsArrayListTmp);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(FindPersonActivity.this, "Please Enter Person Name", Toast.LENGTH_SHORT).show();
                }

            }
        });
//        back_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        try {
//            String a= json.getString("id");
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        mAdapter = new FindPersonAdapter(FindPersonActivity.this, person);
        recyclerView.setAdapter(mAdapter);
        if (CheckNetwork.isInternetAvailable(FindPersonActivity.this)) {

            new GetProfileTask(FindPersonActivity.this).execute();


        } else {

            Toast.makeText(FindPersonActivity.this, "No Internet Connection.Please Check Your Intent Connection", Toast.LENGTH_LONG).show();
        }
        load_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadMore(FindPersonActivity.this).execute();
            }
        });
    }
    class GetProfileTask extends AsyncTask<String, String, JSONObject> {
        JSONObject json;
        private  Context context;
        private ProgressDialog progress;

        public GetProfileTask(FindPersonActivity findPersonActivity) {
            context = findPersonActivity;
        }
        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("pageno", String.valueOf(i)));
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
                        PersonModal locationLogModel=new PersonModal();
//                        preferances.setValue(CommanUtils.UserId,json.getString("id"));
//                        String a=json.getString("id");
//                    locationLogModel.setId(jObj.getString("id"));
                        locationLogModel.setId(jObj.getString("id"));
                        locationLogModel.setName(jObj.getString("uname"));
                        locationLogModel.setImg(jObj.getString("photo"));

                        person.add(locationLogModel);
                        MemberDetailsArrayListTmp.add(locationLogModel);

                    }

                    mAdapter.notifyDataSetChanged();

                }else{

                    Toast.makeText(FindPersonActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
    class LoadMore extends AsyncTask<String, String, JSONObject> {
        JSONObject json;
        private  Context context;
        private ProgressDialog progress;

        public LoadMore(FindPersonActivity findPersonActivity) {
            context = findPersonActivity;
        }
        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
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
                if (jsonObject.getString("response").equalsIgnoreCase("success"))
                {
                    JSONArray jsonArray = jsonObject.getJSONArray("message");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jObj = jsonArray.getJSONObject(i);
                        PersonModal locationLogModel=new PersonModal();
//                        preferances.setValue(CommanUtils.UserId,json.getString("id"));
//                        String a=json.getString("id");
//                    locationLogModel.setId(jObj.getString("id"));
                        locationLogModel.setId(jObj.getString("id"));
                        locationLogModel.setName(jObj.getString("uname"));
                        locationLogModel.setImg(jObj.getString("photo"));

                        person.add(locationLogModel);
                        MemberDetailsArrayListTmp.add(locationLogModel);

                    }

                    mAdapter.notifyDataSetChanged();

                }else{

                    Toast.makeText(FindPersonActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


}
