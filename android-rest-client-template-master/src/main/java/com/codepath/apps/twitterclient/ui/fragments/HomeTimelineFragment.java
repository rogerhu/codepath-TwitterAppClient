package com.codepath.apps.twitterclient.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.RestClientApp;
import com.codepath.apps.twitterclient.models.MyDatabase;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.network.TweetJsonHttpResponseHandler;

import java.util.List;

/**
 * Created by rhu on 10/29/13.
 */
public class HomeTimelineFragment extends BaseTimelineFragment {

	public boolean offlineMode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.offline_menu, menu);
	}

	@Override
	public void loadMore(LoadType loadType) {

		TweetJsonHttpResponseHandler handler = createTweetHandler(loadType);

		if (loadType == BaseTimelineFragment.LoadType.NEW_TWEETS) {
			Log.d("debug", "Looking for newer tweets");

			// public void getHomeTimeline(long newestTweet, long oldestTweet, int count, AsyncHttpResponseHandler handler) {

			client.getHomeTimeline(getNewerTweets(), 0, TWEET_PER_PAGE, handler);
		} else {
			Log.d("debug", "Looking for older tweets");
			client.getHomeTimeline(0, getOlderTweets(), TWEET_PER_PAGE, handler);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.offline_mode:

				if (!item.isChecked()) {
					item.setChecked(true);

					offlineMode = true;

					tweets.clear();
					initMinMax();
					MyDatabase myDatabase = ((RestClientApp) getContext().getApplicationContext()).getMyDatabase();
					List<Tweet> dbTweets = myDatabase.twitterDao().getRecentTweets();
					if (dbTweets.size() > 0) {
						tweets.addAll(dbTweets);
					}
					tweetAdapter.notifyDataSetChanged();

				} else {
					offlineMode = false;
					item.setChecked(false);
					tweets.clear();
					tweetAdapter.notifyDataSetChanged();

					initMinMax();
					loadMore(LoadType.OLDER_TWEETS);
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}

