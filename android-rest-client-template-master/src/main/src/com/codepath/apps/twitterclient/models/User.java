package com.codepath.apps.twitterclient.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rhu on 10/19/13.
 */
public class User extends Model {
    @Column(name = "name")
    String name;

    @Column(name = "location")
    String location;

    @Column(name = "profile_image_url")
    String profile_image_url;

    @Column(name = "twitter_id")
    String twitter_id;

    @Column(name = "screen_name")
    String screen_name;

    public User() {
        super();
    }

    // Parse model from JSON
    public User(JSONObject object){
        super();

        try {
            this.name = object.getString("name");
            this.location = object.getString("location");
            this.profile_image_url = object.getString("profile_image_url");
            this.twitter_id = object.getString("id");
            this.screen_name = object.getString("screen_name");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getProfileImageUrl() {
        return this.profile_image_url;
    }

    public String getFullName() {
        return this.name;
    }

    public String getTwitterHandle() {
        return this.screen_name;
    }

    public void setFullName(String name) {
        this.name = name;
    }

    public void setProfileImageUrl(String url) {
        this.profile_image_url = url;
    }

    public void setTwitterHandle(String id) {
        this.twitter_id = id;
    }

}
