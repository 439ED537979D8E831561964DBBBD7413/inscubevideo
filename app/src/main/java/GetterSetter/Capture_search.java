package GetterSetter;

import java.util.ArrayList;

/**
 * Created by AASHI on 31-07-2018.
 */

public class Capture_search {
    String _id;
    String User_Id;
    String Post_Category;
    String Capture_Text;
    String createdAt;
    String updatedAt;
    String Cube_Name;
    String User_Name;
    String User_Image;
    String Cubes_Count;
    ArrayList<String> Cubes_Id;
    public Capture_search(String user_Name,String post_Text,String cube_name,String Img,
                         String cubes_count,String id)
    {
        User_Name= user_Name;
        Cube_Name=cube_name;
      Capture_Text = post_Text;
        User_Image=Img;
        Cubes_Count=cubes_count;
        _id=id;

    }

    public String getCubes_Count() {
        return Cubes_Count;
    }

    public String getUser_Image() {
        return User_Image;
    }

    public String get_id() {
        return _id;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public String getCreatedAt() {
        return createdAt;
    }
    public String getPost_Category() {
        return Post_Category;
    }

    public String getCapture_Text() {
        return Capture_Text;
    }

    public String getCube_Name() {
        return Cube_Name;
    }

    public ArrayList<String> getCubes_Id() {
        return Cubes_Id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getUser_Id() {
        return User_Id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


    public void setCubes_Id(ArrayList<String> cubes_Id) {
        Cubes_Id = cubes_Id;
    }

    public void setPost_Category(String post_Category) {
        Post_Category = post_Category;
    }


    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUser_Id(String user_Id) {
        User_Id = user_Id;
    }
}
