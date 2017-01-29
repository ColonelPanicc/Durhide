package co.brookesoftware.mike.smilingpooemoji;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import com.google.android.gms.games.Games;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public void onStart(){
        super.onStart();
        SwitchPreference showCones = (SwitchPreference) findPreference(getString(R.string.preference_key_show_cones));
        showCones.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(MainActivity.mGoogleApiClient != null && MainActivity.mGoogleApiClient.isConnected()){
                    Games.Achievements.unlock(MainActivity.mGoogleApiClient,getString(R.string.achievement_change_a_setting));
                }
                return false;
            }
        });
    }

}
