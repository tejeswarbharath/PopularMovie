package udacity.popular.tejeswar.popularmovie.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import udacity.popular.tejeswar.popularmovie.database.MovieContract.Details;
import udacity.popular.tejeswar.popularmovie.database.MovieContract.Favourites;
import udacity.popular.tejeswar.popularmovie.database.MovieContract.Trailers;
import udacity.popular.tejeswar.popularmovie.database.MovieContract.Review;
/**
 * Created by tejeswar on 10/8/2016.
 */

public class MovieOperations extends ContentProvider

{

    private static final int DETAIL = 1;
    private static final int DETAIL_ID = 2;

    private static final int TRAILER = 3;
    private static final int TRAILER_ID = 4;

    private static final int REVIEW = 5;
    private static final int REVIEW_ID = 6;

    private static final int FAVOURITE = 7;
    private static final int FAVOURITE_ID = 8;

    private static final UriMatcher URI_MATCHER;

    private MovieDataBaseHandler mHelper=null;

    static

    {

        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

        URI_MATCHER.addURI(MovieContract.CONTENT_AUTHORITY,
                Details.TABLE_DETAIL,
                DETAIL);

        URI_MATCHER.addURI(MovieContract.CONTENT_AUTHORITY,
                Details.TABLE_DETAIL + "/#",
                DETAIL_ID);

        URI_MATCHER.addURI(MovieContract.CONTENT_AUTHORITY,
                Review.TABLE_REVIEW,
                REVIEW);

        URI_MATCHER.addURI(MovieContract.CONTENT_AUTHORITY,
                Review.TABLE_REVIEW + "/#",
                REVIEW_ID);

        URI_MATCHER.addURI(MovieContract.CONTENT_AUTHORITY,
                Trailers.TABLE_TRAILER,
                TRAILER);

        URI_MATCHER.addURI(MovieContract.CONTENT_AUTHORITY,
                Trailers.TABLE_TRAILER + "/#",
                TRAILER_ID);

        URI_MATCHER.addURI(MovieContract.CONTENT_AUTHORITY,
                Favourites.TABLE_FAVOURITE,
                FAVOURITE);

        URI_MATCHER.addURI(MovieContract.CONTENT_AUTHORITY,
                Favourites.TABLE_FAVOURITE + "/#",
                FAVOURITE_ID);

    }


    @Override
    public boolean onCreate()
    {

        mHelper = new MovieDataBaseHandler(getContext());

        return true;

    }

