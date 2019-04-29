package Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.aashi.inscubenewbuild.R;

import java.util.List;

import GetterSetter.Category_GridList;

/**
 * Created by AASHI on 03-04-2018.
 */

public class PostCubeAdapterSingle extends RecyclerView.Adapter<PostCubeAdapterSingle.ViewHolder> {

    Context con;
    List<Category_GridList> list;

    PostCubeAdapterSingle a;

    public void setA(PostCubeAdapterSingle a) {
        this.a = a;
    }

    public PostCubeAdapterSingle getA() {
        return a;
    }


    public PostCubeAdapterSingle(Context con, List<Category_GridList> list, PostCubeAdapterSingle a) {
        this.con = con;
        this.list = list;


    }

    @NonNull
    @Override
    public PostCubeAdapterSingle.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_singlecube_design, parent, false);

        return new PostCubeAdapterSingle.ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull PostCubeAdapterSingle.ViewHolder holder, final int position) {

        Glide.with(con).load(list.get(position).getImg()).override(200, 200)
                .into(holder.cube_type_img);

        holder.cube_name.setText(list.get(position).getName());


    }

    @Override
    public int getItemCount() {

        return list.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ImageView cube_type_img;
        TextView cube_name;

        public ViewHolder(View itemView) {
            super(itemView);

            cube_type_img = (ImageView) itemView.findViewById(R.id.view_cube_img);
            cube_name = (TextView) itemView.findViewById(R.id.cube_name);

        }

    }


}
