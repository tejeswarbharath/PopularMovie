package udacity.popular.tejeswar.popularmovie.FlowAdapter;

import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import udacity.popular.tejeswar.popularmovie.R;
import udacity.popular.tejeswar.popularmovie.activities.MovieDetailActivity;
import udacity.popular.tejeswar.popularmovie.parcelable.FavouriteMovie;

/**
 * Created by tejeswar on 10/10/2016.
 */
public class CoverFlowAdapter extends BaseAdapter
{

    private ArrayList<FavouriteMovie> movieList;

    private Context context;

    public CoverFlowAdapter(Context context, ArrayList<FavouriteMovie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public int getCount()
    {

        return movieList.size();

    }

    @Override
    public FavouriteMovie getItem(int position)
    {

        return movieList.get(position);

    }

    @Override
    public long getItemId(int position)
    {

        return position;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        ViewHolder viewHolder;

        if (convertView == null)
        {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_flow_view, null, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imgMovieImage.setImageBitmap(decodeBase64Image(position));

        viewHolder.txtMovieName.setText(movieList.get(position).getName());

        convertView.setOnClickListener(onClickListener(position));

        return convertView;

    }

    private Bitmap decodeBase64Image(int position) {

        byte[] decodedString = Base64.decode(movieList.get(position).getImage(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        return decodedByte;
    }

    private View.OnClickListener onClickListener(final int position) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, MovieDetailActivity.class)
                        //pass the selected movie_id to the next Activity
                        .putExtra("flagData", 1)
                        .putExtra("movieId", movieList.get(position).getId())
                        .putExtra("title", movieList.get(position).getName())
                        .putExtra("year", movieList.get(position).getDate())
                        .putExtra("rating", movieList.get(position).getVote())
                        .putExtra("overview", movieList.get(position).getOverview())
                        .putExtra("poster", movieList.get(position).getImage())
                        .putExtra("duration", movieList.get(position).getDuration())
                        .putParcelableArrayListExtra("trailers", (ArrayList<? extends Parcelable>) movieList.get(position).getTrailers())
                        .putParcelableArrayListExtra("reviews", (ArrayList<? extends Parcelable>) movieList.get(position).getReviews());

                context.startActivity(i);

            }
        };
    }


    public void setItemList(ArrayList<FavouriteMovie> list) {
        this.movieList = list;
    }


    private static class ViewHolder {
        private TextView txtMovieName;
        private ImageView imgMovieImage;

        public ViewHolder(View v)
        {
            imgMovieImage = (ImageView) v.findViewById(R.id.image);
            txtMovieName = (TextView) v.findViewById(R.id.name);
        }
    }

}
