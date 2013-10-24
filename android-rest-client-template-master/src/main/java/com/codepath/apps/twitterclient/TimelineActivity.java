package com.codepath.apps.twitterclient;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.activeandroid.query.Select;
import com.codepath.apps.twitterclient.handlers.TweetCallbackHandler;
import com.codepath.apps.twitterclient.handlers.TweetJsonHttpResponseHandler;
import com.codepath.apps.twitterclient.models.Tweet;

import java.util.ArrayList;
import java.util.List;

import eu.erikw.PullToRefreshListView;

/**
 * Created by rhu on 10/19/13.
 */
public class TimelineActivity extends Fragment {

	private int TWEET_PER_PAGE = 25;
	final int UNINITIALIZED = -1;

	long maxId;
	long minId;

	TweetAdapter tweetAdapter;
	TwitterClient client;
	PullToRefreshListView lvItems;
	boolean offlineMode = false;
/*	private OnNeedDataListener listener;

	public interface OnNeedDataListener {
		public void refresh();
		public void loadMore();
	};*/

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_twitter_stream, container, false);
		initMinMax();

		tweetAdapter = new TweetAdapter(getActivity().getBaseContext(), new ArrayList<Tweet>());

		client = RestClientApp.getRestClient();
		this.loadMore();
		lvItems = (PullToRefreshListView) v.findViewById(R.id.listView);
		lvItems.setAdapter(tweetAdapter);
		lvItems.setOnScrollListener(new EndlessScrollListener() {

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				TimelineActivity.this.loadMore();
			}
		});
		lvItems.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {

			@Override
			public void onRefresh() {
				TimelineActivity.this.refreshTweets();
			}
		});

		return v;
	}

	public void initMinMax() {
		maxId = UNINITIALIZED;
		minId = UNINITIALIZED;
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.twitter, menu);
	}

	public void updateMinMax(Tweet tweet) {

		Long postId = tweet.getPostId();

		if (TimelineActivity.this.maxId == UNINITIALIZED || postId > TimelineActivity.this.maxId) {
			this.maxId = postId;
		} else if (TimelineActivity.this.minId == UNINITIALIZED || postId < TimelineActivity.this.minId) {
			this.minId = postId;
		}
	}

	public void addTweet(Tweet tweet, boolean insert) {
		this.updateMinMax(tweet);

		if (insert) {
			tweetAdapter.insert(tweet, 0);
		} else {
			tweetAdapter.add(tweet);
		}
	}

	public TweetJsonHttpResponseHandler createTweetHandler(final boolean insert) {

		TweetJsonHttpResponseHandler handler = new TweetJsonHttpResponseHandler() {

			@Override
			protected void sendFailureMessage(Throwable e, String responseBody) {
				super.sendFailureMessage(e, responseBody);
				Toast.makeText(getActivity().getBaseContext(), "Twitter error: " + responseBody, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onPostExecute() {
				Log.d("debug", "Refresh complete");
				lvItems.onRefreshComplete();
			}
		};

		handler.addCallback(new TweetCallbackHandler() {
			@Override
			public void processItem(Tweet t) {
				TimelineActivity.this.addTweet(t, insert);
			}
		}
		);
		return handler;
	}

	public void refreshTweets() {
		TweetJsonHttpResponseHandler handler = createTweetHandler(true);
		client.getHomeTimeline(0, this.maxId, TWEET_PER_PAGE, handler);
	}
	public void loadMore() {

		if (offlineMode) {
			Log.d("debug", "Offline mode");
			return;
		}

		TweetJsonHttpResponseHandler handler = createTweetHandler(false);
		client.getHomeTimeline(this.minId, 0, TWEET_PER_PAGE, handler);
	}

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
			this.loadMore();
		}
	}
}