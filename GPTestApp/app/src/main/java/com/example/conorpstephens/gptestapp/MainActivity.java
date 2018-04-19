package com.example.conorpstephens.gptestapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ArrayList<JSONObject> users;

    private RecyclerView mRecyclerView;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new GetUsers().execute();
    }

    private void initUI(){
        findViewById(R.id.fab).setOnClickListener(this);
    }

    /**
     * Sets the properties of the recyclerview
     */
    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView");
        mRecyclerView = findViewById(R.id.recyclerview_main);

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(OrientationHelper.VERTICAL);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(llm);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                startActivity(new Intent(this,AddUserActivity.class));
                break;
        }
    }

    /**
     * Creates an ArrayList of JSONObjects from a JSONArray
     * @param jsonArray The JSONArray to be turned into an ArrayList
     * @return an ArrayList of JSONObjects
     */
    public static ArrayList<JSONObject> getListOfJsonObject(
            JSONArray jsonArray) {
        ArrayList<JSONObject> list = new ArrayList<>();

        if (jsonArray != null && jsonArray.length() > 0) {

            for (int index = 0; index < jsonArray.length(); index++) {
                JSONObject jsonObject = null;

                try {
                    jsonObject = jsonArray.getJSONObject(index);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (jsonObject != null) {
                    list.add(jsonObject);
                }
            }
        }
        return list;
    }

    private class GetUsers extends AsyncTask<String, Integer, JSONArray> {
        private ProgressDialog mProgressDialog;
        private String urlString;

        GetUsers() {
            this.urlString = "https://reqres.in/api/users";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create progress dialog
//            mProgressDialog = new ProgressDialog(MainActivity.this);
//            // Set your progress dialog Title
//            mProgressDialog.setTitle("Downloading");
//            // Set your progress dialog Message
//            mProgressDialog.setMessage("Please Wait!");
//            mProgressDialog.setIndeterminate(false);
//            mProgressDialog.setCancelable(false);
//            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            // Show progress dialog
//            mProgressDialog.show();
        }


        /**
         * Initiates a call to the api for each page of users and combines the user JSON objects
         * into a single JSON Array. The array is then returned to onPostExecute
         * @param Url
         * @return The JSONArray with all of the user objects
         */
        @Override
        protected JSONArray doInBackground(String... Url) {
            String result;
            int pages = 1;
            JSONObject jsonObject = null;
            JSONArray jsonArray = new JSONArray();
            result = downloadUrl(urlString);
            try {
                jsonObject = new JSONObject(result);
                pages = jsonObject.getInt("total_pages");
                jsonArray = jsonObject.optJSONArray("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(int i = 2; i <= pages; i++){
                result = downloadUrl(urlString + "?page=" + i);
                try {
                    jsonObject = new JSONObject(result);
                    JSONArray tempJsonArray = jsonObject.optJSONArray("data");

                    for(int j=1; j<tempJsonArray.length();j++){
                        jsonArray.put(tempJsonArray.get(j));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return jsonArray;
        }

        /**
         * Gets the response from the url parameter
         * @param myurl URL to be used in the request
         * @return
         */
        private String downloadUrl(String myurl){
            BufferedReader reader;
            try {
                URL url = new URL(myurl);
                HttpsURLConnection connection = (HttpsURLConnection)
                        url.openConnection();  //HTTP connection is open
                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while((line = reader.readLine())!= null){
                    sb.append(line);
                }
                return sb.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * This will set the adapter for the recyclerview, which will update the UI
         * @param result The resulting JSONArray with all of the user objects
         */
        @Override
        protected void onPostExecute(JSONArray result) {
            //mProgressDialog.dismiss();
            Log.d(TAG, "Result: " + result);

            JSONObject jsonObject = null;

            users = getListOfJsonObject(result);
            userAdapter = new UserAdapter(MainActivity.this,users);
            Log.d(TAG, "setting adapter");
            mRecyclerView.setAdapter(userAdapter);
        }
    }
}
