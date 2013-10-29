package com.codepath.apps.twitterclient.ui.fragments;

import android.util.Log;

import com.codepath.apps.twitterclient.network.TweetJsonHttpResponseHandler;

/**
 * Created by rhu on 10/29/13.
 */
public class MentionsTimelineFragment extends BaseTimelineFragment {

	public boolean offlineMode;

	@Override
	public void loadMore(LoadType loadType) {

		TweetJsonHttpResponseHandler handler = createTweetHandler(loadType);

		if (loadType == LoadType.NEW_TWEETS) {
			Log.d("debug", "Looking for newer tweets");

			client.getMentionsTimeline(getNewerTweets(), 0, TWEET_PER_PAGE, handler);
		}
		else {
			Log.d("debug", "Looking for older tweets");
			client.getMentionsTimeline(0, getOlderTweets(), TWEET_PER_PAGE, handler);
		}
	}
}

