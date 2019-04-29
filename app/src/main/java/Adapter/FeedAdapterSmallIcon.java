package Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.test.aashi.inscubenewbuild.CubeBasedPost;
import com.test.aashi.inscubenewbuild.R;

import java.util.ArrayList;
import java.util.List;

import GetterSetter.Category_GridList;

/**
 * Created by AASHI on 30-03-2018.
 */


public class FeedAdapterSmallIcon extends RecyclerView.Adapter<FeedAdapterSmallIcon.ViewHolder> {


    Context con;
    ArrayList<String> Cube_Id, Cube_Cat_Id, Cube_name, Cube_Image;

    public FeedAdapterSmallIcon(Context con, ArrayList<String> Cube_Id, ArrayList<String> Cube_Cat_Id, ArrayList<String> Cube_name,
                                ArrayList<String> Cube_Image) {
        this.con = con;
        this.Cube_Id = Cube_Id;
        this.Cube_Cat_Id = Cube_Cat_Id;
        this.Cube_name = Cube_name;
        this.Cube_Image = Cube_Image;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_smallicon_design, parent, false);
        return new ViewHolder(v);

    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position)
    {
        Glide.with(con).load(Cube_Image.get(position)).override(80, 100)
                .into(holder.cube_type_img);
        holder.cube_name.setText(Cube_name.get(position));

        holder.main.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v)
                                           {
                                               Intent intent = new Intent(con ,CubeBasedPost.class);
                                               intent.putExtra("cube_id",Cube_Id.get(position));
                                               con.startActivity(intent);
                                           }
                                       }
        );

    }
    @Override
    public int getItemCount() {

        return Cube_name.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        PorterShapeImageView cube_type_img;
        TextView cube_name;
        LinearLayout main;

        public ViewHolder(View itemView) {
            super(itemView);

            cube_type_img = (PorterShapeImageView) itemView.findViewById(R.id.view_cube_img);
            cube_name = (TextView) itemView.findViewById(R.id.cube_name);

            main=(LinearLayout)itemView.findViewById(R.id.main);

        }

    }


}






