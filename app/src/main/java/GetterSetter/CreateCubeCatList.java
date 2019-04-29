package GetterSetter;

/**
 * Created by AASHI on 05-04-2018.
 */

public class CreateCubeCatList {


    private String name, cat_id, img;
    private boolean isSelected = false;
    private boolean isSelect = false;


    public CreateCubeCatList(String name, String cat_id, String img) {
        this.name = name;
        this.cat_id = cat_id;
        this.img = img;

    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}


