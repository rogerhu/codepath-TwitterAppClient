package com.codepath.apps.twitterclient.models;

import org.json.JSONObject;

/**
 * Created by rhu on 10/21/13.
 */
public abstract interface MyBaseModel {
	public abstract void parseJSON(JSONObject jsonObject);
}
