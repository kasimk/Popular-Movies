package info.kasimkovacevic.popularmovies.models;

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
}
