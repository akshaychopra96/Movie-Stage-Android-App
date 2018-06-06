package com.example.akshay.moviestageapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.akshay.moviestageapp.Utilities.NetworkUtils;
import com.squareup.picasso.Picasso;


public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.RecyclerViewHolder> {

    RecyclerviewAdapter(){}

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

    @Override
    public int getItemCount() {
        return 20;
    }

    public class RecyclerViewHolder extends  RecyclerView.ViewHolder{

        ImageView movieImage;
        TextView movieText;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.imageView);
            movieText = itemView.findViewById(R.id.textView);
        }

        void bind(int listIndex) {

                try {
                    Picasso.get()
                            .load(String.valueOf(NetworkUtils.getImageOfMovieDbUrl(MainActivity.movieObject.getImage().get(listIndex))))
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_background)
                            .into(movieImage);
                    Log.d("tag",MainActivity.movieObject.getOriginalTitle().get(listIndex));
                    movieText.setText(MainActivity.movieObject.getOriginalTitle().get(listIndex));
                } catch (Exception e) {
                    e.printStackTrace();
                }

        }
    }



}
