package info.kasimkovacevic.popularmovies.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by kasimkovacevic1 on 1/25/17.
 */
public class Review {

    @JsonProperty("id")
    String id;
    @JsonProperty("author")
    String author;
    @JsonProperty("content")
    String content;
    @JsonProperty("url")
    String url;

    long movieId;

    public Review() {
    }

    public Review(Cursor cursor) {
        id = cursor.getString(cursor.getColumnIndex(ReviewEntry.COLUMN_ID));
        author = cursor.getString(cursor.getColumnIndex(ReviewEntry.COLUMN_AUTHOR));
        content = cursor.getString(cursor.getColumnIndex(ReviewEntry.COLUMN_CONTENT));
        url = cursor.getString(cursor.getColumnIndex(ReviewEntry.COLUMN_URL));
        movieId = cursor.getLong(cursor.getColumnIndex(ReviewEntry.COLUMN_MOVIE_ID));
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ReviewEntry.COLUMN_ID, getId());
        contentValues.put(ReviewEntry.COLUMN_AUTHOR, getAuthor());
        contentValues.put(ReviewEntry.COLUMN_CONTENT, getContent());
        contentValues.put(ReviewEntry.COLUMN_URL, getUrl());
        contentValues.put(ReviewEntry.COLUMN_MOVIE_ID, getMovieId());
        return contentValues;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    //Local storage definition

    public static final String AUTHORITY = "info.kasimkovacevic.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_REVIEWS = "reviews";

    public static final class ReviewEntry implements BaseColumns {

        // MovieEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();


        public static final String TABLE_NAME = "reviews";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_MOVIE_ID = "movie_id";
    }
    //

}
