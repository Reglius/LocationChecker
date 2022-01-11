public class City {
    private String city = "";
    private String latitude = "";
    private String longitude = "";

    public City(String city, String latitude, String longitude){
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCity(){
        return city;
    }
    public String getLat(){
        return latitude;
    }
    public String getLng(){
        return longitude;
    }
}
