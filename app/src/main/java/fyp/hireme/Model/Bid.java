package fyp.hireme.Model;

public class Bid {
    String mechanic_name;
    String bid_date;
    String projectId;

    public String getMechanicId() {
        return mechanicId;
    }

    public void setMechanicId(String mechanicId) {
        this.mechanicId = mechanicId;
    }

    String mechanicId;
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String status;
    int bid_price;

    public Bid(String mechanic_name, String bid_date, String projectId, int bid_price,String status,String mechanicId) {
        this.mechanic_name = mechanic_name;
        this.bid_date = bid_date;
        this.projectId = projectId;
        this.bid_price = bid_price;
        this.status=status;
        this.mechanicId=mechanicId;
    }

    public Bid(){

    }

    public String getMechanic_name() {
        return mechanic_name;
    }

    public void setMechanic_name(String mechanic_name) {
        this.mechanic_name = mechanic_name;
    }

    public String getBid_date() {
        return bid_date;
    }

    public void setBid_date(String bid_date) {
        this.bid_date = bid_date;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public int getBid_price() {
        return bid_price;
    }

    public void setBid_price(int bid_price) {
        this.bid_price = bid_price;
    }
}
