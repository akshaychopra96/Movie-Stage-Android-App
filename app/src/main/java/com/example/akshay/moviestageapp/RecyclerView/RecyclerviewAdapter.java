package com.example.akshay.moviestageapp.RecyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.akshay.moviestageapp.R;
import com.example.akshay.moviestageapp.Utilities.NetworkUtils;
import com.example.akshay.moviestageapp.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.RecyclerViewHolder> {

    static Context context;
    static List<Movie> movies;


    /**
     * Changed the constructor to make sure that it does not recieve the array list.
     * This was done because at time of adapter creation, we do not have nany data.
     * @param context
     */
    public RecyclerviewAdapter( Context context){

        this.context = context;

    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recyclerview_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.bind(position);
    }


    /**
     * To avoid having Null pointer exception, I added a check for null movies
     * This happens when we create the adapter and set it to the recycler view and run the app
     * it tries to count the size of the list while we haven't recieved any data from net.
     * @return
     */
    @Override
    public int getItemCount() {
        if (movies == null)
            return 0;
        else
            return movies.size();
    }

    public class RecyclerViewHolder extends  RecyclerView.ViewHolder{

        ImageView movieImage;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.imageView);
        }

        void bind(int listIndex) {

                try {
                    Picasso.get()
                            .load(String.valueOf(NetworkUtils.getImageOfMovieDbUrl(movies.get(listIndex).getPosterPath(),NetworkUtils.IMAGE_SIZE_PATH)))
                            .placeholder(R.drawable.progress_animation)
                            .into(movieImage);

                } catch (Exception e) {
                    e.printStackTrace();
                }

        }
    }


    /**
     *
     * Change no. 6
     *
     * Created this method to make sure the data is always updated
     * @param moviesNew
     */

    public void switchAdapter( List<Movie> moviesNew) {
        movies = moviesNew;
        notifyDataSetChanged();
    }

}
