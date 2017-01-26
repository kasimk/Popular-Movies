package info.kasimkovacevic.popularmovies.viewholders;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.kasimkovacevic.popularmovies.R;
import info.kasimkovacevic.popularmovies.models.Trailer;
import info.kasimkovacevic.popularmovies.utils.NetworkUtils;

/**
 * Created by kasimkovacevic1 on 1/25/17.
 */
public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.tv_trailer_name)
    protected TextView mTrailerNameTextView;
    protected Trailer mTrailer;

    public TrailerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Uri uri = NetworkUtils.buildTrailerUrl(mTrailer);
        Intent startYoutubeIntent = new Intent(Intent.ACTION_VIEW, uri);
        PackageManager packageManager = v.getContext().getPackageManager();
        if (startYoutubeIntent.resolveActivity(packageManager) != null) {
            v.getContext().startActivity(startYoutubeIntent);
        } else {
            Toast.makeText(v.getContext(), R.string.youtube_video_app_missing, Toast.LENGTH_LONG).show();
        }
    }

    public void bind(Trailer trailer, Context context) {
        this.mTrailer = trailer;
        mTrailerNameTextView.setText(trailer.getName());
    }
}
