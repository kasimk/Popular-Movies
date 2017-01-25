package info.kasimkovacevic.popularmovies.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.kasimkovacevic.popularmovies.R;
import info.kasimkovacevic.popularmovies.adapters.TrailersAdapter;
import info.kasimkovacevic.popularmovies.data.RestClientRouter;
import info.kasimkovacevic.popularmovies.data.TheMovieDBService;
import info.kasimkovacevic.popularmovies.models.Movie;
import info.kasimkovacevic.popularmovies.models.TrailersResponseModel;
import info.kasimkovacevic.popularmovies.utils.NetworkUtils;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String POPULAR_MOVIES_MOVIE_EXTRA = "info.kasimkovacevic.popularmovies.MOVIE_EXTRA";
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


    private TrailersAdapter trailersAdapter;
    private TheMovieDBService theMovieDBService;
    private Observable<TrailersResponseModel> trailersResponse;


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
                Uri uri = getContentResolver().insert(Movie.MovieEntry.CONTENT_URI, movie.getContentValues());
                if (uri != null) {
                    Toast.makeText(MovieDetailsActivity.this, uri.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
        trailersAdapter = new TrailersAdapter(MovieDetailsActivity.this, null);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(MovieDetailsActivity.this);
        trailersRecyclerView.setLayoutManager(gridLayoutManager);
        trailersRecyclerView.setAdapter(trailersAdapter);
        theMovieDBService = RestClientRouter.get();
        loadTrailers();
    }

    private void loadTrailers() {
        trailersResponse = theMovieDBService.listTrailersForMovie(movie.getId(), NetworkUtils.THE_MOVIE_DB_API_KEY);
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

}

