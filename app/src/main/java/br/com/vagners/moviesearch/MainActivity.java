package br.com.vagners.moviesearch;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import br.com.vagners.moviesearch.model.MovieModel;
import br.com.vagners.moviesearch.view.home.HomeAdapter;
import br.com.vagners.moviesearch.view.search.SearchActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private static int SEARCH_VIEW_CODE = 99;

    @BindView(R.id.btn_add)
    FloatingActionButton btnAdd;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.list)
    RecyclerView listView;

    @BindView(R.id.no_result)
    TextView noResult;

    private Realm realm;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSearchView();
            }
        });

        realm = Realm.getDefaultInstance();
        layoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(layoutManager);
        loadMovies();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void openSearchView() {
        if (isOnline())
            startActivityForResult(new Intent(this, SearchActivity.class), SEARCH_VIEW_CODE);
        else
            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SEARCH_VIEW_CODE) {
            if (resultCode == RESULT_OK) {

                MovieModel movieModel = (MovieModel) data.getSerializableExtra(MovieModel.class.getName());
                addMovie(movieModel);
                loadMovies();
            }
        }
    }

    public void addMovie(MovieModel model) {
        realm.beginTransaction();
        realm.insert(model);
        realm.commitTransaction();
    }

    public void loadMovies() {
        RealmResults<MovieModel> result = realm.where(MovieModel.class).findAll();
        if (result.size() != 0) {
            adapter = new HomeAdapter(this, result);
            listView.setAdapter(adapter);
            noResult.setVisibility(View.GONE);
        } else {
            noResult.setVisibility(View.VISIBLE);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
