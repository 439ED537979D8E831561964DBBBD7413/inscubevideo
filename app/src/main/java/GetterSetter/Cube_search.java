package GetterSetter;

/**
 * Created by AASHI on 31-07-2018.
 */

public class Cube_search {


    String _id;
    String Name;
    String Image;
    String Members_Count;
    String Category_Name;
    String Security;
    String userId;

    public Cube_search(String id,String name,String image,String members_Count,String category_Name,String security,String user_id)

    {
        Security =security;
        userId=user_id;
        _id=id;
        Name=name;
        Image=image;
        Members_Count=members_Count;
        Category_Name=category_Name;

    }

    public String getImage() {
        return Image;
    }

    public String get_id() {
        return _id;
    }

    public String getSecurity() {
        return Security;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return Name;
    }

    public String getMembers_Count() {
        return Members_Count;
    }

    public String getCategory_Name() {
        return Category_Name;
    }
}
