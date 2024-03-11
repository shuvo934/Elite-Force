package ttit.com.shuvo.eliteforce.attendance.att_update.new_request.model;

public class ShowShiftList {
    private String shift;
    private String inTime;
    private String lateART;
    private String earlyBT;
    private String outTime;
    private String extendedOT;

    public ShowShiftList(String shift, String inTime, String lateART, String earlyBT, String outTime, String extendedOT) {
        this.shift = shift;
        this.inTime = inTime;
        this.lateART = lateART;
        this.earlyBT = earlyBT;
        this.outTime = outTime;
        this.extendedOT = extendedOT;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getLateART() {
        return lateART;
    }

    public void setLateART(String lateART) {
        this.lateART = lateART;
    }

    public String getEarlyBT() {
        return earlyBT;
    }

    public void setEarlyBT(String earlyBT) {
        this.earlyBT = earlyBT;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getExtendedOT() {
        return extendedOT;
    }

    public void setExtendedOT(String extendedOT) {
        this.extendedOT = extendedOT;
    }
}
