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
    String profileImageUrl;

    @Column(name = "twitter_id")
    Long twitterId;

    @Column(name = "screen_name")
    String screenName;

	@Column(name = "followers_count")
	Long followersCount;

	@Column(name = "friends_count")
	Long friendsCount;

	@Column(name = "description")
	String description;

	public User() {
        super();
    }

    // Parse model from JSON
    public User(JSONObject object){
        super();

        parseJSON(object);

    }

	public void parseJSON(JSONObject object) {

        this.twitterId = getUniqueId(object);

        try {
            this.name = object.getString("name");
            this.location = object.getString("location");
            this.profileImageUrl = object.getString("profile_image_url");
            this.screenName = object.getString("screen_name");
			this.followersCount = object.getLong("followers_count");
	        this.friendsCount = object.getLong("friends_count");
	        this.description = object.getString("description");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getProfileImageUrl() {
        return this.profileImageUrl;
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

    public Long getTwitterId() { return this.twitterId; }

    public String getTwitterHandle() {
        return this.screenName;
    }

    public void setFullName(String name) {
        this.name = name;
    }

    public void setProfileImageUrl(String url) {
        this.profileImageUrl = url;
    }

    public void setTwitterHandle(String handle) {
        this.screenName = handle;
    }

	public Long getFollowersCount() {
		return followersCount;
	}

	public void setFollowersCount(Long followersCount) {
		this.followersCount = followersCount;
	}

	public Long getFriendsCount() {
		return friendsCount;
	}

	public void setFriendsCount(Long friendsCount) {
		this.friendsCount = friendsCount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}