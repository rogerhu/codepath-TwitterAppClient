package com.codepath.apps.twitterclient.ui.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.network.RestClientApp;
import com.codepath.apps.twitterclient.network.TwitterClient;
import com.codepath.apps.twitterclient.ui.fragments.BaseTimelineFragment;
import com.codepath.apps.twitterclient.ui.fragments.HomeTimelineFragment;
import com.codepath.apps.twitterclient.ui.fragments.MentionsTimelineFragment;
import com.codepath.apps.twitterclient.ui.listeners.FragmentTabListener;


/**
 * Created by rhu on 10/23/13.
 */
public class HomeActivity extends FragmentActivity implements BaseTimelineFragment.OnDataUpdateListener {

	private int REQUEST_CODE = 123;

	TwitterClient client;

	enum TabTypes {
		HOME, MENTIONS
	};

	TabTypes curTab;
	BaseTimelineFragment curFragment;
	ActionBar actionBar;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		setUpTabs();
		client = RestClientApp.getRestClient();
	}

	private void setUpTabs() {
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab tab1 = actionBar.newTab().setText("Home").setIcon(R.drawable.home).setTag(TabTypes.HOME);
		tab1.setTabListener(new FragmentTabListener<HomeTimelineFragment>(R.id.fl1, this, "home", HomeTimelineFragment.class));
		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		Tab tab2 = actionBar.newTab().setText("Mentions").setIcon(R.drawable.ic_launcher).setTag(TabTypes.MENTIONS);
		tab2.setTabListener(new FragmentTabListener<MentionsTimelineFragment>(R.id.fl1, this, "mentions", MentionsTimelineFragment.class));
		actionBar.addTab(tab2);

		getCurTab();
	}

	public void getCurTab() {
		Tab selected = actionBar.getSelectedTab();
		curTab = (TabTypes) selected.getTag();

	}

	@Override
	public void onError(Throwable e, final String responseBody) {
		Log.d("debug", "onError here " + e.toString());

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(HomeActivity.this, "Twitter error: " + responseBody, Toast.LENGTH_LONG).show();

			}
		});
	}

	public void openProfile(MenuItem item) {
		Intent i = new Intent(getBaseContext(), ProfileActivity.class);
		startActivity(i);
	}
	public void composeTweet(MenuItem item) {
		Intent i = new Intent(getBaseContext(), ComposeActivity.class);
		startActivityForResult(i, REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
		}
	}

	public void logout(MenuItem item) {
		TwitterClient client = RestClientApp.getRestClient();
		client.clearAccessToken();
		finish();
	}

	public void composeTo(Long tweetId) {
		Intent i = new Intent(getBaseContext(), ComposeActivity.class);
		i.putExtra("tweetId", tweetId);
		startActivityForResult(i, REQUEST_CODE);
	}

		// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.twitter, menu);
		return true;
	}
}