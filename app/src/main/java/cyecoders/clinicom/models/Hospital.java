package cyecoders.clinicom.models;

/**
 * Created by jay on 30/3/18.
 */

public class Hospital {

    private String id;
    private String name;
    private String address;
    private String city;
    private String stars;
    private String latitute;
    private String longitute;
    private String phone;
    private String amneties;

    public Hospital(String id, String name, String address, String city, String stars, String latitute, String longitute, String phone, String amneties) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.stars = stars;
        this.latitute = latitute;
        this.longitute = longitute;
        this.phone = phone;
        this.amneties = amneties;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getLatitute() {
        return latitute;
    }

    public void setLatitute(String latitute) {
        this.latitute = latitute;
    }

    public String getLongitute() {
        return longitute;
    }

    public void setLongitute(String longitute) {
        this.longitute = longitute;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAmneties() {
        return amneties;
    }

    public void setAmneties(String amneties) {
        this.amneties = amneties;
    }
}
