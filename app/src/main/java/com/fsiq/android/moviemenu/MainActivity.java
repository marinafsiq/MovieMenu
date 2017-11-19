package com.fsiq.android.moviemenu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fsiq.android.moviemenu.Utilities.JsonUtils;
import com.fsiq.android.moviemenu.Utilities.NetworkUtils;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, MovieAdapter.MovieAdapterOnClickHandler{
    private final String ORDERBY_POPULARITY_DESC = "popularity.desc";
    private final String ORDERBY_VOTE_DESC = "vote_average.desc";

    RecyclerView mRecyclerView;
    MovieAdapter mMovieAdapter;
    MovieData mMovieData;

    private LinearLayout mErrorLinearLayout;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mErrorLinearLayout = (LinearLayout)findViewById(R.id.ll_error);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewElem);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);

        // Set the layoutManager on mRecyclerView
        mRecyclerView.setLayoutManager(layoutManager);

        // Use setHasFixedSize(true) on mRecyclerView to designate that all items in the list will have the same size
        mRecyclerView.setHasFixedSize(true);

        // set mForecastAdapter equal to a new ForecastAdapter
        mMovieAdapter = new MovieAdapter(getApplicationContext(), this);

        // TODO (42) Use mRecyclerView.setAdapter and pass in mForecastAdapter
        mRecyclerView.setAdapter(mMovieAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    int visibleItemCount = mRecyclerView.getLayoutManager().getChildCount();
                    int totalItemCount = mRecyclerView.getLayoutManager().getItemCount();
                    int pastVisiblesItems = ((GridLayoutManager)mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                    if(mMovieData.getStatus() != MovieData.Status.RUNNING) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount
                                && pastVisiblesItems >= 0 && totalItemCount >= 20) {
                            mMovieData = new MovieData();
                            mMovieData.execute("", "nextPage");
                        }
                    }

                }
            }
        });

        mMovieData = new MovieData();
        mMovieData.execute(ORDERBY_POPULARITY_DESC);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.order_options, R.layout.my_spinner_layout);
        adapter.setDropDownViewResource(R.layout.my_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        return true;
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //get selected item
        String itemSelected = (String)parent.getItemAtPosition(position);

        Log.d("SpinnerActivity", "itemSelected = " + itemSelected);
        if(itemSelected.compareTo("Top rated") == 0){
            itemSelected = ORDERBY_VOTE_DESC;
        }else{
            itemSelected = ORDERBY_POPULARITY_DESC;
        }
        mMovieData = new MovieData();
        mMovieData.execute(itemSelected);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(String movieData) {
        Log.d("MainActivity", "onClick  - movieId: " + movieData);
        Context context = this;
        Class destinationClass = MovieDetailActivity.class;
        Intent intentToStartMovieDetailActivity = new Intent(context, destinationClass);
        // TODO (1) Pass the weather to the DetailActivity
        intentToStartMovieDetailActivity.putExtra(Intent.EXTRA_TEXT, movieData);
        startActivity(intentToStartMovieDetailActivity);

    }

    public class MovieData extends AsyncTask<String, Void, ArrayList<Movie>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            if(params.length >= 2 && params[1].compareTo("nextPage") == 0){
                mMovieAdapter.numPagesFetched = mMovieAdapter.numPagesFetched + 1;
                if(mMovieAdapter.numPagesFetched > 1000) {
                    Toast.makeText(MainActivity.this, "There is no more movies.", Toast.LENGTH_SHORT).show();
                    return null;
                }
            }
            else{
                mMovieAdapter.numPagesFetched = 1;
            }

            if(params[0].compareTo("")==0 && (mMovieAdapter.currentOrderBy.compareTo("")==0)){
                mMovieAdapter.currentOrderBy =  ORDERBY_POPULARITY_DESC;
            }
            else if(params[0].compareTo("")!=0){
                mMovieAdapter.currentOrderBy = params[0];
            }
            URL url = NetworkUtils.buildUrl(mMovieAdapter.currentOrderBy, mMovieAdapter.numPagesFetched);
            try{
                Log.d("MovieData", "doInBackground");
                String jsonResult = NetworkUtils.getResponseFromHttpUrl(url);
                return JsonUtils.getFilmListFromJson(jsonResult);
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return  null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> moviesList) {
            super.onPostExecute(moviesList);
            mProgressBar.setVisibility(View.INVISIBLE);
            Log.d("MainActivity", "onPostExecute");
            Log.d("MainActivity", "calling setMoviesArrList");
            if(moviesList != null && moviesList.size() > 0){
                showMovieData();
                mMovieAdapter.setMoviesArrList(moviesList);
            }else{
                showErrorMessage();
            }
        }
    }


    public void showMovieData(){
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorLinearLayout.setVisibility(View.INVISIBLE);
    }

    public void showErrorMessage(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorLinearLayout.setVisibility(View.VISIBLE);
    }

    public void tryAgain(View view){
        mMovieData = new MovieData();
        mMovieData.execute("");
    }


}
