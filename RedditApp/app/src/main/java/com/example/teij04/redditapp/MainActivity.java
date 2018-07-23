package com.example.teij04.redditapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;
    ArrayList<HashMap<String, String>> titlelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titlelist = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d(TAG, "entered doINBackgrond");
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://www.reddit.com/r/MadeMeSmile/.json";
            String jsonStr = sh.makeServiceCall(url);

            //Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    //JSONArray title = jsonObj.getJSONArray("title");
                    Iterator<String> keys = jsonObj.keys();
                    while (keys.hasNext()) {
                        Object key = keys.next();
                        JSONObject value = jsonObj.getJSONObject((String)key);
                        String my_title = value.getString("my_title");
                        HashMap<String, String> c = new HashMap<>();
                        c.put("title", my_title);
                        System.out.print(c.entrySet());
                        titlelist.add(c);
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });


                }
            }
            return null;
        }
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, titlelist,R.layout.activity_main,
                    new String[]{"title"}, new int[]{R.id.list});
            lv.setAdapter(adapter);
        }



    }
}

