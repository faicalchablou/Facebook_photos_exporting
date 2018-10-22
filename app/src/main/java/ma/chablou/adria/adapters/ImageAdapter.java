package ma.chablou.adria.adapters;

/**
 * Created by Faical chablou on 21/10/2018.
 */

import android.widget.BaseAdapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;


//Adapter photo  album


public class ImageAdapter extends BaseAdapter {
    private Context mContext;
   public static List<String> idAlbums= Collections.emptyList();;


   public ImageAdapter(Context c, List<String> idAlbums) {
        mContext = c;
        this.idAlbums=idAlbums;
    }


    // Nombre total des rows
    public int getCount() {
        return idAlbums.size();

    }

    public Object getItem(int position) {
        return idAlbums.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext)
                .load(idAlbums.get(position))
                .resize(300,300)
                .centerInside().into(imageView);
        return imageView;
    }


}