package info.kasimkovacevic.popularmovies.data;

import info.kasimkovacevic.popularmovies.models.MoviesResponseModel;
import info.kasimkovacevic.popularmovies.utils.NetworkUtils;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by kasimkovacevic1 on 1/24/17.
 */
public interface TheMovieDBService {

    @GET("/3/movie/{order}")
    Observable<MoviesResponseModel> listPopularMovies(@Path("order") String type, @Query(NetworkUtils.PARAM_API_KEY) String movieDbApiKey);
}
