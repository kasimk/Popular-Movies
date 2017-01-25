package info.kasimkovacevic.popularmovies.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
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

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Movie>> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int FAVOURITES_MOVIES_LOADER_ID = 10;
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
        if (savedInstanceState != null && savedInstanceState.getSerializable(SORT_BY_KEY) != null) {
            moviesEnum = (MOVIES_ENUM) savedInstanceState.getSerializable(SORT_BY_KEY);
        }
        if (moviesEnum == MOVIES_ENUM.POPULAR || moviesEnum == MOVIES_ENUM.TOP_RATED) {
            callApiForNewData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (moviesEnum == MOVIES_ENUM.FAVOURITES) {
            getSupportLoaderManager().restartLoader(FAVOURITES_MOVIES_LOADER_ID, null, this);
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
                        saveMoviewsInDB(model.getMovies());
                    }
                });
    }


    private void saveMoviewsInDB(List<Movie> movies) {
        for (Movie movie : movies) {
            DBHelper.insertOrUpdateMovie(MainActivity.this, movie, true);
        }
    }

    public void onRequestStart() {
        loaderProgressBar.setVisibility(View.VISIBLE);
    }

    public void onSuccess(List<Movie> movieList) {
        showMovies(movieList);
    }

    public void onFailure(String error) {
        showError(error);
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
                getSupportLoaderManager().restartLoader(FAVOURITES_MOVIES_LOADER_ID, null, this);
                break;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_movies_menu, menu);
        return true;
    }

    private void showMovies(List<Movie> movies) {
        moviesRecyclerView.setVisibility(View.VISIBLE);
        loaderProgressBar.setVisibility(View.INVISIBLE);
        errorTextView.setVisibility(View.INVISIBLE);
        moviesAdapter.setMovies(movies);
    }

    private void showError(String error) {
        moviesRecyclerView.setVisibility(View.INVISIBLE);
        loaderProgressBar.setVisibility(View.INVISIBLE);
        errorTextView.setText(error);
        errorTextView.setVisibility(View.VISIBLE);
    }


    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Movie>>(this) {

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
                    Log.e(TAG, "Failed to asynchronously load data.");
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
}
