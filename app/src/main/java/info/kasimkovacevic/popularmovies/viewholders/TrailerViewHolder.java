package info.kasimkovacevic.popularmovies.viewholders;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import info.kasimkovacevic.popularmovies.R;
import info.kasimkovacevic.popularmovies.activities.MovieDetailsActivity;
import info.kasimkovacevic.popularmovies.models.Trailer;
import info.kasimkovacevic.popularmovies.utils.NetworkUtils;

import static info.kasimkovacevic.popularmovies.activities.MovieDetailsActivity.POPULAR_MOVIES_MOVIE_EXTRA;

/**
 * Created by kasimkovacevic1 on 1/25/17.
 */
public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView mTrailerNameTextView;
    Trailer mTrailer;

    public TrailerViewHolder(View itemView) {
        super(itemView);
        mTrailerNameTextView = (TextView) itemView.findViewById(R.id.tv_trailer_name);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Uri uri = NetworkUtils.buildTrailerUrl(mTrailer);
        Intent startYoutubeIntent = new Intent(Intent.ACTION_VIEW, uri);
        PackageManager packageManager = v.getContext().getPackageManager();
        if (startYoutubeIntent.resolveActivity(packageManager) != null) {
            v.getContext().startActivity(startYoutubeIntent);
        } else {
            Toast.makeText(v.getContext(), R.string.youtube_video_app_missing, Toast.LENGTH_LONG).show();
        }
    }

    public void bind(Trailer trailer, Context context) {
        this.mTrailer = trailer;
        mTrailerNameTextView.setText(trailer.getName());
    }
}
