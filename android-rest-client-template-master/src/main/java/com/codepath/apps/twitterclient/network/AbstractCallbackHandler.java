package com.codepath.apps.twitterclient.network;

/**
 * Created by rhu on 10/20/13.
 */
public interface AbstractCallbackHandler<T> {
    public abstract void processItem(T item);
}
