package ttit.com.shuvo.eliteforce.attendance.att_report.map_view.model;

public class LocationNameArray {
    private String firstLocation;
    private String lastLocation;
    private Boolean isWay;
    private String firstTime;
    private String lastTime;
    private String distance;
    private String calcTime;
    private String polyId;
    private String marId;

    public LocationNameArray(String firstLocation, String lastLocation, Boolean isWay, String firstTime, String lastTime, String distance, String calcTime, String polyId, String marId) {
        this.firstLocation = firstLocation;
        this.lastLocation = lastLocation;
        this.isWay = isWay;
        this.firstTime = firstTime;
        this.lastTime = lastTime;
        this.distance = distance;
        this.calcTime = calcTime;
        this.polyId = polyId;
        this.marId = marId;
    }

    public String getFirstLocation() {
        return firstLocation;
    }

    public void setFirstLocation(String firstLocation) {
        this.firstLocation = firstLocation;
    }

    public String getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(String lastLocation) {
        this.lastLocation = lastLocation;
    }

    public Boolean getWay() {
        return isWay;
    }

    public void setWay(Boolean way) {
        isWay = way;
    }

    public String getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(String firstTime) {
        this.firstTime = firstTime;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCalcTime() {
        return calcTime;
    }

    public void setCalcTime(String calcTime) {
        this.calcTime = calcTime;
    }

    public String getPolyId() {
        return polyId;
    }

    public void setPolyId(String polyId) {
        this.polyId = polyId;
    }

    public String getMarId() {
        return marId;
    }

    public void setMarId(String marId) {
        this.marId = marId;
    }
}
