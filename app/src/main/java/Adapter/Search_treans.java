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

import Fragment.Trends_filter;
import GetterSetter.Cube_search;
import GetterSetter.Trend_sarch;

/**
 * Created by AASHI on 31-07-2018.
 */

public class Search_treans extends RecyclerView.Adapter<Search_treans.Myview> {
    Context context;
    String user4;
    private List<Trend_sarch> userlist = new ArrayList<>();
    Search_treans a;

    public void setA(Search_treans a) {
        this.a = a;
    }

    public Search_treans(Context context, List<Trend_sarch> list) {
        this.context = context;
        this.userlist = list;
    }


    @NonNull
    @Override
    public Search_treans.Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SharedPreferences user = context.getSharedPreferences("Logged_in_details", Context.MODE_PRIVATE);
         user4 = user.getString("_id", "");
        View view = LayoutInflater.from(context).inflate(R.layout.hightlist_search, parent, false);
        return new Myview(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Search_treans.Myview holder, final int position) {


        if (userlist.get(position).getTag().length()>15)
        {
            holder.trend.setText("#"+userlist.get(position).getTag().subSequence(0,15)+"...");
        }
        else
        {
            holder.trend.setText("#"+userlist.get(position).getTag());
        }

        holder.trends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("my_tags", "#"+ userlist.get(position).getTag());
                bundle.putString("forview","1");
                Trends_filter get_posts = new Trends_filter();
                get_posts.setArguments(bundle);
                android.app.FragmentManager manager = ((Activity) context).getFragmentManager();
                manager.beginTransaction().replace(R.id.transact, get_posts). commit();
            }
        });
      holder.users.setVisibility(View.GONE);
     holder.highlightposts.setVisibility(View.GONE);
     holder.post.setVisibility(View.GONE);
     holder.capture.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }
    static class Myview extends RecyclerView.ViewHolder {
        CardView highlightposts, users,post,capture,trends;
        ImageView user_image;
        TextView trend;
        Myview(View itemView) {
            super(itemView);
            highlightposts=itemView.findViewById(R.id.highlight_p);
            users=itemView.findViewById(R.id.highlight_cap);
            post=itemView.findViewById(R.id.highlight_c);
            capture=itemView.findViewById(R.id.highlight_u);
            trends = itemView.findViewById(R.id.highlight_t);
           trend = itemView.findViewById(R.id.trend);

        }
    }
}
