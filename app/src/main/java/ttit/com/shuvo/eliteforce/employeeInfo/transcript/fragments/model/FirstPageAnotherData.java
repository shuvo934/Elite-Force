package ttit.com.shuvo.eliteforce.employeeInfo.transcript.fragments.model;

public class FirstPageAnotherData {

    private String job_no;
    private String struc_desg;
    private String job_objective;
    private String divisoin;
    private String department;

    public FirstPageAnotherData(String job_no, String struc_desg, String job_objective, String divisoin, String department) {
        this.job_no = job_no;
        this.struc_desg = struc_desg;
        this.job_objective = job_objective;
        this.divisoin = divisoin;
        this.department = department;
    }

    public String getJob_no() {
        return job_no;
    }

    public void setJob_no(String job_no) {
        this.job_no = job_no;
    }

    public String getStruc_desg() {
        return struc_desg;
    }

    public void setStruc_desg(String struc_desg) {
        this.struc_desg = struc_desg;
    }

    public String getJob_objective() {
        return job_objective;
    }

    public void setJob_objective(String job_objective) {
        this.job_objective = job_objective;
    }

    public String getDivisoin() {
        return divisoin;
    }

    public void setDivisoin(String divisoin) {
        this.divisoin = divisoin;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
