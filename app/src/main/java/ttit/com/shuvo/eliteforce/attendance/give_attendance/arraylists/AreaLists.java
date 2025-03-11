package ttit.com.shuvo.eliteforce.attendance.give_attendance.arraylists;

public class AreaLists {
    private String latitude;
    private String longitude;
    private String coverage;
    private String coa_id;

    public AreaLists(String latitude, String longitude, String coverage, String coa_id) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.coverage = coverage;
        this.coa_id = coa_id;
    }

    public String getCoa_id() {
        return coa_id;
    }

    public void setCoa_id(String coa_id) {
        this.coa_id = coa_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }
}
