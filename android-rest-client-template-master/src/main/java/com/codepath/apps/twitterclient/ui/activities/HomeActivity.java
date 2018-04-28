package com.codepath.apps.twitterclient.ui.activities;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.models.Suggestions;
import com.codepath.apps.twitterclient.network.RestClientApp;
import com.codepath.apps.twitterclient.network.TwitterClient;
import com.codepath.apps.twitterclient.ui.fragments.BaseTimelineFragment;
import com.codepath.apps.twitterclient.ui.fragments.HomeTimelineFragment;
import com.codepath.apps.twitterclient.ui.fragments.MentionsTimelineFragment;
import com.codepath.apps.twitterclient.ui.listeners.FragmentTabListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONObject;

import android.app.SearchManager;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;


/**
 * Created by rhu on 10/23/13.
 */
public class HomeActivity extends AppCompatActivity implements BaseTimelineFragment.OnDataUpdateListener {

	private int REQUEST_CODE = 123;

	TwitterClient client;

	enum TabTypes {
		HOME, MENTIONS
	};

	TabTypes curTab;
	ActionBar actionBar;
	CursorAdapter mCursorAdapter;
	Boolean isPending = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		setUpTabs();
		client = RestClientApp.getRestClient();

	}

	private void setUpTabs() {
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		ActionBar.Tab tab1 = actionBar.newTab().setIcon(R.drawable.home).setTag(TabTypes.HOME);
		tab1.setTabListener(new FragmentTabListener<HomeTimelineFragment>(R.id.fl1, this, "home", HomeTimelineFragment.class));
		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		ActionBar.Tab tab2 = actionBar.newTab().setIcon(R.drawable.bubble).setTag(TabTypes.MENTIONS);
		tab2.setTabListener(new FragmentTabListener<MentionsTimelineFragment>(R.id.fl1, this, "mentions", MentionsTimelineFragment.class));
		actionBar.addTab(tab2);

		getCurTab();
	}

	public void getCurTab() {
		ActionBar.Tab selected = actionBar.getSelectedTab();
		curTab = (TabTypes) selected.getTag();

	}

	@Override
	public void onError(Throwable e, final String responseBody) {
		Log.d("debug", "onError here " + e.toString());

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(HomeActivity.this, "Twitter error: " + responseBody, Toast.LENGTH_LONG).show();

			}
		});
	}

	public void openProfile(MenuItem item) {
		Intent i = new Intent(getBaseContext(), ProfileActivity.class);
		startActivity(i);
	}
	public void composeTweet(MenuItem item) {
		Intent i = new Intent(getBaseContext(), ComposeActivity.class);
		startActivityForResult(i, REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
		}
	}

	public void logout(MenuItem item) {
		TwitterClient client = RestClientApp.getRestClient();
		client.clearAccessToken();
		finish();
	}

	public void composeTo(Long tweetId) {
		Intent i = new Intent(getBaseContext(), ComposeActivity.class);
		i.putExtra("tweetId", tweetId);
		startActivityForResult(i, REQUEST_CODE);
	}

		// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.twitter, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);

		// http://stackoverflow.com/questions/37657161/how-to-implement-search-view-autocomplete-in-actionbar-using-http-request

		final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		mCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, new String[] { SearchManager.SUGGEST_COLUMN_TEXT_1 }, new int[] { android.R.id.text1});
		searchView.setSuggestionsAdapter(mCursorAdapter);

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				// perform query here

				// workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
				// see https://code.google.com/p/android/issues/detail?id=24599
				searchView.clearFocus();
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {

				if (isPending || TextUtils.isEmpty(newText)) {
					return false;
				}

				client.getTypeahead(newText, new JsonHttpResponseHandler() {

					@Override
					public void onPreProcessResponse(ResponseHandlerInterface instance,
							HttpResponse response) {
						isPending = true;
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {

						Suggestions suggestions = new Suggestions(response);
						MatrixCursor cursor = suggestions.getCursor();
						searchView.getSuggestionsAdapter().changeCursor(cursor);
						isPending = false;
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString,
							Throwable throwable) {
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable,
							JSONObject errorResponse) {
						Log.d("failure", throwable.toString());
					}
				});
				return true;
			}

		});


		return true;
	}

}