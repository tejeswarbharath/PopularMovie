package udacity.popular.tejeswar.popularmovie.parcelable;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by tejeswar on 10/9/2016.
 */
public class FavouriteMovie extends Movie implements Parcelable

{

    public FavouriteMovie(String date, String duration, String id, String image, String name, String overview, List<Review> reviews, List<Trailer> trailers, String vote)

    {

        super(date, duration, id, image, name, overview, reviews, trailers, vote);

    }

    public FavouriteMovie(Parcel in)
    {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        super.writeToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FavouriteMovie> CREATOR = new Creator<FavouriteMovie>()
    {

        @Override
        public FavouriteMovie createFromParcel(Parcel in) {
            return new FavouriteMovie(in);
        }

        @Override
        public FavouriteMovie[] newArray(int size) {
            return new FavouriteMovie[size];
        }

    };
}
