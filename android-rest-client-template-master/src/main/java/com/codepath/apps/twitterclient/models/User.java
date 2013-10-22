package com.codepath.apps.twitterclient.models;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by rhu on 10/19/13.
 */
public class User extends Model implements BaseModel {
    @Column(name = "name")
    String name;

    @Column(name = "location")
    String location;

    @Column(name = "profile_image_url")
    String profile_image_url;

    @Column(name = "twitter_id")
    Long twitter_id;

    @Column(name = "screen_name")
    String screen_name;

    public User() {
        super();
    }

    // Parse model from JSON
    public User(JSONObject object){
        super();

        parseJSON(object);

    }

    public void parseJSON(JSONObject object) {

        this.twitter_id = getUniqueId(object);

        try {
            this.name = object.getString("name");
            this.location = object.getString("location");
            this.profile_image_url = object.getString("profile_image_url");
            this.screen_name = object.getString("screen_name");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getProfileImageUrl() {
        return this.profile_image_url;
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

    public String getFullName() {
        return this.name;
    }

    public Long getTwitterId() { return this.twitter_id; }

    public String getTwitterHandle() {
        return this.screen_name;
    }

    public void setFullName(String name) {
        this.name = name;
    }

    public void setProfileImageUrl(String url) {
        this.profile_image_url = url;
    }

    public void setTwitterHandle(String handle) {
        this.screen_name = handle;
    }

}
