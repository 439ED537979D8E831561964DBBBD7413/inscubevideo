package com.test.aashi.inscubenewbuild;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import GetterSetter.ViewCubeList;


public class ViewCubeInfo extends AppCompatActivity {

    RecyclerView view_cube_rv,view_cube_rv1;
    List<ViewCubeList> list =new ArrayList<>();
    ViewCubeAdapter adapter;
    Button joinb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cube);

        view_cube_rv = (RecyclerView) findViewById(R.id.view_cube_rv);
       // view_cube_rv1= (RecyclerView) findViewById(R.id.view_cube_rv1);
        joinb = (Button) findViewById(R.id.joinb);

        view_cube_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        list.add(new ViewCubeList(R.drawable.college, 0));
        list.add(new ViewCubeList(R.drawable.event, 0));
        list.add(new ViewCubeList(R.drawable.personality, 0));
        list.add(new ViewCubeList(R.drawable.college, 0));
        list.add(new ViewCubeList(R.drawable.event, 0));
        list.add(new ViewCubeList(R.drawable.personality, 0));
        list.add(new ViewCubeList(R.drawable.college, 0));


        adapter = new ViewCubeAdapter(getApplicationContext(), list);
        view_cube_rv.setAdapter(adapter);
//        view_cube_rv1.setAdapter(adapter);
        joinb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "Joined", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private class ViewCubeAdapter extends RecyclerView.Adapter<ViewCubeAdapter.ViewHolder> {

        Context con;
        List<ViewCubeList> list;

        public ViewCubeAdapter(Context con, List<ViewCubeList> list) {
            this.con = con;
            this.list = list;

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_cube_design, parent, false);

            return new ViewHolder(v);

        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.view_cube_img.setImageResource(list.get(position).getCube_img());
        }

        @Override
        public int getItemCount() {

            return list.size();

        }

        public class ViewHolder extends RecyclerView.ViewHolder {


            ImageView view_cube_img;


            public ViewHolder(View itemView) {
                super(itemView);

                view_cube_img = (ImageView) itemView.findViewById(R.id.view_cube_img);


            }
        }
    }
}
