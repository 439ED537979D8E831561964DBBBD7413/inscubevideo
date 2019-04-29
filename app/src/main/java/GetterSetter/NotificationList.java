package GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by saravanakumar8 on 20-Dec-17.
 */

public class NotificationList {

    private List<String> Trends_Tags = null;
    private List<String> Cube_Ids = null;
    private String _id;
    private String User_Id;
    private String To_User_Id;
    private String Notify_Type;
    private String Trends_Id;
    private String Trends_Text;
    private String Cube_Id;
    private String Emote_Id;
    private String Opinion_Id;
    private String Emote_Text;
    private Integer View_Status;
    private String Active_Status;
    private String createdAt;
    private String updatedAt;
    private Integer __v;
    private String User_Name;
    private String User_Image;
    private String Cube_Name;
    private String TextAddon;
    private String Post_Id;
    private String Post_Type;
    private String Capture_Id;
    private String Capture_Text;

    public List<String> getTrends_Tags() {
        return Trends_Tags;
    }

    public void setTrends_Tags(List<String> trends_Tags) {
        Trends_Tags = trends_Tags;
    }

    public List<String> getCube_Ids() {
        return Cube_Ids;
    }

    public void setCube_Ids(List<String> cube_Ids) {
        Cube_Ids = cube_Ids;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(String user_Id) {
        User_Id = user_Id;
    }

    public String getTo_User_Id() {
        return To_User_Id;
    }

    public void setTo_User_Id(String to_User_Id) {
        To_User_Id = to_User_Id;
    }

    public String getNotify_Type() {
        return Notify_Type;
    }

    public void setNotify_Type(String notify_Type) {
        Notify_Type = notify_Type;
    }

    public String getTrends_Id() {
        return Trends_Id;
    }

    public void setTrends_Id(String trends_Id) {
        Trends_Id = trends_Id;
    }

    public String getTrends_Text() {
        return Trends_Text;
    }

    public void setTrends_Text(String trends_Text) {
        Trends_Text = trends_Text;
    }

    public String getCube_Id() {
        return Cube_Id;
    }

    public void setCube_Id(String cube_Id) {
        Cube_Id = cube_Id;
    }

    public String getEmote_Id() {
        return Emote_Id;
    }

    public void setEmote_Id(String emote_Id) {
        Emote_Id = emote_Id;
    }

    public String getOpinion_Id() {
        return Opinion_Id;
    }

    public void setOpinion_Id(String opinion_Id) {
        Opinion_Id = opinion_Id;
    }

    public String getEmote_Text() {
        return Emote_Text;
    }

    public void setEmote_Text(String emote_Text) {
        Emote_Text = emote_Text;
    }

    public Integer getView_Status() {
        return View_Status;
    }

    public void setView_Status(Integer view_Status) {
        View_Status = view_Status;
    }

    public String getActive_Status() {
        return Active_Status;
    }

    public void setActive_Status(String active_Status) {
        Active_Status = active_Status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer get__v() {
        return __v;
    }

    public void set__v(Integer __v) {
        this.__v = __v;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public String getUser_Image() {
        return User_Image;
    }

    public void setUser_Image(String user_Image) {
        User_Image = user_Image;
    }

    public String getCube_Name() {
        return Cube_Name;
    }

    public void setCube_Name(String cube_Name) {
        Cube_Name = cube_Name;
    }

    public String getTextAddon() {
        return TextAddon;
    }

    public void setTextAddon(String textAddon) {
        TextAddon = textAddon;
    }

    public String getPost_Id() {
        return Post_Id;
    }

    public void setPost_Id(String post_Id) {
        Post_Id = post_Id;
    }

    public String getPost_Type() {
        return Post_Type;
    }

    public void setPost_Type(String post_Type) {
        Post_Type = post_Type;
    }

    public String getCapture_Id() {
        return Capture_Id;
    }

    public void setCapture_Id(String capture_Id) {
        Capture_Id = capture_Id;
    }

    public String getCapture_Text() {
        return Capture_Text;
    }

    public void setCapture_Text(String capture_Text) {
        Capture_Text = capture_Text;
    }
}

