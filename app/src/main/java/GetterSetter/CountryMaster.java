package GetterSetter;

public class CountryMaster {

    private String _id;
    private String Country_Name;

    public CountryMaster(String _id, String country_Name) {
        this._id = _id;
        Country_Name = country_Name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCountry_Name() {
        return Country_Name;
    }

    public void setCountry_Name(String country_Name) {
        Country_Name = country_Name;
    }
}
