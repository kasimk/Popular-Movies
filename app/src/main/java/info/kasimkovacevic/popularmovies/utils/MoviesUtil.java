package info.kasimkovacevic.popularmovies.utils;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import info.kasimkovacevic.popularmovies.models.Movie;

/**
 * Created by kasimkovacevic1 on 1/24/17.
 */

public class MoviesUtil {

    public static List<Movie> parseListOfMoviesFromCursor(Cursor cursor) {
        List<Movie> movies = new ArrayList<>();
        while (cursor.moveToNext()) {
            movies.add(new Movie(cursor));
        }
        cursor.close();
        return movies;
    }

}
