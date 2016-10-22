package udacity.popular.tejeswar.popularmovie.flowadapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import udacity.popular.tejeswar.popularmovie.R;
import udacity.popular.tejeswar.popularmovie.activities.MovieDetailActivity;
import udacity.popular.tejeswar.popularmovie.database.MovieContract;
import udacity.popular.tejeswar.popularmovie.parcelable.FavouriteMovie;
import udacity.popular.tejeswar.popularmovie.parcelable.Review;
import udacity.popular.tejeswar.popularmovie.parcelable.Trailer;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

/**
 * code tutorial from :
 * http://www.devexchanges.info/2015/11/making-carousel-layout-in-android.html
 */

public class MainCarousel extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private FeatureCoverFlow mCoverFlow;
    //private CoverFlowAdapter mAdapter;
    private CoverFlowCursorAdapter mAdapter;
    private static final int CURSOR_LOADER_ID = 0;

    private ArrayList<FavouriteMovie> list;
    private List<Trailer> movieTrailersList;
    private List<Review> movieReviewsList;


    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.

    static final int COL_MOVIE_ID = 1;
    static final int COL_MOVIE_TITLE = 2;
    static final int COL_MOVIE_YEAR = 3;
    static final int COL_MOVIE_DURATION = 4;
    static final int COL_MOVIE_RATING = 5;
    static final int COL_MOVIE_OVERVIEW = 6;
    static final int COL_MOVIE_POSTER = 7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_carousel);
        mCoverFlow = (FeatureCoverFlow) findViewById(R.id.coverflow);

        //displayFavouriteMovies();

        // initialize our CursorAdapter

        mAdapter = new CoverFlowCursorAdapter(this, null, 0);

        mCoverFlow.setAdapter(mAdapter);

        mCoverFlow.setOnScrollPositionListener(onScrollListener());

        mCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {

                    Intent i = new Intent(MainCarousel.this, MovieDetailActivity.class)
                            //pass the selected movie_id to the next Activity
                            .putExtra("flagData", 1)
                            .putExtra("movieId", cursor.getString(COL_MOVIE_ID))
                            .putExtra("title", cursor.getString(COL_MOVIE_TITLE))
                            .putExtra("year", cursor.getString(COL_MOVIE_YEAR))
                            .putExtra("rating", cursor.getString(COL_MOVIE_RATING))
                            .putExtra("overview", cursor.getString(COL_MOVIE_OVERVIEW))
                            .putExtra("duration", cursor.getString(COL_MOVIE_DURATION));

                    MainCarousel.this.startActivity(i);


                }
            }
        });

    }

    @Override
    protected void onStart() {


        // initialize loader
        getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onStart();

    }

    private FeatureCoverFlow.OnScrollPositionListener onScrollListener() {
        return new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                Log.v("MainActivity", "position: " + position);
            }

            @Override
            public void onScrolling() {
                Log.i("MainCarousel", "scrolling");
            }
        };
    }


    private JSONArray getFavouriteMovies() throws JSONException {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String items = preferences.getString("favourites", "");
        return new JSONArray(items);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        // Attach loader to our flavors database query
        // run when loader is initialized
        return new CursorLoader(this,
                MovieContract.Favourites.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Set the cursor in our CursorAdapter once the Cursor is loaded
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}