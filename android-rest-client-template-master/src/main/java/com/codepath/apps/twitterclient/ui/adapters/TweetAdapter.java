package com.codepath.apps.twitterclient.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.codepath.apps.twitterclient.ui.activities.ProfileActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by rhu on 10/19/13.
 */
public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    List<Tweet> mTweets;

    public TweetAdapter(List<Tweet> tweets) {
        mTweets = tweets;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView userName;
        public TextView timestamp;
        public TextView bodyView;

        public View view;

        public ViewHolder(View itemView) {
            super(itemView);

            view = this.itemView;
            imageView = (ImageView) itemView.findViewById(R.id.userAvatar);
            userName = (TextView) itemView.findViewById(R.id.userName);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            bodyView = (TextView) itemView.findViewById(R.id.tweetText);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_tweet, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    @Override
    public void onBindViewHolder(TweetAdapter.ViewHolder viewHolder, int position) {
        Tweet tweet = mTweets.get(position);
        User user = tweet.getUser();

        ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), viewHolder.imageView);

        TextView fullName = viewHolder.userName;
        String formattedName = "<b>" + user.getFullName() + "</b>" + " <small><font color='#7777777'>@" +
                user.getTwitterHandle() + "</font></small>";

        viewHolder.view.setTag(R.id.TWEET_ID, tweet.getPostId());
        viewHolder.imageView.setTag(R.id.TWITTER_HANDLE, tweet.getUser().getTwitterHandle());

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context ctx = view.getContext();
                String twitterHandle = (String) view.getTag(R.id.TWITTER_HANDLE);
                Intent i = new Intent(ctx, ProfileActivity.class);
                i.putExtra("screenName", twitterHandle);
                Log.d("debug", "Launching with screenName " + twitterHandle);
                ctx.startActivity(i);
            }
        });

        fullName.setText(Html.fromHtml(formattedName));

        viewHolder.timestamp.setText(tweet.getTimestamp());

        viewHolder.bodyView.setText(Html.fromHtml(tweet.getTweet()));
    }
}
