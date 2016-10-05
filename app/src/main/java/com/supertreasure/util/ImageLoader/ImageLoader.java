package com.supertreasure.util.ImageLoader;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import java.io.Serializable;


public interface ImageLoader extends Serializable{
    void displayImage(Activity activity, String path, GFImageView imageView, Drawable defaultDrawable, int width, int height);
    void clearMemoryCache();
}
