package ttit.com.shuvo.eliteforce.basic_model;

public class SalaryMonthList {
    private String month;
    private String Salary;

    public SalaryMonthList(String month, String salary) {
        this.month = month;
        Salary = salary;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getSalary() {
        return Salary;
    }

    public void setSalary(String salary) {
        Salary = salary;
    }
}
