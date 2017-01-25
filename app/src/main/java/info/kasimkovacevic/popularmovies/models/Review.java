package info.kasimkovacevic.popularmovies.models;

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
}
