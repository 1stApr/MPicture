package com.meowt.wallpapers;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static int NOTIFICATION_ID2;
    private Button buttonNext;
    private Button buttonBack;
    private Button buttonMore;
    private Button buttonSet;
    private Button buttonNoAD;
    private Button buttonDownload;
    private Button buttonSearch;
    private EditText editText;
    private ImageView imageView;
    private TextView textView;
    private Picasso Picasso;
    private int dem = -1;
    private int demTemp = -1;
    private int demTemp2 = -1;
    private boolean liked = false;
    public static ArrayList<MyDataModel> list;
    public static final int PERMISSION_REQUEST_CODE =1000;
    private int checkSearch = 0;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
                }
            }
                break;
        }
    }

    protected void onStart() {

        super.onStart();
        checkSearch = 0;
        editText.setVisibility(View.INVISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        checkSearch = 0;


        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },PERMISSION_REQUEST_CODE);
        }

        buttonNext = (Button)findViewById(R.id.buttonNext);
        buttonBack = (Button)findViewById(R.id.buttonBack);
        buttonDownload = (Button)findViewById(R.id.buttonDownload);
        buttonMore = (Button)findViewById(R.id.buttonMore);
        buttonSet = (Button)findViewById(R.id.buttonSet);
        buttonSearch = (Button)findViewById(R.id.buttonSearch) ;
        editText = (EditText)findViewById(R.id.textSearch);
        imageView = (ImageView)findViewById(R.id.imageView);
        textView = (TextView)findViewById(R.id.textView);
        list = new ArrayList<>();
        new GetDataTask().execute();

        Picasso.with(getBaseContext()).load("https://scontent-ort2-2.cdninstagram.com/vp/05c6c3312a2c75026f5d8bc2809d55ec/5D789BC4/t51.2885-15/e35/37958521_252833075549112_8302393747077857280_n.jpg?_nc_ht=scontent-ort2-2.cdninstagram.com").into(imageView);
        textView.setText("Welcome!");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    class GetDataTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;
        int jIndex;
        int x;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (InternetConnection.checkConnection(getApplicationContext())) {
                x=list.size();
                if(x==0)
                    jIndex=0;
                else
                    jIndex=x;
                dialog = new ProgressDialog(MainActivity.this);
                dialog.setTitle("Hey Wait Please!");
                dialog.setMessage("I am getting update data");
                dialog.show();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                final Timer t = new Timer();
                t.schedule(new TimerTask() {
                    public void run() {
                        dialog.dismiss();
                        t.cancel();
                    }
                }, 5000);
            } else {
                Toast.makeText(getApplicationContext(), "Internet Not Available.", Toast.LENGTH_LONG).show();
            }
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {

            JSONObject jsonObject = JSONParser.getDataFromWeb();
            try {
                if (jsonObject != null) {
                    if(jsonObject.length() > 0) {
                        JSONArray array = jsonObject.getJSONArray("Sheet1");
                        int lenArray = array.length();
                        //Log.d("DATA","Length: " + lenArray);
                        if(lenArray > 0) {
                            for( ; jIndex < lenArray; jIndex++) {
                                MyDataModel model = new MyDataModel();
                                JSONObject innerObject = array.getJSONObject(jIndex);
                                String name = innerObject.getString("name");
                                String link = innerObject.getString("link");
                                String useLink = innerObject.getString("useLink");
                                model.setName(name);
                                model.setLink(link);
                                model.setUseLink(useLink);
                                list.add(model);
                                //Log.d("DATA","Data: " + category + "||" + link + "||"+ useLink);
                            }
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Try Againt", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException je) {
                Log.i(JSONParser.TAG, "" + je.getLocalizedMessage());
            }
            //Log.d("PATH","Get Data Succesful: "+ list.size());

//            for(int i = 0;i<list.size();i++){
//               Log.d("TEST","Name: "+ list.get(i).getCategory());
//            }
            Collections.shuffle(list);
            //Log.d("TEST","--------------------------------------");
//            for(int i = 0;i<list.size();i++){
//                Log.d("TEST","Name: "+ list.get(i).getCategory());
//            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_gallery) {
            Toast.makeText(getApplicationContext(), "Please wait!", Toast.LENGTH_LONG).show();
            final int OPEN_GALLERY = 1;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, ""), OPEN_GALLERY);

        } else if (id == R.id.nav_featured) {
            Intent intent = new Intent(MainActivity.this, FeaturedActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        } else if (id == R.id.nav_contribute) {
            Intent intent = new Intent(MainActivity.this, ContributeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_donate) {

            Intent intent = new Intent(MainActivity.this, DonateActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_feedback) {
            Intent intent = new Intent(MainActivity.this, FeedbackActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_information) {
            Intent intent = new Intent(MainActivity.this, InformationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onClickButtonNext(View view) {
        if (InternetConnection.checkConnection(getApplicationContext())) {
            dem++;
            if (dem < list.size()){
                String path = list.get(dem).getLink();
                //Log.d("PATH",path);
                Picasso.with(getBaseContext()).load(path).placeholder(R.drawable.placeholder_downloading).into(imageView);

                textView.setText(list.get(dem).getName());

            } else{
                dem = 0;
                String path = list.get(dem).getLink();
                //Log.d("PATH",path);
                Picasso.with(getBaseContext()).load(path).placeholder(R.drawable.placeholder_downloading).into(imageView);

                textView.setText(list.get(dem).getName());
            }
        } else {
            Toast.makeText(getApplicationContext(), "Internet Not Available.", Toast.LENGTH_LONG).show();
        }
        demTemp = dem;
        demTemp2 = dem;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onClickButtonBack(View view) {
        if (InternetConnection.checkConnection(getApplicationContext())) {
            demTemp--;
            if (demTemp>=0 && demTemp < list.size()){
                String path = list.get(demTemp).getLink();
               // Log.d("PATH",path);
                Picasso.with(getBaseContext()).load(path).placeholder(R.drawable.placeholder_downloading).into(imageView);
                textView.setText(list.get(demTemp).getName());
            } else{
                demTemp = 0;
                String path = list.get(demTemp).getLink();
               // Log.d("PATH",path);
                Picasso.with(getBaseContext()).load(path).placeholder(R.drawable.placeholder_downloading).into(imageView);
                textView.setText(list.get(demTemp).getName());
            }
        } else {
            Toast.makeText(getApplicationContext(), "Internet Not Available.", Toast.LENGTH_LONG).show();
        }
        demTemp2 = demTemp;
    }
    public void onClickButtonMore(View view) {
        String useLink;
        if (InternetConnection.checkConnection(getApplicationContext())) {
            if(demTemp2 == -1){
                Toast.makeText(getApplicationContext(), "Please wait... ", Toast.LENGTH_LONG).show();
                useLink = "https://www.instagram.com/hoangyenchibi812/";
                Uri uri = Uri.parse(useLink);
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                likeIng.setPackage("com.instagram.android");
                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(useLink)));
                }
            }else{
                Toast.makeText(getApplicationContext(), "Please wait... ", Toast.LENGTH_LONG).show();
                useLink = list.get(demTemp2).getUseLink();
                if(useLink.equals("")){
                    useLink = "https://www.instagram.com/meoow1414";
                }
                Uri uri = Uri.parse(useLink);
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                likeIng.setPackage("com.instagram.android");
                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(useLink)));
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Internet Not Available.", Toast.LENGTH_LONG).show();
        }
    }

    public void onclickButtonSet(View view) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            myWallpaperManager.setBitmap(bitmap);
            Toast.makeText(MainActivity.this, "Wallpaper set", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(MainActivity.this,"Error setting wallpaper!", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClickButtonDownload(View view) {
        String linkImage;
        if(demTemp2==-1 ){
            linkImage = "https://scontent-ort2-2.cdninstagram.com/vp/05c6c3312a2c75026f5d8bc2809d55ec/5D789BC4/t51.2885-15/e35/37958521_252833075549112_8302393747077857280_n.jpg?_nc_ht=scontent-ort2-2.cdninstagram.com";

        }else {
            linkImage = list.get(demTemp2).getLink();
        }
        Uri uri = Uri.parse(linkImage);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"Your should grant permission",Toast.LENGTH_SHORT).show();
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },PERMISSION_REQUEST_CODE);
            return;
        }else{
            final AlertDialog alertDialog = new SpotsDialog(MainActivity.this);
            alertDialog.show();
            alertDialog.setMessage("Downloading...");
            String fileName = UUID.randomUUID().toString()+".jpg";
            Picasso.with(getBaseContext())
                    .load(uri)
                    .into(new SaveImage(getBaseContext(),
                            alertDialog,
                            getApplicationContext().getContentResolver(),
                            fileName,
                            "Image description"));
        }

    }

    public void onClickButtonShare(View view) {
        String imageLink = "";
        String useLink = "";
        if (InternetConnection.checkConnection(getApplicationContext())) {
            if(demTemp2 == -1){
                imageLink = "https://scontent-ort2-2.cdninstagram.com/vp/05c6c3312a2c75026f5d8bc2809d55ec/5D789BC4/t51.2885-15/e35/37958521_252833075549112_8302393747077857280_n.jpg?_nc_ht=scontent-ort2-2.cdninstagram.com";
                useLink = "https://www.instagram.com/hoangyenchibi812/";
            }else{
                imageLink = list.get(demTemp2).getLink();
                useLink = list.get(demTemp2).getUseLink();
                if(imageLink.equals("")){
                    imageLink = "https://www.instagram.com/meoow1414";
                    useLink = "https://www.instagram.com/meoow1414";
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Internet Not Available.", Toast.LENGTH_LONG).show();
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "MPicture");
        intent.putExtra(Intent.EXTRA_TEXT, "\n--------------\n" + useLink + "\n--------------\n" + imageLink + "\n--------------\n");
        startActivity(Intent.createChooser(intent, "Share to"));
    }

    public void onClickFullSize(View view){
        String imageLink;
        if (InternetConnection.checkConnection(getApplicationContext())) {
            if(demTemp2 == -1){
                Toast.makeText(getApplicationContext(), "Please wait... ", Toast.LENGTH_LONG).show();
                imageLink = "https://scontent-ort2-2.cdninstagram.com/vp/05c6c3312a2c75026f5d8bc2809d55ec/5D789BC4/t51.2885-15/e35/37958521_252833075549112_8302393747077857280_n.jpg?_nc_ht=scontent-ort2-2.cdninstagram.com";
                Uri uri = Uri.parse(imageLink);
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                likeIng.setPackage("com.instagram.android");
                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(imageLink)));
                }
            }else{
                Toast.makeText(getApplicationContext(), "Please wait... ", Toast.LENGTH_LONG).show();
                imageLink = list.get(demTemp2).getLink();
                if(imageLink.equals("")){
                    imageLink = "https://www.instagram.com/meoow1414";
                }
                Uri uri = Uri.parse(imageLink);
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                likeIng.setPackage("com.instagram.android");
                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(imageLink)));
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Internet Not Available.", Toast.LENGTH_LONG).show();
        }
    }
    public void onClickButtonSearch(View view) {
        if(checkSearch ==0){
            editText.setVisibility(view.VISIBLE);
            checkSearch = 1;
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            editText.setSelection(editText.length());

        }else{
            String url = "https://www.google.com/search?q=";
            String textSearch = String.valueOf(editText.getText());
            textSearch = textSearch.replace(" ","+");
            String link = url+textSearch;
            Uri uri = Uri.parse(link);
            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
            likeIng.setPackage("com.instagram.android");
            try {
                startActivity(likeIng);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
            }
        }

    }

}
