package com.codepath.apps.twitterclient.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TwitterDao {
    // Record finders
    @Query("SELECT * FROM Tweet WHERE post_id = :tweetId LIMIT 1")
    Tweet byTweetId(Long tweetId);

    @Query("SELECT * FROM Tweet ORDER BY created_at")
    List<Tweet> getRecentTweets();

//    @Query("SELECT * FROM User WHERE twitter_id = :twitterId")
//    Tweet getUserById(int twitterId);

    // https://stackoverflow.com/questions/44667160/android-room-insert-relation-entities-using-room
    // https://issuetracker.google.com/issues/62848977
    // https://developer.android.com/training/data-storage/room/referencing-data#understand-no-object-references
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTweet(Tweet... tweets);
}
