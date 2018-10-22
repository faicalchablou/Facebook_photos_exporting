package ma.chablou.adria.activity;


/**
 * Created by Faical chablou on 21/10/2018.
 */

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import ma.chablou.adria.adapters.ImagePagerAdapter;
import ma.chablou.adria.R;
import com.squareup.picasso.Picasso;

//Activity pour afficher les details de l'image
//on va agrandir l'image
public class ImageViewPager extends Activity {

    int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);


        Intent p = getIntent();
        position = p.getExtras().getInt("id");


        Bundle args=p.getBundleExtra("BUNDLE");
        ArrayList<String> albums =(ArrayList<String>) args.getSerializable("albumsID");

        List<ImageView> images = new ArrayList<ImageView>();


        // on recupere tous les images
        for (int i = 0; i < albums.size(); i++) {
            ImageView imageView = new ImageView(this);
            Picasso.with(this).load(albums.get(i)).into(imageView);
            images.add(imageView);
        }


        ImagePagerAdapter pageradapter = new ImagePagerAdapter(images);
        ViewPager viewpager = (ViewPager) findViewById(R.id.pager);
        viewpager.setAdapter(pageradapter);

        // Affichage ge l'image selon la position
        viewpager.setCurrentItem(position);
    }
}