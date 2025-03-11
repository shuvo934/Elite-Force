package ttit.com.shuvo.eliteforce.tracking.arraylists;

public class AllVehicleLocationList {
    private String ell_id;
    private String empId;
    private String lat;
    private String lng;
    private String time;
    private String speed;
    private String address;
    private String accuracy;
    private String bearing;
    private String di_id;
    private String vi_id;
    private String is_stopped;
    private String vh_name;
    private String vh_reg_no;
    private String di_name;

    public AllVehicleLocationList(String ell_id, String empId, String lat, String lng, String time, String speed, String address, String accuracy, String bearing, String di_id, String vi_id, String is_stopped, String vh_name, String vh_reg_no, String di_name) {
        this.ell_id = ell_id;
        this.empId = empId;
        this.lat = lat;
        this.lng = lng;
        this.time = time;
        this.speed = speed;
        this.address = address;
        this.accuracy = accuracy;
        this.bearing = bearing;
        this.di_id = di_id;
        this.vi_id = vi_id;
        this.is_stopped = is_stopped;
        this.vh_name = vh_name;
        this.vh_reg_no = vh_reg_no;
        this.di_name = di_name;
    }

    public String getEll_id() {
        return ell_id;
    }

    public void setEll_id(String ell_id) {
        this.ell_id = ell_id;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getBearing() {
        return bearing;
    }

    public void setBearing(String bearing) {
        this.bearing = bearing;
    }


    public String getDi_id() {
        return di_id;
    }

    public void setDi_id(String di_id) {
        this.di_id = di_id;
    }

    public String getVi_id() {
        return vi_id;
    }

    public void setVi_id(String vi_id) {
        this.vi_id = vi_id;
    }

    public String getIs_stopped() {
        return is_stopped;
    }

    public void setIs_stopped(String is_stopped) {
        this.is_stopped = is_stopped;
    }

    public String getVh_name() {
        return vh_name;
    }

    public void setVh_name(String vh_name) {
        this.vh_name = vh_name;
    }

    public String getVh_reg_no() {
        return vh_reg_no;
    }

    public void setVh_reg_no(String vh_reg_no) {
        this.vh_reg_no = vh_reg_no;
    }

    public String getDi_name() {
        return di_name;
    }

    public void setDi_name(String di_name) {
        this.di_name = di_name;
    }
}
