package fyp.hireme.Model;

public class favourite_projects {
    String projectId;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public favourite_projects(String projectId, String title, String description, String latitude, String longitude, String status, String requiredService, String image,int budget) {
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.requiredService = requiredService;
        this.image = image;
        this.budget=budget;
    }

    String title;
    String description;
    String latitude;
    String longitude;
    String status;
    String requiredService;
    String image;
    int budget;

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequiredService() {
        return requiredService;
    }

    public void setRequiredService(String requiredService) {
        this.requiredService = requiredService;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
