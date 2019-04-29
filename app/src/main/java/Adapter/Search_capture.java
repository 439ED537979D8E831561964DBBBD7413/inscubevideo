package Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.aashi.inscubenewbuild.R;
import com.test.aashi.inscubenewbuild.get_posts;

import java.util.ArrayList;
import java.util.List;

import GetterSetter.Capture_search;
import GetterSetter.SearchAdapter;

/**
 * Created by AASHI on 31-07-2018.
 */

public class Search_capture  extends RecyclerView.Adapter<Search_capture.Myview> {
    Context context;
    SharedPreferences user;
    String users2;
    List<Capture_search> list =new ArrayList<>();
    Search_capture a;
    public void setA(Search_capture a) {
        this.a = a;
    }
    public Search_capture(Context context,List<Capture_search> list)
    {
        this.context =context;
        this.list =list;
    }


    @NonNull
    @Override
    public Search_capture.Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        user =context.getSharedPreferences("Logged_in_details", Context.MODE_PRIVATE);
        users2 =user.getString("_id", "");
        View view = LayoutInflater.from(context).inflate(R.layout.hightlist_search, parent, false);
        return new Myview(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Search_capture.Myview holder, final int position)
    {    Glide.with(context).load(list.get(position).getUser_Image())
            .override(200, 200)
//                    .placeholder(R.drawable.loading_spinner)
            .into(holder.my_img);

        holder.username.setText(list.get(position).getUser_Name());
       // holder.text.setText("posted a");
     //   holder.category.setText(list.get(position).getPost_Category());

     //   holder.posttime.setText(list.get(position).getCreatedAt());
     //   holder.posttime.setVisibility(View.GONE);
        holder.cubes.setText(list.get(position).getCube_Name());
        if (list.get(position).getCapture_Text().length()>50)
        {
            holder.posttext.setText(list.get(position).getCapture_Text().subSequence(0,49)+"...");
        }
        else
        {
            holder.posttext.setText(list.get(position).getCapture_Text());
        }
        // holder.more.setText(list.get(position).getCubes_Count());
        holder.more.setText("and " +list.get(position).getCubes_Count()+" more cubes");
        holder.post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Bundle bundle=new Bundle();
                bundle.putString("id",list.get(position).get_id());
                get_posts get_posts=new get_posts();
                get_posts.setArguments(bundle);
                android.app.FragmentManager manager = ((Activity) context).getFragmentManager();
                manager.beginTransaction().replace(R.id.transact,get_posts).addToBackStack(null).commit();

            }
        });
        holder.usrs.setVisibility(View.GONE);
        holder.post.setVisibility(View.GONE);
        holder.trends.setVisibility(View.GONE);
        holder.capture.setVisibility(View.GONE);
//search user

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }
    static class Myview extends RecyclerView.ViewHolder {
        ImageView my_img;
        CardView post,usrs,capture,highlights,trends;
        TextView username,cubes,text,posttext,posttime,category,more;
        ImageView user_image;
        TextView name,hash,hash1,hash2;



        Myview(View itemView)
        {
            super(itemView);
            capture=itemView.findViewById(R.id.highlight_c);
            highlights=itemView.findViewById(R.id.highlight_p);
            trends=itemView.findViewById(R.id.highlight_t);
            post=(CardView)itemView.findViewById(R.id.highlight_cap);
            usrs=(CardView)itemView.findViewById(R.id.highlight_u);
            more=itemView.findViewById(R.id.more3);
            my_img=itemView.findViewById(R.id.user_image3);
            username=itemView.findViewById(R.id.user_name3);
            cubes=itemView.findViewById(R.id.cubes3);
           // text=itemView.findViewById(R.id.texts);
            posttext=itemView.findViewById(R.id.post_text3);
          //  posttime=itemView.findViewById(R.id.post_time);
           // category=itemView.findViewById(R.id.cta);

        }
    }
}
