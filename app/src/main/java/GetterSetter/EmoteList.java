package GetterSetter;

import java.util.ArrayList;

/**
 * Created by AASHI on 21-04-2018.
 */

public class EmoteList {


    String Emote_Id,Emote_Text,Emote_Count,Emote_post_Id;
    ArrayList<String> Emote_User_Ids;

    public EmoteList(String emote_Id, String emote_Text, String emote_Count, String emote_post_Id, ArrayList<String> emote_User_Ids) {
        Emote_Id = emote_Id;
        Emote_Text = emote_Text;
        Emote_Count = emote_Count;
        Emote_post_Id = emote_post_Id;
        Emote_User_Ids = emote_User_Ids;
    }

    public String getEmote_Id() {
        return Emote_Id;
    }

    public void setEmote_Id(String emote_Id) {
        Emote_Id = emote_Id;
    }

    public String getEmote_Text() {
        return Emote_Text;
    }

    public void setEmote_Text(String emote_Text) {
        Emote_Text = emote_Text;
    }

    public String getEmote_Count() {
        return Emote_Count;
    }

    public void setEmote_Count(String emote_Count) {
        Emote_Count = emote_Count;
    }

    public String getEmote_post_Id() {
        return Emote_post_Id;
    }

    public void setEmote_post_Id(String emote_post_Id) {
        Emote_post_Id = emote_post_Id;
    }

    public ArrayList<String> getEmote_User_Ids() {
        return Emote_User_Ids;
    }

    public void setEmote_User_Ids(ArrayList<String> emote_User_Ids) {
        Emote_User_Ids = emote_User_Ids;
    }
}
