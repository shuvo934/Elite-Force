package ttit.com.shuvo.eliteforce.leave.leave_application.approve_application.model;

public class ForwardHistoryList {
    private String forby;
    private String forComm;
    private String forTo;

    public ForwardHistoryList(String forby, String forComm, String forTo) {
        this.forby = forby;
        this.forComm = forComm;
        this.forTo = forTo;
    }

    public String getForby() {
        return forby;
    }

    public void setForby(String forby) {
        this.forby = forby;
    }

    public String getForComm() {
        return forComm;
    }

    public void setForComm(String forComm) {
        this.forComm = forComm;
    }

    public String getForTo() {
        return forTo;
    }

    public void setForTo(String forTo) {
        this.forTo = forTo;
    }
}
