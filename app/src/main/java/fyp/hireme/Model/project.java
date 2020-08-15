package fyp.hireme.Model;

public class project {
    String title;
    String description;
    String image;
    String comments;
    String rating;
    String allottedTo;
    int budget;

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public String getAllottedTo() {
        return allottedTo;
    }

    public void setAllottedTo(String allottedTo) {
        this.allottedTo = allottedTo;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRequiredService() {
        return requiredService;
    }

    public void setRequiredService(String requiredService) {
        this.requiredService = requiredService;
    }

    String requiredService;
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String status;
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

    public project(String title, String description, String image, double lat, double lng,String date,String customerId,String status,String requiredService,int budget) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.lat = lat;
        this.lng = lng;
        this.date=date;
        this.customerId=customerId;
        this.status=status;
        this.requiredService=requiredService;
        this.budget=budget;
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
