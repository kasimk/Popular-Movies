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

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import info.kasimkovacevic.popularmovies.models.Trailer;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    /**
     * THE_MOVIE_DB_BASE_URL is used like base url for getting data from api.themoviedb.org
     */
    public final static String THE_MOVIE_DB_BASE_URL = "http://api.themoviedb.org/";
    /**
     * THE_MOVIE_DB_PHOTOS_URL is used like base url for getting photos from image.tmdb.org
     */
    private final static String THE_MOVIE_DB_PHOTOS_URL = "http://image.tmdb.org/t/p/";

    private final static String THE_YOUTUBE_BASE_URL = "http://youtube.com";

    private final static String YOUTUBE_WATCH = "watch";
    private final static String YOUTUBE_VIDEO_QUERY = "v";

    /**
     * THE_MOVIE_DB_API_KEY contains API key for api.themoviedb.org
     */
    public final static String THE_MOVIE_DB_API_KEY = BuildConfig.THE_MOVIE_DB_API_KEY;

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
    public final static String PARAM_API_KEY = "api_key";

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

    public static Uri buildTrailerUrl(Trailer trailer) {
        return Uri.parse(THE_YOUTUBE_BASE_URL).buildUpon().appendPath(YOUTUBE_WATCH).appendQueryParameter(YOUTUBE_VIDEO_QUERY, trailer.getKey()).build();
    }


    /**
     * Check is device connected on any network
     *
     * @param context instance of {@link Context}
     * @return true if device is connected on internet, false if not
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}