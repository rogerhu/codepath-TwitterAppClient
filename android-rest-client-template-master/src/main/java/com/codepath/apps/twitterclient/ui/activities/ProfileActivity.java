package com.codepath.apps.twitterclient.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.models.User;
import com.codepath.apps.twitterclient.network.RestClientApp;
import com.codepath.apps.twitterclient.network.TwitterClient;
import com.codepath.apps.twitterclient.ui.fragments.BaseTimelineFragment;
import com.codepath.apps.twitterclient.ui.fragments.UserTimelineFragment;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

/**
 * Created by rhu on 10/26/13.
 */
public class ProfileActivity extends FragmentActivity implements BaseTimelineFragment.OnDataUpdateListener {

	TwitterClient client;
	User user;
	public int TWEETS_PER_PAGE = 25;

	public void onStart() {
		super.onStart();
		// Now you can switch on and off the progress
		setProgressBarIndeterminateVisibility(true);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_profile);

		Intent i = getIntent();
		final String screenName = i.getStringExtra("screenName");

		JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject json) {
				Log.d("debug", "Got " + json.toString());
				user = new User(json);
				updateProfile();

				Log.d("debug", "Searching for user " + user.getTwitterHandle());
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.profileTweets, new UserTimelineFragment(screenName));
				ft.commit();
				setProgressBarIndeterminateVisibility(false);
			}
		};

		client = RestClientApp.getRestClient();

		// Query the user profile first.
		if (screenName == null) {
			client.getSelfProfile(handler);
		} else {
    		client.getUserProfile(screenName, handler);
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
	public void onError(Throwable e, final String responseBody) {
		Log.d("debug", "onError here " + e.toString());

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getBaseContext(), "Twitter error: " + responseBody, Toast.LENGTH_LONG).show();

			}
		});
	}

	public void composeTo(Long tweetId) {

	}
}