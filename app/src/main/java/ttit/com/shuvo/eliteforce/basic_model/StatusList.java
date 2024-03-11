package ttit.com.shuvo.eliteforce.basic_model;

public class StatusList {
    private String app_code;
    private String approved;
    private String req_date;
    private String req_type;
    private String up_date;
    private String arr_time;
    private String dep_time;
    private String approver;
    private String canceler;

    public StatusList(String app_code, String approved, String req_date, String req_type, String up_date, String arr_time, String dep_time, String approver, String canceler) {
        this.app_code = app_code;
        this.approved = approved;
        this.req_date = req_date;
        this.req_type = req_type;
        this.up_date = up_date;
        this.arr_time = arr_time;
        this.dep_time = dep_time;
        this.approver = approver;
        this.canceler = canceler;
    }

    public String getApp_code() {
        return app_code;
    }

    public void setApp_code(String app_code) {
        this.app_code = app_code;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public String getReq_date() {
        return req_date;
    }

    public void setReq_date(String req_date) {
        this.req_date = req_date;
    }

    public String getReq_type() {
        return req_type;
    }

    public void setReq_type(String req_type) {
        this.req_type = req_type;
    }

    public String getUp_date() {
        return up_date;
    }

    public void setUp_date(String up_date) {
        this.up_date = up_date;
    }

    public String getArr_time() {
        return arr_time;
    }

    public void setArr_time(String arr_time) {
        this.arr_time = arr_time;
    }

    public String getDep_time() {
        return dep_time;
    }

    public void setDep_time(String dep_time) {
        this.dep_time = dep_time;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getCanceler() {
        return canceler;
    }

    public void setCanceler(String canceler) {
        this.canceler = canceler;
    }
}
