package GetterSetter;

import Adapter.Search_capture;

/**
 * Created by AASHI on 31-07-2018.
 */

public class Trend_sarch {

    String _id;
    String Tag;

    public Trend_sarch(String id,String tag)
    {
        _id=id;
        Tag=tag;
    }

    public String get_id() {
        return _id;
    }

    public String getTag() {
        return Tag;
    }
}
