package com.cliffex.Fixezi;

import static com.facebook.FacebookSdk.getApplicationContext;

import com.cliffex.Fixezi.MyUtils.InternetDetect;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.cliffex.Fixezi.Constant.PreferenceConnector;
import com.cliffex.Fixezi.MyUtils.Appconstants;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    TextView toolbar_title;
    private Toolbar toolbar;
    RelativeLayout NavigationUpIM;
    TextView LoginUserTV, LoginAsTradesmanTV, SignupTV, quick_search_tv, tvFbLogin;
    ImageView draw_head;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    Context mContext = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav__trip__type);
        callbackManager = CallbackManager.Factory.create();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        initComp();

        draw_head = (ImageView) findViewById(R.id.draw_head);

        PreferenceConnector.writeString(MainActivity.this, PreferenceConnector.FName, "");
        PreferenceConnector.writeString(MainActivity.this, PreferenceConnector.LName, "");
        PreferenceConnector.writeString(MainActivity.this, PreferenceConnector.HouseNo, "");
        PreferenceConnector.writeString(MainActivity.this, PreferenceConnector.HomePhoneNo, "");
        PreferenceConnector.writeString(MainActivity.this, PreferenceConnector.MobileNo, "");
        PreferenceConnector.writeString(MainActivity.this, PreferenceConnector.Email, "");
        PreferenceConnector.writeString(MainActivity.this, PreferenceConnector.Username, "");
        PreferenceConnector.writeString(MainActivity.this, PreferenceConnector.Password, "");
        PreferenceConnector.writeString(MainActivity.this, PreferenceConnector.Confirm_Password, "");
        PreferenceConnector.writeString(MainActivity.this, PreferenceConnector.Confirm_Email, "");

        LoginUserTV.setOnClickListener(this);
        LoginAsTradesmanTV.setOnClickListener(this);
        SignupTV.setOnClickListener(this);
        quick_search_tv.setOnClickListener(this);

        // toolbar_title.setText("Register/Login");

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));

        View headerView = navigationView.getHeaderView(0);
        LinearLayout navUsername = (LinearLayout) headerView.findViewById(R.id.linearlayout);

        navigationView.setNavigationItemSelectedListener(this);

        if (Appconstants.postCode == null) {
            if (InternetDetect.isConnected(this)) {
                new JsonPostCode().execute();
            } else {
                Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
            }
        } else if (Appconstants.postCode.isEmpty()) {
            if (InternetDetect.isConnected(this)) {
                new JsonPostCode().execute();
            } else {
                Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
            }
        }

        draw_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_ourstory) {
            Intent intent = new Intent(MainActivity.this, OurStory.class);
            startActivity(intent);
        } else if (id == R.id.nav_video) {
            Intent intent = new Intent(MainActivity.this, Website_Url.class);
            startActivity(intent);
        } else if (id == R.id.nav_home) {
        } else if (id == R.id.nav_fb) {
            Intent intent = new Intent(MainActivity.this, Nav_FB.class);
            startActivity(intent);
        } else if (id == R.id.nav_info) {
            Intent intent = new Intent(MainActivity.this, Nav_ContactInfort.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initComp() {

        toolbar = (Toolbar) findViewById(R.id.m_toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        tvFbLogin = (TextView) findViewById(R.id.tvFbLogin);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        LoginUserTV = (TextView) findViewById(R.id.LoginUserTV);
        LoginAsTradesmanTV = (TextView) findViewById(R.id.LoginAsTradesmanTV);
        SignupTV = (TextView) findViewById(R.id.SignupTV);
        quick_search_tv = (TextView) findViewById(R.id.quick_search_tv);

        tvFbLogin.setOnClickListener(v -> {
            FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));
            FacebookSdk.sdkInitialize(mContext);
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    Log.e("kjsgdfkjdgsf", "onCancel");
                }

                @Override
                public void onError(FacebookException error) {
                    Log.e("kjsgdfkjdgsf", "error = " + error.getMessage());
                }

            });
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuth.signOut();
        LoginManager.getInstance().logOut();
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();

                            String profilePhoto = "https://graph.facebook.com/" + token.getUserId() + "/picture?height=500";

                            Log.e("kjsgdfkjdgsf", "profilePhoto = " + profilePhoto);
                            Log.e("kjsgdfkjdgsf", "name = " + user.getDisplayName());
                            Log.e("kjsgdfkjdgsf", "email = " + user.getEmail());
                            Log.e("kjsgdfkjdgsf", "email = " + user.getPhoneNumber());
                            Log.e("kjsgdfkjdgsf", "Userid = " + user.getUid());

//                            socialLoginCall(user.getDisplayName(),
//                                    user.getEmail(), profilePhoto,
//                                    user.getUid());

                        } else {
//                            Toast.makeText(GetStartedActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private class JsonPostCode extends AsyncTask<String, String, String> {
        String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(HttpPAth.Urlpath + "all_postcode");

                List<NameValuePair> list = new ArrayList<NameValuePair>();

                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                InputStream inputStream = httpEntity.getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                StringBuffer stringBuffer = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }

                Appconstants.postCode = new ArrayList<>();
                String Jsondata = stringBuffer.toString();

                Log.e("JsonPostCode", Jsondata);

                JSONArray parentarray = new JSONArray(Jsondata);

                for (int i = 0; i < parentarray.length(); i++) {
                    JSONObject parentobject = parentarray.getJSONObject(i);

                    String post_code = parentobject.getString("post_code");
                    Appconstants.postCode.add(post_code);
                    result = parentobject.getString("result");

                }
                return result;
            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (ClientProtocolException e1) {
                e1.printStackTrace();
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.SignupTV:

                Intent j = new Intent(getApplicationContext(), TermAndCondition.class);
                j.putExtra("From", "Main");
                startActivity(j);

                break;

            case R.id.LoginUserTV:

                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);

                break;

            case R.id.LoginAsTradesmanTV:

                Intent intent = new Intent(getApplicationContext(), TradesmanLoginNew.class);
                startActivity(intent);

                break;

            case R.id.quick_search_tv:

                Intent intent1 = new Intent(getApplicationContext(), Activity_Quick_Search.class);
                intent1.putExtra("status", "quick_search");
                startActivity(intent1);
                break;

            default:

                break;

        }
    }
}
