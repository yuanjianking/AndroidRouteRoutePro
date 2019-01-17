package jp.co.nri.route.bean;

public class Location {

    private String _id;
    private String eventid;
    private String userid;
    private String name;
    private String latitude;
    private String longitude;

    private long startTime;
    private double historyDistance;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public double getHistoryDistance() {
        return historyDistance;
    }

    public void setHistoryDistance(double historyDistance) {
        this.historyDistance = historyDistance;
    }
}
