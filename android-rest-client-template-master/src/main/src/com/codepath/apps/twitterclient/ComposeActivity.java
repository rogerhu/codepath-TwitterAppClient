package com.codepath.apps.twitterclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

/**
 * Created by rhu on 10/19/13.
 */
public class ComposeActivity extends Activity {

    String text;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
    }

    public void cancelCompose(View v) {
        finish();
    }

    public void composeTweet(View v) {
        EditText tweet = (EditText) findViewById(R.id.editText);

        if (tweet.getText() == null) {
            Toast.makeText(getBaseContext(), "Blank tweet", Toast.LENGTH_SHORT).show();
            return;
        }

        text = tweet.getText().toString();

        TwitterClient client = RestClientApp.getRestClient();

        client.postTweet(text, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONObject response) {
                Toast.makeText(getBaseContext(), "Tweet posted!", Toast.LENGTH_SHORT).show();
                Intent data = new Intent();
                data.putExtra("response", response.toString());
                setResult(RESULT_OK, data);
                finish();
            }

            public void onFailure(Throwable e, JSONObject json) {
                e.printStackTrace();
            }
        });
    }

}