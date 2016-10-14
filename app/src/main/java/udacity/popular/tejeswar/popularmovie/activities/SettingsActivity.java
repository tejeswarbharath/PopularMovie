package udacity.popular.tejeswar.popularmovie.activities;

import static android.preference.Preference.*;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import udacity.popular.tejeswar.popularmovie.R;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by tejeswar on 10/9/2016.
 */

public class SettingsActivity extends PreferenceActivity implements OnPreferenceChangeListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general);

        //for all preferences, attach an onPreferenceChangeListener so the UI summary can be updated when the preferences changes
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_sort_key)));

    }


    private void bindPreferenceSummaryToValue(Preference preference)
    {

        preference.setOnPreferenceChangeListener(this);

        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object value)
    {

        String stringValue = value.toString();

        if (preference instanceof ListPreference)
        {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0)
            {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
        else
        {
            preference.setSummary(stringValue);
        }
        return true;
    }

}
