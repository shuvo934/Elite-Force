package ttit.com.shuvo.eliteforce.leave.leave_status.model;

public class EmpLeaveDateList {
    private String d_date;
    private String date_day;
    private String leave_name;
    private String lc_code;
    private String current_balance;
    private String upcoming;

    public EmpLeaveDateList(String d_date, String date_day, String leave_name, String lc_code, String current_balance, String upcoming) {
        this.d_date = d_date;
        this.date_day = date_day;
        this.leave_name = leave_name;
        this.lc_code = lc_code;
        this.current_balance = current_balance;
        this.upcoming = upcoming;
    }

    public String getD_date() {
        return d_date;
    }

    public void setD_date(String d_date) {
        this.d_date = d_date;
    }

    public String getDate_day() {
        return date_day;
    }

    public void setDate_day(String date_day) {
        this.date_day = date_day;
    }

    public String getLeave_name() {
        return leave_name;
    }

    public void setLeave_name(String leave_name) {
        this.leave_name = leave_name;
    }

    public String getLc_code() {
        return lc_code;
    }

    public void setLc_code(String lc_code) {
        this.lc_code = lc_code;
    }

    public String getCurrent_balance() {
        return current_balance;
    }

    public void setCurrent_balance(String current_balance) {
        this.current_balance = current_balance;
    }

    public String getUpcoming() {
        return upcoming;
    }

    public void setUpcoming(String upcoming) {
        this.upcoming = upcoming;
    }
}
