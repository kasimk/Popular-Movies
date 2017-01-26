package info.kasimkovacevic.popularmovies.viewholders;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.kasimkovacevic.popularmovies.R;
import info.kasimkovacevic.popularmovies.models.Review;
import info.kasimkovacevic.popularmovies.models.Trailer;
import info.kasimkovacevic.popularmovies.utils.NetworkUtils;

/**
 * Created by kasimkovacevic1 on 1/25/17.
 */
public class ReviewViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_reviewer_name)
    protected TextView mReviewerNameTextView;
    @BindView(R.id.tv_review_content)
    protected TextView mReviewContentTextView;

    public ReviewViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


    public void bind(Review review) {
        mReviewerNameTextView.setText(review.getAuthor());
        mReviewContentTextView.setText(review.getContent());
    }
}
