package info.kasimkovacevic.popularmovies.activities;

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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.kasimkovacevic.popularmovies.R;
import info.kasimkovacevic.popularmovies.adapters.ReviewsAdapter;
import info.kasimkovacevic.popularmovies.adapters.TrailersAdapter;
import info.kasimkovacevic.popularmovies.data.DBHelper;
import info.kasimkovacevic.popularmovies.data.RestClientRouter;
import info.kasimkovacevic.popularmovies.data.TheMovieDBService;
import info.kasimkovacevic.popularmovies.models.Movie;
import info.kasimkovacevic.popularmovies.models.Review;
import info.kasimkovacevic.popularmovies.models.Trailer;
import info.kasimkovacevic.popularmovies.models.wrappers.ReviewsResponseModel;
import info.kasimkovacevic.popularmovies.models.wrappers.TrailersResponseModel;
import info.kasimkovacevic.popularmovies.utils.NetworkUtils;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.view.View.GONE;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String MOVIE_EXTRA = "info.kasimkovacevic.popularmovies.MOVIE_EXTRA";
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();
    private static final int MOVIE_TASK_ID = 101;
    private static final int REVIEWS_TASK_ID = 102;
    private static final int TRAILERS_TASK_ID = 103;

    @BindView(R.id.tv_movie_title)
    protected TextView movieTitleTextView;
    @BindView(R.id.tv_movie_overview)
    protected TextView movieOverviewTextView;
    @BindView(R.id.tv_user_rating)
    protected TextView movieRatingTextView;
    @BindView(R.id.tv_release_date)
    protected TextView movieReleaseDateTextView;
    @BindView(R.id.iv_movie_poster)
    protected ImageView moviePosterImageView;
    @BindView(R.id.ib_add_movie_to_favorites)
    protected ImageButton addMoviesToFavoritesImageButton;
    @BindView(R.id.rv_trailers)
    protected RecyclerView trailersRecyclerView;
    @BindView(R.id.rv_reviews)
    protected RecyclerView reviewsRecyclerView;
    @BindView(R.id.v_divider)
    protected View firstLineDivider;
    @BindView(R.id.v_divider_2)
    protected View secondLineDivider;
    @BindView(R.id.tv_reviews_label)
    protected TextView reviewsLabel;

    private Movie movie;
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
        if (intent.getExtras() != null && intent.hasExtra(MOVIE_EXTRA)) {
            movie = intent.getParcelableExtra(MOVIE_EXTRA);
            configureUI();
        }
    }

    private void configureUI() {
        movieTitleTextView.setText(movie.getOriginalTitle());
        movieOverviewTextView.setText(movie.getOverview());


        movieRatingTextView.setText(String.valueOf(movie.getVoteAverage()));
        movieReleaseDateTextView.setText(movie.getReleaseDate());
        Picasso.with(MovieDetailsActivity.this).load(NetworkUtils.buildPhotoUrl(movie.getPosterPath())).into(moviePosterImageView);

        //Disabled until movie is loaded from db
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
                DBHelper.insertOrUpdateMovie(MovieDetailsActivity.this, movie, false);
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
        getSupportLoaderManager().initLoader(MOVIE_TASK_ID, null, movieCallbacks);
    }

    /**
     * If has internet connection load reviews from api otherwise load trailers from DB
     */
    private void loadReviews() {
        if (NetworkUtils.isNetworkAvailable(MovieDetailsActivity.this)) {
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
                            if (model.getReviews().size() == 0) {
                                hideReviews();
                            }
                            reviewsAdapter.setReviews(model.getReviews());
                            for (Review review : model.getReviews()) {
                                review.setMovieId(movie.getId());
                                DBHelper.insertOrUpdateReview(MovieDetailsActivity.this, review);
                            }
                        }
                    });
        } else {
            loadReviewsFromDB();
        }
    }

    /**
     * If has internet connection load trailers from api otherwise load trailers from DB
     */
    private void loadTrailers() {
        if (NetworkUtils.isNetworkAvailable(MovieDetailsActivity.this)) {
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
                            if (model.getTrailers().size() == 0) {
                                hideTrailers();
                            }
                            trailersAdapter.setTrailers(model.getTrailers());
                            for (Trailer trailer : model.getTrailers()) {
                                trailer.setMovieId(movie.getId());
                                DBHelper.insertOrUpdateTrailer(MovieDetailsActivity.this, trailer);
                            }
                        }
                    });
        } else {
            loadTrailersFromDB();
        }

    }

    public void loadReviewsFromDB() {
        getSupportLoaderManager().initLoader(REVIEWS_TASK_ID, null, reviewsCallback);
    }

    public void loadTrailersFromDB() {
        getSupportLoaderManager().initLoader(TRAILERS_TASK_ID, null, trailersCallback);
    }

    private void hideReviews() {
        reviewsRecyclerView.setVisibility(GONE);
        secondLineDivider.setVisibility(GONE);
        reviewsLabel.setVisibility(GONE);
    }

    private void hideTrailers() {
        trailersRecyclerView.setVisibility(GONE);
        firstLineDivider.setVisibility(GONE);
    }

    /**
     * Definition of loaders
     */
    private LoaderManager.LoaderCallbacks<Movie> movieCallbacks
            = new LoaderManager.LoaderCallbacks<Movie>() {

        @Override
        public Loader<Movie> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<Movie>(MovieDetailsActivity.this) {
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

    };

    private LoaderManager.LoaderCallbacks<List<Review>> reviewsCallback
            = new LoaderManager.LoaderCallbacks<List<Review>>() {

        @Override
        public Loader<List<Review>> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<List<Review>>(MovieDetailsActivity.this) {

                List<Review> reviews = null;

                @Override
                protected void onStartLoading() {
                    if (reviews != null) {
                        deliverResult(reviews);
                    } else {
                        forceLoad();
                    }
                }

                @Override
                public List<Review> loadInBackground() {
                    List<Review> reviews = new ArrayList<>();
                    String[] args = {String.valueOf(movie.getId())};
                    try {
                        Uri uri = Review.ReviewEntry.CONTENT_URI;
                        Cursor cursor = getContentResolver().query(uri,
                                null,
                                Review.ReviewEntry.COLUMN_MOVIE_ID + "=?", args,
                                null);
                        while (cursor.moveToNext()) {
                            reviews.add(new Review(cursor));
                        }
                        return reviews;
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to asynchronously load reviews.");
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                public void deliverResult(List<Review> data) {
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<List<Review>> loader, List<Review> data) {
            if (data == null || data.size() == 0) {
                hideReviews();
            }
            reviewsAdapter.setReviews(data);
        }

        @Override
        public void onLoaderReset(Loader<List<Review>> loader) {
            reviewsAdapter.setReviews(null);
        }
    };


    private LoaderManager.LoaderCallbacks<List<Trailer>> trailersCallback
            = new LoaderManager.LoaderCallbacks<List<Trailer>>() {

        @Override
        public Loader<List<Trailer>> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<List<Trailer>>(MovieDetailsActivity.this) {

                List<Trailer> trailers = null;

                @Override
                protected void onStartLoading() {
                    if (trailers != null) {
                        deliverResult(trailers);
                    } else {
                        forceLoad();
                    }
                }

                @Override
                public List<Trailer> loadInBackground() {
                    List<Trailer> trailers = new ArrayList<>();
                    String[] args = {String.valueOf(movie.getId())};
                    try {
                        Uri uri = Trailer.TrailerEntry.CONTENT_URI;
                        Cursor cursor = getContentResolver().query(uri,
                                null,
                                Trailer.TrailerEntry.COLUMN_MOVIE_ID + "=?", args,
                                null);
                        while (cursor.moveToNext()) {
                            trailers.add(new Trailer(cursor));
                        }
                        return trailers;
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to asynchronously load trailers.");
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                public void deliverResult(List<Trailer> data) {
                    if (data == null || data.size() == 0) {
                        hideTrailers();
                    }
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<List<Trailer>> loader, List<Trailer> data) {
            trailersAdapter.setTrailers(data);
        }

        @Override
        public void onLoaderReset(Loader<List<Trailer>> loader) {
            trailersAdapter.setTrailers(null);
        }
    };


    /**
     * Definition of loaders END
     */

}

