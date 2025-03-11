package ttit.com.shuvo.eliteforce.movement_reg.movements.arraylists;

public class MovementMapList {
    private String fmrd_lat;
    private String fmrd_long;

    public MovementMapList(String fmrd_lat, String fmrd_long) {
        this.fmrd_lat = fmrd_lat;
        this.fmrd_long = fmrd_long;
    }

    public String getFmrd_lat() {
        return fmrd_lat;
    }

    public void setFmrd_lat(String fmrd_lat) {
        this.fmrd_lat = fmrd_lat;
    }

    public String getFmrd_long() {
        return fmrd_long;
    }

    public void setFmrd_long(String fmrd_long) {
        this.fmrd_long = fmrd_long;
    }
}
