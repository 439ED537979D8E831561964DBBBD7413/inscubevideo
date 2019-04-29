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
 * Created by AASHI on 30-03-2018.
 */


public class CreateCubeCategoryAdapter extends RecyclerView.Adapter<CreateCubeCategoryAdapter.ViewHolder> {


    Context con;
    List<Category_GridList> list;

    CreateCubeCategoryAdapter a;

    public void setA(CreateCubeCategoryAdapter a) {
        this.a = a;
    }

    public CreateCubeCategoryAdapter getA() {
        return a;
    }


    public CreateCubeCategoryAdapter(Context con, List<Category_GridList> list, CreateCubeCategoryAdapter a) {
        this.con = con;
        this.list = list;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_cube_design, parent, false);

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Glide.with(con).load(list.get(position).getImg()).override(200, 200)
                .into(holder.cube_type_img);

        holder.cube_name.setText(list.get(position).getName());

        holder.remove_icon.setVisibility(View.GONE);
        holder.remove_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                list.remove(position);
                a.notifyDataSetChanged();


            }
        });


    }

    @Override
    public int getItemCount() {

        return list.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ImageView cube_type_img, remove_icon;
        TextView cube_name;

        public ViewHolder(View itemView) {
            super(itemView);

            remove_icon = (ImageView) itemView.findViewById(R.id.remove_icon);
            cube_type_img = (ImageView) itemView.findViewById(R.id.view_cube_img);
            cube_name = (TextView) itemView.findViewById(R.id.cube_name);

        }

    }


}






