package ttit.com.shuvo.eliteforce.directory.model;

public class PhoneList {
    private String p_emp_id;
    private String phone;

    public PhoneList(String p_emp_id, String phone) {
        this.p_emp_id = p_emp_id;
        this.phone = phone;
    }

    public String getP_emp_id() {
        return p_emp_id;
    }

    public void setP_emp_id(String p_emp_id) {
        this.p_emp_id = p_emp_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
