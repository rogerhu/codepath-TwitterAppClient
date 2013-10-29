package com.codepath.apps.twitterclient.ui;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by rhu on 10/19/13.
 */
public class TweetAdapter extends ArrayAdapter<Tweet> {

    public TweetAdapter(Context context, List<Tweet> tweets) {
        super(context, R.layout.item_tweet, tweets);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_tweet, null);
        }

        Tweet tweet = getItem(position);

        final ImageView imageView = (ImageView) convertView.findViewById(R.id.userAvatar);

        User user = tweet.getUser();

        ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), imageView);

        TextView fullName = (TextView) convertView.findViewById(R.id.userName);
        String formattedName = "<b>" + user.getFullName() + "</b>" + " <small><font color='#7777777'>@" +
                   user.getTwitterHandle() + "</font></small>";

	    convertView.setTag(R.id.TWEET_ID, tweet.getId());
//	    convertView.setTag(R.id.TWITTER_HANDLE, user.getTwitterHandle().toString());
//	    convertView.setTag(R.id.TWITTER_POST_ID, tweet.getPostId());

	    final View itemView = convertView;

	    imageView.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
			    Context ctx = view.getContext();
			    String twitterHandle = (String) itemView.getTag(R.id.TWITTER_HANDLE);
			    Intent i = new Intent(ctx, ProfileActivity.class);
			    i.putExtra("screenName", twitterHandle);
			    Log.d("debug", "Launching with screenName " + twitterHandle);
			    ctx.startActivity(i);
		    }
	    });

        fullName.setText(Html.fromHtml(formattedName));

        TextView timestamp = (TextView) convertView.findViewById(R.id.timestamp);
        timestamp.setText(tweet.getTimestamp());

        TextView bodyView = (TextView) convertView.findViewById(R.id.tweetText);
        bodyView.setText(Html.fromHtml(tweet.getTweet()));

        return convertView;
    }
}
