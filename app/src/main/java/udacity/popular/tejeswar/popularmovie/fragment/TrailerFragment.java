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
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import udacity.popular.tejeswar.popularmovie.R;
import udacity.popular.tejeswar.popularmovie.parcelable.Trailer;
import udacity.popular.tejeswar.popularmovie.utils;
import udacity.popular.tejeswar.popularmovie.view.TrailerRecyclerViewAdapter;

import static udacity.popular.tejeswar.popularmovie.BuildConfig.OPEN_WEATHER_MAP_API_KEY;
import udacity.popular.tejeswar.popularmovie.utils.*;

/**
 * Created by tejeswar on 10/9/2016.
 */
public class TrailerFragment extends Fragment

{

    private static final String ARG_COLUMN_COUNT = "column_count";

    private int mColumnCount = 1;

    private OnListFragmentInteractionListener mListener;

    private List<Trailer> movieTrailersList;

    private RecyclerView recyclerView;

    private TrailerRecyclerViewAdapter trailerListAdapter;


    // constant value of package & class name of YouTube app
    public static final String YOUTUBE_PACKAGE_NAME = "com.google.android.youtube";
    public static final String YOUTUBE_CLASS_NAME = "com.google.android.youtube.WatchActivity";

    private String movieId;

    private static final String LOG_TAG = "Trailer Fragment";
    private int flagDataType;
    private Intent intent;

    public TrailerFragment()
    {

    }

    @SuppressWarnings("unused")
    public static TrailerFragment newInstance(int columnCount)
    {

        TrailerFragment fragment = new TrailerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        intent = getActivity().getIntent();

        if (intent != null) {
            movieId = intent.getStringExtra("movieId");
            flagDataType = intent.getIntExtra("flagData", 0);
        }

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            //get movieId from MovieDetailActivity
            movieId = getArguments().getString("movieId");
            movieTrailersList = getArguments().getParcelableArrayList("trailer_list");

        }

        if (savedInstanceState != null)
        {

            movieId = savedInstanceState.getString("movieId");
            Log.v("savedInstanceState", "movieid = " + movieId);

        }

        //Check for any issues
        final YouTubeInitializationResult result = YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(getActivity());

        if (result != YouTubeInitializationResult.SUCCESS) {
            //If there are any issues we can show an error dialog.
            result.getErrorDialog(getActivity(), 0).show();
        }

        if (flagDataType == 0)
        {

            //call trailers from API
            requestMovieTrailer(movieId);

        }
        else
        {

            Log.v("xxxxxxxx", "trailers not getting from internet");

        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trailer_list, container, false);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            recyclerView.setAdapter(new TrailerRecyclerViewAdapter(mListener,movieTrailersList));
        }

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Trailer trailer = movieTrailersList.get(position);
                String url = trailer.getTrailer_url();
                Toast.makeText(getContext(), trailer.getTrailer_url() + " is selected!", Toast.LENGTH_SHORT).show();
                launchYoutube(OPEN_WEATHER_MAP_API_KEY, url);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return view;

    }

    private void getLocalData() {
        movieTrailersList = new ArrayList<>();
        List<Trailer> trailers = intent.getParcelableArrayListExtra("trailers");
        movieTrailersList = trailers;
        for (Trailer t : movieTrailersList) {
            System.out.println("TRAILER_URL===========> local" + t.getTrailer_num());
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

    public interface OnListFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Trailer trailer);

    }

    private void launchYoutube(String api_key, String video_id) {

        Intent intent = YouTubeStandalonePlayer.createVideoIntent(getActivity(), api_key, video_id);
        startActivity(intent);

    }

    private void requestMovieTrailer(String movieId) {


        movieTrailersList = new ArrayList<>();


        final String BASE_PATH = "http://api.themoviedb.org/3/movie/";
        final String api_key = "?api_key="+OPEN_WEATHER_MAP_API_KEY;
        String id = movieId;
        final String vid = "/videos";
        String trailer_url = BASE_PATH + id + vid + api_key;

        Log.d(LOG_TAG, "TRAILER URL----------> " + trailer_url);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());


        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, trailer_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with the response
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray results = jsonObject.getJSONArray("results");


                            for (int i = 0; i < results.length(); i++) {

                                JSONObject obj = results.getJSONObject(i);
                                String trailer_key = obj.getString("key");
                                //String youtube_trailer = "https://www.youtube.com/watch?v=" + trailer_key;
                                String youtube_trailer = trailer_key;
                                String trailer_num = "Trailer " + (i + 1);

                                System.out.println("TRAILER NUMBER --------->" + trailer_num);
                                System.out.println("TRAILER URL --------->" + youtube_trailer);

                                Trailer trailer = new Trailer(trailer_num, youtube_trailer);

                                //save trailers in a list
                                movieTrailersList.add(trailer);

                            }

                            if (movieTrailersList != null) {
                                for (Trailer trailer : movieTrailersList) {
                                    System.out.println("TRAILER NUMBER-----------> " + trailer.getTrailer_num());
                                }

                                recyclerView.getAdapter().notifyDataSetChanged();

                            } else {
                                System.out.println(R.string.no_trailers);
                            }

                            trailerListAdapter.setItemList(movieTrailersList);
                            trailerListAdapter.notifyDataSetChanged();
                            Log.d("Trailer list size: ", String.valueOf(movieTrailersList.size()));

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
                            utils.showSuccessDialog(getContext(), R.string.no_connection, R.string.net).show();
                        }
                    }
                });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        trailerListAdapter = new TrailerRecyclerViewAdapter(mListener,movieTrailersList);
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
}

