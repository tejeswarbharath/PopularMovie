package udacity.popular.tejeswar.popularmovie.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import udacity.popular.tejeswar.popularmovie.database.MovieContract.Details;
import udacity.popular.tejeswar.popularmovie.database.MovieContract.Favourites;
import udacity.popular.tejeswar.popularmovie.database.MovieContract.Review;
import udacity.popular.tejeswar.popularmovie.database.MovieContract.Trailers;

/**
 * Created by tejeswar on 10/7/2016.
 */

public class MovieDataBaseHandler extends SQLiteOpenHelper

{

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "MovieManager";

    private static final String TABLE_MOVIES = "movies";


    private static final String TEXT_TYPE = " TEXT";

    private static final String BLOB_TYPE = " BLOB";

    private static final String INTEGER_TYPE = " INTEGER DEFAULT 0";

    private static final String COMMA_SEP = ",";

    public MovieDataBaseHandler(Context context)

    {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    private static final String SQL_TABLE_DETAIL =

            "CREATE TABLE " + Details.TABLE_DETAIL + " (" +
                    Details._ID + " INTEGER PRIMARY KEY," +
                    Details.MOVIE_ID + TEXT_TYPE + COMMA_SEP +
                    Details.TITLE + TEXT_TYPE + COMMA_SEP +
                    Details.YEAR + TEXT_TYPE + COMMA_SEP +
                    Details.DURATION + TEXT_TYPE + COMMA_SEP +
                    Details.RATING + TEXT_TYPE + COMMA_SEP +
                    Details.OVERVIEW + TEXT_TYPE + COMMA_SEP +
                    Details.POSTER + BLOB_TYPE + COMMA_SEP +
                    Details.VIEWED + INTEGER_TYPE + " )";

    private static final String SQL_TABLE_REVIEW =

            "CREATE TABLE " + Review.TABLE_REVIEW + " (" +
                    Review._ID + " INTEGER PRIMARY KEY," +
                    Review.MOVIE_ID + TEXT_TYPE + COMMA_SEP +
                    Review.AUTHOR + TEXT_TYPE + COMMA_SEP +
                    Review.CONTENT + TEXT_TYPE +
                    " )";

    private static final String SQL_TABLE_TRAILER =

            "CREATE TABLE " + Trailers.TABLE_TRAILER + " (" +
                    Trailers._ID + " INTEGER PRIMARY KEY," +
                    Trailers.MOVIE_ID + TEXT_TYPE + COMMA_SEP +
                    Trailers.TRAILER_NUM + TEXT_TYPE + COMMA_SEP +
                    Trailers.TRAILER_URL + TEXT_TYPE +
                    " )";

    private static final String SQL_TABLE_FAVOURITE =

            "CREATE TABLE " + Favourites.TABLE_FAVOURITE + " (" +
                    Favourites._ID + " INTEGER PRIMARY KEY," +
                    Favourites.MOVIE_ID + TEXT_TYPE + COMMA_SEP +
                    Favourites.TITLE + TEXT_TYPE + COMMA_SEP +
                    Favourites.IMAGE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_DETAIL =

            "DROP TABLE IF EXISTS " + Details.TABLE_DETAIL;

    private static final String SQL_DELETE_REVIEW =

            "DROP TABLE IF EXISTS " + Review.TABLE_REVIEW;

    private static final String SQL_DELETE_TRAILER =

            "DROP TABLE IF EXISTS " + Trailers.TABLE_TRAILER;

    private static final String SQL_DELETE_FAVOURITE =

            "DROP TABLE IF EXISTS " + Favourites.TABLE_FAVOURITE;


    private static final String LOG_TAG = MovieDataBaseHandler.class.getSimpleName();


    @Override
    public void onCreate(SQLiteDatabase db)

    {

        db.execSQL(SQL_TABLE_DETAIL);
        db.execSQL(SQL_TABLE_REVIEW);
        db.execSQL(SQL_TABLE_TRAILER);
        db.execSQL(SQL_TABLE_FAVOURITE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)

    {

        db.execSQL(SQL_DELETE_DETAIL);
        db.execSQL(SQL_DELETE_REVIEW);
        db.execSQL(SQL_DELETE_TRAILER);
        db.execSQL(SQL_DELETE_FAVOURITE);

        onCreate(db);

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

        super.onDowngrade(db, oldVersion, newVersion);

    }

}