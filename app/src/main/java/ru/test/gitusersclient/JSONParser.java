package ru.test.gitusersclient;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ost_red on 25.09.2016.
 */
public class JSONParser extends AsyncTask<String, Void, String> {

    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String resultJson = "";

    @Override
    protected String doInBackground(String... string) {
        try {
            URL url = new URL("https://api.github.com/users?since="+string[0]);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            resultJson = buffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJson;
    }

    @Override
    protected void onPostExecute(String strJson) {
        super.onPostExecute(strJson);
        if(strJson!=null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<User>>() {
            }.getType();
            List<User> mUserlist = gson.fromJson(strJson, type);
            EventBus.getDefault().post(new MessageEvent(mUserlist));
        }else{
            EventBus.getDefault().post(new MessageEvent(null));
        }
    }

}

