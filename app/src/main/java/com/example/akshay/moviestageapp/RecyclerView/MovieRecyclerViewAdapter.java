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


public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.RecyclerViewHolder> {

    Context context;
    List<Movie> movies;

    private final ListItemClickListener mOnClickListener;

    public MovieRecyclerViewAdapter(List<Movie> movies, Context context,ListItemClickListener mOnClickListener){

        this.movies = movies;
        this.context = context;
        this.mOnClickListener = mOnClickListener;
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

    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex,List<Movie> moviesList,View view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class RecyclerViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView movieImage;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
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

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition,movies,itemView);
        }
    }

}
