package GetterSetter;

/**
 * Created by AASHI on 30-03-2018.
 */

public class ViewCubeList {

    int cube_img,cube_imgID;

    public ViewCubeList(int cube_img, int cube_imgID) {
        this.cube_img = cube_img;
        this.cube_imgID = cube_imgID;
    }

    public int getCube_img() {
        return cube_img;
    }

    public void setCube_img(int cube_img) {
        this.cube_img = cube_img;
    }

    public int getCube_imgID() {
        return cube_imgID;
    }

    public void setCube_imgID(int cube_imgID) {
        this.cube_imgID = cube_imgID;
    }
}
