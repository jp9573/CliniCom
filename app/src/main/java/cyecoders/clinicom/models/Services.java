package cyecoders.clinicom.models;

/**
 * Created by jay on 30/3/18.
 */

public class Services {

    private String id;
    private String name;
    private String detail;
    private String price;
    private String stars;
    private String numberOfVoters;

    public Services(String name, String detail, String price, String stars, String numberOfVoters) {
        this.name = name;
        this.detail = detail;
        this.price = price;
        this.stars = stars;
        this.numberOfVoters = numberOfVoters;
    }

    public Services(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getNumberOfVoters() {
        return numberOfVoters;
    }

    public void setNumberOfVoters(String numberOfVoters) {
        this.numberOfVoters = numberOfVoters;
    }
}
