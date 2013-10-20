package com.codepath.apps.twitterclient.models;

import android.text.format.DateUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


@Table(name="Tweets")
public class Tweet extends Model {

    private User user;

	// Define table fields
	@Column(name = "text")
	String text;
	
	@Column(name = "post_id")
	Long post_id;
	
	@Column(name = "source")
	String source;
	
	@Column(name = "created_at")
    Date created_at;
	
	public Tweet() {
		super();
	}
		
	// Parse model from JSON
	public Tweet(JSONObject object){
		super();

        String creationString = "";

		try {
            JSONObject jsonUser = object.getJSONObject("user");
            this.user = new User(jsonUser);
            this.text = object.getString("text");
			this.post_id = object.getLong("id");
            creationString = object.getString("created_at");
		} catch (JSONException e) {
			e.printStackTrace();
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.getDefault());
            this.created_at = dateFormat.parse(creationString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.user.save();

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
		
			Tweet tweet = new Tweet(tweetJson);
			tweet.save();

			tweets.add(tweet);
		}
		
		return tweets;
	}

    public Long getPostId() {
        return this.post_id;
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
        result = (String) DateUtils.getRelativeTimeSpanString(this.created_at.getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
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

//	public String getUserId() {
//		return userId;
//	}
	
/*	// Record Finders
	public static SampleModel byId(long id) {
	   return new Select().from(SampleModel.class).where("id = ?", id).executeSingle();
	}
	
	public static ArrayList<SampleModel> recentItems() {
      return new Select().from(SampleModel.class).orderBy("id DESC").limit("300").execute();
	}*/
}
