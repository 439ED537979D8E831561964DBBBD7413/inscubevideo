package GetterSetter;

/**
 * Created by AASHI on 12-04-2018.
 */

public class Comment_List {

    String Comment_User_Id, Comment_User_Name, Comment_Text, Comment_Post_Id, Comment_User_Image, Comment_Id,Comment_Time;

    public Comment_List(String comment_User_Id, String comment_User_Name, String comment_Text, String comment_Post_Id,
                        String comment_User_Image, String comment_Id, String comment_Time) {
        Comment_User_Id = comment_User_Id;
        Comment_User_Name = comment_User_Name;
        Comment_Text = comment_Text;
        Comment_Post_Id = comment_Post_Id;
        Comment_User_Image = comment_User_Image;
        Comment_Id = comment_Id;
        Comment_Time = comment_Time;
    }

    public String getComment_User_Id() {
        return Comment_User_Id;
    }

    public void setComment_User_Id(String comment_User_Id) {
        Comment_User_Id = comment_User_Id;
    }

    public String getComment_User_Name() {
        return Comment_User_Name;
    }

    public void setComment_User_Name(String comment_User_Name) {
        Comment_User_Name = comment_User_Name;
    }

    public String getComment_Text() {
        return Comment_Text;
    }

    public void setComment_Text(String comment_Text) {
        Comment_Text = comment_Text;
    }

    public String getComment_Post_Id() {
        return Comment_Post_Id;
    }

    public void setComment_Post_Id(String comment_Post_Id) {
        Comment_Post_Id = comment_Post_Id;
    }

    public String getComment_User_Image() {
        return Comment_User_Image;
    }

    public void setComment_User_Image(String comment_User_Image) {
        Comment_User_Image = comment_User_Image;
    }

    public String getComment_Id() {
        return Comment_Id;
    }

    public void setComment_Id(String comment_Id) {
        Comment_Id = comment_Id;
    }

    public String getComment_Time() {
        return Comment_Time;
    }

    public void setComment_Time(String comment_Time) {
        Comment_Time = comment_Time;
    }
}
