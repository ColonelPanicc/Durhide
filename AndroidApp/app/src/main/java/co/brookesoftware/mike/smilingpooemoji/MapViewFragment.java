package co.brookesoftware.mike.smilingpooemoji;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.addAll;

public class MapViewFragment extends Fragment {

    private MapView mMapView;
    private GoogleMap googleMap;
    private Map<Marker, Bitmap> imagesToDisplay;

    private RequestQueue requestQueue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        imagesToDisplay = new HashMap<Marker, Bitmap>();
        View rootView = inflater.inflate(R.layout.location_fragment, container, false);
        System.out.println("inflating");

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately
        System.out.println("resumed");

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                System.out.println("checking permissions");
                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MainActivity.REQUEST_LOCATION_PERMISSION);
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.getUiSettings().setMapToolbarEnabled(false);
                googleMap.setBuildingsEnabled(false);
                googleMap.setMyLocationEnabled(true);

                CameraPosition camPos = new CameraPosition.Builder().target(new LatLng(54.7817499,-1.5872562)).zoom(15).build();
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(camPos));

                try {
                    getAllCameras();
                } catch (IOException | JSONException e) {
                    // todo tell user? sort of makes app unusable...
                    e.printStackTrace();
                }

            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this.getActivity());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public void onConnected() {
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(MainActivity.mGoogleApiClient);
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(16).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void addCamera(final double lng, final double lat, String url, final double lat1, final double lng1,
                           final double lat2, final double lng2) {
        // Build request
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        // use bitmap

                        //todo make this actually decent code
                        LatLng p1 = new LatLng(lat, lng);
                        LatLng p2 = new LatLng(lat1, lng1);
                        LatLng p3 = new LatLng(lat2, lng2);
                        ArrayList<LatLng> pointList = new ArrayList<>();
                        pointList.add(p1);
                        pointList.add(p2);
                        pointList.add(p3);

                        addPolygon(pointList);

                        Marker marker = googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(lat, lng))
                                .title("Camera!")
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker))
                        );

                        imagesToDisplay.put(marker, bitmap);
                        GoogleMap.OnMarkerClickListener listener = new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                Bitmap image = imagesToDisplay.get(marker);
                                showMyDialog(mMapView.getContext(), image);
                                return false;
                            }
                        };

                        googleMap.setOnMarkerClickListener(listener);
                    }
                }, 0, 0, null, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        displayVolleyError(error, "addCamera");
                    }
                });
        // Ensure we have a queue
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this.getActivity());
        }
        // Carry out request ASYNC
        requestQueue.add(request);
    }

    private void displayVolleyError(VolleyError error, String whileLoading) {
        Toast.makeText(this.getActivity(), "Error in " + whileLoading + ": " + error.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void getAllCameras() throws IOException, JSONException {
        // read the url
        String url = "http://durhide.herokuapp.com/api/cameras/camera/";
        // Build request
        JsonArrayRequest stringRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject camera = response.getJSONObject(i);
                                double lat = 0;
                                lat = camera.getDouble("Lat");
                                double lng = camera.getDouble("Long");
                                String lnk = camera.getString("ImgLink");

                                // now deal with the second points
                                double lat1 = camera.getDouble("LatRange1");
                                double lng1 = camera.getDouble("LongRange1");

                                double lat2 = camera.getDouble("LatRange2");
                                double lng2 = camera.getDouble("LongRange2");

                                addCamera(lng, lat, lnk, lat1, lng1, lat2, lng2);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        displayVolleyError(error, "getALlCameras");
                    }
                }
        );
        // Ensure we have a queue
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this.getActivity());
        }
        // Carry out request ASYNC
        requestQueue.add(stringRequest);
    }

    private void showMyDialog(Context context, Bitmap bmp) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.image_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);
        dialogBuilder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        ImageView imageView = (ImageView) dialogView.findViewById(R.id.imgBigCameraView);
        imageView.setImageBitmap(bmp);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private View getImageWindow() {
        LinearLayout infoView = new LinearLayout(mMapView.getContext());
        LinearLayout.LayoutParams infoViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        infoView.setOrientation(LinearLayout.HORIZONTAL);
        infoView.setLayoutParams(infoViewParams);

        ImageView infoImageView = new ImageView(mMapView.getContext());
        //Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
        Drawable drawable = (Drawable) getResources().getLayout(R.layout.image_window_layout);
        infoImageView.setImageDrawable(drawable);
        infoView.addView(infoImageView);
        return infoView;
    }

    private void addPolygon(List<LatLng> vertices) {
        googleMap.addPolygon(new PolygonOptions()
                .addAll(vertices)
                .strokeColor(Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(this.getActivity(), R.color.polygon_border))))
                .fillColor(Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(this.getActivity(), R.color.polygon_fill)))));
    }

}

