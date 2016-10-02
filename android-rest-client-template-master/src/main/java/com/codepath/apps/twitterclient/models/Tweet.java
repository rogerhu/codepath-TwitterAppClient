package com.codepath.apps.twitterclient.models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


@Table(database = MyDatabase.class)
public class Tweet extends com.raizlabs.android.dbflow.structure.BaseModel implements MyBaseModel {

    @Column
    @ForeignKey(saveForeignKeyModel = false)
    User user;

    // Define table fields
    @Column(name = "text")
    String text;

    @PrimaryKey
    @Unique
    @Column(name = "post_id")
    Long post_id;

    @Column(name = "created_at")
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
            try {
                this.user = ModelCreator
                        .createOrUpdate(jsonUser, User.getUniqueId(jsonUser),
                                User_Table.twitter_id, User.class);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return;
            } catch (InstantiationException e) {
                e.printStackTrace();
            }

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
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            try {
                Tweet tweet = ModelCreator
                        .createOrUpdate(tweetJson, getUniqueId(tweetJson),
                                Tweet_Table.post_id, Tweet.class);
                tweets.add(tweet);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
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

    // Record finders
    public static Tweet byId(Long tweetId) {
        return SQLite.select().from(Tweet.class).where(Tweet_Table.post_id.eq(tweetId))
                .querySingle();

    }

    public static List<Tweet> getRecentItems() {
        return SQLite.select().from(Tweet.class).orderBy(Tweet_Table.created_at, false).queryList();
    }
}
