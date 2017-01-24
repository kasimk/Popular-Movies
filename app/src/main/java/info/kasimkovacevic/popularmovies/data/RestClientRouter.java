package info.kasimkovacevic.popularmovies.data;


import info.kasimkovacevic.popularmovies.utils.NetworkUtils;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by kasimkovacevic1 on 1/24/17.
 */
public class RestClientRouter {

    private static TheMovieDBService theMovieDBService;

    static {
        setupRestClient();
    }

    private RestClientRouter() {
    }

    public static TheMovieDBService get() {
        return theMovieDBService;
    }

    private static void setupRestClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .baseUrl(NetworkUtils.THE_MOVIE_DB_BASE_URL).client(client).build();

        theMovieDBService = retrofit.create(TheMovieDBService.class);
    }
}