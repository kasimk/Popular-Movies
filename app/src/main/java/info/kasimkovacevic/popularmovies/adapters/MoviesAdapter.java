package info.kasimkovacevic.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import info.kasimkovacevic.popularmovies.R;
import info.kasimkovacevic.popularmovies.models.Movie;
import info.kasimkovacevic.popularmovies.viewholders.MovieViewHolder;

/**
 * Created by kasimkovacevic1 on 1/6/17.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    List<Movie> movies;
    Context context;

    public MoviesAdapter(Context context) {
        this.context = context;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_list_item, parent, false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(movies.get(position), context);
    }

    @Override
    public int getItemCount() {
        return movies != null ? movies.size() : 0;
    }
}
