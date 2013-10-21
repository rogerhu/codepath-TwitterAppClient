package com.codepath.apps.twitterclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.twitterclient.handlers.TweetCallbackHandler;
import com.codepath.apps.twitterclient.handlers.TweetJsonHttpResponseHandler;
import com.codepath.apps.twitterclient.models.Tweet;

import java.util.ArrayList;

import eu.erikw.PullToRefreshListView;

/**
 * Created by rhu on 10/19/13.
 */
public class TimelineActivity extends Activity {

    private int TWEET_PER_PAGE = 25;
    private int REQUEST_CODE = 123;

    long maxId = 0;
    long minId = -1;

    TweetAdapter tweetAdapter;
    TwitterClient client;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_stream);

        tweetAdapter = new TweetAdapter(getBaseContext(), new ArrayList<Tweet>());

        client = RestClientApp.getRestClient();
        this.loadMore();

/*        ListView lvItems = (ListView) findViewById(R.id.listView);
        lvItems.setAdapter(tweetAdapter);

        lvItems.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                TimelineActivity.this.loadMore();
            }
        });
*/

       PullToRefreshListView lvItems = (PullToRefreshListView) findViewById(R.id.listView);
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
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.twitter, menu);
        return true;
    }

    public void composeTweet(MenuItem item) {
        Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    }

    public void updateMinMax(Tweet tweet) {

        Long postId = tweet.getPostId();

        if (postId > TimelineActivity.this.maxId) {
            this.maxId = postId;
        }

        else if (TimelineActivity.this.minId == -1 || postId < TimelineActivity.this.minId) {
            this.minId = postId;
        }
    }

    public void addTweet(Tweet tweet, boolean insert) {
        this.updateMinMax(tweet);

        if (insert) {
            tweetAdapter.insert(tweet, 0);
        }
        else {
            tweetAdapter.add(tweet);
        }
    }

    public void refreshTweets() {
        TweetJsonHttpResponseHandler handler = new TweetJsonHttpResponseHandler();

        handler.addCallback(new TweetCallbackHandler() {
                    @Override
                    public void processItem(Tweet t) {
                        TimelineActivity.this.addTweet(t, true);
                    }
                }
        );

        client.getHomeTimeline(0, this.maxId, TWEET_PER_PAGE, handler);
    }

    public void loadMore() {

        TweetJsonHttpResponseHandler handler = new TweetJsonHttpResponseHandler();

        handler.addCallback(new TweetCallbackHandler() {
            @Override
            public void processItem(Tweet t) {
                TimelineActivity.this.addTweet(t, false);
            }
        });

        client.getHomeTimeline(this.minId, 0, TWEET_PER_PAGE, handler);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            this.refreshTweets();
        }
    }

    public void logout(MenuItem item) {
        TwitterClient client = RestClientApp.getRestClient();
        client.clearAccessToken();
        finish();
    }
}