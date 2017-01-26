package info.kasimkovacevic.popularmovies.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.kasimkovacevic.popularmovies.R;
import info.kasimkovacevic.popularmovies.adapters.MoviesAdapter;
import info.kasimkovacevic.popularmovies.data.DBHelper;
import info.kasimkovacevic.popularmovies.utils.MoviesUtil;
import info.kasimkovacevic.popularmovies.data.RestClientRouter;
import info.kasimkovacevic.popularmovies.data.TheMovieDBService;
import info.kasimkovacevic.popularmovies.models.Movie;
import info.kasimkovacevic.popularmovies.models.wrappers.MoviesResponseModel;
import info.kasimkovacevic.popularmovies.utils.MOVIES_ENUM;
import info.kasimkovacevic.popularmovies.utils.NetworkUtils;
import info.kasimkovacevic.popularmovies.utils.ScreenUtils;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int FAVOURITES_MOVIES_LOADER_ID = 10;
    private static final int ALL_MOVIES_LOADER_ID = 101;
    private static final String SORT_BY_KEY = "info.kasimkovacevic.popularmovies.SORT_BY_KEY";

    @BindView(R.id.tv_error)
    TextView errorTextView;
    @BindView(R.id.pb_loader)
    ProgressBar loaderProgressBar;
    @BindView(R.id.rv_movies)
    RecyclerView moviesRecyclerView;

    private MoviesAdapter moviesAdapter;
    private MOVIES_ENUM moviesEnum;
    private TheMovieDBService theMovieDBService;
    private Observable<MoviesResponseModel> response;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        int numOfSpans = ScreenUtils.getScreenWidth() / 260 + 1;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, numOfSpans);
        moviesRecyclerView.setLayoutManager(gridLayoutManager);
        moviesAdapter = new MoviesAdapter(this);
        moviesRecyclerView.setAdapter(moviesAdapter);
        moviesEnum = MOVIES_ENUM.POPULAR;
        theMovieDBService = RestClientRouter.get();
        //Remember sort option on screen rotation
        if (savedInstanceState != null && savedInstanceState.getSerializable(SORT_BY_KEY) != null) {
            moviesEnum = (MOVIES_ENUM) savedInstanceState.getSerializable(SORT_BY_KEY);
        }
        //If sort is popular or top rated load movies from api
        if (moviesEnum == MOVIES_ENUM.POPULAR || moviesEnum == MOVIES_ENUM.TOP_RATED) {
            callApiForNewData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //if sort is favourites, load/reload movies from DB, need to refresh screen if some movie is removed from favourite movies
        if (moviesEnum == MOVIES_ENUM.FAVOURITES) {
            getSupportLoaderManager().restartLoader(FAVOURITES_MOVIES_LOADER_ID, null, favouriteMoviesCallback);
        }
    }

    private void callApiForNewData() {
        onRequestStart();
        response = theMovieDBService.loadMovies(moviesEnum.toString(), NetworkUtils.THE_MOVIE_DB_API_KEY);
        response.subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()
                ).
                subscribe(new Observer<MoviesResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onFailure(e.getMessage());
                    }

                    @Override
                    public void onNext(MoviesResponseModel model) {
                        onSuccess(model.getMovies());
                        //save movies data in local storage, if no internet connection show movies from local storage
                        DBHelper.saveMoviesInDB(MainActivity.this, model.getMovies());
                    }
                });
    }


    /**
     * show progress bar when movies loading from api start
     */
    public void onRequestStart() {
        loaderProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Called on api load success or db load success
     *
     * @param movieList represent loaded list of {@link Movie}
     */
    public void onSuccess(List<Movie> movieList) {
        showMovies(movieList);
    }

    public void onFailure(String error) {
        showError(error);
        loadAllMoviesFromDB();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SORT_BY_KEY, moviesEnum);
    }

    @Override
    protected void onDestroy() {
        if (response != null) {
            response.unsubscribeOn(Schedulers.io());
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_by_popularity:
                moviesEnum = MOVIES_ENUM.POPULAR;
                getSupportLoaderManager().destroyLoader(FAVOURITES_MOVIES_LOADER_ID);
                callApiForNewData();
                break;
            case R.id.action_sort_by_rating:
                moviesEnum = MOVIES_ENUM.TOP_RATED;
                getSupportLoaderManager().destroyLoader(FAVOURITES_MOVIES_LOADER_ID);
                callApiForNewData();
                break;
            case R.id.action_show_favorites:
                moviesEnum = MOVIES_ENUM.FAVOURITES;
                getSupportLoaderManager().restartLoader(FAVOURITES_MOVIES_LOADER_ID, null, favouriteMoviesCallback);
                break;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_movies_menu, menu);
        return true;
    }

    /**
     * Hide error fields and show list of provided movies
     *
     * @param movies represents list of {@link Movie} that need to be shown
     */
    private void showMovies(List<Movie> movies) {
        moviesRecyclerView.setVisibility(View.VISIBLE);
        loaderProgressBar.setVisibility(View.INVISIBLE);
        errorTextView.setVisibility(View.INVISIBLE);
        moviesAdapter.setMovies(movies);
    }

    /**
     * Hide list of movies and show error
     *
     * @param error represents error message
     */
    private void showError(String error) {
        moviesRecyclerView.setVisibility(View.INVISIBLE);
        loaderProgressBar.setVisibility(View.INVISIBLE);
        errorTextView.setText(error);
        errorTextView.setVisibility(View.VISIBLE);
    }


    /**
     * Call loading all movies from DB
     */
    private void loadAllMoviesFromDB() {
        getSupportLoaderManager().initLoader(ALL_MOVIES_LOADER_ID, null, allMoviesCallback);
    }


    //Favourite movies loader
    LoaderManager.LoaderCallbacks<List<Movie>> favouriteMoviesCallback = new LoaderManager.LoaderCallbacks<List<Movie>>() {
        @Override
        public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<List<Movie>>(MainActivity.this) {

                List<Movie> mMovies = null;

                @Override
                protected void onStartLoading() {
                    if (mMovies != null) {
                        deliverResult(mMovies);
                    } else {
                        forceLoad();
                    }
                }

                @Override
                public List<Movie> loadInBackground() {
                    String[] args = {String.valueOf(1)};
                    try {
                        Cursor cursor = getContentResolver().query(Movie.MovieEntry.CONTENT_URI,
                                null,
                                Movie.MovieEntry.COLUMN_FAVOURITE + "=?", args,
                                Movie.MovieEntry.COLUMN_VOTE_AVERAGE);

                        return MoviesUtil.parseListOfMoviesFromCursor(cursor);
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to asynchronously load favourite movies");
                        e.printStackTrace();
                        return null;
                    }
                }

                public void deliverResult(List<Movie> data) {
                    mMovies = data;
                    super.deliverResult(mMovies);
                }
            };

        }

        @Override
        public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
            onSuccess(data);
        }

        @Override
        public void onLoaderReset(Loader<List<Movie>> loader) {
            moviesAdapter.setMovies(null);
        }
    };


    //All movies loader
    LoaderManager.LoaderCallbacks<List<Movie>> allMoviesCallback = new LoaderManager.LoaderCallbacks<List<Movie>>() {

        @Override
        public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<List<Movie>>(MainActivity.this) {

                List<Movie> mMovies = null;

                @Override
                protected void onStartLoading() {
                    if (mMovies != null) {
                        deliverResult(mMovies);
                    } else {
                        forceLoad();
                    }
                }

                @Override
                public List<Movie> loadInBackground() {
                    try {
                        Cursor cursor = getContentResolver().query(Movie.MovieEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                Movie.MovieEntry.COLUMN_VOTE_AVERAGE + " DESC");

                        return MoviesUtil.parseListOfMoviesFromCursor(cursor);
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to asynchronously load movies.");
                        e.printStackTrace();
                        return null;
                    }
                }

                public void deliverResult(List<Movie> data) {
                    mMovies = data;
                    super.deliverResult(mMovies);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
            onSuccess(data);
            Toast.makeText(MainActivity.this, R.string.load_from_local_storage_message, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onLoaderReset(Loader<List<Movie>> loader) {

        }
    };
}
