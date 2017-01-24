package info.kasimkovacevic.popularmovies.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by kasimkovacevic1 on 1/6/17.
 */
public class Movie implements Parcelable {
    @JsonProperty("poster_path")
    String posterPath;
    @JsonProperty("adult")
    boolean forAdult;
    @JsonProperty("overview")
    String overview;
    @JsonProperty("release_date")
    String releaseDate;
    @JsonProperty("genre_ids")
    long[] genreIds;
    @JsonProperty("id")
    long id;
    @JsonProperty("original_title")
    String originalTitle;
    @JsonProperty("original_language")
    String originalLanguage;
    @JsonProperty("title")
    String title;
    @JsonProperty("backdrop_path")
    String backdropPath;
    @JsonProperty("popularity")
    double popularity;
    @JsonProperty("vote_count")
    long voteCount;
    @JsonProperty("video")
    boolean hasVideo;
    @JsonProperty("vote_average")
    float voteAverage;

    public Movie() {
    }

    public Movie(Cursor cursor) {
        posterPath = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH));
        forAdult = cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_FOR_ADULT)) == 1 ? true : false;
        overview = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_OVERVIEW));
        releaseDate = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE));
        id = cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_ID));
        originalTitle = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_ORIGINAL_TITLE));
        originalLanguage = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_ORIGINAL_LANGUAGE));
        title = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_TITLE));
        backdropPath = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_BACKDROP_PATH));
        popularity = cursor.getDouble(cursor.getColumnIndex(MovieEntry.COLUMN_POPULARITY));
        hasVideo = cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_VIDEO)) == 1 ? true : false;
        voteAverage = cursor.getFloat(cursor.getColumnIndex(MovieEntry.COLUMN_VOTE_AVERAGE));
        voteCount = cursor.getLong(cursor.getColumnIndex(MovieEntry.COLUMN_VOTE_COUNT));
    }

    protected Movie(Parcel in) {
        posterPath = in.readString();
        forAdult = in.readByte() != 0;
        overview = in.readString();
        releaseDate = in.readString();
        genreIds = in.createLongArray();
        id = in.readLong();
        originalTitle = in.readString();
        originalLanguage = in.readString();
        title = in.readString();
        backdropPath = in.readString();
        popularity = in.readDouble();
        voteCount = in.readLong();
        hasVideo = in.readByte() != 0;
        voteAverage = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeByte((byte) (forAdult ? 1 : 0));
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeLongArray(genreIds);
        dest.writeLong(id);
        dest.writeString(originalTitle);
        dest.writeString(originalLanguage);
        dest.writeString(title);
        dest.writeString(backdropPath);
        dest.writeDouble(popularity);
        dest.writeLong(voteCount);
        dest.writeByte((byte) (hasVideo ? 1 : 0));
        dest.writeFloat(voteAverage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieEntry.COLUMN_ID, getId());
        contentValues.put(MovieEntry.COLUMN_POSTER_PATH, getPosterPath());
        contentValues.put(MovieEntry.COLUMN_FOR_ADULT, isForAdult());
        contentValues.put(MovieEntry.COLUMN_OVERVIEW, getOverview());
        contentValues.put(MovieEntry.COLUMN_RELEASE_DATE, getReleaseDate());
        contentValues.put(MovieEntry.COLUMN_ORIGINAL_TITLE, getOriginalTitle());
        contentValues.put(MovieEntry.COLUMN_ORIGINAL_LANGUAGE, getOriginalLanguage());
        contentValues.put(MovieEntry.COLUMN_TITLE, getTitle());
        contentValues.put(MovieEntry.COLUMN_BACKDROP_PATH, getBackdropPath());
        contentValues.put(MovieEntry.COLUMN_POPULARITY, getPopularity());
        contentValues.put(MovieEntry.COLUMN_VOTE_COUNT, getVoteCount());
        contentValues.put(MovieEntry.COLUMN_VIDEO, isHasVideo());
        contentValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, getVoteAverage());
        return contentValues;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public boolean isForAdult() {
        return forAdult;
    }

    public void setForAdult(boolean forAdult) {
        this.forAdult = forAdult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public long[] getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(long[] genreIds) {
        this.genreIds = genreIds;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(long voteCount) {
        this.voteCount = voteCount;
    }

    public boolean isHasVideo() {
        return hasVideo;
    }

    public void setHasVideo(boolean hasVideo) {
        this.hasVideo = hasVideo;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }


    //Local storage definition

    public static final String AUTHORITY = "info.kasimkovacevic.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {

        // MovieEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_FOR_ADULT = "for_adult";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_ORIGINAL_LANGUAGE = "original_language";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_VIDEO = "video";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
    }
    //

}
