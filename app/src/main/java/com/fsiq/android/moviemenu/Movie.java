package com.fsiq.android.moviemenu;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Marina on 03/09/17.
 */

public class Movie implements Parcelable{
    private String id;
    private String title;
    private String poster_path;
    private Float vote_average;
    private String overview;
    private String release_date;

    public Movie (String pid, String ptitle, String pposter_patch, Float pvote_average, String poverview, String prelease_date){
        id = pid;
        title = ptitle;
        poster_path = pposter_patch;
        vote_average = pvote_average;
        overview = poverview;
        release_date = prelease_date;
    }

    public String getId(){
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public Float getVote_average() {
        return vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    private Movie (Parcel in){
        id = in.readString();
        title = in.readString();
        poster_path = in.readString();
        vote_average = in.readFloat();
        overview = in.readString();
        release_date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(poster_path);
        dest.writeFloat(vote_average);
        dest.writeString(overview);
        dest.writeString(release_date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }

    };
}
