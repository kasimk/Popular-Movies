package info.kasimkovacevic.popularmovies.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kasimkovacevic1 on 1/6/17.
 */
public class Movie implements Serializable {
    @JsonProperty("poster_path")
    String posterPath;
    @JsonProperty("adult")
    Boolean forAdult;
    @JsonProperty("overview")
    String overview;
    @JsonProperty("release_date")
    String releaseDate;
    @JsonProperty("genre_ids")
    List<Long> genreIds;
    @JsonProperty("id")
    Long id;
    @JsonProperty("original_title")
    String originalTitle;
    @JsonProperty("original_language")
    String originalLanguage;
    @JsonProperty("title")
    String title;
    @JsonProperty("backdrop_path")
    String backdropPath;
    @JsonProperty("popularity")
    Double popularity;
    @JsonProperty("vote_count")
    Long voteCount;
    @JsonProperty("video")
    Boolean hasVideo;
    @JsonProperty("vote_average")
    Float voteAverage;

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Boolean getForAdult() {
        return forAdult;
    }

    public void setForAdult(Boolean forAdult) {
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

    public List<Long> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Long> genreIds) {
        this.genreIds = genreIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Long voteCount) {
        this.voteCount = voteCount;
    }

    public Boolean getHasVideo() {
        return hasVideo;
    }

    public void setHasVideo(Boolean hasVideo) {
        this.hasVideo = hasVideo;
    }

    public Float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Float voteAverage) {
        this.voteAverage = voteAverage;
    }
}
