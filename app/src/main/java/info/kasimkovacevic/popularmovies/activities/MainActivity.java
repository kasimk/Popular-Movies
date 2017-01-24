package info.kasimkovacevic.popularmovies.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class MainActivity extends AppCompatActivity {

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
        if (item.getItemId() == R.id.action_sort_by_rating) {
            moviesEnum = MOVIES_ENUM.TOP_RATED;
        } else {
            moviesEnum = MOVIES_ENUM.POPULAR;
        }
        callApiForNewData();
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
}
