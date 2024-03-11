package ttit.com.shuvo.eliteforce.attendance.att_report.model;

public class ReportInformation {
    private String name;
    private String id;
    private String band;
    private String str_des;
    private String fun_des;
    private String job_no;
    private String status;
    private String shift;
    private String division;
    private String department;
    private String joining;
    private String prm_loc;
    private String sec_loc;

    public ReportInformation(String name, String id, String band, String str_des, String fun_des, String job_no, String status, String shift, String division, String department, String joining, String prm_loc, String sec_loc) {
        this.name = name;
        this.id = id;
        this.band = band;
        this.str_des = str_des;
        this.fun_des = fun_des;
        this.job_no = job_no;
        this.status = status;
        this.shift = shift;
        this.division = division;
        this.department = department;
        this.joining = joining;
        this.prm_loc = prm_loc;
        this.sec_loc = sec_loc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }

    public String getStr_des() {
        return str_des;
    }

    public void setStr_des(String str_des) {
        this.str_des = str_des;
    }

    public String getFun_des() {
        return fun_des;
    }

    public void setFun_des(String fun_des) {
        this.fun_des = fun_des;
    }

    public String getJob_no() {
        return job_no;
    }

    public void setJob_no(String job_no) {
        this.job_no = job_no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJoining() {
        return joining;
    }

    public void setJoining(String joining) {
        this.joining = joining;
    }

    public String getPrm_loc() {
        return prm_loc;
    }

    public void setPrm_loc(String prm_loc) {
        this.prm_loc = prm_loc;
    }

    public String getSec_loc() {
        return sec_loc;
    }

    public void setSec_loc(String sec_loc) {
        this.sec_loc = sec_loc;
    }
}
