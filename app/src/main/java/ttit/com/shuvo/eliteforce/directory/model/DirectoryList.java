package ttit.com.shuvo.eliteforce.directory.model;

public class DirectoryList {
    private String emp_id;
    private String emp_name;
    private String div_name;
    private String dep_name;
    private String des_name;
    private String email_name;
    private String contact_no;
    private String no;


    public DirectoryList(String emp_id, String emp_name, String div_name, String dep_name, String des_name, String email_name,String contact_no,String no) {
        this.emp_id = emp_id;
        this.emp_name = emp_name;
        this.div_name = div_name;
        this.dep_name = dep_name;
        this.des_name = des_name;
        this.email_name = email_name;
        this.contact_no = contact_no;
        this.no = no;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getDiv_name() {
        return div_name;
    }

    public void setDiv_name(String div_name) {
        this.div_name = div_name;
    }

    public String getDep_name() {
        return dep_name;
    }

    public void setDep_name(String dep_name) {
        this.dep_name = dep_name;
    }

    public String getDes_name() {
        return des_name;
    }

    public void setDes_name(String des_name) {
        this.des_name = des_name;
    }

    public String getEmail_name() {
        return email_name;
    }

    public void setEmail_name(String email_name) {
        this.email_name = email_name;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }
}
