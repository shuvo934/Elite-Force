package ttit.com.shuvo.eliteforce.payRoll.pay_slip.model;

public class MonthSelectList {
    private String monthName;
    private String monthId;
    private String monthstart;
    private String monthEnd;

    public MonthSelectList(String monthName, String monthId, String monthstart, String monthEnd) {
        this.monthName = monthName;
        this.monthId = monthId;
        this.monthstart = monthstart;
        this.monthEnd = monthEnd;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String getMonthId() {
        return monthId;
    }

    public void setMonthId(String monthId) {
        this.monthId = monthId;
    }

    public String getMonthstart() {
        return monthstart;
    }

    public void setMonthstart(String monthstart) {
        this.monthstart = monthstart;
    }

    public String getMonthEnd() {
        return monthEnd;
    }

    public void setMonthEnd(String monthEnd) {
        this.monthEnd = monthEnd;
    }
}
