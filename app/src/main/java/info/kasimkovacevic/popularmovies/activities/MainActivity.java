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

import info.kasimkovacevic.popularmovies.R;
import info.kasimkovacevic.popularmovies.adapters.MoviesAdapter;
import info.kasimkovacevic.popularmovies.asynctasks.FetchDataAsyncTask;
import info.kasimkovacevic.popularmovies.listeners.FetchDataListener;
import info.kasimkovacevic.popularmovies.models.Error;
import info.kasimkovacevic.popularmovies.models.Movie;
import info.kasimkovacevic.popularmovies.utils.MOVIES_ENUM;
import info.kasimkovacevic.popularmovies.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity implements FetchDataListener {

    private TextView errorTextView;
    private ProgressBar loaderProgressBar;
    private RecyclerView moviesRecyclerView;
    private MoviesAdapter moviesAdapter;
    private MOVIES_ENUM moviesEnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        errorTextView = (TextView) findViewById(R.id.tv_error);
        loaderProgressBar = (ProgressBar) findViewById(R.id.pb_loader);
        moviesRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        moviesRecyclerView.setLayoutManager(gridLayoutManager);
        moviesAdapter = new MoviesAdapter(this);
        moviesRecyclerView.setAdapter(moviesAdapter);
        moviesEnum = MOVIES_ENUM.POPULAR;
        new FetchDataAsyncTask(this).execute(NetworkUtils.buildUrl(moviesEnum));
    }

    @Override
    public void onRequestStart() {
        loaderProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(List<Movie> movieList) {
        showMovies(movieList);
    }

    @Override
    public void onFailure(Error error) {
        showError(error);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sort_by_rating) {
            moviesEnum = MOVIES_ENUM.TOP_RATED;
        } else {
            moviesEnum = MOVIES_ENUM.POPULAR;
        }
        new FetchDataAsyncTask(this).execute(NetworkUtils.buildUrl(moviesEnum));
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

    private void showError(Error error) {
        moviesRecyclerView.setVisibility(View.INVISIBLE);
        loaderProgressBar.setVisibility(View.INVISIBLE);
        errorTextView.setText(error.getErrorMessage());
        errorTextView.setVisibility(View.VISIBLE);
    }
}
