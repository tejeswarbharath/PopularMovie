package udacity.popular.tejeswar.popularmovie.view;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import udacity.popular.tejeswar.popularmovie.R;
import udacity.popular.tejeswar.popularmovie.fragment.ReviewFragment.OnListFragmentInteractionListener;
import udacity.popular.tejeswar.popularmovie.parcelable.Review;
/**
 * Created by tejeswar on 10/9/2016.
 */
public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.ViewHolder>
{

    private List<Review> reviewsList;

    private final OnListFragmentInteractionListener mListener;

    public ReviewRecyclerViewAdapter(OnListFragmentInteractionListener mListener, List<Review> reviewsList)

    {

        this.mListener = mListener;

        this.reviewsList = reviewsList;

    }


    @Override
    public int getItemCount()
    {
        if (reviewsList != null)
            return reviewsList.size();
        return 0;
    }

    public void setItemList(List<Review> itemList)
    {

        this.reviewsList = itemList;

    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public final View mView;
        public final TextView mAuthorView;
        public final TextView mContentView;
        public Review mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mAuthorView = (TextView) view.findViewById(R.id.author);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    @Override
    public ReviewRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = reviewsList.get(position);
        holder.mAuthorView.setText(reviewsList.get(position).getAuthor());
        holder.mContentView.setText(reviewsList.get(position).getContent());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }
}