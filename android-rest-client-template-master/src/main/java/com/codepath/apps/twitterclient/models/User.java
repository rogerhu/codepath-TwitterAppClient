package com.codepath.apps.twitterclient.models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

// BaseModel, not Model
// @PrimaryKey
// @ForeignKey cannot have column
// SQLiteWrapper
// compiling tables before Table_
// types have to match
//com.raizlabs.android.dbflow.structure.InvalidDBConfiguration: Model object: com.codepath.apps.twitterclient.models.Tweet is not registered with a Database. Did you forget an annotation?
// instantRun
// android.database.sqlite.SQLiteException: table Tweet has no column named user_twitter_id (code 1): , while compiling: INSERT INTO `Tweet`(`user_twitter_id`,`text`,`post_id`,`created_at`) VALUES (?,?,?,?)
// debugging with stetho
// https://github.com/Raizlabs/DBFlow/blob/master/usage2/Intro.md
// https://github.com/Raizlabs/DBFlow/issues/993
/**
 * Created by rhu on 10/19/13.
 */
@Table(database=MyDatabase.class)
public class User extends BaseModel implements MyBaseModel {

    @Column(name = "name")
    String name;

    @Column(name = "location")
    String location;

    @Column(name = "profile_image_url")
    String profileImageUrl;

    @PrimaryKey
    Long twitter_id;

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

        this.twitter_id = getUniqueId(object);

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

    public Long getTwitterId() { return this.twitter_id; }

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