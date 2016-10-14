package udacity.popular.tejeswar.popularmovie.activities;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import udacity.popular.tejeswar.popularmovie.R;
import udacity.popular.tejeswar.popularmovie.fragment.MovieDetailFragment;
import udacity.popular.tejeswar.popularmovie.parcelable.FavouriteMovie;
import udacity.popular.tejeswar.popularmovie.parcelable.Trailer;
import udacity.popular.tejeswar.popularmovie.parcelable.Review;
import udacity.popular.tejeswar.popularmovie.utils;
import udacity.popular.tejeswar.popularmovie.GridSpacingItemDecoration;
import org.json.JSONObject;
import static android.support.v4.app.NavUtils.navigateUpFromSameTask;

/**
 * Created by tejeswar on 10/9/2016.
 */

public class FavouriteListActivity extends AppCompatActivity

{

    public boolean mTwoPane;

    private RecyclerView recyclerView;

    private FavouriteItemRecyclerViewAdapter mAdapter;

    private ArrayList<FavouriteMovie> list;

    private List<Trailer> movieTrailersList;

    private List<Review> movieReviewsList;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_favourite_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        toolbar.setTitle(getTitle());

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null)

        {

            actionBar.setDisplayHomeAsUpEnabled(true);

        }

        recyclerView = (RecyclerView) findViewById(R.id.favourite_list);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        assert recyclerView != null;

        displayFavouriteMovies(recyclerView);

        int spanCount = 2;

        int spacing = 20;

        boolean includeEdge = false;

        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        if (findViewById(R.id.favourite_detail_container) != null)
        {

            mTwoPane = true;

        }

    }

    @Override

    protected void onStart()
    {

        super.onStart();

        displayFavouriteMovies(recyclerView);

    }

    private JSONArray getFavouriteMovies() throws JSONException
    {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String items = preferences.getString("favourites", "");

        return new JSONArray(items);

    }


    private void displayFavouriteMovies(RecyclerView recyclerView)

    {

        list = new ArrayList<>();

        try

        {

            JSONArray arr = getFavouriteMovies();

            for (int i = 0; i < arr.length(); i++)

            {

                JSONObject obj = arr.getJSONObject(i);
                String movie_id = obj.getString("movie_id");
                String movie_name = obj.getString("movie_name");
                String movie_image = obj.getString("movie_image");
                String movie_overview = obj.getString("movie_overview");
                String movie_date = obj.getString("movie_date");
                String movie_vote = obj.getString("movie_vote");
                String movie_duration = obj.getString("movie_duration");
                JSONArray trailers = obj.getJSONArray("movie_trailers");
                JSONArray reviews = obj.getJSONArray("movie_reviews");


                if (trailers.length() > 0)

                {

                    movieTrailersList = new ArrayList<>();

                    for (int j = 0; j < trailers.length(); j++)

                    {

                        JSONObject trailer = trailers.getJSONObject(j);

                        String trailer_num = trailer.getString("trailer_num");

                        String trailer_url = trailer.getString("trailer_url");

                        Trailer t = new Trailer(trailer_num, trailer_url);

                        movieTrailersList.add(t);

                    }

                }

                if (reviews.length() >= 0)

                {

                    movieReviewsList = new ArrayList<>();

                    for (int j = 0; j < reviews.length(); j++)

                    {

                        JSONObject reviewObj = reviews.getJSONObject(j);

                        String review_author = reviewObj.getString("review_author");

                        String review_content = reviewObj.getString("review_content");

                        Review rev = new Review(review_author, review_content);

                        movieReviewsList.add(rev);

                    }

                }

                System.out.println("TRAILER SIZE---------->" + movieTrailersList.size());

                System.out.println("MOVIE NAME = " + movie_name);

                for (Trailer t : movieTrailersList)

                {

                    System.out.println("TRAILER: " + t.getTrailer_url());

                }

                FavouriteMovie fav = new FavouriteMovie(movie_date,movie_duration,movie_id, movie_image, movie_name,movie_overview,movieReviewsList,movieTrailersList,movie_vote);

                list.add(fav);

                System.out.println("FAVOURITE MOVIES SIZE---------> " + list.size());

                setupRecyclerView(recyclerView);

            }

        }

        catch (JSONException e)

        {

            e.printStackTrace();

            Log.e("ERROR", e.getMessage());

        }

    }


    @Override

    public boolean onOptionsItemSelected(MenuItem item)

    {

        int id = item.getItemId();

        if (id == android.R.id.home)

        {

            navigateUpFromSameTask(this);

            return true;

        }

        return super.onOptionsItemSelected(item);

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView)

    {

        mAdapter = new FavouriteItemRecyclerViewAdapter(list);

        mAdapter.setItemList(list);

        mAdapter.notifyDataSetChanged();

        recyclerView.setAdapter(mAdapter);

        recyclerView.getAdapter().notifyDataSetChanged();

    }


    public class FavouriteItemRecyclerViewAdapter extends RecyclerView.Adapter<FavouriteItemRecyclerViewAdapter.Viewholder>

    {

        private ArrayList<FavouriteMovie> movieList;

        public FavouriteItemRecyclerViewAdapter(ArrayList<FavouriteMovie> list)

        {

            this.movieList = list;

        }

        public class Viewholder extends RecyclerView.ViewHolder

        {

            public final View mView;

            public final ImageView mImageView;

            public final TextView mTitleView;

            public FavouriteMovie mItem;

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

        @Override

        public Viewholder onCreateViewHolder(ViewGroup parent, int viewType)

        {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_content, parent, false);

            return new Viewholder(view);

        }

        //Displaying data for Tablet

        @Override

        public void onBindViewHolder(final Viewholder holder, int position)

        {

            holder.mItem = movieList.get(position);

            holder.mTitleView.setText(holder.mItem.getName());

            holder.mImageView.setImageBitmap(utils.decodeBase64Image(holder.mItem.getImage()));

            holder.mView.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v)
                {

                    if (mTwoPane)

                    {

                        Bundle arguments = new Bundle();
                        arguments.putString(MovieDetailFragment.ARG_ITEM_ID, holder.mItem.getId());
                        arguments.putString("movieId", holder.mItem.getId());
                        arguments.putInt("flagData", 1);
                        arguments.putString("title", holder.mItem.getName());
                        arguments.putString("year", holder.mItem.getDate());
                        arguments.putString("rating", holder.mItem.getVote());
                        arguments.putString("overview", holder.mItem.getOverview());
                        arguments.putString("poster", holder.mItem.getImage());
                        arguments.putString("duration", holder.mItem.getDuration());

                        arguments.putParcelableArrayList("trailers", (ArrayList<? extends Parcelable>) holder.mItem.getTrailers());
                        arguments.putParcelableArrayList("reviews", (ArrayList<? extends Parcelable>) holder.mItem.getReviews());

                        MovieDetailFragment fragment = new MovieDetailFragment();

                        fragment.setArguments(arguments);

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.favourite_detail_container, fragment)
                                .commit();

                    }

                    else

                    {

                        Context context = v.getContext();

                        Intent intent = new Intent(context, MovieDetailActivity.class);

                        intent.putExtra(MovieDetailFragment.ARG_ITEM_ID, holder.mItem.getId())
                                .putExtra("flagData", 1)
                                .putExtra("movieId", holder.mItem.getId())
                                .putExtra("title", holder.mItem.getName())
                                .putExtra("year", holder.mItem.getDate())
                                .putExtra("rating", holder.mItem.getVote())
                                .putExtra("overview", holder.mItem.getOverview())
                                .putExtra("duration", holder.mItem.getDuration());

                        context.startActivity(intent);

                    }
                }
            });
        }

        @Override
        public int getItemCount()

        {

            if (movieList != null)
                return movieList.size();
            return 0;

        }

        public void setItemList(ArrayList<FavouriteMovie> list)

        {

            this.movieList = list;

        }

    }
}
