package com.salmanibrahim.firebasechat.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;



public class ImageUtils {

    public static final int AVATAR_WIDTH = 128;
    public static final int AVATAR_HEIGHT = 128;

    /**
     * Rounded avatar photo
     * @param context
     * @param src bitmap image
     * @return RoundedBitmapDrawable is the input to the function setImageDrawable()
     */
    public static RoundedBitmapDrawable roundedImage(Context context, Bitmap src){
        /*Rounded avatar*/
        Resources res = context.getResources();
        RoundedBitmapDrawable dr =
                RoundedBitmapDrawableFactory.create(res, src);
        dr.setCornerRadius(Math.max(src.getWidth(), src.getHeight()) / 2.0f);

        return dr;
    }

    /**
     * For rectangular images, cut the image in a square and take the center
     * The image will not be distorted when making an avatar
     * @param srcBmp
     * @return
     */
    public static Bitmap cropToSquare(Bitmap srcBmp){
        Bitmap dstBmp = null;
        if (srcBmp.getWidth() >= srcBmp.getHeight()){

            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    srcBmp.getWidth()/2 - srcBmp.getHeight()/2,
                    0,
                    srcBmp.getHeight(),
                    srcBmp.getHeight()
            );

        }else{
            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    0,
                    srcBmp.getHeight()/2 - srcBmp.getWidth()/2,
                    srcBmp.getWidth(),
                    srcBmp.getWidth()
            );
        }

        return dstBmp;
    }

    /**
     * Convert bitmap image to String base64
     * @param imgBitmap
     * @return
     */
    public static String encodeBase64(Bitmap imgBitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    /**
     * Reduce pixel count to avoid errors Firebase Database OutOfMemory
     * @param is input
     * @param reqWidth width after reducing size
     * @param reqHeight height after reducing size
     * @return
     */
    public static Bitmap makeImageLite(InputStream is, int width, int height,
                                       int reqWidth, int reqHeight) {
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        // Calculate inSampleSize
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(is, null, options);
    }


    public static InputStream convertBitmapToInputStream(Bitmap bitmap){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
        return bs;
    }

}
