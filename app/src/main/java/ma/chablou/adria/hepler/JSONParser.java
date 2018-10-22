package ma.chablou.adria.hepler;


/**
 * Created by Faical chablou on 21/10/2018.
 */

import android.util.Log;
import ma.chablou.adria.model.AlbumItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
public class JSONParser {

    public static ArrayList<AlbumItem> albumId = new ArrayList<AlbumItem>();

    public static ArrayList<AlbumItem> getAlbumsId(String obj) {
        albumId.clear();

        System.out.println("objectfaical"+obj+"");
        try {
            JSONObject object1 = (JSONObject) new JSONTokener(obj).nextValue();
            JSONArray arr = object1.getJSONArray("data");

             String s;
            for (int i = 0; i < arr.length(); i++) {
                object1 = arr.getJSONObject(i);
                s = object1.getString("id");


                AlbumItem albumItem =new AlbumItem(object1.getString("id"),object1.getString("name"),object1.getString("created_time"));
                albumId.add(albumItem);
            }
        }
        catch (Exception c){
        }
        return albumId;
    }
    public static String getName(JSONObject obj) {
        String s1 = "";
        try {

             s1 = obj.getString("name");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return s1;
    }
    public static String getId(JSONObject obj) {
        String s1 = "";
        try {

            s1 = obj.getString("id");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return s1;
    }
    public static String getAlbums(JSONObject obj) {
        String s1 = "";
        try {

            Log.e("faicall",obj.toString());
            s1 = obj.getString("albums");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return s1;
    }

}