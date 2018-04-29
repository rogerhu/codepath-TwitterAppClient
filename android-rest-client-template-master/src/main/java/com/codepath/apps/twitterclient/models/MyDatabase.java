package com.codepath.apps.twitterclient.models;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities={Tweet.class}, version=1)
@TypeConverters({DateTypeConverter.class})
public abstract class MyDatabase extends RoomDatabase {

    public abstract TwitterDao twitterDao();

    public static final String NAME = "MyDataBase";

}
