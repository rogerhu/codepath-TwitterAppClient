package com.codepath.apps.twitterclient.network;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "BbzrLsQgzSXlK4xvVorHwA";       // Change this
    public static final String REST_CONSUMER_SECRET = "O2R7JEgTZ4DnxohnkfyU284Dk3q1VCmNzHKrlPQyUO4"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://cprest"; // Change this (here and in manifest)

	public final String BLANK = "";

    // Defines the handler events for the OAuth flow
    public static interface OAuthAccessHandler {
        public void onLoginSuccess();
        public void onLoginFailure(Exception e);
    }

    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

	public void getSelfProfile(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");

		client.get(apiUrl, handler);
	}

	public void getUserProfile(String screenName, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("users/show.json");
		RequestParams params = new RequestParams();

        params.put("screen_name", screenName);

		client.get(apiUrl, params, handler);
	}

	public void getSelfTimeline(long newestTweet, long oldestTweet, int count, AsyncHttpResponseHandler handler) {
		getUserTimeline(BLANK, newestTweet, oldestTweet, count, handler);
	}

	public void getUserTimeline(String screenName, long newestTweet, long oldestTweet, int count, AsyncHttpResponseHandler handler) {

		String apiUrl = getApiUrl("statuses/user_timeline.json");

		RequestParams params = new RequestParams();

		if (!screenName.equals(BLANK)) {
			params.put("screen_name", screenName);
		}

		getTimeline(apiUrl, params, newestTweet, oldestTweet, count, handler);
	}

	public void getMentionsTimeline(long newestTweet, long oldestTweet, int count, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");

		RequestParams params = new RequestParams();
		getTimeline(apiUrl, params, newestTweet, oldestTweet, count, handler);
	}

	public void getHomeTimeline(long newestTweet, long oldestTweet, int count, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");

		RequestParams params = new RequestParams();

		getTimeline(apiUrl, params, newestTweet, oldestTweet, count, handler);
	}

	public void getTimeline(String apiUrl, RequestParams params, long newestTweet, long oldestTweet, int count, AsyncHttpResponseHandler handler) {

        if (oldestTweet > 0) {
            Log.d("debug", "Requesting max_id=" + oldestTweet);
            params.put("max_id", String.valueOf(oldestTweet-1));
        }

        if (newestTweet > 0) {
            Log.d("debug", "Requesting since_id=" + newestTweet);
            params.put("since_id", String.valueOf(newestTweet));
        }

        params.put("count", String.valueOf(count));
        client.get(apiUrl, params, handler);
    }

    public void postTweet(String status, Long parentTweet, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");

         RequestParams params = new RequestParams();
         params.put("status", status);

	     if (parentTweet != null) {
		     params.put("in_reply_to_status_id", String.valueOf(parentTweet));
	     }

         client.post(apiUrl, params, handler);
    }

    public void getUserSettings(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/settings.json");

        client.get(apiUrl, handler);
    }

    public void getUserByScreenName(String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("/users/show.json");

        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);

        client.get(apiUrl, params, handler);

    }
    /* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
     * 2. Define the parameters to pass to the request (query or body)
     *    i.e RequestParams params = new RequestParams("foo", "bar");
     * 3. Define the request method and make a call to the client
     *    i.e client.get(apiUrl, params, handler);
     *    i.e client.post(apiUrl, params, handler);
     */
}