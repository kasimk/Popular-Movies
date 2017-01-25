package info.kasimkovacevic.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import info.kasimkovacevic.popularmovies.models.Movie;
import info.kasimkovacevic.popularmovies.models.Review;

public class MovieContentProvider extends ContentProvider {

    public static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;
    public static final int REVIEWS = 102;
    public static final int REVIEWS_WITH_ID = 103;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(Movie.AUTHORITY, Movie.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(Movie.AUTHORITY, Movie.PATH_MOVIES + "/#", MOVIE_WITH_ID);
        uriMatcher.addURI(Review.AUTHORITY, Review.PATH_REVIEWS, REVIEWS);
        uriMatcher.addURI(Review.AUTHORITY, Review.PATH_REVIEWS + "/*", REVIEWS_WITH_ID);

        return uriMatcher;
    }

    private DBHelper mMovieDBHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMovieDBHelper = new DBHelper(context);
        return true;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mMovieDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        long id = 0;
        switch (match) {
            case MOVIES:
                id = db.insert(Movie.MovieEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(Movie.MovieEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case REVIEWS:
                id = db.insert(Review.ReviewEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(Review.ReviewEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = mMovieDBHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case MOVIES:
                retCursor = db.query(Movie.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case REVIEWS:
                retCursor = db.query(Review.ReviewEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }


    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mMovieDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int affectedRows;
        String id = "";
        switch (match) {
            case MOVIE_WITH_ID:
                id = uri.getPathSegments().get(1);
                affectedRows = db.delete(Movie.MovieEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            case REVIEWS_WITH_ID:
                id = uri.getPathSegments().get(1);
                affectedRows = db.delete(Review.ReviewEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (affectedRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return affectedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mMovieDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int affectedRows = 0;
        switch (match) {
            case MOVIE_WITH_ID:
                affectedRows = db.update(Movie.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case REVIEWS_WITH_ID:
                affectedRows = db.update(Review.ReviewEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return affectedRows;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("NOT IMPLEMENTED METHOD");
    }


}
