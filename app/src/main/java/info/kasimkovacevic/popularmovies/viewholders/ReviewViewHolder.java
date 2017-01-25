package info.kasimkovacevic.popularmovies.viewholders;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import info.kasimkovacevic.popularmovies.R;
import info.kasimkovacevic.popularmovies.models.Review;
import info.kasimkovacevic.popularmovies.models.Trailer;
import info.kasimkovacevic.popularmovies.utils.NetworkUtils;

/**
 * Created by kasimkovacevic1 on 1/25/17.
 */
public class ReviewViewHolder extends RecyclerView.ViewHolder {

    TextView mReviewerNameTextView;
    TextView mReviewContentTextView;

    public ReviewViewHolder(View itemView) {
        super(itemView);
        mReviewerNameTextView = (TextView) itemView.findViewById(R.id.tv_reviewer_name);
        mReviewContentTextView = (TextView) itemView.findViewById(R.id.tv_review_content);
    }


    public void bind(Review review) {
        mReviewerNameTextView.setText(review.getAuthor());
        mReviewContentTextView.setText(review.getContent());
    }
}
