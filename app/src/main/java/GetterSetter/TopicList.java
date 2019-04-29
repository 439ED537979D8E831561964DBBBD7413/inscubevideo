package GetterSetter;

import java.util.ArrayList;

/**
 * Created by AASHI on 06-04-2018.
 */

public class TopicList {

    String Topic_Name,Topic_Description,topic_id,topic_cube_id,topic_user;

    ArrayList<String> File_name,File_type;


    public TopicList(String topic_Name, String topic_Description,String topicid,String topiccubeid,String topicuser,
                     ArrayList<String> file_name, ArrayList<String> file_type) {
        Topic_Name = topic_Name;
        Topic_Description = topic_Description;
        File_name = file_name;
        File_type = file_type;
        topic_id =topicid;
        topic_cube_id = topiccubeid;
        topic_user =topicuser;

    }


    public String getTopic_Name() {
        return Topic_Name;
    }

    public void setTopic_Name(String topic_Name)
    {


        Topic_Name = topic_Name;
    }

    public String getTopic_user() {
        return topic_user;
    }

    public String getTopic_Description()
    {
        return Topic_Description;
    }

    public String getTopic_cube_id() {
        return topic_cube_id;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_Description(String topic_Description) {
        Topic_Description = topic_Description;
    }
    public ArrayList<String> getFile_name() {
        return File_name;
    }

    public void setFile_name(ArrayList<String> file_name) {
        File_name = file_name;
    }
    public ArrayList<String> getFile_type() {
        return File_type;
    }

    public void setFile_type(ArrayList<String> file_type) {
        File_type = file_type;
    }
}
