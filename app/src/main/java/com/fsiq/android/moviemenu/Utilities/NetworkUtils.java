package com.fsiq.android.moviemenu.Utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;

/**
 * Created by Marina on 02/09/17.
 */

public final class NetworkUtils {

    public static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    public static final String API_KEY_VALUE = "";
    public static final String API_KEY_QUERY_STR = "api_key";
    public static final String SORT_BY_QUERY_STR = "sort_by";
    public static final String NUM_PAGE = "page";

    public static URL buildUrl(String sort_by, int numPage){
        Log.d("NetworkUtils", "BuildUrl - String sort_by received = "+ sort_by);
        if(sort_by == null || sort_by.trim().isEmpty()) sort_by = "popularity.desc";
        Log.d("NetworkUtils", "BuildUrl - String sort_by received = "+ sort_by);
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_QUERY_STR, API_KEY_VALUE)
                .appendQueryParameter(SORT_BY_QUERY_STR, sort_by)
                .appendQueryParameter(NUM_PAGE, numPage+"").build();
        URL url = null;
        try{
            url = new URL(uri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        Log.d("NetworkUtils", "buildUrl - url: "+ url);
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        Log.d("NetworkUtils", "buildUrl - urlConnection: "+ urlConnection.toString());
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        }
        finally {
            urlConnection.disconnect();
        }
    }
}
