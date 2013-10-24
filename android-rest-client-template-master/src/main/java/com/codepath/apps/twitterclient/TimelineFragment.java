package com.codepath.apps.twitterclient;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.twitterclient.handlers.TweetCallbackHandler;
import com.codepath.apps.twitterclient.handlers.TweetJsonHttpResponseHandler;
import com.codepath.apps.twitterclient.models.Tweet;

import java.util.ArrayList;

import eu.erikw.PullToRefreshListView;

/**
 * Created by rhu on 10/19/13.
 */
public class TimelineFragment extends Fragment {

	final int UNINITIALIZED = -1;

	long maxId;
	long minId;

	TweetAdapter tweetAdapter;

	PullToRefreshListView lvItems;
	private OnDataUpdateListener listener;

	public interface OnDataUpdateListener {
		public void refresh();
		public void loadMore();
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

		tweetAdapter = new TweetAdapter(getActivity().getBaseContext(), new ArrayList<Tweet>());

		listener.loadMore();
		lvItems = (PullToRefreshListView) v.findViewById(R.id.listView);
		lvItems.setAdapter(tweetAdapter);
		lvItems.setOnScrollListener(new EndlessScrollListener() {

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				listener.loadMore();
			}
		});
		lvItems.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {

			@Override
			public void onRefresh() {
				listener.refresh();
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

		if (TimelineFragment.this.maxId == UNINITIALIZED || postId > TimelineFragment.this.maxId) {
			this.maxId = postId;
		} else if (TimelineFragment.this.minId == UNINITIALIZED || postId < TimelineFragment.this.minId) {
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
				TimelineFragment.this.addTweet(t, insert);
			}
		}
		);
		return handler;
	}

	public TweetJsonHttpResponseHandler createRefreshResponseHandler() {
		return createTweetHandler(true);
	}

	public TweetJsonHttpResponseHandler createLoadMoreResponseHandler() {
		return createTweetHandler(false);
	}

	public long getMinId() {
		return minId;
	}

	public long getMaxId() {
		return maxId;
	}

}