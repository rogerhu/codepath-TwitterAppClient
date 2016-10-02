package com.codepath.apps.twitterclient.models;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.property.Property;
import com.raizlabs.android.dbflow.structure.Model;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by rhu on 10/21/13.
 */
public class ModelCreator<T extends Model & MyBaseModel> {

    public static <T extends Model & MyBaseModel> T createOrUpdate(JSONObject jsonObject, Long uniqueId, Property<Long> uniqueColumn, Class<T> classType) throws InstantiationException, IllegalAccessException{
        List<T> items = new Select().from(classType).where(uniqueColumn.eq(uniqueId)).queryList();

        T entry;
        if (items.size() > 0) {
            entry = (T) items.get(0);
            //Log.d("debug", "Found existing entry...updating " + entry.getId());
        }
        else {
            entry = classType.newInstance();
        }

        entry.parseJSON(jsonObject);
        entry.save();

        return entry;
    }


}
