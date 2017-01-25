package info.kasimkovacevic.popularmovies.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by kasimkovacevic1 on 1/25/17.
 */
public class Trailer {

    @JsonProperty("id")
    String id;
    @JsonProperty("iso_639_1")
    String iso639;
    @JsonProperty("iso_3166_1")
    String iso3166;
    @JsonProperty("key")
    String key;
    @JsonProperty("name")
    String name;
    @JsonProperty("site")
    String site;
    @JsonProperty("size")
    long size;
    @JsonProperty("type")
    String type;

    long movieId;

    public Trailer() {
    }

    public Trailer(Cursor cursor) {
        id = cursor.getString(cursor.getColumnIndex(TrailerEntry.COLUMN_ID));
        iso639 = cursor.getString(cursor.getColumnIndex(TrailerEntry.COLUMN_ISO_639));
        iso3166 = cursor.getString(cursor.getColumnIndex(TrailerEntry.COLUMN_ISO_3166));
        key = cursor.getString(cursor.getColumnIndex(TrailerEntry.COLUMN_KEY));
        name = cursor.getString(cursor.getColumnIndex(TrailerEntry.COLUMN_NAME));
        site = cursor.getString(cursor.getColumnIndex(TrailerEntry.COLUMN_SITE));
        size = cursor.getLong(cursor.getColumnIndex(TrailerEntry.COLUMN_SIZE));
        type = cursor.getString(cursor.getColumnIndex(TrailerEntry.COLUMN_TYPE));
        movieId = cursor.getLong(cursor.getColumnIndex(TrailerEntry.COLUMN_MOVIE_ID));
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TrailerEntry.COLUMN_ID, getId());
        contentValues.put(TrailerEntry.COLUMN_ISO_639, getIso639());
        contentValues.put(TrailerEntry.COLUMN_ISO_3166, getIso3166());
        contentValues.put(TrailerEntry.COLUMN_KEY, getKey());
        contentValues.put(TrailerEntry.COLUMN_NAME, getName());
        contentValues.put(TrailerEntry.COLUMN_SITE, getSite());
        contentValues.put(TrailerEntry.COLUMN_TYPE, getType());
        contentValues.put(TrailerEntry.COLUMN_MOVIE_ID, getMovieId());
        contentValues.put(TrailerEntry.COLUMN_SITE, getSize());
        return contentValues;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso639() {
        return iso639;
    }

    public void setIso639(String iso639) {
        this.iso639 = iso639;
    }

    public String getIso3166() {
        return iso3166;
    }

    public void setIso3166(String iso3166) {
        this.iso3166 = iso3166;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public static final String PATH_TRAILERS = "trailers";

    public static final class TrailerEntry implements BaseColumns {

        // MovieEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILERS).build();


        public static final String TABLE_NAME = "trailers";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_ISO_639 = "iso_639_1";
        public static final String COLUMN_ISO_3166 = "iso_3166_1";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SITE = "site";
        public static final String COLUMN_SIZE = "size";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_MOVIE_ID = "movie_id";

    }
    //


}
