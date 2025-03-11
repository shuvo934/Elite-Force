package ttit.com.shuvo.eliteforce.login.model;

public class UserInfoList {
    private String userName;
    private String user_fname;
    private String user_lname;
    private String email;
    private String contact;
    private String emp_id;
    private String usr_name;
    private boolean isp_user;

    public UserInfoList(String userName, String user_fname, String user_lname, String email, String contact, String emp_id, String usr_name, boolean isp_user) {
        this.userName = userName;
        this.user_fname = user_fname;
        this.user_lname = user_lname;
        this.email = email;
        this.contact = contact;
        this.emp_id = emp_id;
        this.usr_name = usr_name;
        this.isp_user = isp_user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUser_fname() {
        return user_fname;
    }

    public void setUser_fname(String user_fname) {
        this.user_fname = user_fname;
    }

    public String getUser_lname() {
        return user_lname;
    }

    public void setUser_lname(String user_lname) {
        this.user_lname = user_lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getUsr_name() {
        return usr_name;
    }

    public void setUsr_name(String usr_name) {
        this.usr_name = usr_name;
    }

    public boolean isIsp_user() {
        return isp_user;
    }

    public void setIsp_user(boolean isp_user) {
        this.isp_user = isp_user;
    }
}
