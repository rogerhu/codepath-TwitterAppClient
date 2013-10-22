package com.codepath.apps.twitterclient.models;

import com.activeandroid.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rhu on 10/21/13.
 */
public abstract interface BaseModel {
	public abstract void parseJSON(JSONObject jsonObject);
}
