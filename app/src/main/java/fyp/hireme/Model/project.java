package fyp.hireme.Model;

public class project {
    String title;
    String description;
    String image;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    String customerId;
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String date;
    double lat,lng;

    public project(String title, String description, String image, double lat, double lng,String date,String customerId) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.lat = lat;
        this.lng = lng;
        this.date=date;
        this.customerId=customerId;
    }
    public project(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
