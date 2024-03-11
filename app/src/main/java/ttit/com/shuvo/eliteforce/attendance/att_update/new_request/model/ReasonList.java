package ttit.com.shuvo.eliteforce.attendance.att_update.new_request.model;

public class ReasonList {
    private String reason_id;
    private String reason_name;

    public ReasonList(String reason_id, String reason_name) {
        this.reason_id = reason_id;
        this.reason_name = reason_name;
    }

    public String getReason_id() {
        return reason_id;
    }

    public void setReason_id(String reason_id) {
        this.reason_id = reason_id;
    }

    public String getReason_name() {
        return reason_name;
    }

    public void setReason_name(String reason_name) {
        this.reason_name = reason_name;
    }
}
