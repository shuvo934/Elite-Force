package ttit.com.shuvo.eliteforce.attendance.att_update.new_request.model;

public class LocUpdateList {
    private String locID;
    private String location;

    public LocUpdateList(String locID, String location) {
        this.locID = locID;
        this.location = location;
    }

    public String getLocID() {
        return locID;
    }

    public void setLocID(String locID) {
        this.locID = locID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
