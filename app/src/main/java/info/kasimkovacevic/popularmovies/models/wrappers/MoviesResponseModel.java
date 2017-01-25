package info.kasimkovacevic.popularmovies.models.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import info.kasimkovacevic.popularmovies.models.Movie;

/**
 * Created by kasimkovacevic1 on 1/24/17.
 */

public class MoviesResponseModel {

    @JsonProperty("page")
    int page;
    @JsonProperty("total_results")
    int totalResults;
    @JsonProperty("total_pages")
    int totalPages;
    @JsonProperty("results")
    List<Movie> movies;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
