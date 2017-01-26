package info.kasimkovacevic.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import info.kasimkovacevic.popularmovies.R;
import info.kasimkovacevic.popularmovies.models.Trailer;
import info.kasimkovacevic.popularmovies.viewholders.MovieViewHolder;
import info.kasimkovacevic.popularmovies.viewholders.TrailerViewHolder;

/**
 * Created by kasimkovacevic1 on 1/25/17.
 */
public class TrailersAdapter extends RecyclerView.Adapter<TrailerViewHolder> {

    private List<Trailer> trailers;
    private Context context;


    public TrailersAdapter(Context context, List<Trailer> trailers) {
        this.context = context;
        this.trailers = trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailers_list_item, parent, false);
        TrailerViewHolder viewHolder = new TrailerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.bind(trailers.get(position), context);
    }

    @Override
    public int getItemCount() {
        return trailers != null ? trailers.size() : 0;
    }
}
