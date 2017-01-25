package info.kasimkovacevic.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import info.kasimkovacevic.popularmovies.models.Movie;
import info.kasimkovacevic.popularmovies.models.Movie.MovieEntry;
import info.kasimkovacevic.popularmovies.models.Review;

/**
 * Created by kasimkovacevic1 on 1/24/17.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "moviesDB.db";
    private static final int VERSION = 5;

    public static void insertOrUpdateMovie(Context context, Movie movie) {
        String stringId = Long.toString(movie.getId());
        Uri uri = Movie.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();
        String[] args = {stringId};
        ContentValues contentValues = movie.getContentValues();
        int rows = context.getContentResolver().update(uri, contentValues, Movie.MovieEntry.COLUMN_ID + "=?", args);
        if (rows == 0) {
            context.getContentResolver().insert(Movie.MovieEntry.CONTENT_URI, contentValues);
        }
    }

    public static void insertOrUpdateReview(Context context, Review review) {
        String stringId = review.getId();
        Uri uri = Review.ReviewEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();
        String[] args = {stringId};
        ContentValues contentValues = review.getContentValues();
        int rows = context.getContentResolver().update(uri, contentValues, Review.ReviewEntry.COLUMN_ID + "=?", args);
        if (rows == 0) {
            context.getContentResolver().insert(Review.ReviewEntry.CONTENT_URI, contentValues);
        }
    }


    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_MOVIES_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                MovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT, " +
                MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT, " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                MovieEntry.COLUMN_POPULARITY + " DOUBLE, " +
                MovieEntry.COLUMN_POSTER_PATH + " TEXT, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                MovieEntry.COLUMN_TITLE + " TEXT, " +
                MovieEntry.COLUMN_VIDEO + " BOOLEAN, " +
                MovieEntry.COLUMN_VOTE_AVERAGE + " DOUBLE, " +
                MovieEntry.COLUMN_VOTE_COUNT + " LONG, " +
                MovieEntry.COLUMN_FAVOURITE + " BOOLEAN, " +
                MovieEntry.COLUMN_FOR_ADULT + " BOOLEAN);";

        final String CREATE_REVIEWS_TABLE = "CREATE TABLE " + Review.ReviewEntry.TABLE_NAME + " (" +
                Review.ReviewEntry.COLUMN_ID + " STRING PRIMARY KEY, " +
                Review.ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                Review.ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                Review.ReviewEntry.COLUMN_URL + " TEXT, " +
                Review.ReviewEntry.COLUMN_MOVIE_ID + " INTEGER, " +
                " FOREIGN KEY (" + Review.ReviewEntry.COLUMN_MOVIE_ID + ") REFERENCES " + MovieEntry.TABLE_NAME + "(" + MovieEntry.COLUMN_ID + "));";

        db.execSQL(CREATE_MOVIES_TABLE);
        db.execSQL(CREATE_REVIEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Review.ReviewEntry.TABLE_NAME);
        onCreate(db);
    }
}
