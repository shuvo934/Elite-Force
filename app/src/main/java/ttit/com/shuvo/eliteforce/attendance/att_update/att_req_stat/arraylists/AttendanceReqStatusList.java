package ttit.com.shuvo.eliteforce.attendance.att_update.att_req_stat.arraylists;

public class AttendanceReqStatusList {
    private String arm_id;
    private String arm_reason;
    private String arm_add_during_cause;
    private String att_date;
    private String att_time;
    private String time_type;
    private String arm_approved;
    private String arm_comments;
    private String approver_name;
    private String designation;

    public AttendanceReqStatusList(String arm_id, String arm_reason, String arm_add_during_cause, String att_date, String att_time, String time_type, String arm_approved, String arm_comments, String approver_name, String designation) {
        this.arm_id = arm_id;
        this.arm_reason = arm_reason;
        this.arm_add_during_cause = arm_add_during_cause;
        this.att_date = att_date;
        this.att_time = att_time;
        this.time_type = time_type;
        this.arm_approved = arm_approved;
        this.arm_comments = arm_comments;
        this.approver_name = approver_name;
        this.designation = designation;
    }

    public String getArm_id() {
        return arm_id;
    }

    public void setArm_id(String arm_id) {
        this.arm_id = arm_id;
    }

    public String getArm_reason() {
        return arm_reason;
    }

    public void setArm_reason(String arm_reason) {
        this.arm_reason = arm_reason;
    }

    public String getArm_add_during_cause() {
        return arm_add_during_cause;
    }

    public void setArm_add_during_cause(String arm_add_during_cause) {
        this.arm_add_during_cause = arm_add_during_cause;
    }

    public String getAtt_date() {
        return att_date;
    }

    public void setAtt_date(String att_date) {
        this.att_date = att_date;
    }

    public String getAtt_time() {
        return att_time;
    }

    public void setAtt_time(String att_time) {
        this.att_time = att_time;
    }

    public String getTime_type() {
        return time_type;
    }

    public void setTime_type(String time_type) {
        this.time_type = time_type;
    }

    public String getArm_approved() {
        return arm_approved;
    }

    public void setArm_approved(String arm_approved) {
        this.arm_approved = arm_approved;
    }

    public String getArm_comments() {
        return arm_comments;
    }

    public void setArm_comments(String arm_comments) {
        this.arm_comments = arm_comments;
    }

    public String getApprover_name() {
        return approver_name;
    }

    public void setApprover_name(String approver_name) {
        this.approver_name = approver_name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
