package info.kasimkovacevic.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

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
}
