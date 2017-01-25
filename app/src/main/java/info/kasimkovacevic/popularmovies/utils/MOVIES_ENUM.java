package info.kasimkovacevic.popularmovies.utils;

/**
 * Created by kasimkovacevic1 on 1/6/17.
 */

public enum MOVIES_ENUM {

    POPULAR("popular"),
    FAVOURITES("favourites"),
    TOP_RATED("top_rated");

    private final String text;

    private MOVIES_ENUM(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
