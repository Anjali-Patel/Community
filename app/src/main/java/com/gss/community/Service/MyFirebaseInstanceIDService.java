package com.gss.community.Service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.gss.community.Utility.CommanUtils;
import com.gss.community.Utility.SharedPreferenceUtils;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
//    SharedPreferences sharedPreferences;
//    SharedPreferences.Editor editor;
    Context context;
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        SharedPreferenceUtils.getInstance(context).setValue(CommanUtils.FCM,refreshedToken);
        Log.d("FCM Token TokenRefresh",refreshedToken);

//        sharedPreferences = getSharedPreferences("ID", Context.MODE_PRIVATE);
//        editor = sharedPreferences.edit();
//        editor.putString("fcmtoken", refreshedToken);
//        editor.apply();

       /* String status = sharedPreferences.getString("status", Constant.LOGGED_OUT);

        if (status.equalsIgnoreCase(Constant.LOGGED_IN)) {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
            String userId = sharedPreferences.getString("UserID", "0");
            Log.d("status",status);
            Log.d("token",userId);
            Log.d("token",refreshedToken);
            if(getApplicationContext().getSharedPreferences("notifications",MODE_PRIVATE).getString("state","id").equalsIgnoreCase("on"))
            new api().execute(refreshedToken, userId);
        }*/
    }


/*
    class api extends AsyncTask<String,String,JSONObject>
    {
        JSONObject json;
        @Override
        protected JSONObject doInBackground(String... params) {
            try
            {
                String url="http://104.236.14.235/jewelnetinsort/index.php/Api/updatedevicetoken?data=";
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("devicetoken",params[0]));
                nameValuePairs.add(new BasicNameValuePair("type", "android"));
                nameValuePairs.add(new BasicNameValuePair("userid", params[1]));
                Log.d("data",nameValuePairs.toString());
                json = RestJsonClient.post(url,nameValuePairs,nameValuePairs);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return  json;
        }

        protected  void onPostExecute(JSONObject params)
        {
            super.onPostExecute(params);
        }
    }
*/

}