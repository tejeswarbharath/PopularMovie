package udacity.popular.tejeswar.popularmovie.activities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.Volley;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Picasso;
import udacity.popular.tejeswar.popularmovie.GridSpacingItemDecoration;
import udacity.popular.tejeswar.popularmovie.R;
import udacity.popular.tejeswar.popularmovie.Utils;
import udacity.popular.tejeswar.popularmovie.database.MovieContract;
import udacity.popular.tejeswar.popularmovie.database.MovieContract.Details;
import udacity.popular.tejeswar.popularmovie.database.MovieDataBaseHandler;
import udacity.popular.tejeswar.popularmovie.fragment.MovieDetailFragment;
import udacity.popular.tejeswar.popularmovie.parcelable.MovieImage;
import udacity.popular.tejeswar.popularmovie.BuildConfig;

/**
 * Created by tejeswar on 10/5/2016.
 */

public class MovieActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>

{

    private boolean mTwoPane;

    private List<String> movieImages = new ArrayList<>();

    private ArrayList<MovieImage> list;

    private SharedPreferences mSharedPreferences;

    private String sortOption;

    private MovieItemRecyclerViewAdapter mAdapter;

    private RecyclerView recyclerView;

    private ImageLoader imageLoader;

    private String encodedString = "";

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState)

    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_list);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(myToolbar);

        MovieDataBaseHandler movieHelper =new MovieDataBaseHandler(this);

        movieHelper.getWritableDatabase();

        if (imageLoader == null)

        {

                imageLoader = ImageLoader.getInstance();

                imageLoader.init(ImageLoaderConfiguration.createDefault(this));

        }

        recyclerView = (RecyclerView) findViewById(R.id.movie_list);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        //add spacing
        int spanCount = 2; // 2 columns
        int spacing = 20; // 20px
        boolean includeEdge = false;

        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        assert recyclerView != null;

        setupRecyclerView(recyclerView);

        if (findViewById(R.id.movie_detail_container) != null)

        {

            mTwoPane = true;

        }

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        sortOption = mSharedPreferences.getString(getString(R.string.pref_sort_key), "popular");

        updateMovies();

    }

    @Override

    protected void onStart()

    {

        super.onStart();

        updateMovies();

    }

    private void updateMovies()

    {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        sortOption = mSharedPreferences.getString(getString(R.string.pref_sort_key), "popular");

        requestMovies();

    }

    String addMovie(long movieId, String title, String image)

    {

        String id = String.valueOf(movieId);

        String movie_id = "";

        Cursor locationCursor = this.getContentResolver().query
                (Details.CONTENT_URI,
                        new String[]{Details.MOVIE_ID},
                        Details.MOVIE_ID + " = ?",
                        new String[]{id},
                        null);

        if (locationCursor.moveToFirst())

        {

                int movieIdIndex = locationCursor.getColumnIndex(Details.MOVIE_ID);

                movie_id = locationCursor.getString(movieIdIndex);

        }

        else

        {

            ContentValues movieValues = new ContentValues();

            movieValues.put(Details.MOVIE_ID, id);

            movieValues.put(Details.TITLE, title);

            movieValues.put(Details.POSTER, image);

            Uri insertedUri = this.getContentResolver().insert(
                    Details.CONTENT_URI,
                    movieValues);

            movie_id = String.valueOf(ContentUris.parseId(insertedUri));

        }

        locationCursor.close();

        return movie_id;

    }

    private void requestMovies()

    {

        final String BASE_PATH = "http://api.themoviedb.org/3/movie/";

        final String sort_order = sortOption;

        final String api_key = "?api_key=" + BuildConfig.OPEN_WEATHER_MAP_API_KEY ;

        Log.e("SORT OPTION", sort_order);

        String original_url = BASE_PATH + sort_order + api_key;

        RequestQueue queue = Volley.newRequestQueue(MovieActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, original_url,

                new Response.Listener<String>()

                {

                    @Override
                    public void onResponse(String response)

                    {

                        System.out.println(response);

                        try

                        {

                            list = new ArrayList<>();

                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray results = jsonObject.getJSONArray("results");

                            for (int i = 0; i < results.length(); i++)

                            {

                                JSONObject obj = results.getJSONObject(i);

                                long movie_id = obj.getLong("id");

                                String movie_name = obj.getString("title");

                                String movie_image = obj.getString("poster_path");

                                final String IMAGE_BASE_PATH = "http://image.tmdb.org/t/p/";

                                String image_size = "w342";

                                String posterPath = IMAGE_BASE_PATH + image_size + movie_image;

                                MovieImage movieImage = new MovieImage(movie_id, movie_name, posterPath);

                                list.add(movieImage);

                                Log.v("xxxxx-add", "adding movie: " + movie_name);

                                movieImages.add(posterPath);

                                loadImageBitmap(posterPath);

                                addMovie(movie_id, movie_name, posterPath);

                            }

                            for ( MovieImage img : list)

                            {

                                Log.d("MOVIE ID: ", String.valueOf(img.getId()));
                                Log.d("MOVIE NAME: ", img.getName());
                                Log.d("MOVIE IMAGE: ", img.getImage());

                            }

                            int itemCount = movieImages.size();

                            System.out.println("IMAGE COUNT...>>>>>>>>>" + itemCount);

                            mAdapter.setItemList(list);

                            mAdapter.notifyDataSetChanged();

                            recyclerView.getAdapter().notifyDataSetChanged();

                        }

                        catch (JSONException e)
                        {

                            e.printStackTrace();

                        }

                    }
                },

                new Response.ErrorListener()

                {

                    @Override

                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof NoConnectionError)
                        {

                            Utils.showSuccessDialog(MovieActivity.this, R.string.no_connection, R.string.net).show();

                        }

                    }

                });

        queue.add(stringRequest);

    }

    private void loadImageBitmap(String image_url)

    {

        imageLoader.loadImage(image_url, new SimpleImageLoadingListener() {

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)

            {

                ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();

                loadedImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOS);

                encodedString = Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);

            }

        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)

    {

        getMenuInflater().inflate(R.menu.menu_popular_movies, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }


        if (id == R.id.action_fav)
        {

            try {
                if (Utils.getFavouriteMovies(this) != null)
                {
                    Intent intent = new Intent(this, FavouriteListActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Utils.showSuccessDialog(this, R.string.action_favorite, R.string.no_favorites).show();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return super.onOptionsItemSelected(item);

    }

    private void setupRecyclerView(RecyclerView recyclerView)
    {

        mAdapter = new MovieItemRecyclerViewAdapter(this, movieImages, list);

        recyclerView.setAdapter(mAdapter);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)

    {

        return new CursorLoader(this,
                MovieContract.Details.CONTENT_URI,
                null,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {

    }

    public class MovieItemRecyclerViewAdapter extends RecyclerView.Adapter<MovieItemRecyclerViewAdapter.Viewholder>
    {

        private List<String> imageUrls;

        private List<MovieImage> movieImages;

        private Context context;

        public MovieItemRecyclerViewAdapter(Context context, List<String> imageUrls, List<MovieImage> movieImages)
        {

            this.context = context;
            this.imageUrls = imageUrls;
            this.movieImages = movieImages;

        }

        @Override
        public int getItemCount()

        {

            if (movieImages != null)
                return movieImages.size();
            return 0;

        }

        public void setItemList(List<MovieImage> movieImages)

        {

            this.movieImages = movieImages;

        }

        @Override

        public MovieItemRecyclerViewAdapter.Viewholder onCreateViewHolder(ViewGroup parent, int viewType)

        {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_list_content, parent, false);

            return new Viewholder(view);

        }

        @Override

        public void onBindViewHolder(final Viewholder holder, int position)

        {

            holder.mItem = movieImages.get(position);

            Picasso.with(context)
                    .load(movieImages.get(position).getImage())
                    .fit()
                    .error(R.drawable.ic_error)
                    .placeholder(R.drawable.ic_loading)
                    .into(holder.mImageView);

            holder.mTitleView.setText(movieImages.get(position).getName());

            holder.mView.setOnClickListener(new View.OnClickListener()

            {

                @Override
                public void onClick(View v)
                {

                    System.out.println(holder.mItem.getName());
                    if (mTwoPane)
                    {

                        Bundle arguments = new Bundle();
                        arguments.putString(MovieDetailFragment.ARG_ITEM_ID, Long.toString(holder.mItem.getId()));
                        arguments.putString("movieId", Long.toString(holder.mItem.getId()));
                        arguments.putInt("flagData", 0);
                        arguments.putString("title", holder.mItem.getName());

                        MovieDetailFragment fragment = new MovieDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.movie_detail_container, fragment)
                                .commit();

                    }
                    else
                    {

                        Context context = v.getContext();

                        Intent intent = new Intent(context, MovieDetailActivity.class);

                        intent.putExtra("movieId", Long.toString(holder.mItem.getId()))
                                .putExtra("flagData", 0)
                                .putExtra("title", holder.mItem.getName());

                        context.startActivity(intent);

                    }

                }

            });

        }

        public class Viewholder extends RecyclerView.ViewHolder

        {

            public final View mView;
            public final ImageView mImageView;
            public final TextView mTitleView;

            public MovieImage mItem;

            public Viewholder(View view)

            {

                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.img_movie);
                mTitleView = (TextView) view.findViewById(R.id.txt_name);

            }

            @Override

            public String toString()

            {

                return super.toString() + " '" + mTitleView.getText() + "'";

            }

        }
    }
}