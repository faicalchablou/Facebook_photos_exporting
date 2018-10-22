package ma.chablou.adria.activity;

/**
 * Created by Faical chablou on 21/10/2018.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import ma.chablou.adria.R;
import ma.chablou.adria.adapters.ImageAdapter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;


//Activity pour Afficher la liste des photos d'un album choisi
//l'affichage est dans un Grid
public class PicturesAlbumActivity extends AppCompatActivity {

    GridView gridview;

    public static  List<String> listPhotos= Collections.emptyList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_grid_layout);




        Intent intent=getIntent();
        Bundle args=intent.getBundleExtra("BUNDLE");

        //On recupere le nom de l'album est on le met comme titre
        setTitle("nom Album: "+args.getString("AlbumName"));


        gridview = (GridView) findViewById(R.id.gridview);


        //on recupere la liste des photos de l'album
        listPhotos =(List<String>) args.getSerializable("albums");

        //Si l'album est vide on affiche un Toast
        if(listPhotos.size()==0){
            Toast.makeText(this, "L'Album choisi est vide", Toast.LENGTH_SHORT).show();
        }

        gridview.setAdapter(new ImageAdapter(this,listPhotos));


        //lorsqu'on click sur une photo pour l'agrandir
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Intent i = new Intent(getApplicationContext(), ImageViewPager.class);
                i.putExtra("id", position);

                Bundle arg=new Bundle();
                arg.putSerializable("albumsID",(Serializable) listPhotos);

                i.putExtra("BUNDLE",arg);
                startActivity(i);

            }
        });
    }




    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}