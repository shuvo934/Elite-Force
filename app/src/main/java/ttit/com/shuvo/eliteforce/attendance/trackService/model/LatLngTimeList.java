package ttit.com.shuvo.eliteforce.attendance.trackService.model;

import com.google.android.gms.maps.model.LatLng;

public class LatLngTimeList {
    private LatLng latLng;
    private String time;

    public LatLngTimeList(LatLng latLng, String time) {
        this.latLng = latLng;
        this.time = time;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
