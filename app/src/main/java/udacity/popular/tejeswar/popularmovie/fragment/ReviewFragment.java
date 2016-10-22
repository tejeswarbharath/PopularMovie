package udacity.popular.tejeswar.popularmovie.fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import udacity.popular.tejeswar.popularmovie.R;
import udacity.popular.tejeswar.popularmovie.parcelable.Review;
import udacity.popular.tejeswar.popularmovie.Utils;
import udacity.popular.tejeswar.popularmovie.view.ReviewRecyclerViewAdapter;

import static udacity.popular.tejeswar.popularmovie.BuildConfig.OPEN_WEATHER_MAP_API_KEY;

/**
 * Created by tejeswar on 10/9/2016.
 */
public class ReviewFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String LOG_TAG = "ReviewFragment";

    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private List<Review> movieReviewList;
    private String movieId;
    private RecyclerView recyclerView;
    private ReviewRecyclerViewAdapter reviewListAdapter;
    private Intent intent;

    private int flagDataType;

    public ReviewFragment() {
    }

    @SuppressWarnings("unused")
    public static ReviewFragment newInstance(int columnCount) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        intent = getActivity().getIntent();

        if (intent != null)
        {

            movieId = intent.getStringExtra("movieId");

            flagDataType = intent.getIntExtra("flagData", 0);

        }

        if (getArguments() != null)
        {

            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            movieId = getArguments().getString("movieId");
            List<Review> reviews = getArguments().getParcelableArrayList("review_list");
            movieReviewList = reviews;

        }

        if (savedInstanceState != null)
        {

            movieId = savedInstanceState.getString("movieId");
            Log.v("savedInstanceState", "movieid = " + movieId);

        }


        if (flagDataType == 0) {
            //call trailers from API
            requestMovieReviews(movieId);
        } else {
            Log.v("xxxxxxxx", "trailers not getting from internet");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("movieId", movieId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_review_list, container, false);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new ReviewRecyclerViewAdapter(mListener,movieReviewList));
        }
        return view;
    }

    private void getLocalData() {
        movieReviewList = new ArrayList<>();
        List<Review> reviews = intent.getParcelableArrayListExtra("reviews");

        movieReviewList = reviews;
        for (Review r : movieReviewList) {
            System.out.println("Review===========> " + r.getAuthor());
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnListFragmentInteractionListener {

        void onListFragmentInteraction(Review review);
    }

    private void requestMovieReviews(String movieId) {

        movieReviewList = new ArrayList<>();

        final String BASE_PATH = "http://api.themoviedb.org/3/movie/";
        final String api_key = "?api_key=" + OPEN_WEATHER_MAP_API_KEY;
        String id = movieId;
        final String vid = "/reviews";
        final String reviews_url = BASE_PATH + id + vid + api_key;

        Log.d("TRAILER URL--------> ", reviews_url);

        RequestQueue queue = Volley.newRequestQueue(getActivity());


        StringRequest stringRequest = new StringRequest(Request.Method.GET, reviews_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with the response
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

                                    //add to a review object
                                    Review reviews = new Review(author, content);
                                    movieReviewList.add(reviews);

                                }

                            }

                            //==============================LOGS=================================/


                            if (movieReviewList != null) {
                                for (Review review : movieReviewList) {
                                    Log.d("AUTHOR: ", String.valueOf(review.getAuthor()));
                                    Log.d("CONTENT: ", review.getContent());
                                }
                            }
                            //====================================================================/

                            if (movieReviewList.size() == 0) {
                                author = "No Reviews Available";
                                content = " ";

                                //add to a review object
                                Review reviews = new Review(author, content);
                                movieReviewList.add(reviews);
                            }

                            Log.d("Review list size ", String.valueOf(movieReviewList.size()));

                            //updates recyclerview once data is fetched from the API call
                            recyclerView.getAdapter().notifyDataSetChanged();
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
                        //other catches
                        if (error instanceof NoConnectionError) {
                            //show dialog no net connection
                            Utils.showSuccessDialog(getContext(), R.string.no_connection, R.string.net).show();
                        }
                    }
                });


        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        reviewListAdapter = new ReviewRecyclerViewAdapter(mListener,movieReviewList);


    }
}
