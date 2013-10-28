package com.codepath.apps.twitterclient.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.codepath.apps.twitterclient.network.RestClientApp;
import com.codepath.apps.twitterclient.network.TweetJsonHttpResponseHandler;
import com.codepath.apps.twitterclient.network.TwitterClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rhu on 10/26/13.
 */
public class ProfileActivity extends FragmentActivity implements TimelineFragment.OnDataUpdateListener {

	TwitterClient client;
	User user;
	public int TWEETS_PER_PAGE = 25;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		Intent i = getIntent();
		String screenName = i.getStringExtra("screen_name");

		client = RestClientApp.getRestClient();

		if (screenName == null) {
			client.getSelfProfile(new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject json) {
					Log.d("debug", "Got " + json.toString());
					user = new User(json);
					updateProfile();

					Log.d("debug", "Searching for user " + user.getTwitterHandle());
					FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
					ft.replace(R.id.profileTweets, new TimelineFragment());
					ft.commit();
				}
			});
		}


	}

	public void updateProfile() {

		ImageView imageView = (ImageView) findViewById(R.id.profileImage);

		ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), imageView);

		TextView profileName = (TextView) findViewById(R.id.profileName);
		profileName.setText(user.getFullName());

		TextView followerText = (TextView) findViewById(R.id.followerText);
		followerText.setText(user.getFollowersCount().toString() + " Followers");

		TextView followingText = (TextView) findViewById(R.id.followingText);
		followingText.setText(user.getFriendsCount().toString() + " Following");

		TextView profileDescription = (TextView) findViewById(R.id.profileDescription);
		profileDescription.setText(user.getDescription());

	}

	@Override
	public void loadMore(final TimelineFragment.LoadType loadType) {
		TimelineFragment fragment = (TimelineFragment) getSupportFragmentManager().findFragmentById(R.id.profileTweets);

		if (user == null) {
			Log.d("debug", "User object has not been set..not loading");
			return;
		}

		TweetJsonHttpResponseHandler handler = fragment.createTweetHandler(loadType);

		if (loadType == TimelineFragment.LoadType.NEW_TWEETS) {
			client.getUserTimeline(user.getTwitterHandle(), fragment.getNewerTweets(), 0, TWEETS_PER_PAGE, handler);
		} else {
			client.getUserTimeline(user.getTwitterHandle(), 0, fragment.getOlderTweets(), TWEETS_PER_PAGE, handler);

		}

	}

	@Override
	public void onError(Throwable e, final String responseBody) {
		Log.d("debug", "onError here " + e.toString());

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getBaseContext(), "Twitter error: " + responseBody, Toast.LENGTH_LONG).show();

			}
		});
	}

}