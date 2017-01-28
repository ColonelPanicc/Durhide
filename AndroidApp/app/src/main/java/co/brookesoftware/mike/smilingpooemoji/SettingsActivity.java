package co.brookesoftware.mike.smilingpooemoji;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class SettingsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent();
            View bar = LayoutInflater.from(this).inflate(R.layout.settings_bar, root, false);
            root.addView(bar, 0);
        }else{
            FrameLayout root = (FrameLayout) findViewById(android.R.id.list).getParent();
            View bar = LayoutInflater.from(this).inflate(R.layout.settings_bar, root, false);
            root.addView(bar, 0);
        }
    }


}
