package com.codepath.apps.twitterclient.network;

import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.ui.adapters.TweetAdapter;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

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
    public void onSuccess(int statusCode, Header[] headers, JSONArray jsonTweets) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        tweets = Tweet.fromJson(jsonTweets);

        for (int i = 0; i < tweets.size(); i++) {
            Tweet tweet = tweets.get(i);

            for (int j = 0; j < callbacks.size(); j++) {
                callbacks.get(j).processItem(tweet);
            }
        }
        this.onPostExecute();
    }

    public void onPostExecute() {

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString,
            Throwable e) {
        Log.d("debug", "Failed request" + responseString);
        e.printStackTrace();
    }

	protected void sendFailureMessage(Throwable e, String responseBody) {
		Log.d("debug", "sendFailureMessage: " + responseBody);
	}


}
