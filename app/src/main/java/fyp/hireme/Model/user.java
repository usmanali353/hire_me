package fyp.hireme.Model;

public class user {
    String name;
    String email;
    String phone;
    String password;
    String role;
    String offered_service;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String status;
 public user(){

 }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getOffered_service() {
        return offered_service;
    }

    public void setOffered_service(String offered_service) {
        this.offered_service = offered_service;
    }

    public user(String name, String email, String phone, String password, String role, String offered_service,String status) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.offered_service = offered_service;
        this.status=status;
    }
}
