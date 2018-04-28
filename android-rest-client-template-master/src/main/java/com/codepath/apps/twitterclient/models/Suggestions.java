package com.codepath.apps.twitterclient.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.SearchManager;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by rhu on 10/2/16.
 */

public class Suggestions {

    ArrayList<String> topics;

    ArrayList<String> users;

    private static final String[] sAutocompleteColNames = new String[] {
            BaseColumns._ID,                         // necessary for adapter
            SearchManager.SUGGEST_COLUMN_TEXT_1      // the full search term
    };

    public ArrayList<String> getTopics() {
        return topics;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public MatrixCursor getCursor() {

        MatrixCursor cursor = new MatrixCursor(sAutocompleteColNames);

        ArrayList<String> topics = getTopics();
      //  Object[] row1 = new Object[] { 0, ""};
      //  cursor.addRow(row1);

        for (int index = 1; index < topics.size(); index++) {
            Object[] row = new Object[] { index, topics.get(index)};
            cursor.addRow(row);
        }
        return cursor;
    }

    public Suggestions() {
        topics = new ArrayList<>();
        users = new ArrayList<>();
    }

    public Suggestions(JSONObject object) {
        super();
        try {
            JSONArray jsonUsers = object.getJSONArray("users");
            JSONArray jsonTopics = object.getJSONArray("topics");
            topics = getTopics(jsonTopics);
            users = getUsers(jsonTopics);
        } catch (JSONException e) {
            Log.d("here", "here");
        }
    }

    public static ArrayList<String> getTopics(JSONArray jsonArray) throws JSONException {
        ArrayList<String> topics = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            String topic = ((JSONObject) jsonArray.get(i)).getString("topic");
            topics.add(topic);
        }
        return topics;
    }

    public static ArrayList<String> getUsers(JSONArray jsonArray) throws JSONException {
        ArrayList<String> users = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            String topic = ((JSONObject) jsonArray.get(i)).getString("screen_name");
            users.add(topic);
        }
        return users;
    }

}
