package udacity.popular.tejeswar.popularmovie.view;

import android.view.LayoutInflater;
import java.util.List;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import udacity.popular.tejeswar.popularmovie.R;
import udacity.popular.tejeswar.popularmovie.fragment.TrailerFragment.OnListFragmentInteractionListener;
import udacity.popular.tejeswar.popularmovie.parcelable.Trailer;
import java.util.List;

/**
 * Created by tejeswar on 10/9/2016.
 */
public class TrailerRecyclerViewAdapter extends RecyclerView.Adapter<TrailerRecyclerViewAdapter.ViewHolder>
{

    private List<Trailer> trailerList;

    private final OnListFragmentInteractionListener mListener;

    public TrailerRecyclerViewAdapter(OnListFragmentInteractionListener mListener, List<Trailer> trailerList) {
        this.mListener = mListener;
        this.trailerList = trailerList;
    }


    @Override
    public TrailerRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trailer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        holder.trailer = trailerList.get(position);
        holder.trailer_num.setText(trailerList.get(position).getTrailer_num());
        //holder.trailer_url.setText(trailerList.get(position).getTrailerUrl());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.trailer);
                }
            }
        });
    }

    public void setItemList(List<Trailer> trailers)
    {

        this.trailerList = trailers;

    }

    @Override
    public int getItemCount()
    {
        if (trailerList != null)
            return trailerList.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder

    {
        public View mView;
        public TextView trailer_num;
        //public TextView trailer_url;
        public Trailer trailer;

        public ViewHolder(View view)

        {

            super(view);
            mView = view;
            trailer_num  =  (TextView) view.findViewById(R.id.txt_trailer);
            //trailer_url = (TextView) view.findViewById(R.id.txt_url);

        }

        @Override

        public String toString()

        {

            return super.toString() + " '" + trailer_num.getText() + "'";

        }

    }
}
