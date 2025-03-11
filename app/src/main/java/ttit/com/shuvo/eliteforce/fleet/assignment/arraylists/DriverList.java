package ttit.com.shuvo.eliteforce.fleet.assignment.arraylists;

public class DriverList {
    private String di_id;
    private String di_full_name;
    private String di_nick_name;
    private String di_emp_id;

    public DriverList(String di_id, String di_full_name, String di_nick_name, String di_emp_id) {
        this.di_id = di_id;
        this.di_full_name = di_full_name;
        this.di_nick_name = di_nick_name;
        this.di_emp_id = di_emp_id;
    }

    public String getDi_id() {
        return di_id;
    }

    public void setDi_id(String di_id) {
        this.di_id = di_id;
    }

    public String getDi_full_name() {
        return di_full_name;
    }

    public void setDi_full_name(String di_full_name) {
        this.di_full_name = di_full_name;
    }

    public String getDi_nick_name() {
        return di_nick_name;
    }

    public void setDi_nick_name(String di_nick_name) {
        this.di_nick_name = di_nick_name;
    }

    public String getDi_emp_id() {
        return di_emp_id;
    }

    public void setDi_emp_id(String di_emp_id) {
        this.di_emp_id = di_emp_id;
    }
}
