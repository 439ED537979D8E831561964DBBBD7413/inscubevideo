package Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.test.aashi.inscubenewbuild.CubeBasedPost;
import com.test.aashi.inscubenewbuild.CubeView;
import com.test.aashi.inscubenewbuild.Cubes;
import com.test.aashi.inscubenewbuild.R;

import java.util.ArrayList;
import java.util.List;

import GetterSetter.Cube_search;


public class Search_cubes extends RecyclerView.Adapter<Search_cubes.Myview> {
    Context context;
    private List<Cube_search> userlist = new ArrayList<>();
    Search_cubes a;

    public void setA(Search_cubes a) {
        this.a = a;
    }

    public Search_cubes(Context context, List<Cube_search> list) {
        this.context = context;
        this.userlist = list;
    }


    @NonNull
    @Override
    public Search_cubes.Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SharedPreferences user = context.getSharedPreferences("Logged_in_details", Context.MODE_PRIVATE);
        String users = user.getString("_id", "");
        View view = LayoutInflater.from(context).inflate(R.layout.cube_list, parent, false);
        return new Myview(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Search_cubes.Myview holder, final int position) {
        holder.setIsRecyclable(false);
//search user

        Glide.with(context).load(userlist.get(position).getImage())
                .override(200, 200)
//                    .placeholder(R.drawable.loading_spinner)
                .into(holder.user_image);
        holder.hash1.setVisibility(View.VISIBLE);
       // holder.cubetpe.setVisibility(View.GONE);
        holder.hash.setVisibility(View.VISIBLE);
        holder.name.setText(userlist.get(position).getName());
        holder.hash.setText(userlist.get(position).getMembers_Count()+" Members");
        holder.hash1.setText("( "+userlist.get(position).getCategory_Name()+" )");
      //  holder.hash2.setText(userlist.get(position).getHash_Tag_3());
        holder.cubes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String a = String.valueOf(position);
                Intent i = new Intent(context, CubeView.class);
                i.putExtra("cube_update","0");
                i.putExtra("Cube_Id", userlist.get(position) .get_id());
                i.putExtra("Cube_User_Id", userlist.get(position).getUserId());
                i.putExtra("Cube_Type",    userlist.get(position).getSecurity());
                i.putExtra("Feeds","1");
                i.putExtra("Position", a);
                ((Activity)context).startActivityForResult(i,90);


            }
        });
        final String cube_type = userlist.get(position).getSecurity();
        if (cube_type.contains("Open"))
        {
            holder.cubetpe.setImageResource(R.drawable.globe);

        }
        else if (cube_type.contains("Close")) {

            holder.cubetpe.setImageResource(R.drawable.lock);
        }



       holder.jon.setVisibility(View.GONE);


    //    holder.users.setVisibility(View.GONE);
      //  holder.highlightposts.setVisibility(View.GONE);
       // holder.capture.setVisibility(View.GONE);
        //holder.trends.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }
    static class Myview extends RecyclerView.ViewHolder
    {

        CardView highlightposts,users,capture,trends;
        LinearLayout cubes;
        ImageView user_image,cubetpe;
        TextView name, hash, hash1, hash2;
        Button jon;


        Myview(View itemView) {
            super(itemView);
           // capture =itemView.findViewById(R.id.highlight_cap);
            cubes=itemView.findViewById(R.id.layout1);
            jon=itemView.findViewById(R.id.join_button);
            //trends=itemView.findViewById(R.id.highlight_t);
            //highlightposts = itemView.findViewById(R.id.highlight_p);
            //users=itemView.findViewById(R.id.highlight_u);
            cubetpe=itemView.findViewById(R.id.cube_join_info_img);
            user_image = itemView.findViewById(R.id.cube_img);
            name = itemView.findViewById(R.id.cube_name);
            hash = itemView.findViewById(R.id.members1);
            hash1 = itemView.findViewById(R.id.cube_type2);
          //  hash2 = itemView.findViewById(R.id.hastag3);
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 90:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(context, "finish", Toast.LENGTH_SHORT).show();

                }

                break;
        }
    }
}