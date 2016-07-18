package br.com.vagners.moviesearch.view.search;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.Serializable;

import br.com.vagners.moviesearch.R;
import br.com.vagners.moviesearch.controller.MovieController;
import br.com.vagners.moviesearch.model.MovieModel;
import br.com.vagners.moviesearch.view.home.HomeAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vagnerss on 17/07/16.
 */

public class SearchActivity extends AppCompatActivity {

    private static String TAG = SearchActivity.class.getName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.cover)
    ImageView cover;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.year)
    TextView year;

    @BindView(R.id.genre)
    TextView genre;

    @BindView(R.id.country)
    TextView country;

    @BindView(R.id.director)
    TextView director;


    @BindView(R.id.plot)
    TextView plot;

    @BindView(R.id.actors)
    TextView actors;

    @BindView(R.id.awards)
    TextView awards;

    @BindView(R.id.writer)
    TextView writer;

    @BindView(R.id.btn_add)
    Button btnAdd;


    private SearchView searchView;

    private MovieModel movieModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMovie();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        final MenuItem actionMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) actionMenuItem.getActionView();
        actionMenuItem.expandActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadMovie(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadMovie(String title) {
        MovieController.getInstance().getMovie(title, new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (response.body().getResponse().equals("True"))
                    showMovie(response.body());
                else
                    defaultMovie();
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                defaultMovie();
            }
        });
    }

    public void showMovie(MovieModel model) {
        name.setText(model.getTitle());
        Picasso.with(this).load(model.getPoster()).into(cover);

        year.setText(model.getReleased());
        genre.setText(model.getGenre());
        country.setText(model.getCountry());
        director.setText(model.getDirector());

        plot.setText(model.getPlot());
        actors.setText(model.getActors());
        awards.setText(model.getAwards());
        writer.setText(model.getWriter());

        movieModel = model;
    }

    public void defaultMovie() {
        name.setText(getString(R.string.nda));
        cover.setImageResource(R.drawable.no_cover);

        year.setText(getString(R.string.nda));
        genre.setText(getString(R.string.nda));
        country.setText(getString(R.string.nda));
        director.setText(getString(R.string.nda));

        plot.setText(getString(R.string.nda));
        actors.setText(getString(R.string.nda));
        awards.setText(getString(R.string.nda));
        writer.setText(getString(R.string.nda));

        Toast.makeText(this, getString(R.string.no_movie), Toast.LENGTH_LONG).show();
    }

    public void addMovie() {
        if (movieModel != null) {
            Intent intent = this.getIntent();
            intent.putExtra(MovieModel.class.getName(), (Serializable) movieModel);
            this.setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, getString(R.string.search_validation), Toast.LENGTH_LONG).show();
        }
    }

}
