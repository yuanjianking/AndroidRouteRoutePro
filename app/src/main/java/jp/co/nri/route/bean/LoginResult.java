package jp.co.nri.route.bean;

public class LoginResult extends Result {

    private String objid;
    private String name;

    public String getObjid() {
        return objid;
    }

    public void setObjid(String objid) {
        this.objid = objid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
