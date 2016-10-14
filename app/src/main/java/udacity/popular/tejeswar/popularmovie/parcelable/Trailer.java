package udacity.popular.tejeswar.popularmovie.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tejeswar on 10/9/2016.
 */

public class Trailer implements Parcelable

{

    String trailer_num;

    String trailer_url;

    public Trailer(String trailer_num, String trailer_url)
    {

        this.trailer_num = trailer_num;
        this.trailer_url = trailer_url;

    }


    protected Trailer(Parcel in) {
        trailer_num = in.readString();
        trailer_url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trailer_num);
        dest.writeString(trailer_url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>()
    {

        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }

    };

    public String getTrailer_num() {
        return trailer_num;
    }

    public String getTrailer_url() {
        return trailer_url;
    }

}
