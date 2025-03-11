package ttit.com.shuvo.eliteforce.fleet.assignment.arraylists;

public class VehicleList {
    private String vi_id;
    private String year;
    private String model;
    private String name;
    private String reg_no;
    private String di_id;
    private String di_emp_id;
    private String di_name;

    public VehicleList(String vi_id, String year, String model, String name, String reg_no, String di_id, String di_emp_id, String di_name) {
        this.vi_id = vi_id;
        this.year = year;
        this.model = model;
        this.name = name;
        this.reg_no = reg_no;
        this.di_id = di_id;
        this.di_emp_id = di_emp_id;
        this.di_name = di_name;
    }

    public String getDi_id() {
        return di_id;
    }

    public void setDi_id(String di_id) {
        this.di_id = di_id;
    }

    public String getDi_emp_id() {
        return di_emp_id;
    }

    public void setDi_emp_id(String di_emp_id) {
        this.di_emp_id = di_emp_id;
    }

    public String getDi_name() {
        return di_name;
    }

    public void setDi_name(String di_name) {
        this.di_name = di_name;
    }

    public String getVi_id() {
        return vi_id;
    }

    public void setVi_id(String vi_id) {
        this.vi_id = vi_id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
    }
}
