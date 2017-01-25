package info.kasimkovacevic.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import info.kasimkovacevic.popularmovies.models.Movie;

/**
 * Created by kasimkovacevic1 on 1/25/17.
 */

public class DBHelper {

    public static void insertOrUpdateMovie(Context context, Movie movie) {
        String stringId = Long.toString(movie.getId());
        Uri uri = Movie.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();
        String[] args = {String.valueOf(movie.getId())};
        ContentValues contentValues = movie.getContentValues();
        int rows = context.getContentResolver().update(uri, contentValues, Movie.MovieEntry.COLUMN_ID + "=?", args);
        if (rows == 0) {
            context.getContentResolver().insert(Movie.MovieEntry.CONTENT_URI, contentValues);
        }
    }

}
