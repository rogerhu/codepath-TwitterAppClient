package com.codepath.apps.twitterclient;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

/**
 * Created by rhu on 10/23/13.
 */
public class MainActivity extends FragmentActivity {

	private int REQUEST_CODE = 123;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpTabs();
	}
	private void setUpTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		ActionBar.Tab tab1 = actionBar.newTab().setText("Home").setIcon(R.drawable.ic_launcher).setTag("HomelineFragment");
		tab1.setTabListener(new FragmentTabListener<TimelineActivity>(R.id.fl1, this, "first", TimelineActivity.class));
		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		ActionBar.Tab tab2 = actionBar.newTab().setText("Mentions").setIcon(R.drawable.ic_launcher).setTag("OtherFragment");
		tab2.setTabListener(new FragmentTabListener<TimelineActivity>(R.id.fl1, this, "first", TimelineActivity.class));
		actionBar.addTab(tab2);

	}

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