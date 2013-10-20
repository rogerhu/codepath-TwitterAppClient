package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.utils.L;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rhu on 10/19/13.
 */
public class TimelineActivity extends Activity {

    private int TWEET_PER_PAGE = 25;
    private int REQUEST_CODE = 123;
    private long maxId = 0;
    private long minId = -1;

    TweetAdapter tweetAdapter;
    TwitterClient client;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_stream);

        tweetAdapter = new TweetAdapter(getBaseContext(), new ArrayList<Tweet>());
        ListView lvItems = (ListView) findViewById(R.id.listView);
        lvItems.setAdapter(tweetAdapter);
        lvItems.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                TimelineActivity.this.loadMore();
            }
        });
        client = RestClientApp.getRestClient();
        this.loadMore();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.twitter, menu);
        return true;
    }

    public void composeTweet(MenuItem item) {
        Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    };

    public void loadMore() {

        client.getHomeTimeline(this.minId, this.maxId, TWEET_PER_PAGE, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONArray jsonTweets) {
                ArrayList<Tweet> tweets = new ArrayList<Tweet>();
                tweets = Tweet.fromJson(jsonTweets);

                for (int i = 0; i < tweets.size(); i++) {
                    Tweet tweet = tweets.get(i);
                    Long postId = tweet.getPostId();

                    if (postId > TimelineActivity.this.maxId) {
                        TimelineActivity.this.maxId = postId;
                    }

                    else if (TimelineActivity.this.minId == -1 || postId < TimelineActivity.this.minId) {
                        TimelineActivity.this.minId = postId;
                    }

                    tweetAdapter.add(tweet);
                }
                Log.d("debug", "Total items:" + tweetAdapter.getCount());
            }

            @Override
            public void onFailure(Throwable e, JSONObject errorResponse) {
                e.printStackTrace();
            }

        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String text = data.getStringExtra("text");

            User u = new User();
            u.setFullName("test");

            Tweet tweet = new Tweet();
            tweet.setText(text);
            tweet.setPostId(Long.valueOf("123"));
            tweet.setCreatedAt(System.currentTimeMillis());
            u.save();
            tweet.save();

        }
    }

    public void logout(MenuItem item) {
        TwitterClient client = RestClientApp.getRestClient();
        client.clearAccessToken();
        finish();
    }
}