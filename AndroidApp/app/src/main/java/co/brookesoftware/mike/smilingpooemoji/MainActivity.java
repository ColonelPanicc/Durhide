package co.brookesoftware.mike.smilingpooemoji;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.location.LocationServices;

import static co.brookesoftware.mike.smilingpooemoji.R.layout.activity_main;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_ACHIEVEMENTS = 1;
    protected static final int REQUEST_LOCATION_PERMISSION = 50;

    CoordinatorLayout coordinatorMainActivity;

    protected static GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);

        coordinatorMainActivity = (CoordinatorLayout) findViewById(R.id.coordinatorMainActivity);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */,
                            this /* OnConnectionFailedListener */)
                    .addConnectionCallbacks(this)
                    .addApi(Games.API)
                    .addScope(Games.SCOPE_GAMES)
                    .addApi(LocationServices.API)

                    .build();
        }
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.btnAchievements) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient),
                        REQUEST_ACHIEVEMENTS);
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_check_achievements));
            } else {
                Snackbar snackbar = Snackbar
                        .make(coordinatorMainActivity, "Google Play Games is not connected", Snackbar.LENGTH_LONG)
                        .setAction("CONNECT", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
                                    Toast.makeText(getApplicationContext(), "Attempting to connect...", Toast.LENGTH_LONG).show();
                                    mGoogleApiClient.clearDefaultAccountAndReconnect();
                                }
                            }
                        });
                snackbar.show();
            }
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    FragmentManager fragmentManager = getFragmentManager();
                    Fragment newFragment = new MapViewFragment();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.map_view_fragment, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Google play games connection methods
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar snackbar = Snackbar
                .make(coordinatorMainActivity, "Failed to connect to Google Play Games", Snackbar.LENGTH_LONG)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
                            Toast.makeText(getApplicationContext(), "Attempting to connect...", Toast.LENGTH_LONG).show();
                            mGoogleApiClient.clearDefaultAccountAndReconnect();
                        }
                    }
                });
        snackbar.show();
        System.out.println("Connection error code: " + connectionResult.getErrorCode());
        Toast.makeText(getApplicationContext(), "Error code: " + connectionResult.getErrorCode(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_open_durhide));
        }

        Player me = Games.Players.getCurrentPlayer(mGoogleApiClient);

        // Load views to modify
        ImageView userImageView = (ImageView) findViewById(R.id.imgUserIcon);
        TextView userNameView = (TextView) findViewById(R.id.lblUserName);
        TextView userInfoView = (TextView) findViewById(R.id.lblUserInfo);

        // Show image
        ImageManager mgr = ImageManager.create(this);
        if (me.hasHiResImage()) {
            mgr.loadImage(userImageView, me.getHiResImageUri());
        } else {
            mgr.loadImage(userImageView, me.getIconImageUri());
        }
        // Show username
        userNameView.setText(me.getDisplayName());

        // Show info(?)
        userInfoView.setText("Level " + me.getLevelInfo().getCurrentLevel().getLevelNumber() + " " + me.getTitle());

        // Run map position initialisation
        ((MapViewFragment) getFragmentManager().findFragmentById(R.id.map_view_fragment)).onConnected();

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getApplicationContext(), "Connection suspended", Toast.LENGTH_LONG).show();
    }

}
