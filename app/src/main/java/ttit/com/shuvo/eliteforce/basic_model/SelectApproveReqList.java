package ttit.com.shuvo.eliteforce.basic_model;

public class SelectApproveReqList {
    private String reqCode;
    private String name;
    private String id;
    private String appdate;
    private String upDate;
    private String darmID;
    private String darmEmpId;

    public SelectApproveReqList(String reqCode, String name, String id, String appdate, String upDate, String darmID, String darmEmpId) {
        this.reqCode = reqCode;
        this.name = name;
        this.id = id;
        this.appdate = appdate;
        this.upDate = upDate;
        this.darmID = darmID;
        this.darmEmpId = darmEmpId;
    }

    public String getReqCode() {
        return reqCode;
    }

    public void setReqCode(String reqCode) {
        this.reqCode = reqCode;
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

    public String getAppdate() {
        return appdate;
    }

    public void setAppdate(String appdate) {
        this.appdate = appdate;
    }

    public String getUpDate() {
        return upDate;
    }

    public void setUpDate(String upDate) {
        this.upDate = upDate;
    }

    public String getDarmID() {
        return darmID;
    }

    public void setDarmID(String darmID) {
        this.darmID = darmID;
    }

    public String getDarmEmpId() {
        return darmEmpId;
    }

    public void setDarmEmpId(String darmEmpId) {
        this.darmEmpId = darmEmpId;
    }
}
