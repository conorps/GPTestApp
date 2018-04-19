package com.example.conorpstephens.gptestapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by conor.p.stephens@ibm.com on 4/19/18.
 */

public class AddUserActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText userNameEditText;
    private EditText userJobEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initUI();
    }

    private void initUI() {
        userNameEditText = findViewById(R.id.edittext_add_user_name);
        userJobEditText = findViewById(R.id.edittext_add_user_job);

        findViewById(R.id.button_add_user_submit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add_user_submit:
                submitButtonClicked();
                break;
        }
    }

    private void submitButtonClicked() {
        String userName, userJob;
        if(TextUtils.isEmpty(userNameEditText.getText()) || TextUtils.isEmpty(userJobEditText.getText())){
            Toast.makeText(this, R.string.fields_are_mandatory, Toast.LENGTH_LONG).show();
            return;
        } else{
            userName = userNameEditText.getText().toString();
            userJob = userJobEditText.getText().toString();
            new PostNewUser(this).execute(userName,userJob);
        }
    }

    public class PostNewUser extends AsyncTask<String, String, String> {
        private String urlString;
        private Activity context;

        public PostNewUser(Activity context){
            this.urlString = "https://reqres.in/api/users";
            this.context =  context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpsURLConnection connection = null;
            String name = params[0];
            String job = params[1];
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name",name);
                jsonObject.put("job",job);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String response = null;
            try {
                //Create connection
                URL url = new URL(urlString);
                connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setUseCaches(false);
                //connection.setDoInput(true);// true indicates the server returns response
                connection.setDoOutput(true);// true indicates POST request

                //Send request
                if (jsonObject != null) {
                    Log.d("AddUSerActivity", jsonObject.toString());
                    // sends POST data
                    OutputStreamWriter writer = new OutputStreamWriter(
                            connection.getOutputStream());
                    writer.write(jsonObject.toString());
                    writer.flush();
                }

                //Get Response
                InputStream is = connection.getInputStream();
                response = getResponseString(is);

                Log.i("URL=", "" + urlString);
                Log.i("Response Code=", "" + connection.getResponseCode());
                Log.i("Response Message=", connection.getResponseMessage());
                Log.i("Response Data=", "" + response);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            try {
                return connection.getResponseMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return response;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(context,s,Toast.LENGTH_LONG).show();
            context.finish();
        }
    }
    private static String getResponseString(InputStream is) throws IOException {
        StringBuffer response = null;
        if (is != null) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            response = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            bufferedReader.close();
        }

        return response != null ? response.toString() : null;
    }

}
