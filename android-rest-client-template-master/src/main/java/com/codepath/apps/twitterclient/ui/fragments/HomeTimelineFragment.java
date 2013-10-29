package com.codepath.apps.twitterclient.ui.fragments;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.network.TweetJsonHttpResponseHandler;

import java.util.List;

/**
 * Created by rhu on 10/29/13.
 */
public class HomeTimelineFragment extends BaseTimelineFragment {

	public boolean offlineMode;

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.twitter, menu);
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

	public void offlineMode(MenuItem item) {

		if (!item.isChecked()) {
			item.setChecked(true);

			offlineMode = true;

			tweetAdapter.clear();
			initMinMax();
			List<Tweet> tweets = Tweet.getRecentItems();
			if (tweets.size() > 0) {
				tweetAdapter.addAll(tweets);
			}
		} else {
			offlineMode = false;
			item.setChecked(false);
			tweetAdapter.clear();
			initMinMax();
			loadMore(LoadType.OLDER_TWEETS);
		}
	}
}

