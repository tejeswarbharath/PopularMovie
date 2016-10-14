package udacity.popular.tejeswar.popularmovie.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tejeswar on 10/9/2016.
 */
public class Review implements Parcelable
{
    String author;
    String content;

    public Review(String author, String content) {
        this.author = author;
        this.content = content;
    }

    protected Review(Parcel in)
    {
        author = in.readString();
        content = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public static Creator<Review> getCREATOR() {
        return CREATOR;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
