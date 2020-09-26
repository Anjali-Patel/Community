package com.gss.community.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gss.community.Activities.NotificationActivity;
import com.gss.community.Adapter.MessageNotificationAdapter;
import com.gss.community.CheckNetwork;
import com.gss.community.Constant;
import com.gss.community.Model.MessageNotificationModal;
import com.gss.community.Model.NotificationModal;
import com.gss.community.R;
import com.gss.community.RestJsonClient;
import com.gss.community.Utility.CommanUtils;
import com.gss.community.Utility.SharedPreferenceUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("ValidFragment")
public class MessageNotification extends Fragment {
    RecyclerView my_recycler_view;
ProgressBar pb;
    String user_id;
    Button load_more;
    private RecyclerView.Adapter mAdapter1;
    private RecyclerView.LayoutManager layoutManager1;
    private ArrayList<MessageNotificationModal> person1;
String type="msg";
int i=1;
    String url;
    public MessageNotification() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_message_notification, container, false);
        my_recycler_view=v.findViewById(R.id.my_recycler_view);
        user_id= SharedPreferenceUtils.getInstance(getActivity()).getStringValue(CommanUtils.UserId, "");
        layoutManager1 = new LinearLayoutManager(getActivity());
        pb=v.findViewById(R.id.pb);
        load_more=v.findViewById(R.id.load_more);
        my_recycler_view.setLayoutManager(layoutManager1);
        person1= new ArrayList<>();
        my_recycler_view.setHasFixedSize(true);
        mAdapter1 = new MessageNotificationAdapter( getActivity(),person1);
        my_recycler_view.setAdapter(mAdapter1);
        url= Constant.Notification;
        load_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadMOre((NotificationActivity) getActivity()).execute();


            }
        });

        if (CheckNetwork.isInternetAvailable(getActivity())) {
            new Notification((NotificationActivity) getActivity()).execute();
        }else{
            Toast.makeText(getActivity(), "No Internet Connection.Please Check Your Intent Connection", Toast.LENGTH_LONG).show();
        }

        return  v;

    }

//    private void execute() {
//    }

    class Notification extends AsyncTask<String, String, JSONObject> {
        JSONObject json;
        private Context context;
        private ProgressDialog progress;

        public Notification(NotificationActivity findPersonActivity) {
            context = findPersonActivity;
        }


        @Override
        protected JSONObject doInBackground(String... params) {
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("notification_type", type));
                nameValuePairs.add(new BasicNameValuePair("pageno", String.valueOf(i)));

                json = RestJsonClient.post(url, nameValuePairs);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }


        @Override
        protected void onPreExecute() {
//
            pb.setVisibility(View.VISIBLE);


        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {



            try {
               pb.setVisibility(View.GONE);
                person1.clear();
                if (jsonObject.getString("response").equalsIgnoreCase("success")) {
                    JSONObject message=jsonObject.getJSONObject("message");
                    JSONArray jsonArray1 = message.getJSONArray("msg");
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject jObj = jsonArray1.getJSONObject(i);
                        MessageNotificationModal eventList=new MessageNotificationModal();
//                    locationLogModel.setId(jObj.getString("id"));
                        eventList.setId(jObj.getString("id"));
                        eventList.setTitle(jObj.getString("title"));
                        eventList.setDate(jObj.getString("created_date")+" "+jObj.getString("time"));
                        eventList.setType("msg");
                        person1.add(eventList);

                    }

                    mAdapter1.notifyDataSetChanged();


                }else{
                    Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

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

        public LoadMOre(NotificationActivity findPersonActivity) {
            context = findPersonActivity;
        }


        @Override
        protected JSONObject doInBackground(String... params) {
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("notification_type", type));
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
//                person1.clear();
                if (jsonObject.getString("response").equalsIgnoreCase("success")) {
                    JSONObject message=jsonObject.getJSONObject("message");
                    JSONArray jsonArray = message.getJSONArray("msg");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jObj = jsonArray.getJSONObject(i);
                        MessageNotificationModal eventList=new MessageNotificationModal();
//                    locationLogModel.setId(jObj.getString("id"));
                        eventList.setId(jObj.getString("id"));
                        eventList.setTitle(jObj.getString("title"));
                        eventList.setDate(jObj.getString("created_date")+" "+jObj.getString("time"));
                        eventList.setType("msg");
                        person1.add( eventList);
                    }
                    mAdapter1.notifyDataSetChanged();
                }else{
                    Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }



}
