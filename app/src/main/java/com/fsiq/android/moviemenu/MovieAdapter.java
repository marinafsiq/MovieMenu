package com.fsiq.android.moviemenu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.squareup.picasso.Picasso;

/**
 * Created by Marina on 03/09/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private ArrayList<Movie> moviesArrList;
    private Context context;
    public int numPagesFetched;
    public String currentOrderBy;

    private final MovieAdapterOnClickHandler clickHandler;

    public interface MovieAdapterOnClickHandler{
        void onClick(String movieId);
    }

    public MovieAdapter(Context pcontext, MovieAdapterOnClickHandler pclickHandler){
        context = pcontext;
        clickHandler = pclickHandler;
        numPagesFetched = 1;
        currentOrderBy = "";
    }


    @Override
    public MovieAdapter.MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(this.getClass().toString(), "onCreateViewHolder");
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View movieView = layoutInflater.inflate(R.layout.movie_grid_item, parent, false);

        return new MovieAdapterViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieAdapterViewHolder viewHolderolder, int position) {
        Log.d(this.getClass().toString(), "onBindViewHolder - position: " + position);
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" + moviesArrList.get(position).getPoster_path())
                .placeholder(getPlaceHolder(moviesArrList.get(position).getTitle()))
                .into(viewHolderolder.poster_img);

        if (position == getItemCount() - 1) {
            // load more data here.
        }

    }

    public Drawable getPlaceHolder(String movieName){
        Bitmap bm = Bitmap.createBitmap(100, 150, Bitmap.Config.ARGB_8888);
        bm.eraseColor(Color.parseColor("lightgray"));

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTextSize(12);

        StringBuilder newName = new StringBuilder();
        for(int i=0; i<movieName.length(); i++){
            newName.append(movieName.charAt(i));
            if(i>=16 && i%16 == 0){
                newName.append("\n");
            }
        }
        Log.d("MovieAdapter", "newName: " + newName);
        Canvas canvas = new Canvas(bm);
        canvas.drawText(newName.toString(), 0, bm.getHeight()/2, paint);


        return new BitmapDrawable(context.getResources() ,bm);
    }

    @Override
    public int getItemCount() {
        if(moviesArrList == null)
            return 0;
        else
            return moviesArrList.size();
    }

    public void setMoviesArrList(ArrayList<Movie> arrList){
        Log.d(this.getClass().toString(), "setMoviesArrList   arrList.size: " + arrList.size());
        if(numPagesFetched > 1)
            moviesArrList.addAll(arrList);
        else
            moviesArrList = arrList;
        notifyDataSetChanged();
    }

    //ViewHolder
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView poster_img;
        public MovieAdapterViewHolder(View view){
            super(view);
            Log.d(this.getClass().toString(), "Constructor being executed");
            poster_img = (ImageView) view.findViewById(R.id.poster_img);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movieClicked = moviesArrList.get(adapterPosition);
            String data = movieClicked.getTitle()+"|"+
                            movieClicked.getPoster_path()+"|"+
                            movieClicked.getOverview()+"|"+
                            movieClicked.getVote_average()+"|"+
                            movieClicked.getRelease_date();
            clickHandler.onClick(data);
        }
    }


}
