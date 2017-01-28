package com.tamtam.android.tamtam.services.image;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.tamtam.android.tamtam.R;

import java.lang.ref.WeakReference;
import java.net.URI;

/**
 * Created by antoninpa on 23/01/17.
 */


/**
 * All the good advices from
 * <a href="https://developer.android.com/training/displaying-bitmaps/load-bitmap.html">
 *     Android documentation on images loading</a>.
 */
public class BitmapUtils {


    /**
     * Loads a bitmap from an image file's path in an {@link ImageView}.
     * Places a placeholder {@link Bitmap} while loading.
     * @param imagePath final image to load in the {@link ImageView}
     * @param imageView to populate with the new image file
     * @param placeHolderBitmap placeholder {@link Bitmap} to display while loading the final image.
     */
    public static void LoadBitmap(String imagePath, ImageView imageView, Bitmap placeHolderBitmap, int reqWidth,int reqHeight) {
        if (cancelPotentialWork(imagePath, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView, reqWidth, reqHeight);
            final AsyncDrawable asyncDrawable =
                    new AsyncDrawable(imageView.getContext().getResources(), placeHolderBitmap, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(imagePath);
        }
    }


    /**
     * This functions reads a picture (not storing it into memory) to define its bounds.
     * @param imagePath absolute path where the ptoho is located
     * @return an array with width at index 0, height at index 1, measured in pixels.
     */
    public static int[] readBounds(String imagePath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);//.decodeResource(getResources(), ressource, options);
        int[] dimensions = new int[2];
        dimensions[0] = options.outWidth;
        dimensions[1] = options.outHeight;
        return dimensions;
        //String imageType = options.outMimeType;
    }


    /**
     * Copy paste from Android documentation to find the scale factor to resize an image according
     * to a view size. This function keeps the final image bigger than reqWidth and reqHeight.
     * @param imageWidth real (raw) width of the input image in pixels.
     * @param imageHeight real (raw) height of the input image in pixels.
     * @param reqWidth minimum width of the loaded image.
     * @param reqHeight minimum height of the loaded image.
     * @return inSampleSize value to use with {@link BitmapFactory}
     */
    public static int calculateInSampleSize(final int imageWidth,final int imageHeight,
                                            final int reqWidth, final int reqHeight) {
        int inSampleSize = 1;

        if (imageHeight > reqHeight || imageWidth > reqWidth) {

            final int halfHeight = imageHeight / 2;
            final int halfWidth = imageWidth/ 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }



    public static Bitmap decodeSampledBitmapFromPath(String imagePath,
                                                     int reqWidth,
                                                     int reqHeight) {
        // First decode to read dimensions
        int[] imageBounds;
        imageBounds = readBounds(imagePath);

        // Calculate inSampleSize
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = calculateInSampleSize( imageBounds[0],imageBounds[1],
                                                      reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imagePath, options);
    }


    /**
     * Asynchronous task to load an image from a path, rescale the bitmap
     * and set it in the destination {@link android.widget.ImageView}.
     * Following Android implementation directives from :
     * <a href="https://developer.android.com/training/displaying-bitmaps/process-bitmap.html">
     *     android doc to multitask images loading</a>
     */
    public static class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private String data = null;
        private int reqWidth, reqHeight;

        public BitmapWorkerTask(ImageView imageView, int reqWidth, int reqHeight) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
            this.reqWidth = reqWidth;
            this.reqHeight= reqHeight;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
                data = params[0]; // ImagePath
                return decodeSampledBitmapFromPath(data, reqWidth, reqHeight);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        // also cancel previously on going task if there was one...
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                // if this is the same task as the on going task on the image view :
                final BitmapWorkerTask bitmapWorkerTask =
                        getBitmapWorkerTask(imageView);
                if (this == bitmapWorkerTask && imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }


    /**
     * This drawable keeps a reference to
     * the real image loading {@link AsyncTask} : {@link BitmapWorkerTask}
     * and displays a waiter image meanwhile
     */
    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap,
                             BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap); // displays the image pointed by res in the imageview
            bitmapWorkerTaskReference =
                    new WeakReference<BitmapWorkerTask>(bitmapWorkerTask); // ref to async task
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }






    /**
     * Stop On going {@link BitmapWorkerTask} on a specific {@link ImageView}.
     * @param data URI of the new {@link BitmapWorkerTask}
     *             to compare to the old {@link BitmapWorkerTask}'s {@link URI}.
     * @param imageView on which there is an old on going {@link BitmapWorkerTask}.
     * @return {@code true} if the old task has different data compared to the new one
     * and is stopped correctly.
     */
    public static boolean cancelPotentialWork(String data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final String bitmapData = bitmapWorkerTask.data;
            // If bitmapData is not yet set or it differs from the new data
            if (bitmapData == null || !bitmapData.equals(data)) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }


    /**
     * Retruns the {@link BitmapWorkerTask} on going in an {@link ImageView} if any.
     * this implementation checks out first the asyncDrawable associated with the {@link ImageView}.
     * @param imageView on which a {@link BitmapWorkerTask} is on going.
     * @return the currently on goin {@link BitmapWorkerTask}.
     */
    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }


}
