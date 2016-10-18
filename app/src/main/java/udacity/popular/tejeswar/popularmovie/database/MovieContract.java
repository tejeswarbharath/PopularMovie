package udacity.popular.tejeswar.popularmovie.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by tejeswar on 10/8/2016.
 */

public class MovieContract

{

        public static final String CONTENT_AUTHORITY="udacity.popular.tejeswar.popularmovie";

        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

        public MovieContract()

        {

        }

        public static abstract class Details implements BaseColumns

        {

            public static final String TABLE_DETAIL = "details";

            public static final String MOVIE_ID ="movie_id";

            public static final String TITLE="title";

            public static final String YEAR="year";

            public static final String DURATION = "duration";

            public static final String RATING = "rating";

            public static final String OVERVIEW = "overview";

            public static final String POSTER ="poster";

            public static final String VIEWED = "viewed";

            public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_DETAIL).build();

            public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_DETAIL;

            public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_DETAIL;

            public static Uri buildDetailsUri(long id)

            {

                return ContentUris.withAppendedId(CONTENT_URI, id);

            }

        }

        public static abstract class Review implements BaseColumns

        {

            public static final String MOVIE_ID="movie_id";

            public static final String TABLE_REVIEW ="review";

            public static final String AUTHOR = "author";

            public static final String CONTENT = "content";

            public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_REVIEW).build();

            public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_REVIEW;

            public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_REVIEW;

            public static Uri buildReviewUri(long id)

            {

                return ContentUris.withAppendedId(CONTENT_URI, id);

            }

        }

        public static abstract class Trailers implements BaseColumns

        {

            public static final String MOVIE_ID="movie_id";

            public static final String TABLE_TRAILER = "trailer";

            public static final String TRAILER_NUM = "trailer_number";

            public static final String TRAILER_URL ="trailer_url";

            public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_TRAILER).build();

            public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_TRAILER;

            public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_TRAILER;

            public static Uri buildTrailerUri(long id)

            {

                return ContentUris.withAppendedId(CONTENT_URI, id);

            }

        }

        public static abstract class Favourites implements BaseColumns

        {

            public static final String MOVIE_ID="movie_id";

            public static final String TABLE_FAVOURITE = "favourite";

            public static final String TITLE = "title";

            public static final String IMAGE ="image";

            public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_FAVOURITE).build();

            public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_FAVOURITE;

            public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_FAVOURITE;

            public static Uri buildFavouriteUri(long id)

            {

                return ContentUris.withAppendedId(CONTENT_URI, id);

            }

        }

}
