package ttit.com.shuvo.eliteforce.check_in_out.arraylists;

import android.graphics.Bitmap;

public class OnGoingCheckList {
    private String cior_id;
    private String cior_register_no;
    private String cior_company_info;
    private String check_date;
    private String cior_company_loc_info;
    private String cior_lat_val;
    private String cior_long_val;
    private String in_time;
    private String cior_in_remarks;
    private Bitmap cior_blob;

    public OnGoingCheckList(String cior_id, String cior_register_no, String cior_company_info, String check_date, String cior_company_loc_info, String cior_lat_val, String cior_long_val, String in_time, String cior_in_remarks, Bitmap cior_blob) {
        this.cior_id = cior_id;
        this.cior_register_no = cior_register_no;
        this.cior_company_info = cior_company_info;
        this.check_date = check_date;
        this.cior_company_loc_info = cior_company_loc_info;
        this.cior_lat_val = cior_lat_val;
        this.cior_long_val = cior_long_val;
        this.in_time = in_time;
        this.cior_in_remarks = cior_in_remarks;
        this.cior_blob = cior_blob;
    }

    public String getCior_id() {
        return cior_id;
    }

    public void setCior_id(String cior_id) {
        this.cior_id = cior_id;
    }

    public String getCior_register_no() {
        return cior_register_no;
    }

    public void setCior_register_no(String cior_register_no) {
        this.cior_register_no = cior_register_no;
    }

    public String getCior_company_info() {
        return cior_company_info;
    }

    public void setCior_company_info(String cior_company_info) {
        this.cior_company_info = cior_company_info;
    }

    public String getCheck_date() {
        return check_date;
    }

    public void setCheck_date(String check_date) {
        this.check_date = check_date;
    }

    public String getCior_company_loc_info() {
        return cior_company_loc_info;
    }

    public void setCior_company_loc_info(String cior_company_loc_info) {
        this.cior_company_loc_info = cior_company_loc_info;
    }

    public String getCior_lat_val() {
        return cior_lat_val;
    }

    public void setCior_lat_val(String cior_lat_val) {
        this.cior_lat_val = cior_lat_val;
    }

    public String getCior_long_val() {
        return cior_long_val;
    }

    public void setCior_long_val(String cior_long_val) {
        this.cior_long_val = cior_long_val;
    }

    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }

    public String getCior_in_remarks() {
        return cior_in_remarks;
    }

    public void setCior_in_remarks(String cior_in_remarks) {
        this.cior_in_remarks = cior_in_remarks;
    }

    public Bitmap getCior_blob() {
        return cior_blob;
    }

    public void setCior_blob(Bitmap cior_blob) {
        this.cior_blob = cior_blob;
    }
}
