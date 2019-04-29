package GetterSetter;

/**
 * Created by AASHI on 29-03-2018.
 */

public class CubeCategoeyList {


    String cube_name, cube_members_count, cube_type, cube_join_info, cube_image, cube_id, security_code, cat_id, cubeuser_id, selected_position;
    private boolean isSelected = false;


    public CubeCategoeyList(String cube_name, String cubeuser_id, String cube_id, String cube_image, String cube_members_count, String cube_type,
                            String security_code, String cat_id, String selected_position) {
        this.cube_name = cube_name;
        this.cube_image = cube_image;
        this.cube_members_count = cube_members_count;
        this.cube_type = cube_type;
        this.cube_id = cube_id;
        this.security_code = security_code;
        this.cat_id = cat_id;
        this.cubeuser_id = cubeuser_id;
        this.selected_position = selected_position;
    }

    public String getSelected_position() {
        return selected_position;
    }

    public void setSelected_position(String selected_position) {
        this.selected_position = selected_position;
    }

    public String getCubeuser_id() {
        return cubeuser_id;
    }

    public void setCubeuser_id(String cubeuser_id) {
        this.cubeuser_id = cubeuser_id;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getSecurity_code() {
        return security_code;
    }

    public void setSecurity_code(String security_code) {
        this.security_code = security_code;
    }

    public String getCube_id() {
        return cube_id;
    }

    public void setCube_id(String cube_id) {
        this.cube_id = cube_id;
    }

    public String getCube_name() {
        return cube_name;
    }

    public void setCube_name(String cube_name) {
        this.cube_name = cube_name;
    }

    public String getCube_image() {
        return cube_image;
    }

    public void setCube_image(String cube_image) {
        this.cube_image = cube_image;
    }

    public String getCube_members_count() {
        return cube_members_count;
    }

    public void setCube_members_count(String cube_members_count) {
        this.cube_members_count = cube_members_count;
    }

    public String getCube_type() {
        return cube_type;
    }

    public void setCube_type(String cube_type) {
        this.cube_type = cube_type;
    }

    public String getCube_join_info() {
        return cube_join_info;
    }

    public void setCube_join_info(String cube_join_info) {
        this.cube_join_info = cube_join_info;
    }
}
