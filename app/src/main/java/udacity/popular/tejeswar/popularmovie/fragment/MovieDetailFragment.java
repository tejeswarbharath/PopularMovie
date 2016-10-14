package udacity.popular.tejeswar.popularmovie.fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import udacity.popular.tejeswar.popularmovie.BuildConfig;
import udacity.popular.tejeswar.popularmovie.R;
import udacity.popular.tejeswar.popularmovie.database.MovieContract;
import udacity.popular.tejeswar.popularmovie.parcelable.Review;
import udacity.popular.tejeswar.popularmovie.parcelable.Trailer;
import udacity.popular.tejeswar.popularmovie.utils;
import udacity.popular.tejeswar.popularmovie.view.ReviewRecyclerViewAdapter;
import udacity.popular.tejeswar.popularmovie.view.TrailerRecyclerViewAdapter;

import static udacity.popular.tejeswar.popularmovie.BuildConfig.*;
import static udacity.popular.tejeswar.popularmovie.utils.*;

public class MovieDetailFragment extends Fragment

{

    private static final String LOG_TAG = "OverviewFragment";

    public static final String ARG_ITEM_ID = "movieId";

    private static final String STATE_ID = "movie_id";
    private static final String STATE_DATA = "flagDataType";
    private static final String STATE_TITLE = "title";
    private static final String STATE_YEAR = "year";
    private static final String STATE_DURATION = "duration";
    private static final String STATE_RATING = "rating";
    private static final String STATE_VOTE = "vote_ave";
    private static final String STATE_OVERVIEW = "overview";
    private static final String STATE_POSTER = "poster";

    private String movieId;
    private String mTitle;
    private String mYear;
    private String mDuration;
    private String mRating;
    private String mOverview;
    private String mPoster;
    private String first_trailer_url = "";

    private TextView txtYear;
    private TextView txtDuration;
    private TextView txtDescription;
    private ImageView imgPoster;
    private RatingBar ratingBar;

    private float vote_average;

    private RecyclerView trailerRecyclerView;
    private TrailerRecyclerViewAdapter trailerListAdapter;
    private List<Trailer> movieTrailersList;
    private TrailerFragment.OnListFragmentInteractionListener mTrailerListener;

    private List<Review> movieReviewList;
    private RecyclerView reviewRecvyclerView;
    private ReviewRecyclerViewAdapter reviewListAdapter;
    private ReviewFragment.OnListFragmentInteractionListener mReviewListener;

    private int flagDataType;

    private int mColumnCount = 1;

    private Intent intent;

    private TextView txtTrailer;

    public MovieDetailFragment()
    {

    }


    @Override
    public void onCreate(Bundle savedInstanceState)

    {

        super.onCreate(savedInstanceState);

        intent = getActivity().getIntent();

        if (getArguments().containsKey(ARG_ITEM_ID))

        {

            flagDataType = getArguments().getInt("flagData", 0);
            movieId = getArguments().getString("movieId");
            mTitle = getArguments().getString("title");
            mYear = getArguments().getString("year");
            mDuration = getArguments().getString("duration");
            mRating = getArguments().getString("rating");
            vote_average = getArguments().getFloat("vote_ave");
            mOverview = getArguments().getString("overview");
            mPoster = getArguments().getString("poster");

            movieTrailersList = getArguments().getParcelableArrayList("trailer_list");
            movieReviewList = getArguments().getParcelableArrayList("review_list");

            Activity activity = this.getActivity();

            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);

            if (appBarLayout != null)

            {

                appBarLayout.setTitle(mTitle);

            }

        }

        if (savedInstanceState != null)

        {

            movieId = savedInstanceState.getString(STATE_ID);
            flagDataType = savedInstanceState.getInt(STATE_DATA);
            mTitle = savedInstanceState.getString(STATE_TITLE);
            mYear = savedInstanceState.getString(STATE_YEAR);
            mDuration = savedInstanceState.getString(STATE_DURATION);
            mRating = savedInstanceState.getString(STATE_RATING);
            vote_average = savedInstanceState.getFloat(STATE_VOTE);
            mOverview = savedInstanceState.getString(STATE_OVERVIEW);
            mPoster = savedInstanceState.getString(STATE_POSTER);

        }


