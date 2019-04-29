package Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.test.aashi.inscubenewbuild.R;

import java.util.ArrayList;
import java.util.List;

import GetterSetter.NotificationCubeList;

/**
 * Created by saravanakumar8 on 16-Dec-17.
 */


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {


    List<NotificationCubeList> list;
    Context context;

    public NotificationAdapter(Context context, List<NotificationCubeList> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.notification_icon_design, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {


        Glide.with(context).load(list.get(i).getCube_img())
                .override(200, 200).into(viewHolder.imgThumbnail);

        viewHolder.imgtxt.setText(list.get(i).getCube_name());

        viewHolder.notification_count.setText(list.get(i).getNotification_count());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgThumbnail;
        public TextView imgtxt, notification_count;


        public ViewHolder(View itemView) {
            super(itemView);

            imgThumbnail = (ImageView) itemView.findViewById(R.id.notification_reicon);
            imgtxt = (TextView) itemView.findViewById(R.id.notification_rename);
            notification_count = (TextView) itemView.findViewById(R.id.notification_count);



        }


    }

}


