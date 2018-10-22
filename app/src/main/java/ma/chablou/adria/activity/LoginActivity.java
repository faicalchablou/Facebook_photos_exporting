package ma.chablou.adria.activity;

/**
 * Created by Faical chablou on 21/10/2018.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import ma.chablou.adria.R;
import ma.chablou.adria.fragment.FacebookFragment;

//activity d'initiatisation
public class LoginActivity extends AppCompatActivity {

    private com.facebook.login.widget.LoginButton loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
                fragment = new FacebookFragment();
                fm.beginTransaction()
                        .add(R.id.fragment_container, fragment)
                        .commit();
        }

    }
}
