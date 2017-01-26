package info.kasimkovacevic.popularmovies.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by kasimkovacevic1 on 1/26/17.
 */

public class ScreenUtils {

    /**
     * @return device width in density pixels
     */
    public static int getScreenWidth() {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        return (int) (displayMetrics.widthPixels / displayMetrics.density);
    }
}
