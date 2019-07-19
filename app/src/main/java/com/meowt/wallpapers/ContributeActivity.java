package com.meowt.wallpapers;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ContributeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

    private Button buttonSend;
    private TextView textView;
    private TextView textViewName;
    private TextView textViewLinkImage;
    private TextView textViewLinkSocialNetworking;
    private TextView textViewNote;
    private EditText editTextName;
    private EditText editTextLinkImage;
    private EditText editTextLinkSocialNetworking;
    private EditText editTextNote;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribute);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        buttonSend = (Button)findViewById(R.id.buttonSend);
        textView = (TextView)findViewById(R.id.textView);
        textViewName = (TextView)findViewById(R.id.textViewName);
        textViewLinkImage = (TextView)findViewById(R.id.textViewLinkImage);
        textViewLinkSocialNetworking = (TextView)findViewById(R.id.textViewLinkSocialNetworking);
        textViewName = (TextView)findViewById(R.id.textViewName);
        editTextName = (EditText)findViewById(R.id.editTextName) ;
        editTextLinkImage = (EditText)findViewById(R.id.editTextLinkImage) ;
        editTextLinkSocialNetworking = (EditText)findViewById(R.id.editTextLinkSocialNetworking) ;
        editTextNote = (EditText)findViewById(R.id.editTextNote) ;

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
            Intent intent = new Intent(ContributeActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivityIfNeeded(intent, 0);
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(getApplicationContext(), "Commingsoon!", Toast.LENGTH_LONG).show();

        } else if (id == R.id.nav_featured) {
            Toast.makeText(getApplicationContext(), "Commingsoon!", Toast.LENGTH_LONG).show();

        } else if (id == R.id.nav_contribute) {

        } else if (id == R.id.nav_donate) {
            Intent intent = new Intent(ContributeActivity.this, DonateActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_feedback) {
            Intent intent = new Intent(ContributeActivity.this, FeedbackActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_information) {
            Intent intent = new Intent(ContributeActivity.this, InformationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClickButtonSend(View view) {

        String name = editTextName.getText().toString();
        String linkImage = editTextLinkImage.getText().toString();
        String linkSocialNetworking = editTextLinkSocialNetworking.getText().toString();
        String note = editTextNote.getText().toString();
        if(name.equals("")||linkImage.equals("")) {
            //Log.d("CONTRIBUTE","Result: " + name.equals("") + " || " + linkImage.equals(""));
            Toast.makeText(getApplicationContext(), "Name person & link image cannot be empty!", Toast.LENGTH_LONG).show();
        }else{
//            Log.d("CONTRIBUTE","Contribute data:\n"
//                    + "Name: " + name
//                    + "\nLink Image: " + linkImage
//                    + "\nLink Social Networking: " + linkSocialNetworking
//                    + "\nNote: " + note);
            Toast.makeText(getApplicationContext(), "Use Gmail to send contribute!", Toast.LENGTH_LONG).show();
            Intent Email = new Intent(Intent.ACTION_SEND);
            Email.setType("text/email");
            Email.putExtra(Intent.EXTRA_EMAIL, new String[]{"tuan98.tran@gmail.com"});
            Email.putExtra(Intent.EXTRA_SUBJECT, "Contribute MPicture");
            Email.putExtra(Intent.EXTRA_TEXT, "Dear ...,\n"
                    + "Contribute data:\n"
                    + "Name: " + name
                    + "\nLink Image: " + linkImage
                    + "\nLink Social Networking: " + linkSocialNetworking
                    + "\nNote: " + note);
            startActivity(Intent.createChooser(Email, "Send Contribute:"));
        }
    }

    public void onClickEditTextName(View view) {

        editTextName.setText("");
    }

    public void onClickEditTextLinkImage(View view) {

        editTextLinkImage.setText("");
    }

    public void onClickEditTextLinkSocialNetworking(View view) {

        editTextLinkSocialNetworking.setText("");
    }

    public void onClickEditTextNote(View view) {

        editTextNote.setText("");
    }

}
