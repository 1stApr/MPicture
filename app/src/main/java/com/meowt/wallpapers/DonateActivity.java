package com.meowt.wallpapers;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class DonateActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private  Button buttonPalpal;
    private ToggleButton toggleButtonDonate;
    private  ImageView imageView;
    private TextView textView;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        buttonPalpal = (Button)findViewById(R.id.buttonDownload);
        toggleButtonDonate = (ToggleButton)findViewById(R.id.toggleButtonDonate) ;
        imageView = (ImageView)findViewById(R.id.imageView);
        textView = (TextView)findViewById(R.id.textView);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
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

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(DonateActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivityIfNeeded(intent, 0);
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(getApplicationContext(), "Commingsoon!", Toast.LENGTH_LONG).show();

        } else if (id == R.id.nav_featured) {
            Toast.makeText(getApplicationContext(), "Commingsoon!", Toast.LENGTH_LONG).show();

        } else if (id == R.id.nav_contribute) {
            Intent intent = new Intent(DonateActivity.this, ContributeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_donate) {

        } else if (id == R.id.nav_feedback) {
            Intent intent = new Intent(DonateActivity.this, FeedbackActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_information) {
            Intent intent = new Intent(DonateActivity.this, InformationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onclickButtonPalpal(View view) {
        Toast.makeText(getApplicationContext(), "Please wait... ", Toast.LENGTH_LONG).show();
        String payPalLink;
        payPalLink = "https://www.paypal.me/meeooooow/";
        Uri uri = Uri.parse(payPalLink);
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
        likeIng.setPackage("com.palpal.android");
        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(payPalLink)));
        }
    }

    public void onClickButtonDonate(View view) {
        if(toggleButtonDonate.isChecked()){
            imageView.setImageResource(R.drawable.qrpay);
            textView.setText("QRPAY");
        }else{
            imageView.setImageResource(R.drawable.momo);
            textView.setText("MOMO");
        }
    }
}
