package co.brookesoftware.mike.smilingpooemoji;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryan on 29/01/17.
 */

public class Intersection extends Activity {
    private JSONArray arr;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String str = extras.getString("jsondata");
            try {
                arr = new JSONArray(str);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        LocationManager locationManager;
        String context = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(context);

        Criteria crta = new Criteria();
        crta.setAccuracy(Criteria.ACCURACY_FINE);
        crta.setAltitudeRequired(false);
        crta.setBearingRequired(false);
        crta.setCostAllowed(true);
        crta.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(crta, true);

// String provider = LocationManager.GPS_PROVIDER;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        updateWithNewLocation(location);

        locationManager.requestLocationUpdates(provider, 1000, 0, (android.location.LocationListener) locationListener);
    }

    private final LocationListener locationListener = new LocationListener()
    {

        @Override
        public void onLocationChanged(Location location) {
            updateWithNewLocation(location);
        }

        public void onProviderDisabled(String provider) {
            updateWithNewLocation(null);
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    };
    private void updateWithNewLocation(Location location) {

        TriangleIntersection t = new TriangleIntersection();
        for (int i = 0; i < arr.length(); i++) {
            try {
                JSONObject obj = arr.getJSONObject(i);
                List<LatLng> region = new ArrayList<LatLng>();

                region.add(new LatLng(obj.getDouble("Lat"), obj.getDouble("Long")));
                region.add(new LatLng(obj.getDouble("LatRange1"), obj.getDouble("LongRange1")));
                region.add(new LatLng(obj.getDouble("LatRange2"), obj.getDouble("LongRange2")));

                boolean b = t.pointIsInRegion(location.getLatitude(), location.getLongitude(), region);
                if (b) {
                    // then we can point out these things are the main stuff
                    Toast.makeText(getApplicationContext(),"Caught on Candid Camera", Toast.LENGTH_LONG);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

