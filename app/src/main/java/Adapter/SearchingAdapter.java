package Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import GetterSetter.SearchAdapter;
import GetterSetter.Search_user;


public class SearchingAdapter extends RecyclerView.Adapter<SearchingAdapter.Myview> {
    Context context;
    SharedPreferences user;
    String users;
    List<SearchAdapter> list =new ArrayList<>();
    SearchingAdapter a;
    public void setA(SearchingAdapter a)
    {
        this.a = a;
    }
    public SearchingAdapter(Context context,List<SearchAdapter> list)
    {
         this.context =context;
         this.list =list;
    }


    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        user =context.getSharedPreferences("Logged_in_details", Context.MODE_PRIVATE);
        users =user.getString("_id", "");
        View view = LayoutInflater.from(context).inflate(R.layout.hightlist_search, parent, false);
        return new Myview(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Myview holder, final int position)
    {    Glide.with(context).load(list.get(position).getUser_Image())
                .override(200, 200)
//                    .placeholder(R.drawable.loading_spinner)
                .into(holder.my_img);

       if (list.get(position).getUser_Name().length() >20)
       {
            holder.username.setText(list.get(position).getUser_Name().subSequence(0,20)+"...");

       }
       else
       {
           holder.username.setText(list.get(position).getUser_Name());
       }




       holder.text.setText("posted a");
       holder.category.setText(list.get(position).getPost_Category());

        holder.posttime.setText(list.get(position).getCreatedAt());
       holder.posttime.setVisibility(View.GONE);


       if (list.get(position).getCube_Name().length()>20)
       {
           holder.cubes.setText(list.get(position).getCube_Name().subSequence(0,20)+"...");
       }
       else
       {
           holder.cubes.setText(list.get(position).getCube_Name());
       }

       if (list.get(position).getPost_Text().length()>30)
       {
           holder.posttext.setText(list.get(position).getPost_Text().subSequence(0,30)+"...");
       }
       else
       {
           holder.posttext.setText(list.get(position).getPost_Text());
       }
         // holder.more.setText(list.get(position).getCubes_Count());
       holder.more.setText("and " +list.get(position).getCubes_Count()+" more cubes");
       holder.post.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v)
           {
               Bundle bundle=new Bundle();
               bundle.putString("my_id",list.get(position).get_id());
               get_posts get_posts=new get_posts();
               get_posts.setArguments(bundle);
               android.app.FragmentManager manager = ((Activity) context).getFragmentManager();
               manager.beginTransaction().replace(R.id.transact,get_posts).addToBackStack(null).commit();
           }
       });

        holder.trends.setVisibility(View.GONE);
        holder.cubes1.setVisibility(View.GONE);
        holder.capture.setVisibility(View.GONE);
       holder.usrs.setVisibility(View.GONE);

//search user

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }
     static class Myview extends RecyclerView.ViewHolder {
        ImageView my_img;
        CardView post,usrs,trends,capture,cubes1;
           TextView username,cubes,text,posttext,posttime,category,more;
           ImageView user_image;
           TextView name,hash,hash1,hash2;



        Myview(View itemView)
        {
            super(itemView);
            trends=itemView.findViewById(R.id.highlight_c);
            capture=itemView.findViewById(R.id.highlight_cap);
            cubes1=itemView. findViewById(R.id.highlight_t);
            post=(CardView)itemView.findViewById(R.id.highlight_p);
            usrs=(CardView)itemView.findViewById(R.id.highlight_u);
            more=itemView.findViewById(R.id.more);
            my_img=itemView.findViewById(R.id.grid_img1);
            username=itemView.findViewById(R.id.user_name);
            cubes=itemView.findViewById(R.id.cubes);
            text=itemView.findViewById(R.id.texts);
            posttext=itemView.findViewById(R.id.post_text);
            posttime=itemView.findViewById(R.id.post_time);
            category=itemView.findViewById(R.id.cta);

        }
    }
}
