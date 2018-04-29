package com.codepath.apps.twitterclient.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

@Entity
public class Tweet implements MyBaseModel {

    @Embedded User user;

    // Define table fields
    @ColumnInfo
    String text;

    @PrimaryKey
    @ColumnInfo(name = "post_id")
    Long post_id;

    @ColumnInfo(name = "created_at")
    Date created_at;

    public Tweet() {
        super();
    }

    // Parse model from JSON
    public Tweet(JSONObject object) {
        super();
        parseJSON(object);

    }

    public void parseJSON(JSONObject object) {

        this.post_id = (Long) getUniqueId(object);

        String creationString = "";

        try {

            JSONObject jsonUser = object.getJSONObject("user");
            this.user = new User(jsonUser);
            this.text = object.getString("text");
            creationString = object.getString("created_at");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy",
                    Locale.getDefault());
            this.created_at = dateFormat.parse(creationString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
                tweets.add(new Tweet(tweetJson));
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }

        return tweets;
    }

    public Long getPostId() {
        return this.post_id;
    }

    public static Long getUniqueId(JSONObject tweetJson) {
        Long postId = Long.valueOf("0");

        try {
            postId = tweetJson.getLong("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postId;
    }

    // Getters
    public User getUser() {
        return user;
    }

    public String getTweet() {
        return this.text;

    }

    public String getTimestamp() {
        String result;
        result = (String) DateUtils
                .getRelativeTimeSpanString(this.created_at.getTime(), System.currentTimeMillis(),
                        DateUtils.MINUTE_IN_MILLIS);
        return result;
    }

    // Setters
    public void setText(String text) {
        this.text = text;
    }

    public void setPostId(Long id) {
        this.post_id = id;
    }

    public void setCreatedAt(Date date) {
        this.created_at = date;
    }

    public void setCreatedAt(Long l) {
        this.created_at = new Date(l);
    }

    public void setUser(User u) {
        this.user = u;
    }
}
