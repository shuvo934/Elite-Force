package ttit.com.shuvo.eliteforce.leave.leave_balance.model;

public class LeaveBalanceList {
    private String category;
    private String code;
    private String opening_qty;
    private String earn;
    private String consumed;
    private String transfered;
    private String cash_taken;
    private String balance_qty;

    public LeaveBalanceList(String category, String code, String opening_qty, String earn, String consumed, String transfered, String cash_taken, String balance_qty) {
        this.category = category;
        this.code = code;
        this.opening_qty = opening_qty;
        this.earn = earn;
        this.consumed = consumed;
        this.transfered = transfered;
        this.cash_taken = cash_taken;
        this.balance_qty = balance_qty;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOpening_qty() {
        return opening_qty;
    }

    public void setOpening_qty(String opening_qty) {
        this.opening_qty = opening_qty;
    }

    public String getEarn() {
        return earn;
    }

    public void setEarn(String earn) {
        this.earn = earn;
    }

    public String getConsumed() {
        return consumed;
    }

    public void setConsumed(String consumed) {
        this.consumed = consumed;
    }

    public String getTransfered() {
        return transfered;
    }

    public void setTransfered(String transfered) {
        this.transfered = transfered;
    }

    public String getCash_taken() {
        return cash_taken;
    }

    public void setCash_taken(String cash_taken) {
        this.cash_taken = cash_taken;
    }

    public String getBalance_qty() {
        return balance_qty;
    }

    public void setBalance_qty(String balance_qty) {
        this.balance_qty = balance_qty;
    }
}
