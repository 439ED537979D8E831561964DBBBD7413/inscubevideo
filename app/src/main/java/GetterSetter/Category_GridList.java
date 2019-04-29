package GetterSetter;

/**
 * Created by saravanakumar8 on 20-Dec-17.
 */

public class Category_GridList {

    private String name;

    private int img;

    public Category_GridList() {

    }

    public Category_GridList(String name, int img) {
        this.name = name;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}