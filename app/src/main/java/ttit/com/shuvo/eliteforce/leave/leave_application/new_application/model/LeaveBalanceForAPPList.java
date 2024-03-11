package ttit.com.shuvo.eliteforce.leave.leave_application.new_application.model;

public class LeaveBalanceForAPPList {
    private String name;
    private String qty;

    public LeaveBalanceForAPPList(String name, String qty) {
        this.name = name;
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
