package com.codepath.apps.twitterclient;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.codepath.apps.twitterclient.models.MyDatabase;
import com.codepath.apps.twitterclient.network.TwitterClient;
import com.facebook.stetho.Stetho;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 * 
 *     TwitterClient client = RestClientApp.getRestClient();
 *     // use client to send requests to API
 *     
 */
public class RestClientApp extends Application {

    private static Context context;

    MyDatabase myDatabase;

    @Override
    public void onCreate() {
        super.onCreate();

        // https://medium.com/@ajaysaini.official/building-database-with-room-persistence-library-ecf7d0b8f3e9
        myDatabase = Room.databaseBuilder(this, MyDatabase.class, MyDatabase.NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();
        Stetho.initializeWithDefaults(this);

        context = getApplicationContext();

        // Create global configuration and initialize ImageLoader with this configuration
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
        		cacheInMemory().cacheOnDisc().build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
            .defaultDisplayImageOptions(defaultOptions)
            .build();
        ImageLoader.getInstance().init(config);
    }
    
    public static TwitterClient getRestClient() {
    	return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, RestClientApp.context);
    }

    public MyDatabase getMyDatabase() {
        return myDatabase;
    }


}