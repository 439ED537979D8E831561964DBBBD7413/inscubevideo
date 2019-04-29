package com.test.aashi.inscubenewbuild;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;

public class LoadImage extends AsyncTask<Object, Void, Bitmap> {
    private ImageView imv;
    private String path;

    public LoadImage(ImageView imv, String urlpath) {
        this.imv = imv;
        this.path = urlpath;
    }

    @Override
    protected Bitmap doInBackground(Object... params) {
        Bitmap bitmap = null;

        try {
            bitmap = retriveVideoFrameFromVideo(path);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if (result != null && imv != null) {
            imv.setVisibility(View.VISIBLE);
            imv.setImageBitmap(result);
        } else {
            imv.setVisibility(View.GONE);
        }
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

}