package info.kasimkovacevic.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import info.kasimkovacevic.popularmovies.R;
import info.kasimkovacevic.popularmovies.models.Review;
import info.kasimkovacevic.popularmovies.viewholders.ReviewViewHolder;

/**
 * Created by kasimkovacevic1 on 1/25/17.
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewViewHolder> {

    List<Review> reviews;


    public ReviewsAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_list_item, parent, false);
        ReviewViewHolder viewHolder = new ReviewViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.bind(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        return reviews != null ? reviews.size() : 0;
    }
}
