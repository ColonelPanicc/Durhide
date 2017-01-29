package co.brookesoftware.mike.smilingpooemoji;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by ryan on 29/01/17.
 */

public class TriangleIntersection {

    public boolean pointIsInRegion(double x, double y, List<LatLng> points) {

        int i;
        int j;
        boolean result = false;
        for(i = 0, j = points.size() - 1; i < points.size(); j=i++) {
            if((points.get(i).longitude > y) != (points.get(j).longitude > y) &&
                    (x < (points.get(j).latitude - points.get(i).latitude) * (y - points.get(i).longitude) / (points.get(j).longitude - points.get(i).longitude) + points.get(i).latitude)) {
                result = !result;
            }
        }
        return result;
    }

}
