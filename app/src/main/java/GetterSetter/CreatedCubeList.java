package GetterSetter;

import com.test.aashi.inscubenewbuild.StoryPage;

/**
 * Created by AASHI on 13-04-2018.
 */

public class CreatedCubeList {

    String Cube_Name,Cube_Image,Cube_id,Cube_member,Cube_type;


    public CreatedCubeList(String cube_Name, String cube_Image, String cube_id,String Cube_member,String Cube_type) {
        this.Cube_Name = cube_Name;
        this.Cube_Image = cube_Image;
        this.Cube_id = cube_id;
        this.Cube_member=Cube_member;
        this.Cube_type=Cube_type;
    }

    public String getCube_member() {
        return Cube_member;
    }

    public void setCube_member(String cube_member) {
        Cube_member = cube_member;
    }

    public String getCube_type() {
        return Cube_type;
    }

    public void setCube_type(String cube_type) {
        Cube_type = cube_type;
    }

    public String getCube_Name() {
        return Cube_Name;
    }

    public void setCube_Name(String cube_Name) {
        Cube_Name = cube_Name;
    }

    public String getCube_Image() {
        return Cube_Image;
    }

    public void setCube_Image(String cube_Image) {
        Cube_Image = cube_Image;
    }

    public String getCube_id() {
        return Cube_id;
    }

    public void setCube_id(String cube_id) {
        Cube_id = cube_id;
    }
}
