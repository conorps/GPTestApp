package com.example.conorpstephens.gptestapp;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by conor.p.stephens@ibm.com on 4/18/18.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = UserAdapter.class.getSimpleName();

    private ArrayList<JSONObject> users;
    private Context activity;

    public UserAdapter(Context context, ArrayList<JSONObject> objects) {
        Log.d(TAG, "Adapter Constructor");
        this.users = objects;
        this.activity = context;
    }

    /**
     * Defines the CardView for each item in the list of users
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView userNameView;
        ImageView userPhotoView;

        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview_user);
            userPhotoView = itemView.findViewById(R.id.imageview_user_photo);
            userNameView = itemView.findViewById(R.id.textview_user_name);
        }

    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int index) {
        Log.d(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_main_user_card, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, int position) {
        JSONObject jsonObject = users.get(position);

        Log.d(TAG, "onBindViewHolder");

        try {
            Log.d(TAG, jsonObject.getString("first_name"));
            holder.userNameView.setText(jsonObject.getString("first_name") + " " + jsonObject.getString("last_name"));
            new DownloadImageTask(holder.userPhotoView).execute(jsonObject.getString("avatar"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    /**
     * To download the user profile images Asynchronously. This is called from onBindViewHolder
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
