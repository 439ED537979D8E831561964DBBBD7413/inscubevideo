package Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.test.aashi.inscubenewbuild.CubeCapturePost;
import com.test.aashi.inscubenewbuild.R;

import java.util.ArrayList;

import Adapter.FeedAdapterSmallIcon;
import GetterSetter.Category_GridList;

public class CubeCaptureFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Category_GridList> grid_element = new ArrayList<>();
    FeedAdapterSmallIcon adapter;
    LinearLayout Post_Cube;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.cube_capture_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Post_Cube = (LinearLayout) view.findViewById(R.id.Post_Cube);

        recyclerView = (RecyclerView) view.findViewById(R.id.capture_recycler_view);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//
//        grid_element.add(new Category_GridList("College", R.drawable.college));
//        grid_element.add(new Category_GridList("Events", R.drawable.event));
//        grid_element.add(new Category_GridList("Company", R.drawable.company));
//        grid_element.add(new Category_GridList("Community/Organisation", R.drawable.community));
//        grid_element.add(new Category_GridList("Entertainment", R.drawable.entertainment));
//        grid_element.add(new Category_GridList("Family/Friends", R.drawable.family));
//        grid_element.add(new Category_GridList("Personality", R.drawable.personality));
//        grid_element.add(new Category_GridList("Movies", R.drawable.movies));
//        grid_element.add(new Category_GridList("Product/Brand", R.drawable.product));
//
//        adapter = new Adapter.FeedAdapterSmallIcon(getActivity(), grid_element);
//        recyclerView.setAdapter(adapter);


        Post_Cube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getActivity(), CubeCapturePost.class);
                startActivity(in);

            }
        });

    }


}




