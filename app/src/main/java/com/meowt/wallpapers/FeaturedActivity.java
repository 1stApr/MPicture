package com.meowt.wallpapers;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FeaturedActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private RecyclerView recyclerView;

    Bitmap[] bitmaps;
    int size = 0;

    public static ArrayList<MyDataModel> arrayList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_featured);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(MainActivity.search){
            arrayList = MainActivity.listSearch;
            size = arrayList.size();
        }else {
            arrayList = MainActivity.list;
            size = arrayList.size();
        }

        try {
            bitmaps = getBitmap();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recyclerView = (RecyclerView)findViewById(R.id.stagger);
        MyRecyclerAdapter myRecyclerAdapter = new MyRecyclerAdapter(bitmaps);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(myRecyclerAdapter);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private Bitmap[] getBitmap() throws IOException {

        final Bitmap[] tempBitmap = new Bitmap[50];
//        tempBitmap[0] = BitmapFactory.decodeResource(getResources(),R.drawable.one);

        for(int i = 0;i<50;i++){
            final int position = i;
            new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        final String name = MainActivity.list.get(position).getLink();
                        URL url = new URL(name);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        Bitmap myBitmap = BitmapFactory.decodeStream(input);
                        tempBitmap[position] = myBitmap;
                    } catch (IOException e) {
                        // Null
                    }
                }
            }).start();
        }

        return tempBitmap;
    }

    private class MyRecyclerAdapter extends RecyclerView.Adapter<GridHolder>{
        Bitmap[] bitmaps;


        public MyRecyclerAdapter(Bitmap[] bitmaps) {
            this.bitmaps = bitmaps;
        }

        @NonNull
        @Override
        public GridHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,int i) {

            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = LayoutInflater.from(FeaturedActivity.this).inflate(R.layout.row_item,viewGroup,false);

            return new GridHolder(view);

        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public void onBindViewHolder(@NonNull GridHolder gridHolder, int position) {
            gridHolder.imageView.setImageBitmap(bitmaps[position]);
            gridHolder.textView.setText(arrayList.get(position).getName());
            gridHolder.path = arrayList.get(position).getLink();
            gridHolder.useLink = arrayList.get(position).getUseLink();
        }

        @Override
        public int getItemCount() {
            return bitmaps.length;
        }


    }
    private class GridHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        String path;
        String useLink;
        public GridHolder(@NonNull final View itemView) {
            super(itemView);
            imageView =itemView.findViewById(R.id.row_img);
            textView = itemView.findViewById(R.id.name_text);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(path);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setPackage("com.instagram.android");
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(path)));
                    }

                }
            });
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),textView.getText(), Toast.LENGTH_LONG).show();
//                    Uri uri = Uri.parse(useLink);
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    intent.setPackage("com.instagram.android");
//                    try {
//                        startActivity(intent);
//                    } catch (ActivityNotFoundException e) {
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(useLink)));
//                    }
                }
            });
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

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(FeaturedActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivityIfNeeded(intent, 0);
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(getApplicationContext(), "Commingsoon!", Toast.LENGTH_LONG).show();

        } else if (id == R.id.nav_featured) {
            Toast.makeText(getApplicationContext(), "Commingsoon!", Toast.LENGTH_LONG).show();

        } else if (id == R.id.nav_contribute) {
            Intent intent = new Intent(FeaturedActivity.this, ContributeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_donate) {

        } else if (id == R.id.nav_feedback) {
            Intent intent = new Intent(FeaturedActivity.this, FeedbackActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_information) {
            Intent intent = new Intent(FeaturedActivity.this, InformationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
