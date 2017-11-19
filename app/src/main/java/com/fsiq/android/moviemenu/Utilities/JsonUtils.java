package com.fsiq.android.moviemenu.Utilities;

import android.util.Log;

import com.fsiq.android.moviemenu.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marina on 03/09/17.
 */

public final class JsonUtils {

    public static ArrayList<Movie> getFilmListFromJson(String jsonStr) throws JSONException{
        Log.d("JsonUtils", "starting getFilmListFromJson");
        final String OWM_MESSAGE_CODE = "cod";
        final String RESULTS = "results";
        final String ID = "id";
        final String TITLE = "title";
        final String POSTER_PATH = "poster_path";
        final String SYNOPSIS = "overview";
        final String USER_RATING = "vote_average";
        final String RELEASE_DATE = "release_date";

        JSONObject jsonObj = new JSONObject(jsonStr);

        /* Is there an error? */
        if (jsonObj.has(OWM_MESSAGE_CODE)) {
            int errorCode = jsonObj.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }



        JSONArray jsonArr = jsonObj.getJSONArray(RESULTS);
        ArrayList<Movie> moviesArray = new ArrayList<>();

        for (int i=0; i<jsonArr.length(); i++){
            JSONObject movieObj = jsonArr.getJSONObject(i);
            String id = movieObj.getString(ID);
            String user_rating = movieObj.getString(USER_RATING);
            Float num = Float.valueOf(user_rating);
            String title = movieObj.getString(TITLE);
            String posterPath = movieObj.getString(POSTER_PATH);
            String synopsis = movieObj.getString(SYNOPSIS);
            String release_date = movieObj.getString(RELEASE_DATE);
            //Log.d("JsonUtils", "Parser: id=" + id + ", user_rating=" + num + ", title=" + title +
              //      ", poster-path=" + posterPath + ", synopsis=" + synopsis + ", release_date=" + release_date);
            moviesArray.add(new Movie(id, title, posterPath, num, synopsis, release_date));
        }




        return moviesArray;
    }

}
