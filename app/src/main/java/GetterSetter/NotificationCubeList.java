package GetterSetter;

/**
 * Created by AASHI on 30-03-2018.
 */

public class NotificationCubeList
{

    String cube_name, notification_count;
    int cube_id, cube_img;

    public NotificationCubeList(String cube_name, int cube_img, int cube_id, String notification_count) {
        this.cube_name = cube_name;
        this.cube_img = cube_img;
        this.cube_id = cube_id;
        this.notification_count = notification_count;
    }

    public String getNotification_count() {
        return notification_count;
    }

    public void setNotification_count(String notification_count) {
        this.notification_count = notification_count;
    }

    public String getCube_name() {
        return cube_name;
    }

    public void setCube_name(String cube_name) {
        this.cube_name = cube_name;
    }

    public int getCube_img() {
        return cube_img;
    }

    public void setCube_img(int cube_img) {
        this.cube_img = cube_img;
    }

    public int getCube_id() {
        return cube_id;
    }

    public void setCube_id(int cube_id) {
        this.cube_id = cube_id;
    }
}
