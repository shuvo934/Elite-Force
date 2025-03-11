package ttit.com.shuvo.eliteforce.fleet.req_stat.arraylists;

public class RequisitionList {
    private final String fr_code;
    private final String req_stat;
    private final String req_date;
    private final String from_location;
    private final String from_date;
    private final String to_location;
    private final String to_date;
    private final String vh_type;
    private final String fr_qty;
    private final String fr_travelers_qty;

    public RequisitionList(String fr_code, String req_stat, String req_date, String from_location, String from_date, String to_location, String to_date, String vh_type, String fr_qty, String fr_travelers_qty) {
        this.fr_code = fr_code;
        this.req_stat = req_stat;
        this.req_date = req_date;
        this.from_location = from_location;
        this.from_date = from_date;
        this.to_location = to_location;
        this.to_date = to_date;
        this.vh_type = vh_type;
        this.fr_qty = fr_qty;
        this.fr_travelers_qty = fr_travelers_qty;
    }

    public String getFr_code() {
        return fr_code;
    }

    public String getReq_stat() {
        return req_stat;
    }

    public String getReq_date() {
        return req_date;
    }

    public String getFrom_location() {
        return from_location;
    }

    public String getFrom_date() {
        return from_date;
    }

    public String getTo_location() {
        return to_location;
    }

    public String getTo_date() {
        return to_date;
    }

    public String getVh_type() {
        return vh_type;
    }

    public String getFr_qty() {
        return fr_qty;
    }

    public String getFr_travelers_qty() {
        return fr_travelers_qty;
    }
}
