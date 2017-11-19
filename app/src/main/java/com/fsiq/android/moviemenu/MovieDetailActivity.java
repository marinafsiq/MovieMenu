package com.fsiq.android.moviemenu;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Log.d("MovieDetailedActivity", "onCreate do novo intent");
        TextView title = (TextView) findViewById(R.id.title_tv);
        ImageView poster = (ImageView) findViewById(R.id.poster_detail_tv);
        TextView userRating = (TextView) findViewById(R.id.user_rating_tv);
        TextView releaseDate = (TextView) findViewById(R.id.release_date_tv);
        TextView synopsis = (TextView) findViewById(R.id.synopsis_data);


        // TODO (2) Display the weather forecast that was passed from MainActivity
        Intent intent = getIntent();
        if(intent.hasExtra(Intent.EXTRA_TEXT)){
            String textSent = intent.getStringExtra(Intent.EXTRA_TEXT);
            String[] textSentArr = textSent.split("\\|");
            Log.d("MovieDetailedActivity", "textSent: " + textSent);
            Log.d("MovieDetailedActivity", "array size: " + textSentArr.length);
            title.setText(textSentArr[0]);
            Picasso.with(this).load("http://image.tmdb.org/t/p/w780/" + textSentArr[1]).into(poster);
            synopsis.setText(textSentArr[2]);
            userRating.setText(textSentArr[3]);
            releaseDate.setText(textSentArr[4]);
        }
    }
}
