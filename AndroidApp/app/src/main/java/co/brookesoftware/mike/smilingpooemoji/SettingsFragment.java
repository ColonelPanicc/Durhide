package co.brookesoftware.mike.smilingpooemoji;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by olivermcleod on 28/01/2017.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

}
