package udacity.popular.tejeswar.popularmovie.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tejeswar on 10/9/2016.
 */
public class MovieImage implements Parcelable

{

    public long id;

    public String name;

    public String image;

    public static Creator<MovieImage> getCREATOR() {
        return CREATOR;
    }

    public long getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName()
    {
        return name;
    }

    public void setId(long id){
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MovieImage(long id, String image, String name)
    {
        this.id = id;
        this.image = image;
        this.name = name;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeLong(id);
        dest.writeString(image);
        dest.writeString(name);

    }

    protected MovieImage(Parcel in)
    {

        id=in.readLong();
        name=in.readString();
        image=in.readString();

    }


    public static final Creator<MovieImage> CREATOR = new Creator<MovieImage>()
    {
        @Override
        public MovieImage createFromParcel(Parcel in) {
            return new MovieImage(in);
        }

        @Override
        public MovieImage[] newArray(int size)
        {
            return new MovieImage[size];
        }
    };

}
