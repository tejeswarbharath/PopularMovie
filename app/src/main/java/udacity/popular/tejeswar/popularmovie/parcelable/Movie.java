package udacity.popular.tejeswar.popularmovie.parcelable;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tejeswar on 10/9/2016.
 */
public class Movie implements Parcelable
{

    public String id;
    public String name;
    public String image;
    String overview;
    String date;
    String vote;
    String duration;
    List<Trailer> trailers;
    List<Review> reviews;

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {

        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeString(overview);
        dest.writeString(date);
        dest.writeString(duration);
        dest.writeTypedList(trailers);

    }

    public Movie(String date, String duration, String id, String image, String name, String overview, List<Review> reviews, List<Trailer> trailers, String vote)

    {

        this.date = date;
        this.duration = duration;
        this.id = id;
        this.image = image;
        this.name = name;
        this.overview = overview;
        this.reviews = reviews;
        this.trailers = trailers;
        this.vote = vote;

    }

    public Movie(Parcel in)

    {

        this.date = in.readString();
        this.duration = in.readString();
        this.id = in.readString();
        this.image = in.readString();
        this.name = in.readString();
        this.overview = in.readString();
        this.reviews = in.createTypedArrayList(Review.CREATOR);
        this.trailers = in.createTypedArrayList(Trailer.CREATOR);

    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


    public static Creator<Movie> getCREATOR() {
        return CREATOR;
    }

    public String getDate() {
        return date;
    }

    public String getDuration() {
        return duration;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public String getVote() {
        return vote;
    }

    public void setTrailers(List<Trailer> trailers)
    {
        this.trailers = trailers;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }
}
