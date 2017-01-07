package info.kasimkovacevic.popularmovies.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import info.kasimkovacevic.popularmovies.listeners.FetchDataListener;
import info.kasimkovacevic.popularmovies.models.Error;
import info.kasimkovacevic.popularmovies.models.Movie;
import info.kasimkovacevic.popularmovies.utils.NetworkUtils;

/**
 * Created by kasimkovacevic1 on 1/6/17.
 */
public class FetchDataAsyncTask extends AsyncTask<URL, Void, FetchDataAsyncTask.ResponseModel> {

    public static final String JSON_ARRAY_RESULTS_KEY = "results";
    FetchDataListener fetchDataListener;

    public FetchDataAsyncTask(FetchDataListener fetchDataListener) {
        this.fetchDataListener = fetchDataListener;
    }

    /**
     * These method will call {@link FetchDataListener} onRequestStart
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        fetchDataListener.onRequestStart();
    }

    /**
     * These method will retrieve data over HTTP and parse data into {@link ResponseModel}
     *
     * @param urls represents {@link URL} which will be contacted
     * @return object of {@link ResponseModel}, if fetching or parsing data fails response model will contain error, otherwise data will be stored in response model
     */
    @Override
    protected ResponseModel doInBackground(URL... urls) {
        ResponseModel responseModel = new ResponseModel();
        try {
            String data = NetworkUtils.getResponseFromHttpUrl(urls[0]);
            JSONObject response = new JSONObject(data);
            JSONArray results = response.getJSONArray(JSON_ARRAY_RESULTS_KEY);
            ObjectMapper mapper = new ObjectMapper();
            int moviesLength = results.length();
            Movie[] movies = new Movie[moviesLength];
            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                Movie movie = mapper.readValue(result.toString(), Movie.class);
                movies[i] = movie;
            }
            responseModel.setMovies(Arrays.asList(movies));
        } catch (JSONException | IOException e) {
            Log.e(FetchDataAsyncTask.class.getSimpleName(), e.getMessage());
            Error error = new Error();
            error.setErrorMessage(e.getMessage());
            responseModel.setError(error);
        }
        return responseModel;
    }

    /**
     * These method will call {@link FetchDataListener} onFailure if response model contains error or onSuccess if response model contains data
     *
     * @param responseModel can contain data from api or error if fetching or parsing data fails
     */
    @Override
    protected void onPostExecute(ResponseModel responseModel) {
        if (responseModel.getError() != null) {
            fetchDataListener.onFailure(responseModel.getError());
        } else {
            fetchDataListener.onSuccess(responseModel.getMovies());
        }
    }


    /**
     * Response model can contain data from api or error if fetching or parsing data fails
     */
    public class ResponseModel {
        private Error error;
        private List<Movie> movies;

        public Error getError() {
            return error;
        }

        public void setError(Error error) {
            this.error = error;
        }

        public List<Movie> getMovies() {
            return movies;
        }

        public void setMovies(List<Movie> movies) {
            this.movies = movies;
        }
    }
}
