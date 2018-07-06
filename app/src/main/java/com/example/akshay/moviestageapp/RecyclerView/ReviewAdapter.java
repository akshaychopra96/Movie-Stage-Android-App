package com.example.akshay.moviestageapp.RecyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.akshay.moviestageapp.R;
import com.example.akshay.moviestageapp.model.Review;
import com.example.akshay.moviestageapp.model.Trailer;

import java.util.List;

/**
 * Created by akshay on 26/6/18.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

     TextView reviewTV;
    List<Review> reviews;
    Context context;


    public ReviewAdapter(Context context, List<Review> reviews){
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        ReviewViewHolder viewHolder = new ReviewViewHolder(view);

        return viewHolder; }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {

        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder{

        public ReviewViewHolder(View itemView) {
            super(itemView);
            reviewTV = itemView.findViewById(R.id.reviewTV);
        }

        public void bind(int position) {
            reviewTV.setText(reviews.get(position).getContent());
        }
    }



}
