package ttit.com.shuvo.eliteforce.fleet.driver_assignment.arraylists;

public class DriverAssignmentList {
    private String fra_pk;
    private String fra_requision_id;
    private String fra_fleet_vi_id;
    private String fr_code;
    private String visit_purpose;
    private String requester_name;
    private String fr_requester_mobile_no;
    private String from_location;
    private String from_date;
    private String to_location;
    private String to_date;
    private String vh_type;
    private String fr_travelers_qty;
    private String vh_year;
    private String vh_model;
    private String vh_name;
    private String vh_reg_no;
    private String fra_driver_acknoledgement;
    private String fra_remarks;

    public DriverAssignmentList(String fra_pk, String fra_requision_id, String fra_fleet_vi_id, String fr_code,
                                String visit_purpose, String requester_name, String fr_requester_mobile_no,
                                String from_location, String from_date, String to_location, String to_date,
                                String vh_type, String fr_travelers_qty, String vh_year, String vh_model,
                                String vh_name, String vh_reg_no, String fra_driver_acknoledgement, String fra_remarks) {
        this.fra_pk = fra_pk;
        this.fra_requision_id = fra_requision_id;
        this.fra_fleet_vi_id = fra_fleet_vi_id;
        this.fr_code = fr_code;
        this.visit_purpose = visit_purpose;
        this.requester_name = requester_name;
        this.fr_requester_mobile_no = fr_requester_mobile_no;
        this.from_location = from_location;
        this.from_date = from_date;
        this.to_location = to_location;
        this.to_date = to_date;
        this.vh_type = vh_type;
        this.fr_travelers_qty = fr_travelers_qty;
        this.vh_year = vh_year;
        this.vh_model = vh_model;
        this.vh_name = vh_name;
        this.vh_reg_no = vh_reg_no;
        this.fra_driver_acknoledgement = fra_driver_acknoledgement;
        this.fra_remarks = fra_remarks;
    }

    public String getFra_pk() {
        return fra_pk;
    }

    public void setFra_pk(String fra_pk) {
        this.fra_pk = fra_pk;
    }

    public String getFra_requision_id() {
        return fra_requision_id;
    }

    public void setFra_requision_id(String fra_requision_id) {
        this.fra_requision_id = fra_requision_id;
    }

    public String getFra_fleet_vi_id() {
        return fra_fleet_vi_id;
    }

    public void setFra_fleet_vi_id(String fra_fleet_vi_id) {
        this.fra_fleet_vi_id = fra_fleet_vi_id;
    }

    public String getVisit_purpose() {
        return visit_purpose;
    }

    public void setVisit_purpose(String visit_purpose) {
        this.visit_purpose = visit_purpose;
    }

    public String getRequester_name() {
        return requester_name;
    }

    public void setRequester_name(String requester_name) {
        this.requester_name = requester_name;
    }

    public String getFr_requester_mobile_no() {
        return fr_requester_mobile_no;
    }

    public void setFr_requester_mobile_no(String fr_requester_mobile_no) {
        this.fr_requester_mobile_no = fr_requester_mobile_no;
    }

    public String getFrom_location() {
        return from_location;
    }

    public void setFrom_location(String from_location) {
        this.from_location = from_location;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_location() {
        return to_location;
    }

    public void setTo_location(String to_location) {
        this.to_location = to_location;
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

    public String getFr_travelers_qty() {
        return fr_travelers_qty;
    }

    public void setFr_travelers_qty(String fr_travelers_qty) {
        this.fr_travelers_qty = fr_travelers_qty;
    }

    public String getVh_year() {
        return vh_year;
    }

    public void setVh_year(String vh_year) {
        this.vh_year = vh_year;
    }

    public String getVh_model() {
        return vh_model;
    }

    public void setVh_model(String vh_model) {
        this.vh_model = vh_model;
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

    public String getFra_driver_acknoledgement() {
        return fra_driver_acknoledgement;
    }

    public void setFra_driver_acknoledgement(String fra_driver_acknoledgement) {
        this.fra_driver_acknoledgement = fra_driver_acknoledgement;
    }

    public String getFra_remarks() {
        return fra_remarks;
    }

    public void setFra_remarks(String fra_remarks) {
        this.fra_remarks = fra_remarks;
    }

    public String getFr_code() {
        return fr_code;
    }

    public void setFr_code(String fr_code) {
        this.fr_code = fr_code;
    }
}
