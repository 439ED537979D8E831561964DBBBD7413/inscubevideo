package Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.test.aashi.inscubenewbuild.LoadImage;
import com.test.aashi.inscubenewbuild.R;

import java.net.URL;
import java.util.ArrayList;

import Fragment.MultipleFragment;
import uk.co.jakelee.vidsta.VidstaPlayer;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by AASHI on 28-07-2018.
 */

public class ImageAdapter extends BaseAdapter {

    ArrayList<String> img_name, img_status;
    Context context;
    ImageAdapter adapter;
    GridView rv;
    MultipleFragment mul = new MultipleFragment();
    LayoutInflater inflter;
    int a;

    public void setAdapter(ImageAdapter adapter) {

        this.adapter = adapter;

    }

    public ImageAdapter(Context context, ArrayList<String> img_name, GridView rv, ImageAdapter adapter, int a) {
        super();
        this.context = context;
        this.img_name = img_name;
        this.img_status = img_status;
        this.rv = rv;
        this.adapter = adapter;
        this.a = a;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return img_name.size();
    }
    @Override
    public Object getItem(int position) {
        return img_name.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {
       final ViewHolder viewHolder;

        if (convertView == null) {


            convertView = inflter.inflate(R.layout.post_multi_imt_design, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.vid_play_button = (ImageView) convertView.findViewById(R.id.vid_play_button);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.img);
            viewHolder.player = (VidstaPlayer) convertView.findViewById(R.id.vid);
            viewHolder.vid_thumb = (ImageView) convertView.findViewById(R.id.vid_thumb);

            viewHolder.main_layout = (RelativeLayout) convertView.findViewById(R.id.main_layout);
            viewHolder.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            convertView.setTag(viewHolder);
            viewHolder.vid_play_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

                    final View mView = inflater.inflate(R.layout.video, null);

                    viewHolder.vidPopupWindoww = new PopupWindow(
                            mView,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    );
                    final VidstaPlayer player;
                    ImageView closeb;

                    player = (VidstaPlayer) mView.findViewById(R.id.vid);
                    closeb = (ImageView) mView.findViewById(R.id.closeb);

                    player.setVideoSource(img_name.get(i).toString());
                    player.setFullScreen(false);
                    player.setFullScreenButtonVisible(false);


                    closeb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            player.stop();
                            viewHolder.vidPopupWindoww.dismiss();
                        }
                    });


                    viewHolder.vidPopupWindoww.setFocusable(true);

                    viewHolder.vidPopupWindoww.setAnimationStyle(R.style.popup_window_animation_phone);

                    viewHolder.vidPopupWindoww.showAtLocation(mView, Gravity.CENTER, 0, 0);

                    dimBehind(viewHolder.vidPopupWindoww);

                }
            });
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;


        Log.i("Test", "onBindViewHolder: " + img_name);
        if (img_name.size() != 1) {

            ViewGroup.LayoutParams params = viewHolder.main_layout.getLayoutParams();
            params.width = 100;
            params.height=100;
            viewHolder.main_layout.setLayoutParams(params);


        } else {

            ViewGroup.LayoutParams params = viewHolder.main_layout.getLayoutParams();
            params.height = height;
            params.width = width;
            viewHolder.main_layout.setLayoutParams(params);

        }

        if (img_name.get(i).contains(".jpg") || img_name.get(i).contains(".gif") ||
                img_name.get(i).contains(".jpeg")
                || img_name.get(i).contains(".png")) {
            viewHolder.imageView.setScaleType(ImageView.ScaleType.CENTER);

            viewHolder.imageView.setVisibility(View.VISIBLE);
            // viewHolder.vid.setVisibility(View.GONE);
            viewHolder.vid_thumb.setVisibility(View.GONE);
            viewHolder.vid_play_button.setVisibility(View.GONE);
            Glide.with(context)
                    .load(img_name.get(i))
                    .placeholder(null)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .override(350, 400)
                    .into(viewHolder.imageView);
          /*  Glide.with(context)
                    .load(img_name.get(i))
                    .override(350, 350)
                    .placeholder(R.drawable.loader)
                    .into(viewHolder.img);
                    */
        }
        if (img_name.get(i).toString().contains(".mp4") || img_name.get(i).toString().contains(".3gp") ||
                img_name.get(i).toString().contains(".flv")
                || img_name.get(i).toString().contains(".mkv")) {

            viewHolder.imageView.setVisibility(View.GONE);
            viewHolder.vid_play_button.setVisibility(View.VISIBLE);
            viewHolder.vid_thumb.setVisibility(View.VISIBLE);

            try {
                new LoadImage(viewHolder.vid_thumb, img_name.get(i).toString()).execute();

            } catch (Exception e) {
                e.printStackTrace();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }



        return convertView;
    }

    public class ViewHolder
    {
        VidstaPlayer player;
        PopupWindow vidPopupWindoww;
         ImageView imageView;
        RelativeLayout main_layout;
        ImageView vid_play_button,  vid_thumb;

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



    private class AsyncTaskShowImage extends AsyncTask<String, Bitmap, Bitmap> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap=null;
            try {
                URL url = new URL(params[0]);
                bitmap= BitmapFactory.decodeStream(url.openConnection().getInputStream());
            }catch (Exception e){
                e.printStackTrace();
            }

            return  bitmap;

        }
    @Override
    protected void onPostExecute(Bitmap result) {
        progressDialog.cancel();
        Dialog builder = new Dialog(context);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        builder.setCanceledOnTouchOutside(true);

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });
        PhotoView imageView1 = new PhotoView(context);
        imageView1.setImageBitmap(result);

        PhotoViewAttacher mAttacher = new PhotoViewAttacher(imageView1);
        mAttacher.setZoomable(true);
        //imageView1.setImageURI(uri1);
        builder.addContentView(imageView1, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();

    }


    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(context,
                "Loading Image",
                "");
    }

}
}
