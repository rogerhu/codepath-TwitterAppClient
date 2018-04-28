package com.codepath.apps.twitterclient.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rhu on 10/2/16.
 */

public class Topic {

    public static ArrayList<String> Topic(JSONArray jsonArray) throws JSONException {
        ArrayList<String> topics = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            String topic = ((JSONObject) jsonArray.get(i)).getString("topic");
            topics.add(topic);
        }
        return topics;
    }
}
