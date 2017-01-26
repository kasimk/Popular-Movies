package info.kasimkovacevic.popularmovies.viewholders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import info.kasimkovacevic.popularmovies.R;
import info.kasimkovacevic.popularmovies.activities.MovieDetailsActivity;
import info.kasimkovacevic.popularmovies.models.Movie;
import info.kasimkovacevic.popularmovies.utils.NetworkUtils;

import static info.kasimkovacevic.popularmovies.activities.MovieDetailsActivity.MOVIE_EXTRA;

/**
 * Created by kasimkovacevic1 on 1/7/17.
 */
public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView movieBackDropImageView;
    Movie movie;

    public MovieViewHolder(final View itemView) {
        super(itemView);
        movieBackDropImageView = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
        itemView.setOnClickListener(this);
    }


    public void bind(Movie movie, Context context) {
        this.movie = movie;
        Uri uri = NetworkUtils.buildPhotoUrl(movie.getPosterPath());
        Picasso.with(context).load(uri).into(movieBackDropImageView);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), MovieDetailsActivity.class);
        intent.putExtra(MOVIE_EXTRA, movie);
        view.getContext().startActivity(intent);
    }
}