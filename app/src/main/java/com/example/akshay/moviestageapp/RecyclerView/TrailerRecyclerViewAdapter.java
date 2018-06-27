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
import com.example.akshay.moviestageapp.model.Trailer;
import com.example.akshay.moviestageapp.model.TrailerResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Callback;

public class TrailerRecyclerViewAdapter extends RecyclerView.Adapter<TrailerRecyclerViewAdapter.TrailerRecyclerViewHolder> {

    ImageView trailerThumbnailIV;
    List<Trailer> trailers;
    Context context;

    public TrailerRecyclerViewAdapter(Context context, List<Trailer> trailers){
        this.context = context;
        this.trailers = trailers;
    }


    @NonNull
    @Override
    public TrailerRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailer_item_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        TrailerRecyclerViewHolder viewHolder = new TrailerRecyclerViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull TrailerRecyclerViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public class TrailerRecyclerViewHolder extends RecyclerView.ViewHolder{


        public TrailerRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
          trailerThumbnailIV = itemView.findViewById(R.id.trailerThumbnailIV);
        }

        public void bind(int position) {


            try {
                Picasso.get()
                        .load(String.valueOf(NetworkUtils.getThumbnailImageOfTrailerURL(trailers.get(position).getKey(),0)))
                        .placeholder(R.drawable.progress_animation)
                        .error(R.drawable.image_not_found)
                        .into(trailerThumbnailIV);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}
