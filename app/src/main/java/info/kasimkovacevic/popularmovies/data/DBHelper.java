package info.kasimkovacevic.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.List;

import info.kasimkovacevic.popularmovies.activities.MainActivity;
import info.kasimkovacevic.popularmovies.models.Movie;
import info.kasimkovacevic.popularmovies.models.Movie.MovieEntry;
import info.kasimkovacevic.popularmovies.models.Review;
import info.kasimkovacevic.popularmovies.models.Trailer;

/**
 * Created by kasimkovacevic1 on 1/24/17.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "moviesDB.db";
    private static final int VERSION = 1;

    /**
     * This method will save list of {@link Movie} in SQLite, without overriding favourite flag
     *
     * @param movies  list of {@link Movie} for saving in db
     * @param context instance of {@link Context}
     */
    public static void saveMoviesInDB(Context context, List<Movie> movies) {
        for (Movie movie : movies) {
            DBHelper.insertOrUpdateMovie(context, movie, true);
        }
    }

    /**
     * Save of update movie in DB
     * @param context instance of {@link Context}
     * @param movie instance of {@link Movie} for updating or inserting in db
     * @param removeFavourite flag indicates removing favourite flag while inserting/updating other movie data
     */
    public static void insertOrUpdateMovie(Context context, Movie movie, boolean removeFavourite) {
        String stringId = Long.toString(movie.getId());
        Uri uri = Movie.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();
        String[] args = {stringId};
        ContentValues contentValues = movie.getContentValues();
        if (removeFavourite) {
            contentValues.remove(MovieEntry.COLUMN_FAVOURITE);
        }
        int rows = context.getContentResolver().update(uri, contentValues, Movie.MovieEntry.COLUMN_ID + "=?", args);
        if (rows == 0) {
            context.getContentResolver().insert(Movie.MovieEntry.CONTENT_URI, contentValues);
        }
    }

    /**
     * Save or update review in DB
     * @param context instance of {@link Context}
     * @param review instance of {@link Review} for updating or inserting in db
     */
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

    /**
     * Save or update trailer in DB
     * @param context instance of {@link Context}
     * @param trailer instance of {@link Trailer} for updating or inserting in db
     */
    public static void insertOrUpdateTrailer(Context context, Trailer trailer) {
        String stringId = trailer.getId();
        Uri uri = Trailer.TrailerEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();
        String[] args = {stringId};
        ContentValues contentValues = trailer.getContentValues();
        int rows = context.getContentResolver().update(uri, contentValues, Trailer.TrailerEntry.COLUMN_ID + "=?", args);
        if (rows == 0) {
            context.getContentResolver().insert(Trailer.TrailerEntry.CONTENT_URI, contentValues);
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
                Review.ReviewEntry.COLUMN_ID + " TEXT PRIMARY KEY, " +
                Review.ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                Review.ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                Review.ReviewEntry.COLUMN_URL + " TEXT, " +
                Review.ReviewEntry.COLUMN_MOVIE_ID + " INTEGER, " +
                " FOREIGN KEY (" + Review.ReviewEntry.COLUMN_MOVIE_ID + ") REFERENCES " + MovieEntry.TABLE_NAME + "(" + MovieEntry.COLUMN_ID + "));";

        final String CREATE_TRAILERS_TABLE = "CREATE TABLE " + Trailer.TrailerEntry.TABLE_NAME + " (" +
                Trailer.TrailerEntry.COLUMN_ID + " TEXT PRIMARY KEY, " +
                Trailer.TrailerEntry.COLUMN_ISO_639 + " TEXT, " +
                Trailer.TrailerEntry.COLUMN_ISO_3166 + " TEXT, " +
                Trailer.TrailerEntry.COLUMN_KEY + " TEXT NOT NULL, " +
                Trailer.TrailerEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                Trailer.TrailerEntry.COLUMN_SITE + " TEXT, " +
                Trailer.TrailerEntry.COLUMN_SIZE + " LONG, " +
                Trailer.TrailerEntry.COLUMN_TYPE + " TEXT, " +
                Trailer.TrailerEntry.COLUMN_MOVIE_ID + " INTEGER, " +
                " FOREIGN KEY (" + Trailer.TrailerEntry.COLUMN_MOVIE_ID + ") REFERENCES " + MovieEntry.TABLE_NAME + "(" + MovieEntry.COLUMN_ID + "));";

        db.execSQL(CREATE_MOVIES_TABLE);
        db.execSQL(CREATE_REVIEWS_TABLE);
        db.execSQL(CREATE_TRAILERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Review.ReviewEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Trailer.TrailerEntry.TABLE_NAME);
        onCreate(db);
    }
}
