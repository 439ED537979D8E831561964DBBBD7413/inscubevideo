package GetterSetter;

public class StateMaster {

    private String _id;
    private String State_Name;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getState_Name() {
        return State_Name;
    }

    public void setState_Name(String state_Name) {
        State_Name = state_Name;
    }

    public StateMaster(String _id, String state_Name) {
        this._id = _id;
        State_Name = state_Name;
    }
}
