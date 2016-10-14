package udacity.popular.tejeswar.popularmovie;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.view.View;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import udacity.popular.tejeswar.popularmovie.R;

/**
 * Created by tejeswar on 10/10/2016.
 */

public final class utils {

    private static final String FAVOURITES = "favourites";

    private utils()
    {


        throw new AssertionError();

        }

    public static AlertDialog showSuccessDialog(final Context context, int title, int message)

    {


        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(context);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialogInterface, int i)
            {
            }
        });
        return downloadDialog.show();

    }

    public static void saveFavouriteMovies(Context context, JSONObject item, View view) throws JSONException

    {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        JSONArray jsonArray = new JSONArray();

        String json = preferences.getString("favourites", null);

        if (json != null)

        {

            jsonArray = new JSONArray(json);

        }

        jsonArray.put(item);

        preferences.edit().putString(FAVOURITES, jsonArray.toString()).commit();

        Snackbar.make(view, R.string.fav_add, Snackbar.LENGTH_SHORT)

                .setAction("Action", null).show();

    }

    public static JSONArray getFavouriteMovies(Context context) throws JSONException

    {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String items = preferences.getString(FAVOURITES, "");

        return new JSONArray(items);

    }


    public static void removeFromFavourites(Context context, String movieId, View view) throws JSONException

    {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        JSONArray blank = new JSONArray();

        String json = preferences.getString(FAVOURITES, null);

        JSONArray lines = new JSONArray(json);

        for (int i = 0; i < lines.length(); i++)

        {

            JSONObject line = lines.getJSONObject(i);

            String id = line.getString("movie_id");

            if (!id.equals(movieId))
            {
                blank.put(line);
            }

        }

        preferences.edit().putString("favourites", blank.toString()).apply();

        Snackbar.make(view, R.string.fav_remove, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();

    }

    public static String convertImageToBase64(String poster) throws MalformedURLException

    {

        String encodedString = "";

        URL imageurl = new URL(poster);
        try

        {

            Bitmap bm = BitmapFactory.decodeStream(imageurl.openConnection().getInputStream());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            byte[] bytes = baos.toByteArray();

            encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);

        }
        catch (IOException e)

        {

            e.printStackTrace();

        }

        return encodedString;

    }

    public static String convertBitmapToBase64(Bitmap bitmap)

    {

        String encodedString = "";

        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOS);

        encodedString = Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);

        return encodedString;

    }


    public static Bitmap decodeBase64Image(String base64String)
    {

        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);

        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        return decodedByte;

    }
}
