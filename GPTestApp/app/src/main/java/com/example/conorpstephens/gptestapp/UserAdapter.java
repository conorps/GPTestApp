package com.example.conorpstephens.gptestapp;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by conor.p.stephens@ibm.com on 4/18/18.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> implements View.OnClickListener {

    private ArrayList<JSONObject> users;
    private Context activity;

    public UserAdapter(Context context, ArrayList<JSONObject> objects) {
        this.users = objects;
        this.activity = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView userNameView;
        ImageView userPhotoView;



        ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardview_user);
            userPhotoView = (ImageView) itemView.findViewById(R.id.imageview_user_photo);
            userNameView = (TextView) itemView.findViewById(R.id.textview_user_name);
        }

    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, int position) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
