package GetterSetter;

public class Members {
    String name;
    String image;
    String id;
    public Members(String Name,String Image,String Id)
    {
        this.id = Id;
        this.name =Name;
        this.image=Image;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
