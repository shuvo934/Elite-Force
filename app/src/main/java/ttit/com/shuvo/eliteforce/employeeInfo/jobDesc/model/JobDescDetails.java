package ttit.com.shuvo.eliteforce.employeeInfo.jobDesc.model;

public class JobDescDetails {
    private String item_no;
    private String job_desc;

    public JobDescDetails(String item_no, String job_desc) {
        this.item_no = item_no;
        this.job_desc = job_desc;
    }

    public String getItem_no() {
        return item_no;
    }

    public void setItem_no(String item_no) {
        this.item_no = item_no;
    }

    public String getJob_desc() {
        return job_desc;
    }

    public void setJob_desc(String job_desc) {
        this.job_desc = job_desc;
    }
}
