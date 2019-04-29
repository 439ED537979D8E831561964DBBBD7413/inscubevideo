package Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.test.aashi.inscubenewbuild.R;
import com.test.aashi.inscubenewbuild.TrendsPost;

import java.util.ArrayList;

import Adapter.FeedAdapterSmallIcon;
import GetterSetter.Category_GridList;

public class TrendsFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Category_GridList> grid_element = new ArrayList<>();
    FeedAdapterSmallIcon adapter;
    LinearLayout post_hash;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.trends_fragment2, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.trend_recycler_view);

        post_hash = (LinearLayout) view.findViewById(R.id.post_hash);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

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

        post_hash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getActivity(), TrendsPost.class);
                startActivity(in);

            }
        });


    }

}
