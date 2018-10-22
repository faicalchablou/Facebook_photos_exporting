package ma.chablou.adria.activity;


/**
 * Created by Faical chablou on 21/10/2018.
 */

//Activity pour la liste des albums

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import ma.chablou.adria.adapters.AlbumAdapter;
import ma.chablou.adria.R;
import ma.chablou.adria.model.AlbumItem;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListAlbumActivity extends AppCompatActivity implements AlbumAdapter.ItemClickListener {

    AlbumAdapter adapter;

    RecyclerView recyclerView;

    public static ArrayList<String> idb=new ArrayList<>();

    List<AlbumItem> albums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_layout);


        setTitle("La liste de vos Albums");


        Intent intent=getIntent();
        Bundle args=intent.getBundleExtra("BUNDLE");

        albums =(List<AlbumItem>) args.getSerializable("albumsId");


        Log.e("albums faical",albums.toString());

        //le trie des albums selon l'ordre alphabetique comme demmande
        Collections.sort(albums, new Comparator<AlbumItem>() {
            public int compare(AlbumItem album1, AlbumItem album2) {
                return album1.getNameAlbum().compareTo(album2.getNameAlbum());

            }});

        //Collections.reverse(albums);



        recyclerView = (RecyclerView) findViewById(R.id.album);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AlbumAdapter(this, albums);
        adapter.setClickListener(this);

        //binding de l'Adapter
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, final int position) {
        Bundle parameters = new Bundle();
        parameters.putString("fields", "images");
        //Appel a l'API facebook
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + adapter.getItem(position) + "/photos",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                //Traitement du resultat
                        Log.v("TAG", "reponse avec les photos" + response);

                        if (response.getError() == null) {
                            JSONObject joMain = response.getJSONObject();
                            if (joMain.has("data")) {
                                JSONArray jaData = joMain.optJSONArray("data");
                                idb.clear();
                                for (int i = 0; i < jaData.length(); i++) {
                                    try {
                                        JSONObject joAlbum = jaData.getJSONObject(i);
                                        JSONArray jaImages = joAlbum.getJSONArray("images");
                                        if(jaImages.length()>0)
                                        {
                                             idb.add(jaImages.getJSONObject(0).getString("source"));
                                        }
                                    }catch (Exception e){
                                    }
                                }
                                Intent mIntent=new Intent(getApplication(),PicturesAlbumActivity.class);
                                Bundle arg=new Bundle();
                                arg.putString("AlbumName",albums.get(position).getNameAlbum());
                                arg.putSerializable("albums",(Serializable)idb );
                                mIntent.putExtra("BUNDLE",arg);
                                startActivity(mIntent);
                                }

                        }
                    }}
        ).executeAsync();
    }
}