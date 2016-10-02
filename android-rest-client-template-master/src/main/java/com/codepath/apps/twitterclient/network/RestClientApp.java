package com.codepath.apps.twitterclient.network;

import com.codepath.apps.twitterclient.models.Tweet;
import com.facebook.stetho.Stetho;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Delete;

import android.app.Application;
import android.content.Context;

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

    @Override
    public void onCreate() {
        super.onCreate();

        FlowManager.init(new FlowConfig.Builder(this).build());
        Stetho.initializeWithDefaults(this);
        Delete.table(Tweet.class);
        FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);

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
}