package br.com.vagners.moviesearch.view.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.vagners.moviesearch.R;
import br.com.vagners.moviesearch.model.MovieModel;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vagnerss on 17/07/16.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private Context context;
    private List<MovieModel> list;

    public HomeAdapter(Context _context, List<MovieModel> _list) {
        this.context = _context;
        this.list = _list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home, parent, false);
        HomeAdapter.ViewHolder viewHolder = new HomeAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MovieModel model = this.list.get(position);
        holder.name.setText(model.getTitle());
        Picasso.with(this.context).load(model.getPoster()).into(holder.cover);

        holder.year.setText(model.getReleased());
        holder.genre.setText(model.getGenre());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cover)
        ImageView cover;

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.year)
        TextView year;

        @BindView(R.id.genre)
        TextView genre;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}