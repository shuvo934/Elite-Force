package ttit.com.shuvo.eliteforce.attendance.att_update.new_request.model;

public class AttendanceReqType {
    private String attendance_req;
    private String attendance_req_details;

    public AttendanceReqType(String attendance_req, String attendance_req_details) {
        this.attendance_req = attendance_req;
        this.attendance_req_details = attendance_req_details;
    }

    public String getAttendance_req() {
        return attendance_req;
    }

    public void setAttendance_req(String attendance_req) {
        this.attendance_req = attendance_req;
    }

    public String getAttendance_req_details() {
        return attendance_req_details;
    }

    public void setAttendance_req_details(String attendance_req_details) {
        this.attendance_req_details = attendance_req_details;
    }
}
