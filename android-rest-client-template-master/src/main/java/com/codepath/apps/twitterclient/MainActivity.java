package com.codepath.apps.twitterclient;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.codepath.apps.twitterclient.handlers.TweetJsonHttpResponseHandler;
import com.codepath.apps.twitterclient.models.Tweet;

import java.util.List;

/**
 * Created by rhu on 10/23/13.
 */
public class MainActivity extends FragmentActivity implements TimelineFragment.OnDataUpdateListener {

	private int REQUEST_CODE = 123;
	private int TWEET_PER_PAGE = 25;

	TwitterClient client;

	boolean offlineMode = false;

	enum TabTypes {
		HOME, MENTIONS
	};

	TabTypes curTab;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpTabs();
		client = RestClientApp.getRestClient();

	}

	private void setUpTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		ActionBar.Tab tab1 = actionBar.newTab().setText("Home").setIcon(R.drawable.ic_launcher).setTag("HomelineFragment");
		tab1.setTabListener(new FragmentTabListener<TimelineFragment>(R.id.fl1, this, "first", TimelineFragment.class));
		actionBar.addTab(tab1);
		curTab = TabTypes.HOME;
		actionBar.selectTab(tab1);

		ActionBar.Tab tab2 = actionBar.newTab().setText("Mentions").setIcon(R.drawable.ic_launcher).setTag("OtherFragment");
		tab2.setTabListener(new FragmentTabListener<TimelineFragment>(R.id.fl1, this, "first", TimelineFragment.class));
		actionBar.addTab(tab2);

	}

	@Override
	public void refresh() {

		if (curTab == TabTypes.HOME) {
			TimelineFragment fragment = (TimelineFragment) getSupportFragmentManager().findFragmentById(R.id.fl1);
			if (fragment == null) {
				Log.d("debug", "fragment not there");
			} else {
				TweetJsonHttpResponseHandler handler = fragment.createRefreshResponseHandler();
				client.getHomeTimeline(0, fragment.getMaxId(), TWEET_PER_PAGE, handler);
			}
		}
	}

	@Override
	public void loadMore() {

		if (offlineMode) {
			Log.d("debug", "Offline mode");
			return;
		}

		if (curTab == TabTypes.HOME) {
			TimelineFragment fragment = (TimelineFragment) getSupportFragmentManager().findFragmentById(R.id.fl1);
			if (fragment == null) {
				Log.d("debug", "fragment not there");
			} else {
				TweetJsonHttpResponseHandler handler = fragment.createLoadMoreResponseHandler();
				client.getHomeTimeline(fragment.getMinId(), 0, TWEET_PER_PAGE, handler);
			}
		}
	}

	@Override
	public void onError(Throwable e, String responseBody) {
		Toast.makeText(getBaseContext(), "Twitter error: " + responseBody, Toast.LENGTH_SHORT).show();
	}

	/*
	public void offlineMode(MenuItem item) {
		if (item.isChecked() == false) {
			item.setChecked(true);
			offlineMode = true;
			tweetAdapter.clear();
			this.initMinMax();
			List<Tweet> tweets = new Select().from(Tweet.class).orderBy("created_at DESC").execute();
			if (tweets.size() > 0) {
				tweetAdapter.addAll(tweets);
			}
		} else {
			offlineMode = false;
			item.setChecked(false);
			tweetAdapter.clear();
			this.initMinMax();
			listener.loadMore();
		}
	}*/

	public void composeTweet(MenuItem item) {
		Intent i = new Intent(getBaseContext(), ComposeActivity.class);
		startActivityForResult(i, REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
//			this.refreshTweets();
		}
	}

	public void logout(MenuItem item) {
		TwitterClient client = RestClientApp.getRestClient();
		client.clearAccessToken();
		finish();
	}

}