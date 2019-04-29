package Adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.test.aashi.inscubenewbuild.CircleTransform;
import com.test.aashi.inscubenewbuild.LoadImage;
import com.test.aashi.inscubenewbuild.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import Fragment.MultipleFragment;
import uk.co.jakelee.vidsta.VidstaPlayer;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by AASHI on 12-04-2018.
 */


public class Post_MultiImage_Adapter extends RecyclerView.Adapter<Adapter.Post_MultiImage_Adapter.ViewHolder>{

    ArrayList<String> img_name, img_status;
    Context context;
    Adapter.Post_MultiImage_Adapter adapter;
    RecyclerView rv;
    MultipleFragment mul = new MultipleFragment();
    int a;
    VideoSurfaceView videoSurfaceView;
    Bitmap scaledBitmap =null ;
    String filename;
    Bitmap bitmap;
    public void setAdapter(Adapter.Post_MultiImage_Adapter adapter)
    {
        this.adapter = adapter;
    }

    public Post_MultiImage_Adapter(Context context, ArrayList<String> img_name, RecyclerView rv,
                                   Adapter.Post_MultiImage_Adapter adapter, int a) {
        super();

        this.context = context;
        this.img_name = img_name;
        this.img_status = img_status;
        this.rv = rv;
        this.adapter = adapter;
        this.a = a;
    }


