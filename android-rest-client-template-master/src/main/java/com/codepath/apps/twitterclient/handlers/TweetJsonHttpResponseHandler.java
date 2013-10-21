package com.codepath.apps.twitterclient.handlers;

import com.codepath.apps.twitterclient.TweetAdapter;
import com.codepath.apps.twitterclient.handlers.TweetCallbackHandler;
import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rhu on 10/20/13.
 */

public class TweetJsonHttpResponseHandler extends JsonHttpResponseHandler {

    private TweetAdapter adapter;
    private boolean insert;

    private List<TweetCallbackHandler> callbacks = new ArrayList<TweetCallbackHandler>();

    public TweetJsonHttpResponseHandler() {
        super();
    }

    public TweetJsonHttpResponseHandler(List<TweetCallbackHandler> callbacks) {
        super();
        this.callbacks.addAll(callbacks);
    }

    public void addCallback(TweetCallbackHandler callback) {
        this.callbacks.add(callback);
    }

    @Override
    public void onSuccess(JSONArray jsonTweets) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        tweets = Tweet.fromJson(jsonTweets);

        for (int i = 0; i < tweets.size(); i++) {
            Tweet tweet = tweets.get(i);

            for (int j = 0; j < callbacks.size(); j++) {
                callbacks.get(j).processItem(tweet);
            }
        }
    }

    @Override
    public void onFailure(Throwable e, String errorResponse) {
        e.printStackTrace();
    }


}
