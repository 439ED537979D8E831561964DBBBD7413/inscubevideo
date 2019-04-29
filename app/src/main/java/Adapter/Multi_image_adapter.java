package Adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.test.aashi.inscubenewbuild.LoadImage;
import com.test.aashi.inscubenewbuild.R;

import java.io.File;
import java.util.ArrayList;

import Fragment.MultipleFragment;
import uk.co.jakelee.vidsta.VidstaPlayer;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class Multi_image_adapter extends RecyclerView.Adapter<Multi_image_adapter.ViewHolder> {

    ArrayList<String> img_name;
    public Context context;
    Multi_image_adapter adapter;
    RecyclerView rv;
    MultipleFragment mul = new MultipleFragment();
    int a;

    public void setAdapter(Multi_image_adapter adapter)
    {
        this.adapter = adapter;
    }
    public Multi_image_adapter(Context context, ArrayList<String> img_name, RecyclerView rv,
                               Multi_image_adapter adapter, int a) {
        super();

        this.context = context;
        this.img_name = img_name;
        this.rv = rv;
        this.adapter = adapter;
        this.a = a;
    }
    @Override
    public Multi_image_adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.multi_imt_design, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final Multi_image_adapter.ViewHolder viewHolder, final int i) {

        if (a == 1) {

            viewHolder.remove_button.setVisibility(View.GONE);

        }


        if (img_name.get(i).toString().contains(".jpg") || img_name.get(i).toString().contains(".gif") || img_name.get(i).toString().contains(".jpeg")
                || img_name.get(i).toString().contains(".png"))

           {

            viewHolder.img.setVisibility(View.VISIBLE);
            viewHolder.vid.setVisibility(View.GONE);
            viewHolder.vid_play_button.setVisibility(View.GONE);
            Glide.with(context)
                    .load(img_name.get(i))
                    .override(300, 300)
                    .placeholder(R.drawable.loader)
                    .into(viewHolder.img);

            viewHolder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView;

                    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = layoutInflater.inflate(R.layout.test, viewHolder.imagecontainer);
                    //      layout.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    final PhotoView imageView1 = new PhotoView(context);
                    imageView=layout.findViewById(R.id.loadimage);
                    // Creating the PopupWindow
                    viewHolder. changeSortPopUp = new PopupWindow(context);
                    viewHolder. changeSortPopUp.setContentView(layout);
                    viewHolder. changeSortPopUp.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                    viewHolder. changeSortPopUp.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
                    viewHolder. changeSortPopUp.setFocusable(true);
                    viewHolder.changeSortPopUp.setContentView(imageView1);
                    viewHolder.changeSortPopUp.setOutsideTouchable(true);
                    viewHolder.changeSortPopUp.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                    // Some offset to align the popup a bit to the left, and a bit down, relative to button's position.
                    int OFFSET_X = -20;
                    int OFFSET_Y = 95;

                    // Clear the default translucent background
                    viewHolder.  changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());
                    PhotoViewAttacher mAttacher = new PhotoViewAttacher(imageView);

                    mAttacher.setZoomable(true);

                    // Displaying the popup at the specified location, + offsets.
                    viewHolder.changeSortPopUp.showAtLocation(layout, Gravity.CENTER,0,0);
                    Glide.with(context)
                            .load(img_name.get(i))
                            .placeholder(R.drawable.loader)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView1);
                    //      new AsyncTaskShowImage().execute(img_name.get(i));

                    //  zoomImageFromThumb(img_name.get(i),R.drawable.wall,viewHolder.expandedImageView,viewHolder.imagecontainer);


                }
            });


        }
        else if (img_name.get(i).toString().contains(".mp4") ||
                img_name.get(i).toString().contains(".3gp") || img_name.get(i).toString().contains(".flv")
                || img_name.get(i).toString().contains(".mkv")) {

           viewHolder.img.setVisibility(View.GONE);
            Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(img_name.get(i).toString(),
                    MediaStore.Images.Thumbnails.MINI_KIND);
            viewHolder.vid.setVisibility(View.VISIBLE);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(thumbnail);
            viewHolder.vid.setBackgroundDrawable(bitmapDrawable);


/*            final int THUMBSIZE = 128;
            Bitmap thumbImage = ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(img_name.get(i)),
                    THUMBSIZE,
                    THUMBSIZE);*/



            Uri uri = Uri.fromFile(new File(img_name.get(i)));
            /*Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(
                    context.getContentResolver(), uri,
                    MediaStore.Images.Thumbnails.MINI_KIND,
                    (BitmapFactory.Options) null );*/
            Bitmap bitmap = null;
            try {
//                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                bitmap   = ThumbnailUtils.createVideoThumbnail(img_name.get(i), MediaStore.Video.Thumbnails.MINI_KIND);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            viewHolder.vid_play_button.setVisibility(View.VISIBLE);
            viewHolder.img.setImageBitmap(bitmap);
            /*
            String[] filePathColumn = {MediaStore.Images.Media.DATA};


            Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bitmap2 = ThumbnailUtils.createVideoThumbnail(picturePath, MediaStore.Video.Thumbnails.MICRO_KIND);
*/
        }

        viewHolder.remove_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img_name.remove(img_name.get(i).toString());

                adapter.notifyDataSetChanged();

                mul.set(img_name);

            }
        });

    }

    @Override
    public int getItemCount()
    {
        return img_name.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView img, vid_play_button, remove_button, vid_thumb;
        VideoView vid;
        MediaController mc;
        Uri u;
        FrameLayout imagecontainer;
        PopupWindow changeSortPopUp;
        PopupWindow vidPopupWindoww;

        public ViewHolder(View itemView) {
            super(itemView);
            vid_thumb = (ImageView) itemView.findViewById(R.id.vid_thumb);
            vid_play_button = (ImageView) itemView.findViewById(R.id.vid_play_button);
            vid = (VideoView) itemView.findViewById(R.id.vid);
            img = (ImageView) itemView.findViewById(R.id.img);
            remove_button = (ImageView) itemView.findViewById(R.id.remove_button);
            imagecontainer=itemView.findViewById(R.id.image_container);

        }
    }

    public void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent();
            } else {
                container = popupWindow.getContentView();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent().getParent();
            } else {
                container = (View) popupWindow.getContentView().getParent();
            }
        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.93f;
        wm.updateViewLayout(container, p);
    }
}



