package co.brookesoftware.mike.smilingpooemoji;

import android.Manifest;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioAttributes;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.getIntent;
import static android.content.Intent.getIntentOld;

/**
 * Created by ryan on 29/01/17.
 */

public class IntersectionService extends Service {
    private static final String TAG = "JSK;alkg;eiruhgq;i";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 1f;
    private static List<JSONObject> overlappingCamera;
    private volatile JSONArray list;

    public IntersectionService() {

    }
//    public IntersectionService(JSONArray o) {
//        overlappingCamera = new ArrayList<JSONObject>();
//        this.list = o;
//    }

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;
        JSONArray l;

        public LocationListener(String provider, JSONArray list) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
            this.l = list;
        }


        @Override
        public void onLocationChanged(Location location) {
            mLastLocation.set(location);
            System.out.println("location changed");


            TriangleIntersection t = new TriangleIntersection();
            if (list == null) {

                return;
            }
            System.out.println("NOT NULL!");
            for (int i = 0; i < list.length(); i++) {
                try {
                    JSONObject obj = list.getJSONObject(i);
                    List<LatLng> region = new ArrayList<LatLng>();

                    region.add(new LatLng(obj.getDouble("Lat"), obj.getDouble("Long")));
                    region.add(new LatLng(obj.getDouble("LatRange1"), obj.getDouble("LongRange1")));
                    region.add(new LatLng(obj.getDouble("LatRange2"), obj.getDouble("LongRange2")));

                    boolean b = t.pointIsInRegion(location.getLatitude(), location.getLongitude(), region);
                    if (b) {
//                        android.support.v4.app.NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplication()).setContentTitle("Fuck").setContentText("Oh no");
//                        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                        mNotifyMgr.notify(1,mBuilder.build());
//                        System.out.println("In Range!");
                        System.out.println("Yes");
                        Vibrator mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        mVibrator.vibrate(300);
                        Toast.makeText(getApplicationContext(), "You are on camera!", Toast.LENGTH_LONG).show();
                    } else {
                        System.out.println("No");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }


    }

    ;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        initializeLocationManager();

        try {
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


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }



    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int i = super.onStartCommand(intent, flags, startId);
        try {
            String jsonStr = (String) intent.getExtras().get("jsondata");
            System.out.println(jsonStr);
            list = new JSONArray(jsonStr);
            System.out.println("Created new JSON Array");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LocationListener[] mLocationListeners = new LocationListener[]{
                new LocationListener(LocationManager.GPS_PROVIDER, list),
                new LocationListener(LocationManager.NETWORK_PROVIDER, list)
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return i;

        }
        mLocationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListeners[0]);

        return i;
    }
// LatRange1  54.7627115844

    // Long Range 2 -1.57982600982
}
