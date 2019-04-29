package GetterSetter;

/**
 * Created by AASHI on 31-07-2018.
 */

public class Search_user
{
    String _id;

   String Inscube_Name;
   String  Image;
   String Hash_Tag_1;
   String Hash_Tag_2;
   String Hash_Tag_3;
   public Search_user(String id,String image,String inscube_Name,String hash_Tag_1,String hash_Tag_2,String hash_Tag_3)
   {
       _id =id;
       Inscube_Name=inscube_Name;
       Image=image;
       Hash_Tag_1=hash_Tag_1;
       Hash_Tag_2=hash_Tag_2;
       Hash_Tag_3=hash_Tag_3;
   }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setHash_Tag_1(String hash_Tag_1) {
        Hash_Tag_1 = hash_Tag_1;
    }

    public void setHash_Tag_2(String hash_Tag_2) {
        Hash_Tag_2 = hash_Tag_2;
    }

    public void setHash_Tag_3(String hash_Tag_3) {
        Hash_Tag_3 = hash_Tag_3;
    }

    public void setImage(String image) {
        Image = image;
    }

    public void setInscube_Name(String inscube_Name) {
        Inscube_Name = inscube_Name;
    }

    public String get_id() {
        return _id;
    }

    public String getInscube_Name() {
        return Inscube_Name;
    }

    public String getImage() {
        return Image;
    }

    public String getHash_Tag_1() {
        return Hash_Tag_1;
    }

    public String getHash_Tag_2() {
        return Hash_Tag_2;
    }

    public String getHash_Tag_3() {
        return Hash_Tag_3;
    }

}
