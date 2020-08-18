package fyp.hireme.Model;

public class Requests {
    String date;
    String userId;
    String email;
    String role;

    public String getStatus() {
        return status;
    }
 public Requests(){

 }
    public void setStatus(String status) {
        this.status = status;
    }

    String status;

    public Requests(String date, String userId, String email, String role,String status) {
        this.date = date;
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.status=status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