    @SuppressLint("ObsoleteSdkInt")
    @Override
    public Adapter.Post_MultiImage_Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.post_multi_imt_design, viewGroup, false);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Adapter.Post_MultiImage_Adapter.ViewHolder viewHolder = new Adapter.Post_MultiImage_Adapter.ViewHolder(v);
        return viewHolder;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(final Adapter.Post_MultiImage_Adapter.ViewHolder viewHolder, final int i) {
        viewHolder.setIsRecyclable(false);
     //   viewHolder.setIsRecyclable(false);
        videoSurfaceView =new VideoSurfaceView(context);
       if (a == 1) {
            // viewHolder.remove_button.setVisibility(View.GONE);
        }

   /*    DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        */

        Log.i("Test", "onBindViewHolder: " + img_name);
        if (img_name.size() != 1) {

            ViewGroup.LayoutParams params = viewHolder.main_layout.getLayoutParams();
         //   params.width = width - 60;
          //  params.height = height;
            viewHolder.main_layout.setLayoutParams(params);

        }
        else
        {
            ViewGroup.LayoutParams params = viewHolder.main_layout.getLayoutParams();
         //   params.height=height;
          //  params.width = width - 60;
            viewHolder.main_layout.setLayoutParams(params);
        }

  if (img_name.get(i).toString().contains(".jpg") || img_name.get(i).toString().contains(".gif") ||
                img_name.get(i).toString().contains(".jpeg")
                || img_name.get(i).toString().contains(".png")) {

            viewHolder.img.setVisibility(View.VISIBLE);
            // viewHolder.vid.setVisibility(View.GONE);
            viewHolder.vid_thumb.setVisibility(View.GONE);
            viewHolder.vid_play_button.setVisibility(View.GONE);
            String myimage= img_name.get(i);
          try {
          URL url = new URL(myimage);
          HttpURLConnection connection = (HttpURLConnection) url.openConnection();
          connection.setDoInput(true);
          connection.connect();
          InputStream input = connection.getInputStream();
          Bitmap myBitmap = BitmapFactory.decodeStream(input);
          Bitmap converetdImage = getResizedBitmap(myBitmap, 300);
          viewHolder.img.setImageBitmap(converetdImage);
          } catch (IOException e) {
            e.printStackTrace();

           }

        /*    Glide.with(context)
                    .load(img_name.get(i))
                    .placeholder(R.drawable.loader)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .override(250, 600)
                    .into(viewHolder.img);
                    */
  //    Bitmap bitmapImage = BitmapFactory.decodeFile(filename);

       /*   Glide.with(context)
                    .load(img_name.get(i))
                    .override(350, 350)
                    .placeholder(R.drawable.loader)
                    .into(viewHolder.img);
                    */
            viewHolder.img.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
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




                  ShowImage();
                  //  zoomImageFromThumb(img_name.get(i),R.drawable.wall,viewHolder.expandedImageView,viewHolder.imagecontainer);


                }
            });

        }

      if (img_name.get(i).toString().contains(".mp4") || img_name.get(i).toString().contains(".3gp") ||
              img_name.get(i).toString().contains(".flv")
                || img_name.get(i).toString().contains(".mkv")) {

            viewHolder.img.setVisibility(View.GONE);
//            viewHolder.player.setVisibility(View.VISIBLE);
//            viewHolder.player.setVideoSource(img_name.get(i).toString());
//            viewHolder.player.setFullScreen(false);
//            viewHolder.player.setFullScreenButtonVisible(false);
            viewHolder.vid_play_button.setVisibility(View.VISIBLE);
            viewHolder.vid_thumb.setVisibility(View.VISIBLE);

            try {
                /*Bitmap bitmap = retriveVideoFrameFromVideo(img_name.get(i).toString());
                viewHolder.vid_thumb.setImageBitmap(bitmap);
*/
                new LoadImage(viewHolder.vid_thumb, img_name.get(i).toString()).execute();

            } catch (Exception e) {
                e.printStackTrace();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

//            try {
//
//
//                Bitmap bitmap = retriveVideoFrameFromVideo(img_name.get(i).toString());
//
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                Glide.with(context)
//                        .load(stream.toByteArray())
//                        .override(250, 250)
//                        .into(viewHolder.vid_thumb);
//
//                // viewHolder.vid_thumb.setImageBitmap(bitmap);
//
//            } catch (Throwable throwable) {
//                throwable.printStackTrace();
//            }

//            Bitmap bitmap;
//            bitmap = ImageUtil.retriveVideoFrameFromVideo(img_name.get(i).toString());
//            if (bitmap != null) {
//                bitmap = Bitmap.createScaledBitmap(bitmap, 240, 240, false);

            //}


            //  viewHolder.vid_thumb.setImageResource(R.drawable.android);

//            viewHolder.vid.setVisibility(View.VISIBLE);
//            viewHolder.vid_play_button.setVisibility(View.VISIBLE);
//
//            viewHolder.mc = new MediaController(context);
//
//            viewHolder.vid.setMediaController(viewHolder.mc);
//
//            viewHolder.u = Uri.parse(img_name.get(i).toString());
//
//            viewHolder.vid_play_button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    viewHolder.vid.setVideoURI(viewHolder.u);
//                    viewHolder.vid.start();
//
//                    viewHolder.vid_play_button.setVisibility(View.GONE);
//                    viewHolder.mc.hide();
//
//                }
//            });

        }

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
                player.setFullScreenButtonVisible(false);
                closeb.setOnClickListener(new View.OnClickListener()
                {
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


    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);

            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
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

    @Override
    public int getItemCount() {
        return img_name.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView vid_play_button, img, vid_thumb;
        ImageView expandedImageView;
        FrameLayout imagecontainer;
        Point p;


        // VideoView vid;
        PopupWindow changeSortPopUp;
        RelativeLayout main_layout;
        MediaController mc;
        Uri u;
        VidstaPlayer player;
        PopupWindow vidPopupWindoww;

        // FullscreenVideoLayout videoLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            vid_play_button = (ImageView) itemView.findViewById(R.id.vid_play_button);
            expandedImageView=itemView.findViewById(R.id.expanded_image);
            imagecontainer=itemView.findViewById(R.id.image_container);

            //  vid = (VideoView) itemView.findViewById(R.id.vid);

            // videoLayout = (FullscreenVideoLayout) itemView.findViewById(R.id.vid);

            img = (ImageView) itemView.findViewById(R.id.img);
            vid_thumb = (ImageView) itemView.findViewById(R.id.vid_thumb);

            player = (VidstaPlayer) itemView.findViewById(R.id.vid);

            main_layout = (RelativeLayout) itemView.findViewById(R.id.main_layout);

        }


    }
    public void ShowImage()
    {

    }

    private class AsyncTaskShowImage extends AsyncTask<String, Bitmap, Bitmap> {
        Drawable marker;
        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap=null;
            try {

                URL url = new URL(params[0]);
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
               // bitmap.compress(Bitmap.CompressFormat.JPEG, 30, fileOutputStream)
            }
            catch (Exception e){
                e.printStackTrace();
            }

            return  bitmap;

        }


        @Override
        protected void onPostExecute(final Bitmap result) {

            ShowImage();

            ViewGroup viewGroup;

           progressDialog.cancel();
            Resources resources = context.getResources();


            Dialog builder = new Dialog(context);

            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);

           builder. getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
         //   builder. setContentView(R.layout.main);
           // builder.getWindow().setBackgroundDrawable(
            //        new ColorDrawable(Color.TRANSPARENT));
            builder.setCanceledOnTouchOutside(true);

            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    //nothing;
                }
            });
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            final PhotoView imageView1 = new PhotoView(context);
            result.compress(Bitmap.CompressFormat.JPEG,100,stream);

            byte[] byteArray = stream.toByteArray();
            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);

            // Display the compressed bitmap in ImageView

                imageView1.setImageBitmap(compressedBitmap);
            PhotoViewAttacher mAttacher = new PhotoViewAttacher(imageView1);

            mAttacher.setZoomable(true);
           imageView1.getScaleType().compareTo(ImageView.ScaleType.FIT_XY);
           imageView1.setMaximumScale(6);
            builder.addContentView(imageView1
          ,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            builder.show();



        }

        public void onPalette(Palette palette,PhotoView photoView) {
            if (null != palette) {
                ViewGroup parent = (ViewGroup) photoView.getParent().getParent();
                parent.setBackgroundColor(palette.getDarkVibrantColor(Color.GRAY));
            }
        }
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(context,
                    "Loading Image",
                    "");
        }


    }



    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}




