package ttit.com.shuvo.eliteforce.basic_model;

public class ForwardEMPList {
    private String empID;
    private String empName;
    private String empTitle;
    private String designation;
    private String division;

    public ForwardEMPList(String empID, String empName, String empTitle, String designation, String division) {
        this.empID = empID;
        this.empName = empName;
        this.empTitle = empTitle;
        this.designation = designation;
        this.division = division;
    }

    public String getEmpID() {
        return empID;
    }

    public void setEmpID(String empID) {
        this.empID = empID;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpTitle() {
        return empTitle;
    }

    public void setEmpTitle(String empTitle) {
        this.empTitle = empTitle;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }
}
