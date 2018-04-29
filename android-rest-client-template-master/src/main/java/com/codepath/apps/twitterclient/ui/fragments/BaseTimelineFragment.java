package com.codepath.apps.twitterclient.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.RestClientApp;
import com.codepath.apps.twitterclient.models.TwitterDao;
import com.codepath.apps.twitterclient.network.TweetCallbackHandler;
import com.codepath.apps.twitterclient.network.TweetJsonHttpResponseHandler;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.network.TwitterClient;
import com.codepath.apps.twitterclient.ui.listeners.EndlessScrollListener;
import com.codepath.apps.twitterclient.ui.adapters.TweetAdapter;

import java.util.ArrayList;

import eu.erikw.PullToRefreshListView;

/**
 * Created by rhu on 10/19/13.
 */
public abstract class BaseTimelineFragment extends Fragment {

	final int UNINITIALIZED = -1;
	final int TWEET_PER_PAGE = 25;

	long maxId;
	long minId;

	public enum LoadType {
		NEW_TWEETS,
		OLDER_TWEETS
	}

	public TweetAdapter tweetAdapter;
	protected TwitterClient client;

	PullToRefreshListView lvItems;
	protected OnDataUpdateListener listener;

	public interface OnDataUpdateListener {
		public void composeTo(Long tweetId);
		public void onError(Throwable e, String response);
	};

	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (activity instanceof OnDataUpdateListener) {
			listener = (OnDataUpdateListener) activity;
		} else {
			throw new ClassCastException(activity.toString() + " must implement OnDataUpdateListener interface");
		}
	}

	public void onDetach() {
		super.onDetach();
		listener = null;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_twitter_stream, container, false);
		initMinMax();

		lvItems = (PullToRefreshListView) v.findViewById(R.id.listView);

		lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Long tweetId = (Long) view.getTag(R.id.TWEET_ID);
				listener.composeTo(tweetId);
			}
		});

		client = RestClientApp.getRestClient();

		return v;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		tweetAdapter = new TweetAdapter(getActivity().getBaseContext(), new ArrayList<Tweet>());

		lvItems.setAdapter(tweetAdapter);
		lvItems.setOnScrollListener(new EndlessScrollListener() {

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				Log.d("debug", "scroll listener");
				loadMore(LoadType.OLDER_TWEETS);
			}
		});
		lvItems.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {

			@Override
			public void onRefresh() {
				loadMore(LoadType.NEW_TWEETS);
			}
		});
	}

	public void initMinMax() {
		maxId = UNINITIALIZED;
		minId = UNINITIALIZED;
	}

	public void updateMinMax(Tweet tweet) {

		Long postId = tweet.getPostId();

		if (BaseTimelineFragment.this.maxId == UNINITIALIZED || postId > BaseTimelineFragment.this.maxId) {
			this.maxId = postId;
		} else if (BaseTimelineFragment.this.minId == UNINITIALIZED || postId < BaseTimelineFragment.this.minId) {
			this.minId = postId;
		}
	}

	public void addTweet(Tweet tweet, LoadType loadType) {
		this.updateMinMax(tweet);

		if (loadType == LoadType.NEW_TWEETS) {
			tweetAdapter.insert(tweet, 0);
		} else {
			tweetAdapter.add(tweet);
		}
	}

	public TweetJsonHttpResponseHandler createTweetHandler(final LoadType loadType) {

		TweetJsonHttpResponseHandler handler = new TweetJsonHttpResponseHandler() {

			@Override
			protected void sendFailureMessage(Throwable e, String responseBody) {
				super.sendFailureMessage(e, responseBody);
				listener.onError(e, responseBody);
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
				BaseTimelineFragment.this.addTweet(t, loadType);
			}
		}
		);
		handler.addCallback(new TweetCallbackHandler() {
			@Override
			public void processItem(Tweet item) {
				TwitterDao twitterDao = ((RestClientApp) getContext().getApplicationContext()).getMyDatabase().twitterDao();
				twitterDao.insertTweet(item);
			}
		});
		return handler;
	}

	public abstract void loadMore(LoadType loadType);

	public long getNewerTweets() {
		return maxId;
	}

	public long getOlderTweets() {
		return minId;
	}

}