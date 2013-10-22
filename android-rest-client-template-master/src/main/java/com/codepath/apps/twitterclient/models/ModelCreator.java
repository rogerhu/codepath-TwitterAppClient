package com.codepath.apps.twitterclient.models;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by rhu on 10/21/13.
 */
public class ModelCreator<T extends Model & BaseModel> {

    public static <T extends Model & BaseModel> T createOrUpdate(JSONObject jsonObject, String uniqueId, String uniqueColumn, Class<T> classType) throws InstantiationException, IllegalAccessException{
        List<T> items = new Select().from(classType).where(uniqueColumn + " = ?", uniqueId).execute();

        T entry;
        if (items.size() > 0) {
            entry = (T) items.get(0);
            Log.d("debug", "Found existing entry...updating " + entry.getId());
        }
        else {
            entry = classType.newInstance();
        }

        entry.parseJSON(jsonObject);
        entry.save();
        return entry;
    }


}
