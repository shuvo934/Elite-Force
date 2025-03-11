package ttit.com.shuvo.eliteforce.fleet.assignment.arraylists;

public class RequisitionToApproveList {
    private String fr_pk;
    private String fr_code;
    private String divm_name;
    private String dept_name;
    private String req_date;
    private String fr_from_address;
    private String from_date;
    private String to_date;
    private String fr_to_address;
    private String vh_type;
    private String fr_qty;
    private String fr_travelers_qty;
    private String emp_supervisor_emp_id;
    private String emp_name;
    private String requester_supervisor;

    public RequisitionToApproveList(String fr_pk, String fr_code, String divm_name, String dept_name, String req_date,
                                    String fr_from_address, String from_date, String to_date, String fr_to_address, String vh_type, String fr_qty, String fr_travelers_qty, String emp_supervisor_emp_id, String emp_name, String requester_supervisor) {
        this.fr_pk = fr_pk;
        this.fr_code = fr_code;
        this.divm_name = divm_name;
        this.dept_name = dept_name;
        this.req_date = req_date;
        this.fr_from_address = fr_from_address;
        this.from_date = from_date;
        this.to_date = to_date;
        this.fr_to_address = fr_to_address;
        this.vh_type = vh_type;
        this.fr_qty = fr_qty;
        this.fr_travelers_qty = fr_travelers_qty;
        this.emp_supervisor_emp_id = emp_supervisor_emp_id;
        this.emp_name = emp_name;
        this.requester_supervisor = requester_supervisor;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getVh_type() {
        return vh_type;
    }

    public void setVh_type(String vh_type) {
        this.vh_type = vh_type;
    }

    public String getFr_pk() {
        return fr_pk;
    }

    public void setFr_pk(String fr_pk) {
        this.fr_pk = fr_pk;
    }

    public String getFr_code() {
        return fr_code;
    }

    public void setFr_code(String fr_code) {
        this.fr_code = fr_code;
    }

    public String getDivm_name() {
        return divm_name;
    }

    public void setDivm_name(String divm_name) {
        this.divm_name = divm_name;
    }

    public String getDept_name() {
        return dept_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }

    public String getReq_date() {
        return req_date;
    }

    public void setReq_date(String req_date) {
        this.req_date = req_date;
    }

    public String getFr_from_address() {
        return fr_from_address;
    }

    public void setFr_from_address(String fr_from_address) {
        this.fr_from_address = fr_from_address;
    }

    public String getFr_to_address() {
        return fr_to_address;
    }

    public void setFr_to_address(String fr_to_address) {
        this.fr_to_address = fr_to_address;
    }

    public String getFr_qty() {
        return fr_qty;
    }

    public void setFr_qty(String fr_qty) {
        this.fr_qty = fr_qty;
    }

    public String getFr_travelers_qty() {
        return fr_travelers_qty;
    }

    public void setFr_travelers_qty(String fr_travelers_qty) {
        this.fr_travelers_qty = fr_travelers_qty;
    }

    public String getEmp_supervisor_emp_id() {
        return emp_supervisor_emp_id;
    }

    public void setEmp_supervisor_emp_id(String emp_supervisor_emp_id) {
        this.emp_supervisor_emp_id = emp_supervisor_emp_id;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getRequester_supervisor() {
        return requester_supervisor;
    }

    public void setRequester_supervisor(String requester_supervisor) {
        this.requester_supervisor = requester_supervisor;
    }
}
