package udacity.popular.tejeswar.popularmovie.view;


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
public class MovieCursorAdapter extends CursorAdapter

{

    private static final String LOG_TAG = MovieCursorAdapter.class.getSimpleName();

    private Context context;

    public MovieCursorAdapter(Context context, Cursor c, int flags, Context context1)
    {
        super(context, c, flags);
        context = context1;
    }

    @Override

    public View newView(Context context, Cursor cursor, ViewGroup viewGroup)

    {

        int layoutId = R.layout.movie_list_content;

        Log.d(LOG_TAG, "In new View");

        View view = LayoutInflater.from(context).inflate(layoutId, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);

        view.setTag(viewHolder);

        return view;

    }

    @Override

    public void bindView(View view, Context context, Cursor cursor)

    {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        int columnIdIndex = cursor.getColumnIndex(MovieContract.Favourites.MOVIE_ID);

        final String movieID = cursor.getString(columnIdIndex);

        int columnTitleIndex = cursor.getColumnIndex(MovieContract.Favourites.TITLE);

        final String title = cursor.getString(columnTitleIndex);

        viewHolder.txtMovieName.setText(title);

        int imageIndex = cursor.getColumnIndex(MovieContract.Favourites.IMAGE);

        String image = cursor.getString(imageIndex);

        viewHolder.imgMovieImage.setImageBitmap(decodeBase64Image(image));

    }

    private Bitmap decodeBase64Image(String encodedImage)

    {

        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);

        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        return decodedByte;

    }

    private static class ViewHolder

    {

        private TextView txtMovieName;

        private ImageView imgMovieImage;

        public ViewHolder(View v)

        {

            imgMovieImage = (ImageView) v.findViewById(R.id.img_movie);

            txtMovieName = (TextView) v.findViewById(R.id.txt_name);

        }

    }

}