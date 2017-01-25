package info.kasimkovacevic.popularmovies.models.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import info.kasimkovacevic.popularmovies.models.Trailer;

/**
 * Created by kasimkovacevic1 on 1/25/17.
 */

public class TrailersResponseModel {

    @JsonProperty("id")
    long id;
    @JsonProperty("results")
    List<Trailer> trailers;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }
}
