package GetterSetter;

import java.util.ArrayList;

/**
 * Created by AASHI on 12-04-2018.
 */

public class Highlights_PostList {


    String Post_Id;
    String Post_User_Id;
    String Post_Category;
    String Post_Text;
    String Post_Link;
    String Post_Ins_Name;
    String Post_User_Image;
    String Time_Ago;
    String PostLink_Title;
    String PostLink_Description;
    String PostLink_Image;
    String PostLink_Url;
    String ownername;

    public String getTags() {
        return tags;
    }

    String tags;

    ArrayList<String> Cube_Id, Cube_Cat_Id, Cube_name, Cube_Image,
            File_Name, File_Type, Emote_Id, Emote_Text, Emote_Count,Trends_text;

    ArrayList<EmoteList> emoteLists;

    public Highlights_PostList(String post_Id, String post_User_Id, String post_Category, String post_Text, String post_Link,
                               String post_Ins_Name, String post_User_Image, String time_Ago, ArrayList<String> cube_Id,
                               ArrayList<String> cube_Cat_Id, ArrayList<String> cube_name, ArrayList<String> cube_Image,
                               ArrayList<String> file_Name, ArrayList<String> file_Type, ArrayList<String> emote_id,
                               ArrayList<String> emote_text, ArrayList<String> emote_count, ArrayList<EmoteList> emoteLists,
                               String link,
                               String link_title, String link_des, String linkimg, String link_url, ArrayList<String> tags,String ownername)
    {
        Post_Id = post_Id;
        Post_User_Id = post_User_Id;
        Post_Category = post_Category;
        Post_Text = post_Text;
        Post_Link = post_Link;
        Post_Ins_Name = post_Ins_Name;
        Post_User_Image = post_User_Image;
        Time_Ago = time_Ago;
        Cube_Id = cube_Id;
        Cube_Cat_Id = cube_Cat_Id;
        Cube_name = cube_name;
        Cube_Image = cube_Image;
        File_Name = file_Name;
        File_Type = file_Type;
        Emote_Id = emote_id;
        Emote_Text = emote_text;
        Emote_Count = emote_count;
        Trends_text = tags;
        this.emoteLists = emoteLists;
        this.Post_Link = link;
        this.PostLink_Title = link_title;
        this.PostLink_Description = link_des;
        this.PostLink_Image = linkimg;
        this.PostLink_Url = link_url;
        this.ownername = ownername;
        //  this.tags = tags;

    }

    public Highlights_PostList() {

    }


    public String getPostLink_Title() {
        return PostLink_Title;
    }

    public void setPostLink_Title(String postLink_Title) {
        PostLink_Title = postLink_Title;
    }

    public String getPostLink_Description() {
        return PostLink_Description;
    }

    public void setPostLink_Description(String postLink_Description) {
        PostLink_Description = postLink_Description;
    }

    public String getPostLink_Image() {
        return PostLink_Image;
    }

    public void setPostLink_Image(String postLink_Image) {
        PostLink_Image = postLink_Image;
    }

    public String getPostLink_Url() {
        return PostLink_Url;
    }

    public void setPostLink_Url(String postLink_Url) {
        PostLink_Url = postLink_Url;
    }

    public ArrayList<EmoteList> getEmoteLists() {
        return emoteLists;
    }

    public void setEmoteLists(ArrayList<EmoteList> emoteLists) {
        this.emoteLists = emoteLists;
    }

    public ArrayList<String> getEmote_Id() {
        return Emote_Id;
    }

    public void setEmote_Id(ArrayList<String> emote_Id) {
        Emote_Id = emote_Id;
    }

    public ArrayList<String> getEmote_Text() {
        return Emote_Text;
    }

    public void setEmote_Text(ArrayList<String> emote_Text) {
        Emote_Text = emote_Text;
    }

    public ArrayList<String> getEmote_Count() {
        return Emote_Count;
    }

    public void setEmote_Count(ArrayList<String> emote_Count) {
        Emote_Count = emote_Count;
    }

    public String getPost_Id() {
        return Post_Id;
    }

    public void setPost_Id(String post_Id) {
        Post_Id = post_Id;
    }

    public String getPost_User_Id() {
        return Post_User_Id;
    }

    public void setPost_User_Id(String post_User_Id) {
        Post_User_Id = post_User_Id;
    }

    public String getPost_Category() {
        return Post_Category;
    }

    public void setPost_Category(String post_Category) {
        Post_Category = post_Category;
    }

    public String getPost_Text() {
        return Post_Text;
    }

    public void setPost_Text(String post_Text) {
        Post_Text = post_Text;
    }

    public String getPost_Link() {
        return Post_Link;
    }

    public void setPost_Link(String post_Link) {
        Post_Link = post_Link;
    }

    public String getPost_Ins_Name() {
        return Post_Ins_Name;
    }

    public void setPost_Ins_Name(String post_Ins_Name) {
        Post_Ins_Name = post_Ins_Name;
    }

    public String getPost_User_Image() {
        return Post_User_Image;
    }

    public void setPost_User_Image(String post_User_Image) {
        Post_User_Image = post_User_Image;
    }

    public String getTime_Ago() {
        return Time_Ago;
    }

    public void setTime_Ago(String time_Ago) {
        Time_Ago = time_Ago;
    }

    public ArrayList<String> getCube_Id() {
        return Cube_Id;
    }

    public void setCube_Id(ArrayList<String> cube_Id) {
        Cube_Id = cube_Id;
    }

    public ArrayList<String> getCube_Cat_Id() {
        return Cube_Cat_Id;
    }

    public void setCube_Cat_Id(ArrayList<String> cube_Cat_Id) {
        Cube_Cat_Id = cube_Cat_Id;
    }

    public ArrayList<String> getCube_name() {
        return Cube_name;
    }

    public void setCube_name(ArrayList<String> cube_name) {
        Cube_name = cube_name;
    }

    public ArrayList<String> getCube_Image() {
        return Cube_Image;
    }

    public void setCube_Image(ArrayList<String> cube_Image) {
        Cube_Image = cube_Image;
    }

    public ArrayList<String> getFile_Name() {
        return File_Name;
    }

    public void setFile_Name(ArrayList<String> file_Name) {
        File_Name = file_Name;
    }

    public ArrayList<String> getFile_Type() {
        return File_Type;
    }

    public void setFile_Type(ArrayList<String> file_Type) {
        File_Type = file_Type;
    }

    public ArrayList<String> getTrends_text() {
        return Trends_text;
    }

    public String getOwnername() {
        return ownername;
    }
}
