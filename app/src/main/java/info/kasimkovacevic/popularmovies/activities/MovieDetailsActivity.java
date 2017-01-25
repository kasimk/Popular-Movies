package info.kasimkovacevic.popularmovies.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.kasimkovacevic.popularmovies.R;
import info.kasimkovacevic.popularmovies.adapters.ReviewsAdapter;
import info.kasimkovacevic.popularmovies.adapters.TrailersAdapter;
import info.kasimkovacevic.popularmovies.data.DBHelper;
import info.kasimkovacevic.popularmovies.data.RestClientRouter;
import info.kasimkovacevic.popularmovies.data.TheMovieDBService;
import info.kasimkovacevic.popularmovies.models.Movie;
import info.kasimkovacevic.popularmovies.models.wrappers.ReviewsResponseModel;
import info.kasimkovacevic.popularmovies.models.wrappers.TrailersResponseModel;
import info.kasimkovacevic.popularmovies.utils.NetworkUtils;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MovieDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie> {

    public static final String POPULAR_MOVIES_MOVIE_EXTRA = "info.kasimkovacevic.popularmovies.MOVIE_EXTRA";
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();
    private static final int MOVIE_TASK_ID = 101;

    private Movie movie;
    @BindView(R.id.tv_movie_title)
    TextView movieTitleTextView;
    @BindView(R.id.tv_movie_overview)
    TextView movieOverviewTextView;
    @BindView(R.id.tv_user_rating)
    TextView movieRatingTextView;
    @BindView(R.id.tv_release_date)
    TextView movieReleaseDateTextView;
    @BindView(R.id.iv_movie_poster)
    ImageView moviePosterImageView;
    @BindView(R.id.ib_add_movie_to_favorites)
    ImageButton addMoviesToFavoritesImageButton;
    @BindView(R.id.rv_trailers)
    RecyclerView trailersRecyclerView;
    @BindView(R.id.rv_reviews)
    RecyclerView reviewsRecyclerView;


    private TrailersAdapter trailersAdapter;
    private ReviewsAdapter reviewsAdapter;
    private TheMovieDBService theMovieDBService;
    private Observable<TrailersResponseModel> trailersResponse;
    private Observable<ReviewsResponseModel> reviewsResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent.getExtras() != null && intent.hasExtra(POPULAR_MOVIES_MOVIE_EXTRA)) {
            movie = intent.getParcelableExtra(POPULAR_MOVIES_MOVIE_EXTRA);
            configureUI();
        }
    }

    private void configureUI() {
        movieTitleTextView.setText(movie.getOriginalTitle());
        movieOverviewTextView.setText(movie.getOverview());
        movieRatingTextView.setText(String.valueOf(movie.getVoteAverage()));
        movieReleaseDateTextView.setText(movie.getReleaseDate());
        Picasso.with(MovieDetailsActivity.this).load(NetworkUtils.buildPhotoUrl(movie.getPosterPath())).into(moviePosterImageView);

        addMoviesToFavoritesImageButton.setEnabled(false);
        addMoviesToFavoritesImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movie.setFavourite(!movie.isFavourite());
                if (movie.isFavourite()) {
                    addMoviesToFavoritesImageButton.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("@android:drawable/btn_star_big_on", null, null)));
                } else {
                    addMoviesToFavoritesImageButton.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("@android:drawable/btn_star_big_off", null, null)));
                }
                DBHelper.insertOrUpdateMovie(MovieDetailsActivity.this, movie);
            }
        });
        trailersAdapter = new TrailersAdapter(MovieDetailsActivity.this, null);
        reviewsAdapter = new ReviewsAdapter(null);

        LinearLayoutManager trailersLayoutManager = new LinearLayoutManager(MovieDetailsActivity.this);
        trailersRecyclerView.setLayoutManager(trailersLayoutManager);
        LinearLayoutManager reviewsLayoutManager = new LinearLayoutManager(MovieDetailsActivity.this);
        reviewsRecyclerView.setLayoutManager(reviewsLayoutManager);

        trailersRecyclerView.setAdapter(trailersAdapter);
        reviewsRecyclerView.setAdapter(reviewsAdapter);
        theMovieDBService = RestClientRouter.get();
        loadTrailers();
        loadReviews();
        getSupportLoaderManager().initLoader(MOVIE_TASK_ID, null, this);
    }


    private void loadReviews() {
        reviewsResponse = theMovieDBService.loadReviewsForMovie(movie.getId(), NetworkUtils.THE_MOVIE_DB_API_KEY);
        reviewsResponse.subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()
                ).
                subscribe(new Observer<ReviewsResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ReviewsResponseModel model) {
                        reviewsAdapter.setReviews(model.getReviews());
                    }
                });
    }

    private void loadTrailers() {
        trailersResponse = theMovieDBService.loadTrailersForMovie(movie.getId(), NetworkUtils.THE_MOVIE_DB_API_KEY);
        trailersResponse.subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()
                ).
                subscribe(new Observer<TrailersResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(TrailersResponseModel model) {
                        trailersAdapter.setTrailers(model.getTrailers());
                    }
                });

    }

    @Override
    public Loader<Movie> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Movie>(this) {

            Movie mMovie = null;

            @Override
            protected void onStartLoading() {
                if (mMovie != null) {
                    deliverResult(mMovie);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Movie loadInBackground() {
                String[] args = {String.valueOf(movie.getId())};
                try {
                    Uri uri = Movie.MovieEntry.CONTENT_URI;
                    Cursor cursor = getContentResolver().query(uri,
                            null,
                            Movie.MovieEntry.COLUMN_ID + "=?", args,
                            Movie.MovieEntry.COLUMN_VOTE_AVERAGE);
                    if (cursor.moveToNext()) {
                        return new Movie(cursor);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                }
                return null;
            }

            public void deliverResult(Movie data) {
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie data) {
        int res;
        if (data != null && data.isFavourite()) {
            res = getResources().getIdentifier("@android:drawable/btn_star_big_on", null, null);
            movie.setFavourite(true);
        } else {
            res = getResources().getIdentifier("@android:drawable/btn_star_big_off", null, null);
            movie.setFavourite(false);
        }
        addMoviesToFavoritesImageButton.setEnabled(true);
        addMoviesToFavoritesImageButton.setImageDrawable(getResources().getDrawable(res));

    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {

    }
}

