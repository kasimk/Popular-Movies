package info.kasimkovacevic.popularmovies.listeners;

import java.util.List;

import info.kasimkovacevic.popularmovies.models.Error;
import info.kasimkovacevic.popularmovies.models.Movie;

/**
 * Created by kasimkovacevic1 on 1/6/17.
 */
public interface FetchDataListener {
    public void onRequestStart();

    public void onSuccess(List<Movie> movieList);

    public void onFailure(Error error);
}
