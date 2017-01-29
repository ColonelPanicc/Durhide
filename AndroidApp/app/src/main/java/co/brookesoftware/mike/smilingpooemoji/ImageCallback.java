package co.brookesoftware.mike.smilingpooemoji;

import android.graphics.Bitmap;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.future.TransformFuture;

/**
 * Created by ryan on 29/01/17.
 */

public class ImageCallback implements FutureCallback<Bitmap> {
    private String url;

    public ImageCallback(String url) {
        this.url = url;
    }



    @Override
    public void onCompleted(Exception e, Bitmap result) {
                ImageStore.store.put(url, result);
    }
}
