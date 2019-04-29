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

import GetterSetter.SearchAdapter;
import GetterSetter.Search_user;
public class SearchUser_adapter extends RecyclerView.Adapter<SearchUser_adapter.Myview> {
    Context context;
    private SharedPreferences user;
    private String users;
    private List<Search_user> userlist = new ArrayList<>();
    SearchUser_adapter a;

    public void setA(SearchUser_adapter a) {
        this.a = a;
    }

    public SearchUser_adapter(Context context, List<Search_user> list) {
        this.context = context;
        this.userlist = list;
    }


    @NonNull
    @Override
    public SearchUser_adapter.Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        user = context.getSharedPreferences("Logged_in_details", Context.MODE_PRIVATE);
        users = user.getString("_id", "");
        View view = LayoutInflater.from(context).inflate(R.layout.hightlist_search, parent, false);
        return new Myview(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SearchUser_adapter.Myview holder, final int position) {


//search user

        Glide.with(context).load(userlist.get(position).getImage())
                .override(200, 200)
//                    .placeholder(R.drawable.loading_spinner)
                .into(holder.user_image);
        holder.name.setText(userlist.get(position).getInscube_Name());
        holder.hash.setText(userlist.get(position).getHash_Tag_1());
        holder.hash1.setText(userlist.get(position).getHash_Tag_2());
        holder.hash2.setText(userlist.get(position).getHash_Tag_3());
        holder.users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id", userlist.get(position).get_id());
                get_posts get_posts = new get_posts();
                get_posts.setArguments(bundle);
                android.app.FragmentManager manager = ((Activity) context).getFragmentManager();
                manager.beginTransaction().replace(R.id.transact, get_posts).addToBackStack(null).commit();

            }
        });
  holder.highlightposts.setVisibility(View.GONE);
  holder.cubes.setVisibility(View.GONE);
  holder.capture.setVisibility(View.GONE);
  holder.trends.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }

    static class Myview extends RecyclerView.ViewHolder {
        CardView highlightposts,cubes,trends,capture,users;
        ImageView user_image;
        TextView name, hash, hash1, hash2;


        Myview(View itemView) {
            super(itemView);
            highlightposts=itemView.findViewById(R.id.highlight_p);
            cubes=itemView.findViewById(R.id.highlight_c);
            trends=itemView.findViewById(R.id.highlight_cap);
            users=itemView.findViewById(R.id.highlight_u);
            capture=itemView.findViewById(R.id.highlight_t);
            user_image = itemView.findViewById(R.id.user_image);
            name = itemView.findViewById(R.id.user_name1);
            hash = itemView.findViewById(R.id.hashtag1);
            hash1 = itemView.findViewById(R.id.hashtag2);
            hash2 = itemView.findViewById(R.id.hastag3);

        }
    }
}