        //for allowing access in movie poster
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);


        //Check for any issues
        final YouTubeInitializationResult result = YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(getActivity());

        if (result != YouTubeInitializationResult.SUCCESS)
        {

            result.getErrorDialog(getActivity(), 0).show();

        }

        //Log.v("HEHEHE - 1", "flagDataType  => " + flagDataType);

    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {

        super.onSaveInstanceState(outState);

        outState.putString(STATE_ID, movieId);
        outState.putInt(STATE_DATA, flagDataType);
        outState.putString(STATE_TITLE, mTitle);
        outState.putString(STATE_YEAR, mYear);
        outState.putString(STATE_DURATION, mDuration);
        outState.putString(STATE_RATING, mRating);
        outState.putFloat(STATE_VOTE, vote_average);
        outState.putString(STATE_OVERVIEW, mOverview);
        outState.putString(STATE_POSTER, mPoster);

    }

    private void getLocalData()

    {

        List<Trailer> trailers = null;

        List<Review> reviews = null;

        Bundle arguments = getArguments();

        if (arguments.containsKey(ARG_ITEM_ID))

        {

            flagDataType = arguments.getInt("flagData", 0);
            mTitle = arguments.getString("title");
            mYear = arguments.getString("year");
            mDuration = arguments.getString("duration");
            mRating = arguments.getString("rating");
            vote_average = Float.parseFloat(mRating) / 2;
            mOverview = arguments.getString("overview");

        }
        else
        {

            flagDataType = intent.getIntExtra("flagData", 0);
            mTitle = intent.getStringExtra("title");
            mYear = intent.getStringExtra("year");
            mDuration = intent.getStringExtra("duration");
            mRating = intent.getStringExtra("rating");
            vote_average = Float.parseFloat(mRating) / 2;
            mOverview = intent.getStringExtra("overview");

        }


        Uri uri = MovieContract.Favourites.buildFavouriteUri(Long.parseLong(movieId));

        Cursor cursor = getContext().getContentResolver().query(
                uri,
                null,
                MovieContract.Favourites.MOVIE_ID + " = ?",
                new String[]{movieId},
                null);

        if (cursor.moveToFirst())
        {
            int posterIndex = cursor.getColumnIndex(MovieContract.Favourites.IMAGE);
            mPoster = cursor.getString(posterIndex); //column_image is 3rd column
        }

        cursor.close();

                movieReviewList = new ArrayList<>();

                uri = MovieContract.Review.buildReviewUri(Long.parseLong(movieId));

                cursor = getContext().getContentResolver().query(
                        uri,
                        null,
                        MovieContract.Review.MOVIE_ID + " = ?",
                        new String[]{movieId},
                        null);

                int authorIndex = 0;
                int contentIndex = 0;
                if (cursor.moveToFirst())
                {
                    authorIndex = cursor.getColumnIndex(MovieContract.Review.AUTHOR);
                    contentIndex = cursor.getColumnIndex(MovieContract.Review.CONTENT);
                }

                while (cursor.isAfterLast() == false) {

                    String author = cursor.getString(authorIndex);
                    String content = cursor.getString(contentIndex);

                    Review r = new Review(author, content);
                    movieReviewList.add(r);
                }

                cursor.close();


        movieTrailersList = new ArrayList<>();
        uri = MovieContract.Trailers.buildTrailerUri(Long.parseLong(movieId));
        cursor = getContext().getContentResolver().query(
                uri,
                null,
                MovieContract.Trailers.MOVIE_ID + " = ?",
                new String[]{movieId},
                null);

        int numIndex = 0;
        int urlIndex = 0;
        if (cursor.moveToFirst()) {
            numIndex = cursor.getColumnIndex(MovieContract.Trailers.TRAILER_NUM);
            urlIndex = cursor.getColumnIndex(MovieContract.Trailers.TRAILER_URL);
        }

        while (cursor.isAfterLast() == false) {

            String num = cursor.getString(numIndex);
            String url = cursor.getString(urlIndex);

            Trailer t = new Trailer(num, url);
            movieTrailersList.add(t);
        }

        cursor.close();

        setValuesOfView(mYear, mDuration, mOverview, vote_average, mPoster);

    }

    private void requestMovieReviews(String movieId)
    {

        movieReviewList = new ArrayList<>();

        final String BASE_PATH = "http://api.themoviedb.org/3/movie/";
        final String api_key = "?api_key=" + OPEN_WEATHER_MAP_API_KEY;
        String id = movieId;
        final String vid = "/reviews";
        final String reviews_url = BASE_PATH + id + vid + api_key;

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, reviews_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray results = jsonObject.getJSONArray("results");
                            String author = "";
                            String content = "";

                            if (results != null) {
                                for (int i = 0; i < results.length(); i++) {

                                    JSONObject obj = results.getJSONObject(i);
                                    author = obj.getString("author");
                                    content = obj.getString("content");


                                    Review reviews = new Review(author, content);
                                    movieReviewList.add(reviews);

                                }

                            }

                            if (movieReviewList.isEmpty()) {
                                author = "No Reviews Available";
                                content = " ";


                                Review reviews = new Review(author, content);
                                movieReviewList.add(reviews);
                            }

                            Log.d("Review list size ", String.valueOf(movieReviewList.size()));


                            reviewListAdapter.setItemList(movieReviewList);
                            reviewListAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof NoConnectionError) {
                            showSuccessDialog(getActivity(), R.string.no_connection, R.string.net).show();
                        }
                    }
                });


        queue.add(stringRequest);

    }

    private void requestMovieTrailer(String movieId)

    {

        movieTrailersList = new ArrayList<>();

        final String BASE_PATH = "http://api.themoviedb.org/3/movie/";

        final String api_key = "?api_key=a3e6990e5c4ec2eb8a31a0d2b1b356a9";

        String id = movieId;

        final String vid = "/videos";

        String trailer_url = BASE_PATH + id + vid + api_key;

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, trailer_url,
                new Response.Listener<String>()
                {

                    @Override
                    public void onResponse(String response)
                    {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray results = jsonObject.getJSONArray("results");

                            for (int i = 0; i < results.length(); i++)
                            {

                                JSONObject obj = results.getJSONObject(i);
                                String trailer_key = obj.getString("key");

                                String youtube_trailer = trailer_key;
                                String trailer_num = "Trailer " + (i + 1);

                                Trailer trailer = new Trailer(trailer_num, youtube_trailer);
                                movieTrailersList.add(trailer);

                            }

                            if (movieTrailersList.isEmpty())
                            {
                                txtTrailer.setText(R.string.no_trailers);
                            }

                            trailerListAdapter.setItemList(movieTrailersList);
                            trailerListAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof NoConnectionError) {

                            showSuccessDialog(getActivity(), R.string.no_connection, R.string.net).show();

                        }
                    }
                });

        queue.add(stringRequest);

    }

    private void requestMovieDetail(String movieId)
    {

        final String BASE_PATH = "http://api.themoviedb.org/3/movie/";
        final String api_key = "?api_key=" + OPEN_WEATHER_MAP_API_KEY;
        String id = movieId;

        final String original_url = BASE_PATH + id + api_key;
        Log.v(LOG_TAG, "ORIGINAL URL >>>>>>>>" + original_url);

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        final String IMAGE_BASE_PATH = "http://image.tmdb.org/t/p/";

        final String image_size = "w500";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, original_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println(response);

                        try
                        {

                            JSONObject jsonObject = new JSONObject(response);
                            mTitle = jsonObject.getString("title");
                            mYear = jsonObject.getString("release_date").substring(0, 4);
                            mDuration = jsonObject.getString("runtime") + "min";
                            mRating = jsonObject.getString("vote_average") + "/10";
                            vote_average = Float.parseFloat(jsonObject.getString("vote_average"));
                            mOverview = jsonObject.getString("overview");
                            mPoster = IMAGE_BASE_PATH + image_size + jsonObject.getString("poster_path");

                            Log.v("TITLE:>>>>>>>>>>>> ", mTitle);
                            Log.v("mYear:>>>>>>>>>>>> ", mYear);
                            Log.v("mDuration:>>>>>>>>>>> ", mDuration);
                            Log.v("mRating:>>>>>>>>>>>>>> ", mRating);
                            Log.v("mOverview:>>>>>>>>>>>> ", mOverview);
                            Log.v("mPoster:>>>>>>>>>>>>>> ", mPoster);

                            setValuesOfView(mYear, mDuration, mOverview, vote_average, mPoster);

                        }
                        catch (JSONException e)
                        {

                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof NoConnectionError) {

                            showSuccessDialog(getActivity(), R.string.no_connection, R.string.net).show();
                        }
                    }
                });

        queue.add(stringRequest);

    }


    public void setValuesOfView(String mYear, String mDuration, String mOverview, float vote_average, String mPoster) {
        txtYear.setText(mYear);
        txtDuration.setText(mDuration);
        txtDescription.setText(mOverview);
        ratingBar.setRating(vote_average / 2);

        switch (flagDataType) {
            case 0:
                Glide
                        .with(getActivity())
                        .load(mPoster)
                        .placeholder(R.drawable.ic_loading)
                        .fitCenter()
                        .error(R.drawable.ic_error)
                        .into(imgPoster);
                break;
            case 1:
                //set movie poster
                imgPoster.setImageBitmap(decodeBase64Image(mPoster));
                break;

        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        if (movieId != null)
        {


            txtYear = (TextView) rootView.findViewById(R.id.txt_year);


            txtDuration = (TextView) rootView.findViewById(R.id.txt_duration);

            txtDescription = (TextView) rootView.findViewById(R.id.txt_description);

            imgPoster = (ImageView) rootView.findViewById(R.id.img_movie);

            ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);

            Drawable progress = ratingBar.getProgressDrawable();
            DrawableCompat.setTint(progress, Color.YELLOW);

            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();

            if (stars != null)
            {
                stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(1).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            }

            trailerRecyclerView = (RecyclerView) rootView.findViewById(R.id.trailer_recyclerview);
            setTrailerRecyclerView(trailerRecyclerView);

            reviewRecvyclerView = (RecyclerView) rootView.findViewById(R.id.review_recyclerview);
            setReviewRecyclerView(reviewRecvyclerView);

            txtTrailer = (TextView) rootView.findViewById(R.id.txt_trailer);

            switch (flagDataType)
            {

                case 0:

                    requestMovieDetail(movieId);

                    requestMovieTrailer(movieId);

                    requestMovieReviews(movieId);

                    break;

                case 1:

                    getLocalData();

                    break;

            }
        }

        return rootView;

    }

    private void setReviewRecyclerView(RecyclerView reviewRecvyclerView)
    {

        if (mColumnCount <= 1)
        {
            reviewRecvyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        else

        {
            reviewRecvyclerView.setLayoutManager(new GridLayoutManager(getActivity(), mColumnCount));
        }

        reviewListAdapter = new ReviewRecyclerViewAdapter(mReviewListener,movieReviewList);
        reviewRecvyclerView.setAdapter(reviewListAdapter);

    }

    private void setTrailerRecyclerView(RecyclerView trailer_recyclerView)

    {

        if (mColumnCount <= 1) {
            trailer_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            trailer_recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), mColumnCount));
        }

        trailerListAdapter = new TrailerRecyclerViewAdapter(mTrailerListener,movieTrailersList);
        trailer_recyclerView.setAdapter(trailerListAdapter);


        trailer_recyclerView.addOnItemTouchListener(new TrailerFragment.RecyclerTouchListener(getActivity(), trailer_recyclerView, new TrailerFragment.ClickListener() {

            @Override
            public void onClick(View view, int position)

            {

                Trailer trailer = movieTrailersList.get(position);

                String url = trailer.getTrailer_url();

                Toast.makeText(getActivity(), trailer.getTrailer_url() + " is selected!", Toast.LENGTH_SHORT).show();

                launchYoutube(OPEN_WEATHER_MAP_API_KEY, url);

            }

            @Override
            public void onLongClick(View view, int position)

            {

            }
        }));

    }

    private void launchYoutube(String api_key, String video_id) {

        Intent intent = YouTubeStandalonePlayer.createVideoIntent(getActivity(), api_key, video_id);
        startActivity(intent);

    }


}
