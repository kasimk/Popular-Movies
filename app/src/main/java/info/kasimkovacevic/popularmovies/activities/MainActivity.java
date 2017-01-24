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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.kasimkovacevic.popularmovies.R;
import info.kasimkovacevic.popularmovies.adapters.MoviesAdapter;
import info.kasimkovacevic.popularmovies.data.MoviesUtil;
import info.kasimkovacevic.popularmovies.data.RestClientRouter;
import info.kasimkovacevic.popularmovies.data.TheMovieDBService;
import info.kasimkovacevic.popularmovies.models.Movie;
import info.kasimkovacevic.popularmovies.models.MoviesResponseModel;
import info.kasimkovacevic.popularmovies.utils.MOVIES_ENUM;
import info.kasimkovacevic.popularmovies.utils.NetworkUtils;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Movie>> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MOVIES_LOADER_ID = 10;

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

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        moviesRecyclerView.setLayoutManager(gridLayoutManager);
        moviesAdapter = new MoviesAdapter(this);
        moviesRecyclerView.setAdapter(moviesAdapter);
        moviesEnum = MOVIES_ENUM.POPULAR;
        theMovieDBService = RestClientRouter.get();
        callApiForNewData();
    }


    private void callApiForNewData() {
        onRequestStart();
        response = theMovieDBService.listPopularMovies(moviesEnum.toString(), NetworkUtils.THE_MOVIE_DB_API_KEY);
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
                    }
                });
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
                getSupportLoaderManager().destroyLoader(MOVIES_LOADER_ID);
                callApiForNewData();
            case R.id.action_sort_by_rating:
                getSupportLoaderManager().destroyLoader(MOVIES_LOADER_ID);
                moviesEnum = MOVIES_ENUM.TOP_RATED;
                callApiForNewData();
            case R.id.action_show_favorites:
                getSupportLoaderManager().restartLoader(MOVIES_LOADER_ID, null, this);
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

                try {
                    Cursor cursor = getContentResolver().query(Movie.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
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
        moviesAdapter.setMovies(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        moviesAdapter.setMovies(null);
    }
}
