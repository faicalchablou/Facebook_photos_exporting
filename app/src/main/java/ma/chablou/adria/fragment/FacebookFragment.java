package ma.chablou.adria.fragment;

/**
 * Created by Faical chablou on 21/10/2018.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import ma.chablou.adria.activity.ListAlbumActivity;
import ma.chablou.adria.hepler.JSONParser;
import ma.chablou.adria.hepler.ImageHelper;

import ma.chablou.adria.model.AlbumItem;

import ma.chablou.adria.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class FacebookFragment extends Fragment{

    private LoginButton loginButton;
    private Button consulterButton;

    private boolean Enabled = false;

    private final String PENDING_ACTION_BUNDLE_KEY =
            "com.example.hellofacebook:PendingAction";

    //la photo de profil de l'User  par defaut in associe une image par defaut
    private ImageView prictureProfilImageView;
    private TextView labelUserName;
    private PendingAction pendingAction = PendingAction.NONE;

    private boolean canPresentShareDialog;

    private boolean canPresentShareDialogWithPhotos;


    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;

    private ShareDialog shareDialog;

    // list des albums comme variable static
    public static ArrayList<AlbumItem> listAlbums = new ArrayList<AlbumItem>();



    //le callback method pour SDK de Facebook.
    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {


        //Called when the dialog is canceled.
        @Override
        public void onCancel() {
            Log.d("FacebookFragment", "Fermee");
        }


        //Called when the dialog finishes with an error.
        @Override
        public void onError(FacebookException error) {

            //on affiche l'erreur rencontrer lors de developpement
            Log.d("FacebookFragment", String.format("Error: %s", error.toString()));
            String title = getString(R.string.error);
            String alertMessage = error.getMessage();
            showResult(title, alertMessage);
        }
        //Called when the dialog completes without error.
        @Override
        public void onSuccess(Sharer.Result result) {
            Log.d("FacebookFragment", "Succes!");
            if (result.getPostId() != null) {
                String title = getString(R.string.succes);
                String id = result.getPostId();
                String alertMessage = getString(R.string.successfull, id);
                showResult(title, alertMessage);
            }
        }

        private void showResult(String title, String alertMessage) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(alertMessage)
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }
    };

    //on a besoin de l'id de l'utilisateur pour interagir avec lApi facebook pour recuperer ces informations
    String idUser="";

    private enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //on initialise le SDK facebook
        FacebookSdk.sdkInitialize(getActivity());
     }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        //le fragment de connexion Facebook
        View v = inflater.inflate(R.layout.fragment_facebook, parent, false);

        loginButton = (LoginButton) v.findViewById(R.id.loginButton);
        loginButton.setFragment(this);

        callbackManager = CallbackManager.Factory.create();


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Enabled = true;
                consulterButton.setVisibility(View.VISIBLE);
                handlePendingAction();
                updateUI();
            }


            @Override
            public void onCancel() {

                if (pendingAction != PendingAction.NONE) {
                    showAlert();
                    pendingAction = PendingAction.NONE;
                }
                updateUI();
            }

            @Override
            public void onError(FacebookException exception) {
                if (pendingAction != PendingAction.NONE
                        && exception instanceof FacebookAuthorizationException) {
                    showAlert();
                    pendingAction = PendingAction.NONE;
                }
                updateUI();

            }

            private void showAlert() {
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.annule)
                        .setMessage(R.string.no_permission)
                        .setPositiveButton(R.string.ok, null)
                        .show();
            }

        });


        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(
                callbackManager,
                shareCallback);

        if (savedInstanceState != null) {
            String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            pendingAction = PendingAction.valueOf(name);
        }



        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                updateUI();
                handlePendingAction();
            }
        };

        //initialisation
        prictureProfilImageView = (ImageView) v.findViewById(R.id.profilePicture);
        labelUserName = (TextView) v.findViewById(R.id.labelUserName);




        consulterButton = (Button) v.findViewById(R.id.consulterButton);

        consulterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),


                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                if (object != null) {
                                    Log.d("Flogin", object.toString());
                                    String name = JSONParser.getName(object);
                                    String id = JSONParser.getId(object);
                                    String album=JSONParser.getAlbums(object);


                                    listAlbums = JSONParser.getAlbumsId(album);


                                    String s ="Name : "+name+"\n";
                                    s +="Id : "+id+"\n";
                                    s +="albumsId : "+"\n";
                                    s+="albums :"+album+"\n";
                                    idUser=id+"";

                                    System.out.print(s);

                                    Intent mIntent=new Intent(getActivity(),ListAlbumActivity.class);
                                    Bundle arg=new Bundle();
                                    arg.putSerializable("albumsId",(Serializable)listAlbums );
                                    mIntent.putExtra("BUNDLE",arg);

                                    startActivity(mIntent);
                                }
                            }
                        });
                Bundle parameters = new Bundle();

                parameters.putString("fields", "id,name,albums,link");
                request.setParameters(parameters);
                request.executeAsync();
            }
        });



        canPresentShareDialog = ShareDialog.canShow(
                ShareLinkContent.class);

        canPresentShareDialogWithPhotos = ShareDialog.canShow(
                SharePhotoContent.class);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //les permissions Facebook ici on utilise deux :pour acceder a la photo de profil et a les photos de l'User
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile","user_photos"));
                if(!Enabled) {

                    Enabled = true;

                    consulterButton.setVisibility(View.VISIBLE);
                }else{

                    Enabled = false;

                    consulterButton.setVisibility(View.GONE);

                }

            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();


        AppEventsLogger.activateApp(getActivity());

        updateUI();
    }

    @Override
    public void onPause() {
        super.onPause();


        AppEventsLogger.deactivateApp(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }

    private void updateUI() {
        boolean enableButtons = AccessToken.getCurrentAccessToken() != null;

        Profile profile = Profile.getCurrentProfile();

        //si l'User est logger
        if (enableButtons && profile != null) {
            new LoadProfileImage(prictureProfilImageView).execute(profile.getProfilePictureUri(200, 200).toString());
            labelUserName.setText(profile.getFirstName()+profile.getLastName());
            Enabled = true;

            consulterButton.setVisibility(View.VISIBLE);

            // on associe la photo par defaut
        } else {
            Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.default_picture);
            prictureProfilImageView.setImageBitmap(ImageHelper.getRoundedCornerBitmap(getContext(), icon, 200, 200, 200, false, false, false, false));
            labelUserName.setText(null);
            Enabled = false;

            consulterButton.setVisibility(View.GONE);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
    }

    private void handlePendingAction() {
        PendingAction previouslyPendingAction = pendingAction;
        pendingAction = PendingAction.NONE;

        switch (previouslyPendingAction) {
            case NONE:
                break;

        }
    }

    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... uri) {
            String url = uri[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                Bitmap resized = Bitmap.createScaledBitmap(result,200,200, true);
                bmImage.setImageBitmap(ImageHelper.getRoundedCornerBitmap(getContext(),resized,250,200,200, false, false, false, false));

            }
        }
    }

}