    //Multiple Entries or Single Entries
    @Nullable
    @Override
    public String getType(Uri uri)
    {

        switch (URI_MATCHER.match(uri))

        {

            case DETAIL:
                return Details.CONTENT_DIR_TYPE;

            case DETAIL_ID:
                return Details.CONTENT_ITEM_TYPE;

            case REVIEW:
                return Review.CONTENT_DIR_TYPE;

            case REVIEW_ID:
                return Review.CONTENT_ITEM_TYPE;

            case TRAILER:
                return Trailers.CONTENT_DIR_TYPE;

            case TRAILER_ID:
                return Trailers.CONTENT_ITEM_TYPE;

            case FAVOURITE:
                return Favourites.CONTENT_DIR_TYPE;

            case FAVOURITE_ID:
                return Favourites.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)

    {

        Cursor retCursor;

        switch (URI_MATCHER.match(uri))

        {

            case DETAIL:

            {

                retCursor = mHelper.getReadableDatabase().query(
                        Details.TABLE_DETAIL,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                return retCursor;

            }


            case DETAIL_ID:

            {

                retCursor = mHelper.getReadableDatabase().query(
                        Details.TABLE_DETAIL,
                        projection,
                        Details.MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);

                return retCursor;

            }

            case FAVOURITE: {

                retCursor = mHelper.getReadableDatabase().query(
                        Favourites.TABLE_FAVOURITE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }

            case FAVOURITE_ID: {

                retCursor = mHelper.getReadableDatabase().query(
                        Favourites.TABLE_FAVOURITE,
                        projection,
                        MovieContract.Favourites.MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);

                return retCursor;

            }


            case TRAILER:
            {

                retCursor = mHelper.getReadableDatabase().query(
                        Trailers.TABLE_TRAILER,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                return retCursor;

            }

            case TRAILER_ID:
            {

                retCursor = mHelper.getReadableDatabase().query(
                        Trailers.TABLE_TRAILER,
                        projection,
                        Trailers.MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);

                return retCursor;

            }

            case REVIEW:
            {

                retCursor = mHelper.getReadableDatabase().query(
                        MovieContract.Review.TABLE_REVIEW,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                return retCursor;

            }

            case REVIEW_ID:

            {

                retCursor = mHelper.getReadableDatabase().query(
                        MovieContract.Review.TABLE_REVIEW,
                        projection,
                        MovieContract.Review.MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);

                return retCursor;

            }

            default:

            {

                throw new UnsupportedOperationException("Unknown uri: " + uri);

            }

        }

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values)
    {

        final SQLiteDatabase db = mHelper.getWritableDatabase();

        Uri returnUri;

        switch (URI_MATCHER.match(uri))
        {
            case DETAIL:
            {
                long _id = db.insert(Details.TABLE_DETAIL, null, values);

                if (_id > 0)
                {

                    returnUri = Details.buildDetailsUri(_id);

                }
                else
                {

                    throw new SQLException("Failed to insert row into: " + uri);

                }
                break;
            }

            case TRAILER:
            {

                long _id = db.insert(MovieContract.Trailers.TABLE_TRAILER, null, values);

                if (_id > 0)
                {

                    returnUri = MovieContract.Trailers.buildTrailerUri(_id);

                }
                else
                {

                    throw new SQLException("Failed to insert row into: " + uri);

                }
                break;
            }

            case REVIEW:
            {

                long _id = db.insert(MovieContract.Review.TABLE_REVIEW, null, values);

                if (_id > 0)
                {

                    returnUri = MovieContract.Review.buildReviewUri(_id);

                }
                else
                {

                    throw new SQLException("Failed to insert row into: " + uri);

                }
                break;

            }

            case FAVOURITE:
            {

                long _id = db.insert(MovieContract.Favourites.TABLE_FAVOURITE, null, values);

                if (_id > 0)
                {
                    returnUri = MovieContract.Favourites.buildFavouriteUri(_id);
                }
                else
                {
                    throw new SQLException("Failed to insert row into: " + uri);
                }
                break;

            }

            default:
            {

                throw new UnsupportedOperationException("Unknown uri: " + uri);

            }
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)

    {

        final SQLiteDatabase db = mHelper.getWritableDatabase();

        final int match = URI_MATCHER.match(uri);

        int numDeleted;

        switch (match)
        {

            case FAVOURITE_ID :

                numDeleted = db.delete(Favourites.TABLE_FAVOURITE,
                        Favourites.MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});

                break;

            default:

                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        return numDeleted;

    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {

        final SQLiteDatabase db = mHelper.getWritableDatabase();

        int numUpdated = 0;

        if (values == null)
        {

            throw new IllegalArgumentException("Cannot have null content values");

        }

        switch (URI_MATCHER.match(uri))
        {

            case DETAIL:
            {

                numUpdated = db.update(MovieContract.Details.TABLE_DETAIL,
                        values,
                        selection,
                        selectionArgs);
                break;

            }

            case DETAIL_ID:

            {

                numUpdated = db.update(MovieContract.Details.TABLE_DETAIL,
                        values,
                        MovieContract.Details.MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;

            }

            default:

            {

                throw new UnsupportedOperationException("Unknown uri: " + uri);

            }

        }

        if (numUpdated > 0)

        {

            getContext().getContentResolver().notifyChange(uri, null);

        }

        return numUpdated;

    }

}
