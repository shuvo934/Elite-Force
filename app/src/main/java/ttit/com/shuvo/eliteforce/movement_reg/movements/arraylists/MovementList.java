package ttit.com.shuvo.eliteforce.movement_reg.movements.arraylists;

import java.sql.Blob;

public class MovementList {
    private String fmr_code;
    private String vehicle_name;
    private String di_full_name;
    private String ad_name;
    private String m_date;
    private String fmr_movement_type;
    private String fmr_movement_details;
    private String fra_pk;
    private String map_value;
    private String fmrd_id;
    private Blob move_file;
    private String fr_code;

    public MovementList(String fmr_code, String vehicle_name, String di_full_name, String ad_name, String m_date, String fmr_movement_type, String fmr_movement_details, String fra_pk, String map_value, String fmrd_id, Blob move_file, String fr_code) {
        this.fmr_code = fmr_code;
        this.vehicle_name = vehicle_name;
        this.di_full_name = di_full_name;
        this.ad_name = ad_name;
        this.m_date = m_date;
        this.fmr_movement_type = fmr_movement_type;
        this.fmr_movement_details = fmr_movement_details;
        this.fra_pk = fra_pk;
        this.map_value = map_value;
        this.fmrd_id = fmrd_id;
        this.move_file = move_file;
        this.fr_code = fr_code;
    }

    public String getFmr_code() {
        return fmr_code;
    }

    public void setFmr_code(String fmr_code) {
        this.fmr_code = fmr_code;
    }

    public String getVehicle_name() {
        return vehicle_name;
    }

    public void setVehicle_name(String vehicle_name) {
        this.vehicle_name = vehicle_name;
    }

    public String getDi_full_name() {
        return di_full_name;
    }

    public void setDi_full_name(String di_full_name) {
        this.di_full_name = di_full_name;
    }

    public String getAd_name() {
        return ad_name;
    }

    public void setAd_name(String ad_name) {
        this.ad_name = ad_name;
    }

    public String getM_date() {
        return m_date;
    }

    public void setM_date(String m_date) {
        this.m_date = m_date;
    }

    public String getFmr_movement_type() {
        return fmr_movement_type;
    }

    public void setFmr_movement_type(String fmr_movement_type) {
        this.fmr_movement_type = fmr_movement_type;
    }

    public String getFmr_movement_details() {
        return fmr_movement_details;
    }

    public void setFmr_movement_details(String fmr_movement_details) {
        this.fmr_movement_details = fmr_movement_details;
    }

    public String getFra_pk() {
        return fra_pk;
    }

    public void setFra_pk(String fra_pk) {
        this.fra_pk = fra_pk;
    }

    public String getMap_value() {
        return map_value;
    }

    public void setMap_value(String map_value) {
        this.map_value = map_value;
    }

    public String getFmrd_id() {
        return fmrd_id;
    }

    public void setFmrd_id(String fmrd_id) {
        this.fmrd_id = fmrd_id;
    }

    public Blob getMove_file() {
        return move_file;
    }

    public void setMove_file(Blob move_file) {
        this.move_file = move_file;
    }

    public String getFr_code() {
        return fr_code;
    }

    public void setFr_code(String fr_code) {
        this.fr_code = fr_code;
    }
}
