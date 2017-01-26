package info.kasimkovacevic.popularmovies.models.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import info.kasimkovacevic.popularmovies.models.Review;

/**
 * Created by kasimkovacevic1 on 1/25/17.
 */
public class ReviewsResponseModel {

    @JsonProperty("id")
    long id;
    @JsonProperty("page")
    long page;
    @JsonProperty("results")
    List<Review> reviews;
    @JsonProperty("total_pages")
    long totalPages;
    @JsonProperty("total_results")
    long totalResults;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }
}
