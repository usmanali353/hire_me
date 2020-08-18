package fyp.hireme.Model;

public class Notifications {
    String title,date,message;

    public Notifications(String title, String date, String message) {
        this.title = title;
        this.date = date;
        this.message = message;
    }
    public Notifications(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
