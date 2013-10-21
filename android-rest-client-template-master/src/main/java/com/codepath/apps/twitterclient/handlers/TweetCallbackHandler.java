package com.codepath.apps.twitterclient.handlers;

import com.codepath.apps.twitterclient.models.Tweet;

/**
 * Created by rhu on 10/20/13.
 */

public interface TweetCallbackHandler extends AbstractCallbackHandler<Tweet> {
    public abstract void processItem(Tweet item);
}

