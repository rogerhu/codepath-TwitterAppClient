package com.codepath.apps.twitterclient.ui.fragments;

import android.util.Log;

import com.codepath.apps.twitterclient.network.TweetJsonHttpResponseHandler;

/**
 * Created by rhu on 10/29/13.
 */
public class UserTimelineFragment extends BaseTimelineFragment {

	String screenName;

	public UserTimelineFragment(String screenName) {
		this.screenName = screenName;
	}

	@Override
	public void loadMore(LoadType loadType) {

		TweetJsonHttpResponseHandler handler = createTweetHandler(loadType);

		if (loadType == LoadType.NEW_TWEETS) {
			Log.d("debug", "Looking for newer tweets");

			client.getUserTimeline(screenName, getNewerTweets(), 0, TWEET_PER_PAGE, handler);
		}
		else {
			Log.d("debug", "Looking for older tweets");
			client.getUserTimeline(screenName, 0, getOlderTweets(), TWEET_PER_PAGE, handler);

		}
	}
}

