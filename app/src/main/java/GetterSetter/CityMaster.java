package GetterSetter;

public class CityMaster {

    private String _id;
    private String City_Name;


    public CityMaster(String _id, String city_Name) {
        this._id = _id;
        City_Name = city_Name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCity_Name() {
        return City_Name;
    }

    public void setCity_Name(String city_Name) {
        City_Name = city_Name;
    }
}
