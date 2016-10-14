package udacity.popular.tejeswar.popularmovie.FlowAdapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import udacity.popular.tejeswar.popularmovie.R;
import udacity.popular.tejeswar.popularmovie.database.MovieContract;

/**
 * Created by tejeswar on 10/10/2016.
 */

public class CoverFlowCursorAdapter extends CursorAdapter
{

    private static final String LOG_TAG = CoverFlowCursorAdapter.class.getSimpleName();

    private Context context;

    public CoverFlowCursorAdapter(Context context, Cursor c, int flags, Context context1)
    {
        super(context, c, flags);
        context = context1;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)

    {

        Viewholder viewHolder = (Viewholder) view.getTag();

        Log.d(LOG_TAG, "In bind View");

        int columnIdIndex = cursor.getColumnIndex(MovieContract.Favourites.MOVIE_ID);

        final String movieID = cursor.getString(columnIdIndex);

        int columnTitleIndex = cursor.getColumnIndex(MovieContract.Favourites.TITLE);

        final String title = cursor.getString(columnTitleIndex);

        viewHolder.txtMovieName.setText(title);

        int imageIndex = cursor.getColumnIndex(MovieContract.Favourites.IMAGE);

        String image = cursor.getString(imageIndex);

        viewHolder.imgMovieImage.setImageBitmap(decodeBase64Image(image));

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup)

    {

        int layoutId = R.layout.item_flow_view;

        Log.d(LOG_TAG, "In new View");

        View view = LayoutInflater.from(context).inflate(layoutId, viewGroup, false);

        Viewholder viewHolder = new Viewholder(view);

        view.setTag(viewHolder);

        return view;

    }

    private Bitmap decodeBase64Image(String encodedImage)
    {

        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;

    }

    private static class Viewholder
    {

        private TextView txtMovieName;
        private ImageView imgMovieImage;

        public Viewholder(View v)
        {

            imgMovieImage = (ImageView) v.findViewById(R.id.image);

            txtMovieName = (TextView) v.findViewById(R.id.name);

        }
    }

}

