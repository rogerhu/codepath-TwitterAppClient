package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;
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

    ArrayList<Tweet> tweets = new ArrayList<Tweet>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_stream);

        Intent i = new Intent();
        TwitterClient client = RestClientApp.getRestClient();
        client.getHomeTimeline(1, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONArray jsonTweets) {
                ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
                ListView items = (ListView) findViewById(R.id.listView);
                items.setAdapter(new TweetAdapter(getBaseContext(), tweets));

            }

            @Override
            public void onFailure(Throwable e, JSONObject errorResponse) {
                e.printStackTrace();
            }

        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }
}