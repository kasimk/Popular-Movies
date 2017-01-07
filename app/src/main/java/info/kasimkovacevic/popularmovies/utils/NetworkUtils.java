/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.kasimkovacevic.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Scanner;

import info.kasimkovacevic.popularmovies.BuildConfig;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    /**
     * THE_MOVIE_DB_BASE_URL is used like base url for getting data from api.themoviedb.org
     */
    private final static String THE_MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/movie";
    /**
     * THE_MOVIE_DB_PHOTOS_URL is used like base url for getting photos from image.tmdb.org
     */
    private final static String THE_MOVIE_DB_PHOTOS_URL = "http://image.tmdb.org/t/p/";
    /**
     * THE_MOVIE_DB_API_KEY contains API key for api.themoviedb.org
     */
    private final static String THE_MOVIE_DB_API_KEY = BuildConfig.THE_MOVIE_DB_API_KEY;


    /**
     * PHOTO_SIZE_W185 param is used for getting photo with width of 185 px
     */
    private final static String PHOTO_SIZE_W185 = "w185";
    /**
     * PHOTO_SIZE_W342 param is used for getting photo with width of 342 px
     */
    private final static String PHOTO_SIZE_W342 = "w342";
    /**
     * PARAM_API_KEY is url request param
     */
    private final static String PARAM_API_KEY = "api_key";

    private final static String UTF_8_ENCODING = "UTF-8";


    /**
     * These method return {@link Uri} for getting movie poster photo
     *
     * @param posterPath movie poster photo path
     * @return {@link Uri} for getting movie poster photo
     */
    public static Uri buildPhotoUrl(String posterPath) {
        String decodedPath = null;
        try {
            decodedPath = URLDecoder.decode(posterPath, UTF_8_ENCODING);
        } catch (UnsupportedEncodingException e) {
            Log.e(NetworkUtils.class.getSimpleName(), e.getMessage());
            decodedPath = posterPath;
        }
        if (decodedPath.startsWith("/")) {
            decodedPath = decodedPath.substring(1, decodedPath.length());
        }
        return Uri.parse(THE_MOVIE_DB_PHOTOS_URL).buildUpon().appendPath(PHOTO_SIZE_W342).appendPath(decodedPath).build();
    }

    /**
     * These method return {@link URL} for fetch data from themoviedb.org
     *
     * @param moviesEnum is used for sorting movies, sorting can be by popularity and top rated value
     * @return object of {@link URL} for fetching data from themoviedb.org
     */
    public static URL buildUrl(MOVIES_ENUM moviesEnum) {
        Uri builtUri = Uri.parse(THE_MOVIE_DB_BASE_URL).buildUpon()
                .appendPath(moviesEnum.toString())
                .appendQueryParameter(PARAM_API_KEY, THE_MOVIE_DB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}